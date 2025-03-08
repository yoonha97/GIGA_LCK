<template>
  <div class="flex flex-col items-center px-6 py-3 bg-white">
    <!-- 경기 날짜, 시간 및 진행 상태 -->
    <div class="text-center mb-3">
      <span class="text-xl text-gray-600 block mb-2">
        <span class="mr-4">{{ date }}</span>
        <span>{{ time }}</span>
      </span>
      <span class="text-2xl text-gray-500">{{ stage }}</span>
    </div>

    <!-- 팀 정보 및 스코어 -->
    <div class="flex items-center justify-between w-full max-w-2xl mt-10">
      <!-- 홈팀 -->
      <div class="w-32 flex flex-col items-center">
        <div
          class="w-16 h-16 flex items-center justify-center cursor-pointer rounded-lg transition-colors"
          @click="navigateToTeam(homeTeam)"
          title="팀 상세 페이지로 이동"
        >
          <img
            :src="getTeamLogo(homeTeam)"
            class="max-w-full max-h-full object-contain"
          />
        </div>
        <span class="text-gray-800 font-semibold mt-2">{{ homeTeam }}</span>
      </div>

      <!-- 스코어 -->
      <span class="w-20 text-center font-bold text-4xl">{{ score }}</span>

      <!-- 원정팀 -->
      <div class="w-32 flex flex-col items-center">
        <div
          class="w-16 h-16 flex items-center justify-center cursor-pointer rounded-lg transition-colors"
          @click="navigateToTeam(awayTeam)"
          title="팀 상세 페이지로 이동"
        >
          <img
            :src="getTeamLogo(awayTeam)"
            class="max-w-full max-h-full object-contain"
          />
        </div>
        <span class="text-gray-800 font-semibold mt-2">{{ awayTeam }}</span>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';
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

export default defineComponent({
  props: {
    date: {
      type: String,
      required: true,
    },
    time: {
      type: String,
      required: true,
    },
    stage: {
      type: String,
      required: true,
    },
    homeTeam: {
      type: String,
      required: true,
    },
    score: {
      type: String,
      required: true,
    },
    awayTeam: {
      type: String,
      required: true,
    },
  },
  setup() {
    const router = useRouter();

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

    const navigateToTeam = (teamName) => {
      router.push({
        name: 'TeamDetailByName',
        params: { teamName },
      });
    };

    return {
      getTeamLogo,
      navigateToTeam,
    };
  },
});
</script>

<style scoped>
img {
  display: inline-block;
}
</style>
