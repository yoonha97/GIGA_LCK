<template>
  <div class="p-6 bg-gray-100">
    <!-- 연도 및 월 선택 -->
    <div class="flex items-center justify-center mb-4 border-b pb-2">
      <div class="flex items-center space-x-4">
        <button @click="previousMonth" class="px-2 text-lg">◀</button>
        <span class="font-bold text-xl"
          >{{ selectedYear }}.{{ String(selectedMonth).padStart(2, '0') }}</span
        >
        <button @click="nextMonth" class="px-2 text-lg">▶</button>
      </div>
    </div>

    <!-- 팀 필터링 -->
    <div class="mb-4">
      <div
        class="overflow-x-auto pb-2 flex justify-start items-center hide-scrollbar"
      >
        <div class="flex space-x-2 px-2 min-w-full justify-center">
          <button
            class="team-filter-all rounded-full px-4 py-1.5 text-sm font-medium transition-colors"
            :class="
              !selectedTeam
                ? 'bg-blue-500 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            "
            @click="clearTeamFilter"
          >
            전체
          </button>
          <button
            v-for="team in teams"
            :key="team.id"
            class="team-filter-button flex items-center space-x-2 rounded-full px-4 py-2 transition-colors"
            :class="
              selectedTeam === team.id
                ? 'bg-blue-500 text-white'
                : 'bg-gray-200 text-gray-700 hover:bg-gray-300'
            "
          >
            <!-- 팀 로고 - 클릭 시 팀 상세 페이지로 이동 -->
            <div
              class="w-7 h-7 flex-shrink-0 rounded-full bg-white overflow-hidden flex items-center justify-center cursor-pointer"
              @click.stop="navigateToTeam(team.id)"
              title="팀 상세정보 보기"
            >
              <img
                :src="team.logo"
                :alt="team.name"
                class="w-6 h-6 object-contain"
              />
            </div>
            <!-- 팀 이름 - 클릭 시 팀 필터링 -->
            <span
              class="text-sm font-medium"
              @click.stop="toggleTeamFilter(team.id)"
              >{{ team.name }}</span
            >
          </button>
        </div>
      </div>
    </div>

    <!-- 경기 목록 -->
    <div v-if="loading" class="text-center py-4 text-gray-600">로딩 중...</div>
    <div v-else-if="error" class="text-center text-red-500">{{ error }}</div>
    <div
      v-else-if="filteredMatches.length === 0"
      class="text-center text-gray-600"
    >
      <template v-if="selectedTeam">
        {{ getTeamName(selectedTeam) }}의 경기 데이터가 없습니다.
      </template>
      <template v-else> 경기 데이터가 없습니다. </template>
    </div>

    <div v-else>
      <div v-for="(group, date) in groupedMatches" :key="date" class="mb-6">
        <div class="font-bold text-lg py-2 border-b bg-gray-200 px-4">
          {{ date }}
        </div>
        <MatchCard
          v-for="match in group"
          :key="match.matchId"
          :matchId="match.matchId"
          :time="match.time"
          :stage="match.stage"
          :homeTeam="match.homeTeam"
          :score="match.score"
          :awayTeam="match.awayTeam"
          :replayLink="match.replayLink"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue';
import { useRouter } from 'vue-router';
import { useMatchStore } from '@/stores/matchStore';
import MatchCard from '@/components/MatchCard.vue';
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

export default {
  components: {
    MatchCard,
  },
  setup() {
    // 매치 스토어 초기화
    const matchStore = useMatchStore();
    const router = useRouter();

    // 스토어 메서드 로깅
    console.log(
      'Available matchStore methods:',
      Object.keys(matchStore).filter(
        (key) => typeof matchStore[key] === 'function'
      )
    );

    const selectedYear = ref(new Date().getFullYear());
    const selectedMonth = ref(new Date().getMonth());
    const selectedTeam = ref(null);
    const loading = ref(false);
    const error = ref(null);

    const months = ref([
      { value: 1, label: '1월' },
      { value: 2, label: '2월' },
      { value: 3, label: '3월' },
      { value: 4, label: '4월' },
      { value: 5, label: '5월' },
      { value: 6, label: '6월' },
      { value: 7, label: '7월' },
      { value: 8, label: '8월' },
      { value: 9, label: '9월' },
      { value: 10, label: '10월' },
      { value: 11, label: '11월' },
      { value: 12, label: '12월' },
    ]);

    // 팀 데이터
    const teams = ref([
      { id: 'T1', name: 'T1', logo: T1Logo },
      { id: 'GEN', name: 'GEN', logo: GENLogo },
      { id: 'DK', name: 'DK', logo: DKLogo },
      { id: 'BRO', name: 'BRO', logo: BROLogo },
      { id: 'NS', name: 'NS', logo: NSLogo },
      { id: 'DRX', name: 'DRX', logo: DRXLogo },
      { id: 'HLE', name: 'HLE', logo: HLELogo },
      { id: 'KT', name: 'KT', logo: KTLogo },
      { id: 'DNF', name: 'DNF', logo: DNFLogo },
      { id: 'BFX', name: 'BFX', logo: BFXLogo },
    ]);

    const fetchMatches = async () => {
      loading.value = true;
      error.value = null;
      try {
        if (selectedTeam.value) {
          // 팀 필터링된 경기 가져오기
          console.log('Fetching matches for team:', selectedTeam.value);

          // 팀 필터링된 경기 가져오기
          if (typeof matchStore.loadTeamMatches === 'function') {
            console.log('Using API to fetch team matches');
            await matchStore.loadTeamMatches(selectedTeam.value);
          } else {
            console.warn(
              'loadTeamMatches not available, using client-side filtering'
            );
            await matchStore.loadMatches(
              selectedYear.value,
              selectedMonth.value
            );
          }

          console.log('Received matches:', matchStore.matches);
        } else {
          // 일반 월별 경기 가져오기
          console.log(
            `Fetching matches for ${selectedYear.value}.${selectedMonth.value}`
          );
          await matchStore.loadMatches(selectedYear.value, selectedMonth.value);
        }
      } catch (err) {
        console.error('Error fetching matches:', err);
        error.value = '데이터를 불러오는 중 오류가 발생했습니다.';
      } finally {
        loading.value = false;
      }
    };

    // 팀 필터링 기능
    const toggleTeamFilter = (teamId) => {
      if (selectedTeam.value === teamId) {
        selectedTeam.value = null;
      } else {
        selectedTeam.value = teamId;
      }
      fetchMatches(); // 팀 필터 변경 시 데이터 다시 불러오기
    };

    const clearTeamFilter = () => {
      selectedTeam.value = null;
      fetchMatches(); // 필터 해제 시 데이터 다시 불러오기
    };

    const getTeamName = (teamId) => {
      const team = teams.value.find((t) => t.id === teamId);
      return team ? team.name : '';
    };

    // 다음 달 이동
    const nextMonth = () => {
      if (selectedMonth.value === 12) {
        selectedMonth.value = 1;
        selectedYear.value++;
      } else {
        selectedMonth.value++;
      }
    };

    // 이전 달 이동
    const previousMonth = () => {
      if (selectedMonth.value === 1) {
        selectedMonth.value = 12;
        selectedYear.value--;
      } else {
        selectedMonth.value--;
      }
    };

    const filteredMatches = computed(() => {
      // 팀 필터링 없을 때 모든 경기 보여주기
      if (!selectedTeam.value) {
        return matchStore.matches;
      }

      // 팀 필터링 있을 때 팀 경기만 보여주기
      return matchStore.matches.filter(
        (match) =>
          match.homeTeam === selectedTeam.value ||
          match.awayTeam === selectedTeam.value
      );
    });

    const groupedMatches = computed(() => {
      return filteredMatches.value.reduce((acc, match) => {
        const dateKey = new Date(match.date).toLocaleDateString('ko-KR', {
          month: 'long',
          day: 'numeric',
          weekday: 'short',
        });
        if (!acc[dateKey]) acc[dateKey] = [];
        acc[dateKey].push(match);
        return acc;
      }, {});
    });

    const navigateToTeam = (teamId) => {
      router.push({ name: 'TeamDetailById', params: { teamId } });
    };

    watch([selectedYear, selectedMonth], () => {
      fetchMatches();
    });
    onMounted(fetchMatches);

    return {
      matchStore,
      selectedYear,
      selectedMonth,
      selectedTeam,
      teams,
      loading,
      error,
      months,
      groupedMatches,
      nextMonth,
      previousMonth,
      toggleTeamFilter,
      clearTeamFilter,
      getTeamName,
      filteredMatches,
      navigateToTeam,
    };
  },
};
</script>

<style scoped>
button {
  cursor: pointer;
}

.team-filter-button,
.team-filter-all {
  transition: all 0.2s ease;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.05);
}

.team-filter-button:hover,
.team-filter-all:hover {
  transform: translateY(-1px);
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.team-filter-button[class*='bg-blue-500'],
.team-filter-all[class*='bg-blue-500'] {
  box-shadow: 0 2px 4px rgba(59, 130, 246, 0.3);
}

.bg-white {
  border: 1px solid rgba(229, 231, 235, 0.8);
}

.hide-scrollbar {
  -ms-overflow-style: none;
  scrollbar-width: none;
}

.hide-scrollbar::-webkit-scrollbar {
  display: none;
}
</style>
