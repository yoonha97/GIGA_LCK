<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
    <!-- 배경 클릭 시 모달 닫기 -->
    <div
      class="fixed inset-0 bg-black bg-opacity-50 transition-opacity"
      @click="closeModal"
    ></div>

    <!-- 모달 -->
    <div class="flex items-center justify-center min-h-screen p-4">
      <div
        class="relative bg-white rounded-lg shadow-xl max-w-2xl w-full p-6 max-h-[90vh] overflow-y-auto"
      >
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
                <div
                  class="bg-red-50 border border-red-200 rounded-lg p-4 mb-3"
                >
                  <p class="text-red-600 text-sm font-medium">
                    {{ playerStore.analysisError }}
                  </p>
                  <div class="mt-2 text-xs text-gray-600" v-if="isDev">
                    <p>디버그 정보:</p>
                    <p>Game Name: {{ props.gameName }}</p>
                    <p>Tag Line: {{ props.tagLine }}</p>
                  </div>
                </div>
                <button
                  v-if="showAnalysisButton"
                  @click="loadAnalysis"
                  class="mt-2 bg-blue-500 text-white px-3 py-1.5 rounded text-sm hover:bg-blue-600"
                >
                  다시 시도하기
                </button>
              </div>

              <!-- 분석 정보 표시 -->
              <div v-else-if="playerStore.playerAnalysis" class="py-2">
                <!-- 계정 정보 표시 -->
                <div class="mb-4 flex items-center bg-blue-50 rounded-lg p-3">
                  <div
                    class="bg-blue-100 text-blue-800 font-semibold rounded-full py-1 px-3 text-sm mr-2"
                  >
                    {{ playerStore.playerAnalysis.gameName }}#{{
                      playerStore.playerAnalysis.tagLine
                    }}
                  </div>
                  <span class="text-gray-500 text-sm">계정 분석 데이터</span>
                </div>

                <!-- 분석 내용 -->
                <div
                  class="prose prose-sm max-w-none bg-white p-4 rounded-lg border border-gray-200"
                >
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

      // 분석 텍스트 가져오기
      let text = playerStore.playerAnalysis.analysis;

      // '#' 기호로 분리된 섹션 처리 (API 응답 형식에 맞게)
      const sections = text
        .split('#')
        .filter((section) => section.trim() !== '');

      let formattedHtml = '';

      // 각 섹션을 개별적으로 처리
      sections.forEach((section) => {
        section = section.trim();

        // 섹션 제목과 내용 분리
        const sectionParts = section.split('\n', 2);
        const title = sectionParts[0].trim();
        const content = section.substring(title.length).trim();

        // 섹션 HTML 생성
        formattedHtml += `
          <div class="mb-6">
            <h3 class="text-lg font-bold text-blue-800 mb-3">${title}</h3>
            <div class="text-gray-700">
        `;

        // 내용 처리: 단락으로 분리
        const paragraphs = content.split('\n\n').filter((p) => p.trim() !== '');

        // 각 단락 처리
        paragraphs.forEach((paragraph) => {
          // 목록 항목 처리 (예: - 항목1)
          if (paragraph.trim().startsWith('-')) {
            formattedHtml += '<ul class="list-disc pl-6 mb-2 space-y-1.5">';

            paragraph.split('\n').forEach((item) => {
              if (item.trim().startsWith('-')) {
                const itemContent = item.trim().substring(1).trim();
                formattedHtml += `<li>${itemContent}</li>`;
              }
            });

            formattedHtml += '</ul>';
          } else {
            // 일반 단락 처리
            formattedHtml += `<p class="mb-3">${paragraph}</p>`;
          }
        });

        formattedHtml += '</div></div>';
      });

      // 강조 텍스트 변환 (**: 볼드)
      formattedHtml = formattedHtml.replace(
        /\*\*(.*?)\*\*/g,
        '<strong class="font-semibold">$1</strong>'
      );

      return formattedHtml;
    });

    // 선수 이니셜 계산
    const playerInitial = computed(() => {
      return playerStore.currentPlayer?.name
        ? playerStore.currentPlayer.name.charAt(0)
        : '';
    });

    // 개발 환경 확인
    const isDev = computed(() => {
      return import.meta.env.DEV === true;
    });

    // 분석 데이터 로드
    const loadAnalysis = async () => {
      console.log('loadAnalysis called with:', props.gameName, props.tagLine);
      if (props.gameName && props.tagLine) {
        try {
          await playerStore.loadPlayerAnalysis(props.gameName, props.tagLine);
        } catch (error) {
          console.error(
            'Modal component caught error while loading analysis:',
            error
          );
        }
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
      isDev,
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
  line-height: 1.6;
}
.prose strong {
  font-weight: 600;
  color: #111827;
}
.prose h3 {
  position: relative;
  padding-left: 0.75rem;
  border-left: 3px solid #3b82f6;
}
.prose ul {
  margin-top: 0.5em;
  margin-bottom: 1em;
}
.prose li {
  margin-bottom: 0.25em;
}
.prose li strong {
  color: #1e40af;
}
</style>
