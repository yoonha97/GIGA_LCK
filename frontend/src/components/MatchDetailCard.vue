<template>
  <div class="flex flex-col items-center px-6 py-3 bg-white">
    <!-- 공유 섹션 추가 -->
    <div class="flex items-center justify-end w-full mb-4 border-b pb-2">
      <div class="flex space-x-2">
        <button
          class="flex items-center px-3 py-1 bg-gray-100 hover:bg-gray-200 rounded text-sm transition-colors"
          @click="copyLinkToClipboard"
        >
          <span class="material-icons-outlined text-sm mr-1">content_copy</span>
          복사
        </button>
        <button
          class="flex items-center px-3 py-1 bg-yellow-100 hover:bg-yellow-200 rounded text-sm transition-colors"
          @click="shareToKakao"
        >
          <img
            src="@/assets/images/kakao_icon.png"
            alt="카카오톡"
            class="w-3 h-3 mr-1"
          />
          공유
        </button>
      </div>
    </div>

    <!-- 경기 진행 상태 -->
    <div class="text-lg text-gray-600">{{ date }} {{ time }}</div>
    <div class="text-center mb-3">
      <span class="text-2xl text-gray-500">{{ stage }}</span>
    </div>

    <!-- 팀 정보 및 스코어 -->
    <div class="flex items-center justify-between w-full max-w-2xl mt-6">
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
import { defineComponent, onMounted } from 'vue';
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
  setup(props) {
    const router = useRouter();

    // 카카오톡 SDK 초기화
    onMounted(() => {
      initKakaoSDK();
    });

    const initKakaoSDK = () => {
      if (window.Kakao && !window.Kakao.isInitialized()) {
        const kakaoAppKey = '1a716bca082450c8f74ba18b9b79b668';
        window.Kakao.init(kakaoAppKey);
        console.log('Kakao SDK initialized');
      }
    };

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

    // 클립보드에 링크 복사
    const copyLinkToClipboard = () => {
      const url = window.location.href;

      navigator.clipboard
        .writeText(url)
        .then(() => {
          // 복사 성공 시 알림 표시
          alert('링크가 클립보드에 복사되었습니다!');
        })
        .catch((err) => {
          console.error('클립보드 복사 실패:', err);
          alert('링크 복사에 실패했습니다. 다시 시도해주세요.');
        });
    };

    // 팀 로고 URL 가져오기 (카카오톡 공유용)
    const getTeamLogoUrl = (teamName) => {
      // 실제 배포 환경에서의 팀 로고 전체 URL
      // 로컬 파일은 카카오톡 공유에서 작동하지 않으므로 실제 웹에 호스팅된 이미지 URL을 사용해야 함
      const baseUrl = 'https://your-domain.com/images/'; // 실제 배포 도메인으로 변경 필요

      // 팀 이름에 따라 로고 URL 반환
      switch (teamName) {
        case 'T1':
          return `${baseUrl}T1.png`;
        case 'GEN':
          return `${baseUrl}GEN.png`;
        // 다른 팀들에 대한 매핑 추가
        default:
          return `${baseUrl}default-team.png`;
      }
    };

    // 카카오톡으로 공유
    const shareToKakao = () => {
      if (!window.Kakao || !window.Kakao.isInitialized()) {
        console.error('Kakao SDK가 초기화되지 않았습니다.');
        alert('카카오톡 공유 기능을 사용할 수 없습니다.');
        return;
      }

      // 공유할 이미지 URL (홈팀 로고)
      const imageUrl = getTeamLogoUrl(props.homeTeam);

      // 공유할 URL
      const url = window.location.href;

      window.Kakao.Link.sendDefault({
        objectType: 'feed',
        content: {
          title: `${props.homeTeam} vs ${props.awayTeam} 경기 정보`,
          description: `${props.date} ${props.time} | ${props.stage} | 스코어: ${props.score}`,
          imageUrl: imageUrl,
          link: {
            mobileWebUrl: url,
            webUrl: url,
          },
        },
        buttons: [
          {
            title: '경기 정보 보기',
            link: {
              mobileWebUrl: url,
              webUrl: url,
            },
          },
        ],
      });
    };

    return {
      getTeamLogo,
      navigateToTeam,
      copyLinkToClipboard,
      shareToKakao,
    };
  },
});
</script>

<style scoped>
img {
  display: inline-block;
}
</style>
