<template>
  <div class="min-h-screen bg-gray-100 py-10">
    <div class="container mx-auto px-4">
      <!-- 헤더 -->
      <div class="text-center mb-12">
        <h1 class="text-3xl md:text-4xl font-bold mb-4">LCK 팀 목록</h1>
        <p class="text-gray-600 text-lg max-w-3xl mx-auto">
          리그 오브 레전드 챔피언스 코리아(LCK)에 참가하는 모든 팀들을
          확인하세요.<br />팀 로고를 클릭하면 각 팀의 상세 정보 페이지로
          이동합니다.
        </p>
      </div>

      <!-- 팀 목록 그리드 -->
      <div
        class="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 xl:grid-cols-5 gap-6"
      >
        <div
          v-for="team in teams"
          :key="team.id"
          class="bg-white rounded-lg overflow-hidden shadow-md hover:shadow-lg transition-all duration-300 cursor-pointer"
          @click="navigateToTeam(team.id)"
        >
          <div class="p-4 flex flex-col items-center">
            <div
              class="bg-gray-50 w-40 h-40 rounded-lg p-6 mb-4 flex items-center justify-center"
            >
              <img
                :src="team.logo"
                :alt="team.name"
                class="max-w-full max-h-full object-contain"
              />
            </div>
            <h3 class="text-xl font-bold text-center">{{ team.name }}</h3>
            <p class="text-gray-500 text-center mt-1">{{ team.fullName }}</p>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref } from 'vue';
import { useRouter } from 'vue-router';
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
  name: 'TeamListView',
  setup() {
    const router = useRouter();

    // 팀 데이터
    const teams = ref([
      { id: 'T1', name: 'T1', fullName: 'SK Telecom T1', logo: T1Logo },
      { id: 'GEN', name: 'GEN', fullName: 'Gen.G Esports', logo: GENLogo },
      { id: 'DK', name: 'DK', fullName: 'Dplus KIA', logo: DKLogo },
      {
        id: 'BRO',
        name: 'BRO',
        fullName: 'OKSavingsBank BRION',
        logo: BROLogo,
      },
      { id: 'NS', name: 'NS', fullName: 'Nongshim RedForce', logo: NSLogo },
      { id: 'DRX', name: 'DRX', fullName: 'DRX', logo: DRXLogo },
      {
        id: 'HLE',
        name: 'HLE',
        fullName: 'Hanwha Life Esports',
        logo: HLELogo,
      },
      { id: 'KT', name: 'KT', fullName: 'KT Rolster', logo: KTLogo },
      { id: 'DNF', name: 'DNF', fullName: 'DN FREECS', logo: DNFLogo },
      { id: 'BFX', name: 'BFX', fullName: 'BNK FEARX', logo: BFXLogo },
    ]);

    // 팀 상세 페이지로 이동
    const navigateToTeam = (teamId) => {
      router.push({
        name: 'TeamDetailByName',
        params: { teamName: teamId },
      });
    };

    return {
      teams,
      navigateToTeam,
    };
  },
};
</script>
