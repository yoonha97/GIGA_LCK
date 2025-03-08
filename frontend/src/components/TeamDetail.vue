<template>
  <div
    class="bg-white rounded-lg shadow-lg p-6 md:p-8 w-full max-w-7xl mx-auto"
  >
    <!-- 로딩 상태 -->
    <div v-if="teamStore.loading" class="text-center py-20">
      <div
        class="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto mb-6"
      ></div>
      <p class="text-gray-600 text-lg">팀 정보를 불러오는 중...</p>
    </div>

    <!-- 에러 메시지 -->
    <div
      v-else-if="teamStore.error"
      class="text-center text-red-500 py-20 text-lg"
    >
      {{ teamStore.error }}
    </div>

    <!-- 팀 정보 -->
    <div v-else-if="teamStore.currentTeam" class="space-y-10">
      <!-- 팀 헤더 -->
      <div
        class="flex flex-col items-center sm:flex-row sm:items-center sm:justify-between border-b pb-6"
      >
        <div class="flex flex-col sm:flex-row items-center mb-6 sm:mb-0">
          <div
            class="w-32 h-32 bg-gray-100 rounded-lg flex items-center justify-center p-3 mb-4 sm:mb-0 sm:mr-8"
          >
            <img
              :src="getTeamLogo(teamStore.currentTeam.teamName)"
              alt="팀 로고"
              class="max-w-full max-h-full object-contain"
            />
          </div>
          <div class="text-center sm:text-left">
            <h1 class="text-4xl font-bold mb-2">
              {{ teamStore.currentTeam.teamName }}
            </h1>
            <p class="text-gray-500 text-lg">
              {{ getTeamFullName(teamStore.currentTeam.teamName) }}
            </p>
          </div>
        </div>
      </div>

      <!-- 팀 정보 섹션 -->
      <div class="grid grid-cols-1 md:grid-cols-2 gap-10">
        <!-- 기본 정보 -->
        <div class="bg-gray-50 rounded-lg p-6">
          <h2 class="text-2xl font-semibold mb-6 border-b pb-3">구단 정보</h2>
          <div class="space-y-5 text-lg">
            <div>
              <span class="text-gray-600 font-medium">구단주:</span>
              <span class="ml-3">{{
                teamStore.currentTeam.owner || '정보 없음'
              }}</span>
            </div>
            <div>
              <span class="text-gray-600 font-medium">감독:</span>
              <span class="ml-3">{{
                teamStore.currentTeam.headCoach || '정보 없음'
              }}</span>
            </div>
          </div>
        </div>

        <!-- 코치진 -->
        <div class="bg-gray-50 rounded-lg p-6">
          <h2 class="text-2xl font-semibold mb-6 border-b pb-3">코치진</h2>
          <div
            v-if="
              !teamStore.currentTeam.coaches ||
              teamStore.currentTeam.coaches.length === 0
            "
            class="text-gray-500 text-lg py-8 text-center"
          >
            등록된 코치가 없습니다.
          </div>
          <div v-else class="space-y-5 text-lg">
            <div
              v-for="(coach, index) in teamStore.currentTeam.coaches"
              :key="index"
              class="flex items-center"
            >
              <span class="ml-3">{{ coach }}</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 선수 목록 -->
      <div class="bg-gray-50 rounded-lg p-6">
        <h2 class="text-2xl font-semibold mb-6 border-b pb-3">선수 명단</h2>
        <div
          v-if="
            !teamStore.currentTeam.players ||
            teamStore.currentTeam.players.length === 0
          "
          class="text-gray-500 text-lg py-10 text-center"
        >
          등록된 선수가 없습니다.
        </div>
        <div
          v-else
          class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6"
        >
          <div
            v-for="(player, index) in teamStore.currentTeam.players"
            :key="index"
            class="bg-white p-5 rounded-lg shadow-sm flex items-center space-x-4 hover:shadow-md transition-shadow"
          >
            <div
              class="w-14 h-14 bg-blue-500 rounded-full flex items-center justify-center text-white text-xl font-bold cursor-pointer hover:bg-blue-600 transition-colors"
              @click="navigateToTeam()"
              title="팀 상세 페이지로 이동"
            >
              <span>{{ player.charAt(0) }}</span>
            </div>
            <div>
              <div class="font-medium text-lg">{{ player }}</div>
              <div class="text-gray-500 text-sm">선수</div>
            </div>
          </div>
        </div>
      </div>

      <!-- 최근 경기 결과 섹션 (예시) -->
      <div class="bg-gray-50 rounded-lg p-6">
        <h2 class="text-2xl font-semibold mb-6 border-b pb-3">
          최근 경기 결과
        </h2>
        <div class="text-gray-500 text-lg py-10 text-center">
          최근 경기 데이터가 없습니다.
        </div>
      </div>

      <!-- 팀 응원 댓글 섹션 -->
      <SupportSection :teamId="getTeamId()" />
    </div>

    <!-- 데이터 없음 -->
    <div v-else class="text-center text-gray-500 py-20 text-lg">
      팀 정보를 찾을 수 없습니다.
    </div>
  </div>
</template>

<script>
import { useTeamStore } from '@/stores/teamStore';
import { onMounted, onUnmounted } from 'vue';
import { useRoute, useRouter } from 'vue-router';
import T1Logo from '@/assets/images/T1.svg';
import GENLogo from '@/assets/images/GEN.svg';
import DKLogo from '@/assets/images/DK.svg';
import BROLogo from '@/assets/images/BRO.svg';
import NSLogo from '@/assets/images/NS.svg';
import DRXLogo from '@/assets/images/DRX.svg';
import HLELogo from '@/assets/images/HLE.svg';
import KTLogo from '@/assets/images/KT.svg';
import DNFLogo from '@/assets/images/DNF.svg';
import BFXLogo from '@/assets/images/BFX.webp';
import SupportSection from '@/components/SupportSection.vue';

export default {
  components: {
    SupportSection,
  },
  setup() {
    const teamStore = useTeamStore();
    const route = useRoute();
    const router = useRouter();

    onMounted(async () => {
      const { teamId, teamName } = route.params;

      if (teamId) {
        await teamStore.loadTeamById(teamId);
      } else if (teamName) {
        await teamStore.loadTeamByName(teamName);
      }
    });

    onUnmounted(() => {
      teamStore.clearCurrentTeam();
    });

    // 팀 로고 가져오기
    const getTeamLogo = (teamName) => {
      const logos = {
        T1: T1Logo,
        GEN: GENLogo,
        DK: DKLogo,
        BRO: BROLogo,
        NS: NSLogo,
        DRX: DRXLogo,
        HLE: HLELogo,
        KT: KTLogo,
        DNF: DNFLogo,
        BFX: BFXLogo,
      };
      return logos[teamName] || '';
    };

    // 팀 전체 이름 가져오기
    const getTeamFullName = (teamName) => {
      const fullNames = {
        T1: 'T1',
        GEN: 'Gen.G',
        DK: 'Dplus KIA',
        BRO: 'OKSavingsBank BRION',
        NS: 'Nongshim RedForce',
        DRX: 'DRX',
        HLE: 'Hanwha Life Esports',
        KT: 'KT Rolster',
        DNF: 'DN FREECS',
        BFX: 'BNK FEARX',
      };
      return fullNames[teamName] || teamName;
    };

    const navigateToTeam = (player) => {
      // Extract team from player name or navigate to a predefined team
      // For example, if players are named "T1 Faker", "GEN Ruler", etc.

      // If the current page is already a team page, don't navigate again
      if (route.name.includes('TeamDetail')) {
        return;
      }

      // Get the player's team or use the current team as fallback
      const teamName = teamStore.currentTeam.teamName;

      // Navigate to team view
      router.push({
        name: 'TeamDetailByName',
        params: { teamName },
      });
    };

    const getTeamId = () => {
      return route.params.teamId || route.params.teamName;
    };

    return {
      teamStore,
      getTeamLogo,
      getTeamFullName,
      navigateToTeam,
      getTeamId,
    };
  },
};
</script>
