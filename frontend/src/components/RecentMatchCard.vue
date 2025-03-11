<template>
  <div
    class="bg-white rounded-lg p-3 shadow-sm hover:shadow-md transition-shadow cursor-pointer"
    @click="handleMatchClick"
  >
    <div class="flex flex-col md:flex-row justify-between mt-1 mb-2">
      <div class="text-md font-medium mb-2 md:mb-0">
        {{ formatDate(match.date) }}
      </div>
      <!-- 경기 세부 정보 -->
      <div class="flex items-center justify-center py-4">
        <!-- Home Team -->
        <div class="flex-1 flex justify-end items-center">
          <div class="text-right mr-2">
            <div class="font-medium">{{ match.homeTeam }}</div>
          </div>
          <div
            class="w-10 h-10 bg-gray-100 rounded-full p-1 flex items-center justify-center"
          >
            <img
              :src="getTeamLogo(match.homeTeam)"
              alt="홈팀 로고"
              class="w-7 h-7 object-contain"
            />
          </div>
        </div>

        <!-- Score -->
        <div class="mx-3 px-3 py-1 bg-gray-100 rounded-lg flex items-center">
          <span class="font-bold text-md text-gray-800">{{
            getHomeScore(match)
          }}</span>
          <span class="mx-1 text-gray-400 font-bold">:</span>
          <span class="font-bold text-md text-gray-800">{{
            getAwayScore(match)
          }}</span>
        </div>

        <!-- Away Team -->
        <div class="flex-1 flex items-center">
          <div
            class="w-10 h-10 bg-gray-100 rounded-full p-1 flex items-center justify-center"
          >
            <img
              :src="getTeamLogo(match.awayTeam)"
              alt="원정팀 로고"
              class="w-7 h-7 object-contain"
            />
          </div>
          <div class="ml-2">
            <div class="font-medium">{{ match.awayTeam }}</div>
          </div>
        </div>
      </div>
      <!-- 경기 정보 -->
      <div class="flex flex-col items-center justify-center">
        <div class="text-xs text-gray-500">
          {{ match.stage || '공식 경기' }}
        </div>
        <div class="mt-2 flex justify-center">
          <div
            :class="getResultClass(match)"
            class="text-xs py-1 px-3 rounded-full flex items-center text-center"
          >
            <span>{{ getResultText(match) }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
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

// 팀 로고 매핑
const TEAM_LOGOS = {
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

export default {
  name: 'RecentMatchCard',
  props: {
    match: {
      type: Object,
      required: true,
    },
    currentTeamName: {
      type: String,
      required: true,
    },
  },
  emits: ['match-click'],
  setup(props, { emit }) {
    // Handle match click with proper error checking
    const handleMatchClick = () => {
      const matchId = props.match.matchId || props.match.id;
      if (matchId) {
        console.log('Navigating to match:', matchId);
        emit('match-click', matchId);
      } else {
        console.error('No match ID available:', props.match);
      }
    };

    // 날짜 포맷팅
    const formatDate = (dateString) => {
      if (!dateString) return '날짜 없음';

      try {
        const date = new Date(dateString);
        if (isNaN(date.getTime())) return '날짜 형식 오류';

        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');

        return `${year}-${month}-${day}`;
      } catch (err) {
        console.error('Date formatting error:', err);
        return '날짜 없음';
      }
    };

    // 경기 결과 클래스 (스타일링)
    const getResultClass = (match) => {
      const homeTeam = match.homeTeam;
      const awayTeam = match.awayTeam;
      const homeScore = getHomeScore(match);
      const awayScore = getAwayScore(match);
      const currentTeamName = props.currentTeamName;

      // 현재 팀이 홈팀인 경우
      if (homeTeam === currentTeamName) {
        if (homeScore > awayScore) return 'bg-green-100 text-green-800';
        if (homeScore < awayScore) return 'bg-red-100 text-red-800';
        return 'bg-gray-100 text-gray-800';
      }

      // 현재 팀이 원정팀인 경우
      if (awayTeam === currentTeamName) {
        if (awayScore > homeScore) return 'bg-green-100 text-green-800';
        if (awayScore < homeScore) return 'bg-red-100 text-red-800';
        return 'bg-gray-100 text-gray-800';
      }

      return 'bg-gray-100 text-gray-800';
    };

    // 경기 결과 텍스트
    const getResultText = (match) => {
      const homeTeam = match.homeTeam;
      const awayTeam = match.awayTeam;
      const homeScore = getHomeScore(match);
      const awayScore = getAwayScore(match);
      const currentTeamName = props.currentTeamName;

      // 현재 팀이 홈팀인 경우
      if (homeTeam === currentTeamName) {
        if (homeScore > awayScore) return '승리';
        if (homeScore < awayScore) return '패배';
        return '무승부';
      }

      // 현재 팀이 원정팀인 경우
      if (awayTeam === currentTeamName) {
        if (awayScore > homeScore) return '승리';
        if (awayScore < homeScore) return '패배';
        return '무승부';
      }

      return '정보 없음';
    };

    // 팀 로고 가져오기
    const getTeamLogo = (teamName) => {
      if (!teamName) return '';
      return TEAM_LOGOS[teamName] || '';
    };

    // Try different possible score field names
    const getHomeScore = (match) => {
      if (!match) return 0;

      try {
        // 여러 가능한 필드명 시도
        if (match.homeTeamScore !== undefined)
          return Number(match.homeTeamScore);
        if (match.homeScore !== undefined) return Number(match.homeScore);

        // score 필드가 있으면 파싱 시도 (예: "3-1" 형식)
        if (match.score) {
          const parts = match.score.split('-');
          if (parts.length === 2 && !isNaN(parseInt(parts[0]))) {
            return parseInt(parts[0]);
          }
        }

        // result 필드가 있으면 파싱 시도
        if (match.result) {
          // "승" 또는 "패" 등의 텍스트가 아니라 숫자 형식 확인
          const resultMatch = match.result.match(/(\d+)[^\d]+(\d+)/);
          if (resultMatch && resultMatch.length >= 3) {
            // 현재 팀이 홈팀인지 원정팀인지에 따라 다름
            if (match.homeTeam === props.currentTeamName) {
              return parseInt(resultMatch[1]); // 첫 번째 숫자
            } else {
              return parseInt(resultMatch[2]); // 두 번째 숫자
            }
          }
        }
      } catch (err) {
        console.error('Error calculating home score:', err);
      }

      return 0; // 기본값
    };

    const getAwayScore = (match) => {
      if (!match) return 0;

      try {
        // 여러 가능한 필드명 시도
        if (match.awayTeamScore !== undefined)
          return Number(match.awayTeamScore);
        if (match.awayScore !== undefined) return Number(match.awayScore);

        // score 필드가 있으면 파싱 시도 (예: "3-1" 형식)
        if (match.score) {
          const parts = match.score.split('-');
          if (parts.length === 2 && !isNaN(parseInt(parts[1]))) {
            return parseInt(parts[1]);
          }
        }

        // result 필드가 있으면 파싱 시도
        if (match.result) {
          // "승" 또는 "패" 등의 텍스트가 아니라 숫자 형식 확인
          const resultMatch = match.result.match(/(\d+)[^\d]+(\d+)/);
          if (resultMatch && resultMatch.length >= 3) {
            // 현재 팀이 홈팀인지 원정팀인지에 따라 다름
            if (match.homeTeam === props.currentTeamName) {
              return parseInt(resultMatch[2]); // 두 번째 숫자
            } else {
              return parseInt(resultMatch[1]); // 첫 번째 숫자
            }
          }
        }
      } catch (err) {
        console.error('Error calculating away score:', err);
      }

      return 0; // 기본값
    };

    return {
      formatDate,
      getResultClass,
      getResultText,
      getTeamLogo,
      getHomeScore,
      getAwayScore,
      handleMatchClick,
    };
  },
};
</script>
