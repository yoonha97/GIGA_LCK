import { defineStore } from 'pinia';
import { getDetailedPlayerInfo } from '@/api/player';
import { getPlayerAnalysis } from '@/api/analysis';

export const usePlayerStore = defineStore('player', {
  state: () => ({
    players: {}, // 플레이어 데이터 캐시 (키: 플레이어 이름)
    currentPlayer: null, // 현재 선택된 플레이어
    playerAnalysis: null, // 플레이어 분석 데이터
    loading: false, // 로딩 상태
    analysisLoading: false, // 분석 데이터 로딩 상태
    error: null, // 에러 상태
    analysisError: null, // 분석 데이터 에러 상태
  }),

  actions: {
    async loadPlayerByName(playerName) {
      // 이미 로드된 플레이어가 있으면 캐시에서 사용
      if (this.players[playerName]) {
        console.log(
          '플레이어 캐시에서 로드:',
          playerName,
          this.players[playerName]
        );
        this.currentPlayer = this.players[playerName];
        return this.currentPlayer;
      }

      // 새로 로드해야 하는 경우
      this.loading = true;
      this.error = null;

      try {
        const playerData = await getDetailedPlayerInfo(playerName);
        console.log('API에서 플레이어 로드:', playerName, playerData);

        if (!playerData) {
          throw new Error('플레이어 정보를 찾을 수 없습니다.');
        }

        this.players[playerName] = playerData;
        this.currentPlayer = playerData;
        return playerData;
      } catch (error) {
        this.error = '플레이어 정보를 불러오는 중 오류가 발생했습니다.';
        console.error('선수 정보를 불러오는 중 오류 발생:', error);
        return null;
      } finally {
        this.loading = false;
      }
    },

    // 플레이어 분석 정보 로드
    async loadPlayerAnalysis(gameName, tagLine) {
      console.log(
        `loadPlayerAnalysis called with gameName=${gameName}, tagLine=${tagLine}`
      );

      if (!gameName || !tagLine) {
        console.error('Cannot load analysis: missing required parameters', {
          gameName,
          tagLine,
        });
        this.analysisError =
          '분석 정보를 불러오기 위한 필수 매개변수가 누락되었습니다.';
        return null;
      }

      this.analysisLoading = true;
      this.analysisError = null;
      this.playerAnalysis = null;

      try {
        console.log('Calling getPlayerAnalysis API function');
        const analysisData = await getPlayerAnalysis(gameName, tagLine);
        console.log('분석 데이터 로드:', analysisData);

        if (!analysisData) {
          throw new Error('분석 정보를 찾을 수 없습니다.');
        }

        this.playerAnalysis = analysisData;
        return analysisData;
      } catch (error) {
        // 개발 환경에서는 더 자세한 오류 메시지 표시
        if (import.meta.env.DEV) {
          this.analysisError = `분석 정보를 불러오는 중 오류가 발생했습니다. 세부 정보: ${
            error.message || '알 수 없는 오류'
          }`;
        } else {
          this.analysisError = '분석 정보를 불러오는 중 오류가 발생했습니다.';
        }
        console.error('분석 정보를 불러오는 중 오류 발생:', error);
        return null;
      } finally {
        this.analysisLoading = false;
      }
    },

    clearCurrentPlayer() {
      this.currentPlayer = null;
      this.playerAnalysis = null;
    },
  },

  getters: {
    // 포지션 정보 반환 (영어 그대로 사용)
    playerPosition: (state) => {
      console.log('현재 플레이어 상태:', state.currentPlayer);

      if (!state.currentPlayer) {
        return 'Unknown';
      }

      // API에서 가져온 player는 position 속성이 있음
      if (state.currentPlayer.position) {
        console.log('포지션 정보 찾음:', state.currentPlayer.position);
        return state.currentPlayer.position;
      }

      console.log('포지션 정보 없음');
      return 'Unknown';
    },
  },
});
