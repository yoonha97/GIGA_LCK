<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
    <!-- 배경 클릭 시 모달 닫기 -->
    <div
      class="fixed inset-0 bg-black bg-opacity-50 transition-opacity"
      @click="closeModal"
    ></div>

    <!-- 모달 -->
    <div class="flex items-center justify-center min-h-screen p-4">
      <div class="relative bg-white rounded-lg shadow-xl max-w-2xl w-full p-6">
        <div class="flex justify-between items-center mb-6">
          <h3 class="text-2xl font-bold">선수 상세 정보</h3>
          <button
            @click="closeModal"
            class="text-gray-500 hover:text-gray-700 transition-colors"
          >
            <svg
              xmlns="http://www.w3.org/2000/svg"
              class="h-6 w-6"
              fill="none"
              viewBox="0 0 24 24"
              stroke="currentColor"
            >
              <path
                stroke-linecap="round"
                stroke-linejoin="round"
                stroke-width="2"
                d="M6 18L18 6M6 6l12 12"
              />
            </svg>
          </button>
        </div>

        <!-- 로딩 상태 표시 -->
        <div
          v-if="playerStore.loading"
          class="py-12 flex flex-col items-center justify-center"
        >
          <div
            class="w-12 h-12 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mb-4"
          ></div>
          <p class="text-gray-600">선수 정보를 불러오는 중...</p>
        </div>

        <div v-else-if="playerStore.currentPlayer">
          <div class="flex items-center mb-6">
            <div
              class="w-20 h-20 bg-blue-500 rounded-full flex items-center justify-center text-white text-3xl font-bold mr-4"
            >
              <span>{{ playerInitial }}</span>
            </div>
            <div>
              <h4 class="text-xl font-bold">
                {{ playerStore.currentPlayer.name }}
              </h4>
              <p
                v-if="playerStore.currentPlayer.realName"
                class="text-gray-600"
              >
                {{ playerStore.currentPlayer.realName }}
              </p>
              <p class="text-gray-600">
                {{ playerStore.playerPosition }}
              </p>
            </div>
          </div>

          <div class="border-t pt-4">
            <!-- AI 분석 섹션 -->
            <div class="mb-4">
              <h5 class="font-semibold text-lg mb-2">AI 분석</h5>

              <!-- 분석 로딩 중 -->
              <div v-if="playerStore.analysisLoading" class="py-4">
                <div class="flex items-center">
                  <div
                    class="w-5 h-5 border-2 border-blue-500 border-t-transparent rounded-full animate-spin mr-2"
                  ></div>
                  <p class="text-gray-600 text-sm">
                    분석 데이터를 불러오는 중...
                  </p>
                </div>
              </div>

              <!-- 분석 에러 -->
              <div v-else-if="playerStore.analysisError" class="py-2">
                <p class="text-red-500 text-sm">
                  {{ playerStore.analysisError }}
                </p>
                <button
                  v-if="showAnalysisButton"
                  @click="loadAnalysis"
                  class="mt-2 bg-blue-500 text-white px-3 py-1.5 rounded text-sm hover:bg-blue-600"
                >
                  분석 데이터 불러오기
                </button>
              </div>

              <!-- 분석 정보 표시 -->
              <div v-else-if="playerStore.playerAnalysis" class="py-2">
                <div class="prose prose-sm max-w-none">
                  <div v-html="formattedAnalysis"></div>
                </div>
              </div>

              <!-- 분석 버튼 표시 -->
              <div v-else class="py-2">
                <button
                  v-if="showAnalysisButton"
                  @click="loadAnalysis"
                  class="bg-blue-500 text-white px-3 py-1.5 rounded text-sm hover:bg-blue-600"
                >
                  AI 분석 데이터 불러오기
                </button>
                <p v-else class="text-gray-500 text-sm">
                  {{
                    hasPlayerInfo
                      ? '분석 가능한 계정 정보가 없습니다.'
                      : '해당 선수의 분석 데이터가 없습니다.'
                  }}
                </p>
              </div>
            </div>
          </div>
        </div>

        <div v-else-if="playerStore.error" class="py-12 text-center">
          <p class="text-red-500">{{ playerStore.error }}</p>
        </div>

        <div v-else class="py-12 text-center">
          <p class="text-gray-600">선수 정보가 없습니다.</p>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { computed, ref, watch, onMounted } from 'vue';
import { usePlayerStore } from '@/stores/playerStore';

export default {
  name: 'PlayerDetailModal',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
    gameName: {
      type: String,
      default: null,
    },
    tagLine: {
      type: String,
      default: null,
    },
  },
  emits: ['close'],
  setup(props, { emit }) {
    const playerStore = usePlayerStore();

    // 디버깅 로그
    console.log('PlayerDetailModal props:', {
      isOpen: props.isOpen,
      gameName: props.gameName,
      tagLine: props.tagLine,
    });

    const showAnalysisButton = computed(() => {
      const hasValues = props.gameName && props.tagLine;
      console.log(
        'showAnalysisButton computed:',
        hasValues,
        props.gameName,
        props.tagLine
      );
      return hasValues;
    });

    // 선수 데이터가 로드되었는지 확인
    const hasPlayerInfo = computed(() => {
      return playerStore.currentPlayer != null;
    });

    // 마크다운 스타일의 분석 데이터를 HTML로 변환
    const formattedAnalysis = computed(() => {
      if (!playerStore.playerAnalysis || !playerStore.playerAnalysis.analysis) {
        return '';
      }

      // 분석 텍스트에서 마크다운 포맷을 HTML로 변환
      let text = playerStore.playerAnalysis.analysis;

      // 제목 변환
      text = text.replace(
        /##\s+(.*?)(?:\n|$)/g,
        '<h3 class="font-bold text-lg mt-3 mb-2">$1</h3>'
      );

      // 강조 텍스트 변환
      text = text.replace(/\*\*(.*?)\*\*/g, '<strong>$1</strong>');

      // 줄바꿈 유지
      text = text.replace(/\n\n/g, '</p><p class="mb-2">');

      return `<p class="mb-2">${text}</p>`;
    });

    // 선수 이니셜 계산
    const playerInitial = computed(() => {
      return playerStore.currentPlayer?.name
        ? playerStore.currentPlayer.name.charAt(0)
        : '';
    });

    // 분석 데이터 로드
    const loadAnalysis = async () => {
      console.log('loadAnalysis called with:', props.gameName, props.tagLine);
      if (props.gameName && props.tagLine) {
        await playerStore.loadPlayerAnalysis(props.gameName, props.tagLine);
      } else {
        console.error('Cannot load analysis: gameName or tagLine is missing', {
          gameName: props.gameName,
          tagLine: props.tagLine,
        });
      }
    };

    // 모달이 열리면 자동으로 분석 데이터 로드
    const autoLoadAnalysis = async () => {
      if (props.isOpen && showAnalysisButton.value) {
        console.log('Auto-loading analysis data');
        await loadAnalysis();
      }
    };

    // props.isOpen이 변경될 때 자동으로 분석 데이터 로드
    watch(
      () => props.isOpen,
      async (isOpen) => {
        if (isOpen && showAnalysisButton.value) {
          console.log('Modal opened, auto-loading analysis');
          await loadAnalysis();
        }
      }
    );

    // 컴포넌트가 마운트될 때 자동으로 분석 데이터 로드
    onMounted(async () => {
      if (props.isOpen && showAnalysisButton.value) {
        console.log('Component mounted, auto-loading analysis');
        await loadAnalysis();
      }
    });

    const closeModal = () => {
      emit('close');
    };

    return {
      playerStore,
      playerInitial,
      closeModal,
      loadAnalysis,
      showAnalysisButton,
      formattedAnalysis,
      hasPlayerInfo,
    };
  },
};
</script>

<style>
.prose {
  color: #374151;
  max-width: 65ch;
}
.prose p {
  margin-bottom: 0.75em;
}
.prose strong {
  font-weight: 600;
  color: #111827;
}
</style>
