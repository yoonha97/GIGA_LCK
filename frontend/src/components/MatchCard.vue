<template>
  <div
    class="flex items-center justify-between px-6 py-3 bg-white hover:bg-gray-50 hover:shadow-md transition-all duration-200"
    @click="navigateToDetail"
  >
    <!-- 경기 시간 및 진행 상태 -->
    <div class="w-32 flex flex-col">
      <span class="text-sm text-gray-600">{{ time }}</span>
      <span class="text-xs text-gray-500">{{ stage }}</span>
    </div>

    <!-- 팀 정보 및 스코어 -->
    <div class="flex-1 flex items-center justify-between max-w-2xl">
      <div class="w-32 flex items-center justify-end">
        <span class="text-gray-800 font-semibold">{{ homeTeam }}</span>
        <div
          class="w-6 h-6 ml-2 flex items-center justify-center cursor-pointer"
          @click.stop="navigateToTeam(homeTeam)"
          title="팀 상세정보 보기"
        >
          <img
            :src="getTeamLogo(homeTeam)"
            class="max-w-full max-h-full object-contain"
          />
        </div>
      </div>

      <span class="w-20 text-center font-bold text-lg">{{ score }}</span>

      <div class="w-32 flex items-center justify-start">
        <div
          class="w-6 h-6 mr-2 flex items-center justify-center cursor-pointer"
          @click.stop="navigateToTeam(awayTeam)"
          title="팀 상세정보 보기"
        >
          <img
            :src="getTeamLogo(awayTeam)"
            class="max-w-full max-h-full object-contain"
          />
        </div>
        <span class="text-gray-800 font-semibold">{{ awayTeam }}</span>
      </div>
    </div>

    <!-- 다시보기 버튼 -->
    <div class="w-24 text-right">
      <a
        v-if="replayLink"
        href="#"
        target="_blank"
        class="px-2 py-1 bg-gray-200 rounded text-sm text-blue-600"
        @click.stop="openReplayLink"
      >
        다시보기
      </a>
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
    matchId: {
      type: [String, Number],
      required: true,
    },
    time: String,
    stage: String,
    homeTeam: String,
    score: String,
    awayTeam: String,
    replayLink: String,
  },
  setup(props) {
    const router = useRouter();

    const navigateToDetail = () => {
      router.push(`/matches/${props.matchId}`);
    };

    const navigateToTeam = (teamName) => {
      router.push({ name: 'TeamDetailByName', params: { teamName } });
    };

    const openReplayLink = (event) => {
      event.preventDefault(); // 기본 링크 동작 방지

      if (!props.replayLink) return;

      // YouTube 링크에서 최적의 URL 생성
      let finalUrl = props.replayLink;

      // youtube.com/watch?v=VIDEO_ID 형식 처리
      const watchMatch = props.replayLink.match(
        /youtube\.com\/watch\?v=([^&]+)/
      );
      if (watchMatch) {
        const videoId = watchMatch[1];
        // embed URL보다는 원래 URL로 이동 (사용자 경험 측면)
        finalUrl = `https://www.youtube.com/watch?v=${videoId}`;
      }

      // youtu.be/VIDEO_ID 형식 처리
      const shortMatch = props.replayLink.match(/youtu\.be\/([^?&]+)/);
      if (shortMatch) {
        const videoId = shortMatch[1];
        finalUrl = `https://www.youtube.com/watch?v=${videoId}`;
      }

      // 새 창에서 URL 열기
      window.open(finalUrl, '_blank');
    };

    return {
      navigateToDetail,
      navigateToTeam,
      openReplayLink,
    };
  },
  methods: {
    getTeamLogo(teamName) {
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
      return logos[teamName];
    },
  },
});
</script>

<style scoped>
img {
  display: inline-block;
}
</style>
