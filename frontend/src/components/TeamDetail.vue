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
            class="bg-white p-5 rounded-lg shadow-sm flex items-center space-x-4 hover:shadow-md transition-shadow cursor-pointer"
            @click="openPlayerModal(player)"
          >
            <div
              class="w-14 h-14 bg-blue-500 rounded-full flex items-center justify-center text-white text-xl font-bold hover:bg-blue-600 transition-colors"
              title="선수 상세 정보 보기"
            >
              <span>{{ getPlayerInitial(player) }}</span>
            </div>
            <div>
              <div class="font-medium text-lg">{{ getPlayerName(player) }}</div>
              <div class="text-gray-500 text-sm flex items-center">
                <span class="text-blue-500 text-xs">상세 정보 보기</span>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 최근 경기 결과 섹션 -->
      <div class="bg-gray-50 rounded-lg p-6">
        <h2
          class="text-2xl font-semibold mb-6 border-b pb-3 flex justify-between items-center"
        >
          <span>최근 경기 결과</span>
          <span
            v-if="teamStore.recentMatches.length > 0"
            class="text-gray-500 text-lg font-normal"
          >
            {{ getRecentWinsCount() }}승 {{ getRecentLossesCount() }}패
          </span>
        </h2>

        <!-- 로딩 상태 -->
        <div v-if="teamStore.matchesLoading" class="text-center py-8">
          <div
            class="w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto mb-4"
          ></div>
          <p class="text-gray-600">경기 정보를 불러오는 중...</p>
        </div>

        <!-- 에러 상태 -->
        <div
          v-else-if="teamStore.matchesError"
          class="bg-red-50 text-red-600 p-4 rounded-lg mb-4"
        >
          {{ teamStore.matchesError }}
          <button @click="loadRecentMatches" class="ml-2 underline">
            다시 시도
          </button>
        </div>

        <!-- 데이터 없음 -->
        <div
          v-else-if="teamStore.recentMatches.length === 0"
          class="text-gray-500 text-lg py-10 text-center"
        >
          최근 경기 데이터가 없습니다.
        </div>

        <!-- 경기 목록 -->
        <div v-else>
          <div class="space-y-3">
            <RecentMatchCard
              v-for="(match, index) in teamStore.recentMatches"
              :key="index"
              :match="match"
              :current-team-name="teamStore.currentTeam?.teamName"
              @match-click="navigateToMatchDetail"
            />
          </div>
        </div>
      </div>

      <!-- 팀 응원 댓글 섹션 -->
      <SupportSection :teamId="getTeamId()" />
    </div>

    <!-- 데이터 없음 -->
    <div v-else class="text-center text-gray-500 py-20 text-lg">
      팀 정보를 찾을 수 없습니다.
    </div>

    <!-- 선수 모달 -->
    <PlayerDetailModal
      v-if="isPlayerModalOpen"
      :is-open="isPlayerModalOpen"
      :game-name="currentPlayerGameName"
      :tag-line="currentPlayerTagLine"
      @close="closePlayerModal"
    />
  </div>
</template>

<script>
import { useTeamStore } from '@/stores/teamStore';
import { usePlayerStore } from '@/stores/playerStore';
import { onMounted, onUnmounted, ref, computed } from 'vue';
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
import PlayerDetailModal from '@/components/PlayerDetailModal.vue';
import RecentMatchCard from '@/components/RecentMatchCard.vue';

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

// 팀 전체 이름 매핑
const TEAM_FULL_NAMES = {
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

export default {
  components: {
    SupportSection,
    PlayerDetailModal,
    RecentMatchCard,
  },
  setup() {
    const teamStore = useTeamStore();
    const playerStore = usePlayerStore();
    const route = useRoute();
    const router = useRouter();

    // 선수 모달 관련 상태
    const isPlayerModalOpen = ref(false);
    const currentPlayerGameName = ref(null);
    const currentPlayerTagLine = ref(null);

    onMounted(async () => {
      const { teamId, teamName } = route.params;

      if (teamId) {
        await teamStore.loadTeamById(teamId);
        await loadRecentMatches();
      } else if (teamName) {
        await teamStore.loadTeamByName(teamName);
        await loadRecentMatches();
      }
    });

    onUnmounted(() => {
      teamStore.clearCurrentTeam();
      playerStore.clearCurrentPlayer();
    });

    // 최근 경기 불러오기
    const loadRecentMatches = async () => {
      const { teamId, teamName } = route.params;

      try {
        if (teamId) {
          await teamStore.loadRecentMatchesById(teamId);
        } else if (teamName) {
          await teamStore.loadRecentMatchesByName(teamName);
        } else if (teamStore.currentTeam?.teamId) {
          await teamStore.loadRecentMatchesById(teamStore.currentTeam.teamId);
        }
      } catch (err) {
        console.error('최근 경기 로드 중 오류 발생:', err);
      }
    };

    // 날짜 포맷팅
    const formatDate = (dateString) => {
      if (!dateString) return '날짜 없음';

      const date = new Date(dateString);
      const year = date.getFullYear();
      const month = String(date.getMonth() + 1).padStart(2, '0');
      const day = String(date.getDate()).padStart(2, '0');

      return `${year}-${month}-${day}`;
    };

    // 경기 결과 클래스 (스타일링)
    const getResultClass = (match) => {
      const homeTeam = match.homeTeam;
      const awayTeam = match.awayTeam;
      const homeScore = getHomeScore(match);
      const awayScore = getAwayScore(match);
      const currentTeamName = teamStore.currentTeam?.teamName;

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
      const currentTeamName = teamStore.currentTeam?.teamName;

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
      return TEAM_LOGOS[teamName] || '';
    };

    // 팀 전체 이름 가져오기
    const getTeamFullName = (teamName) => {
      return TEAM_FULL_NAMES[teamName] || teamName;
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

    // 경기 상세 페이지로 이동
    const navigateToMatchDetail = (matchId) => {
      if (!matchId) {
        console.error('Match ID is missing');
        return;
      }

      router.push({
        name: 'match-detail',
        params: { id: matchId.toString() },
      });
    };

    const getTeamId = () => {
      return route.params.teamId || route.params.teamName;
    };

    // 선수 카드 클릭 시 모달 열기 (playerStore 사용)
    const openPlayerModal = async (player) => {
      // 모달 열기
      isPlayerModalOpen.value = true;

      // 선수 이름 추출
      const playerName =
        typeof player === 'object' ? player.playerName : player;

      console.log('Opening player modal for:', playerName, player);

      // playerStore를 통해 선수 정보 로드
      await playerStore.loadPlayerByName(playerName);

      // 선수 정보를 로드한 후 Riot ID와 tagLine 설정
      if (playerStore.currentPlayer) {
        // API에서 정보 가져오기
        currentPlayerGameName.value = playerStore.currentPlayer.riotId || null;
        currentPlayerTagLine.value = playerStore.currentPlayer.tagLine || null;

        // 실제 선수 정보 로그
        console.log('Player account info:', {
          riotId: currentPlayerGameName.value,
          tagLine: currentPlayerTagLine.value,
        });
      }
    };

    // 선수 모달 닫기
    const closePlayerModal = () => {
      isPlayerModalOpen.value = false;
      currentPlayerGameName.value = null;
      currentPlayerTagLine.value = null;
      playerStore.clearCurrentPlayer();
    };

    // 선수 이름 추출
    const getPlayerName = (player) => {
      if (typeof player === 'object') {
        return player.playerName;
      } else if (typeof player === 'string') {
        return player;
      }
      return '선수 이름 정보 없음';
    };

    // 선수 초기 문자 추출
    const getPlayerInitial = (player) => {
      if (typeof player === 'object') {
        return player.playerName.charAt(0);
      } else if (typeof player === 'string') {
        return player.charAt(0);
      }
      return '선수 초기 문자 정보 없음';
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
            if (match.homeTeam === teamStore.currentTeam?.teamName) {
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
            if (match.homeTeam === teamStore.currentTeam?.teamName) {
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

    // Environment check for debug output
    const isDevelopment = computed(() => {
      return (
        import.meta.env.MODE === 'development' || import.meta.env.DEV === true
      );
    });

    // 최근 경기 승패 기록 계산
    const getRecentWinsCount = () => {
      if (!teamStore.recentMatches || !teamStore.recentMatches.length) return 0;

      return teamStore.recentMatches.filter((match) => {
        const homeTeam = match.homeTeam;
        const awayTeam = match.awayTeam;
        const homeScore = getHomeScore(match);
        const awayScore = getAwayScore(match);
        const currentTeamName = teamStore.currentTeam?.teamName;

        if (homeTeam === currentTeamName) {
          return homeScore > awayScore;
        } else if (awayTeam === currentTeamName) {
          return awayScore > homeScore;
        }
        return false;
      }).length;
    };

    const getRecentLossesCount = () => {
      if (!teamStore.recentMatches || !teamStore.recentMatches.length) return 0;

      return teamStore.recentMatches.filter((match) => {
        const homeTeam = match.homeTeam;
        const awayTeam = match.awayTeam;
        const homeScore = getHomeScore(match);
        const awayScore = getAwayScore(match);
        const currentTeamName = teamStore.currentTeam?.teamName;

        if (homeTeam === currentTeamName) {
          return homeScore < awayScore;
        } else if (awayTeam === currentTeamName) {
          return awayScore < homeScore;
        }
        return false;
      }).length;
    };

    const getRecentDrawsCount = () => {
      if (!teamStore.recentMatches || !teamStore.recentMatches.length) return 0;

      return teamStore.recentMatches.filter((match) => {
        const homeTeam = match.homeTeam;
        const awayTeam = match.awayTeam;
        const homeScore = getHomeScore(match);
        const awayScore = getAwayScore(match);
        const currentTeamName = teamStore.currentTeam?.teamName;

        if (
          (homeTeam === currentTeamName || awayTeam === currentTeamName) &&
          homeScore === awayScore
        ) {
          return true;
        }
        return false;
      }).length;
    };

    return {
      teamStore,
      playerStore,
      getTeamLogo,
      getTeamFullName,
      navigateToTeam,
      getTeamId,
      isPlayerModalOpen,
      openPlayerModal,
      closePlayerModal,
      getPlayerName,
      getPlayerInitial,
      currentPlayerGameName,
      currentPlayerTagLine,
      formatDate,
      getResultClass,
      getResultText,
      loadRecentMatches,
      getHomeScore,
      getAwayScore,
      isDevelopment,
      getRecentWinsCount,
      getRecentLossesCount,
      getRecentDrawsCount,
      navigateToMatchDetail,
    };
  },
};
</script>
