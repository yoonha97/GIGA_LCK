<template>
  <div v-if="isOpen" class="fixed inset-0 z-50 overflow-y-auto">
    <!-- Backdrop -->
    <div
      class="fixed inset-0 bg-black bg-opacity-50 transition-opacity"
      @click="closeModal"
    ></div>

    <!-- Modal -->
    <div class="flex items-center justify-center min-h-screen p-4">
      <div class="relative bg-white rounded-lg shadow-xl max-w-md w-full p-6">
        <h3 class="text-lg font-bold mb-4">리뷰 수정</h3>

        <form @submit.prevent="handleSubmit">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2"
              >별점</label
            >
            <div class="flex space-x-2">
              <button
                v-for="star in 5"
                :key="star"
                type="button"
                class="focus:outline-none"
                @click="localRating = star"
              >
                <svg
                  :class="
                    star <= localRating ? 'text-yellow-400' : 'text-gray-300'
                  "
                  class="w-8 h-8"
                  fill="currentColor"
                  viewBox="0 0 20 20"
                >
                  <path
                    d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                  />
                </svg>
              </button>
            </div>
          </div>

          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2"
              >코멘트</label
            >
            <textarea
              v-model="localComment"
              rows="3"
              class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="경기에 대한 의견을 남겨주세요..."
            ></textarea>
          </div>

          <div class="flex justify-end space-x-2">
            <button
              type="button"
              @click="closeModal"
              class="px-4 py-2 text-gray-600 hover:text-gray-800"
            >
              취소
            </button>
            <button
              type="submit"
              class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
              :disabled="!isValid"
            >
              수정 완료
            </button>
          </div>
        </form>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, watch } from 'vue';

export default {
  name: 'ReviewEditModal',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
    review: {
      type: Object,
      required: true,
      validator: (value) => {
        return value && typeof value.id !== 'undefined';
      },
    },
  },
  emits: ['update', 'close'],
  setup(props, { emit }) {
    const localRating = ref(0);
    const localComment = ref('');

    // Initialize local state when review changes
    watch(
      () => props.review,
      (newReview) => {
        if (newReview && typeof newReview === 'object') {
          if (typeof newReview.id === 'undefined') {
            console.error('리뷰 객체에 ID가 없습니다:', newReview);
            emit('close');
            return;
          }
          localRating.value = newReview.rating || 0;
          localComment.value = newReview.comment || '';
        }
      },
      { immediate: true }
    );

    const isValid = computed(() => {
      return localRating.value > 0 && localComment.value.trim().length > 0;
    });

    const handleSubmit = () => {
      if (!isValid.value) {
        alert('별점과 코멘트를 모두 입력해주세요.');
        return;
      }

      if (!props.review?.id) {
        console.error('유효하지 않은 리뷰 정보입니다.');
        return;
      }

      emit('update', {
        id: props.review.id,
        rating: localRating.value,
        comment: localComment.value.trim(),
      });
    };

    const closeModal = () => {
      emit('close');
    };

    return {
      localRating,
      localComment,
      isValid,
      handleSubmit,
      closeModal,
    };
  },
};
</script>
