<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
    <!-- 배경 클릭 시 모달 닫기 -->
    <div
      class="fixed inset-0 bg-black bg-opacity-50 transition-opacity"
      @click="closeModal"
    ></div>

    <!-- 모달 -->
    <div class="flex items-center justify-center min-h-screen p-4">
      <div class="relative bg-white rounded-lg shadow-xl max-w-md w-full p-6">
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
            <h5 class="font-semibold text-lg mb-2">선수 정보</h5>

            <div v-if="playerStore.currentPlayer.description" class="mb-4">
              <h5 class="font-semibold text-lg mb-2">선수 소개</h5>
              <p class="text-gray-700">
                {{ playerStore.currentPlayer.description }}
              </p>
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
import { computed } from 'vue';
import { usePlayerStore } from '@/stores/playerStore';

export default {
  name: 'PlayerDetailModal',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
  },
  emits: ['close'],
  setup(props, { emit }) {
    const playerStore = usePlayerStore();

    // 선수 이니셜 계산
    const playerInitial = computed(() => {
      return playerStore.currentPlayer?.name
        ? playerStore.currentPlayer.name.charAt(0)
        : '';
    });

    const closeModal = () => {
      emit('close');
    };

    return {
      playerStore,
      playerInitial,
      closeModal,
    };
  },
};
</script>
