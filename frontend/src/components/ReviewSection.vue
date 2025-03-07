<template>
  <div class="bg-white rounded-lg shadow p-6">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-xl font-bold">리뷰</h2>
      <div class="flex items-center">
        <div class="flex items-center mr-3" v-if="reviews.length > 0">
          <div class="flex space-x-1 mr-1">
            <svg
              class="w-5 h-5 text-yellow-400"
              fill="currentColor"
              viewBox="0 0 20 20"
            >
              <path
                d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
              />
            </svg>
          </div>
          <span class="font-bold">{{ averageRating }}</span>
          <span class="text-gray-500 mx-1">/</span>
          <span class="text-gray-500">5</span>
        </div>
        <span class="text-gray-500 text-sm">{{ reviewCountText }}</span>
      </div>
    </div>

    <!-- 리뷰 작성 폼 -->
    <form
      v-if="!editingReviewId"
      @submit.prevent="submitUserReview"
      class="mb-8"
    >
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2">별점</label>
        <div class="flex space-x-2">
          <button
            v-for="star in 5"
            :key="star"
            type="button"
            class="focus:outline-none"
            @click="rating = star"
          >
            <svg
              :class="star <= rating ? 'text-yellow-400' : 'text-gray-300'"
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
        <label class="block text-gray-700 text-sm font-bold mb-2">코멘트</label>
        <textarea
          v-model="comment"
          rows="3"
          class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="경기에 대한 의견을 남겨주세요..."
        ></textarea>
      </div>

      <button
        type="submit"
        class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
      >
        리뷰 작성
      </button>
    </form>

    <!-- 리뷰 목록 -->
    <div class="space-y-4">
      <div v-if="loading" class="text-center py-4">
        <div
          class="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900 mx-auto"
        ></div>
      </div>

      <div
        v-else-if="reviews.length === 0"
        class="text-center text-gray-500 py-4"
      >
        아직 리뷰가 없습니다.
      </div>

      <div
        v-else
        v-for="review in reviews"
        :key="review.id"
        class="border-b last:border-b-0 pb-4 last:pb-0"
      >
        <div class="flex items-center justify-between mb-2">
          <div class="flex items-center">
            <div class="flex space-x-1">
              <svg
                v-for="star in 5"
                :key="star"
                :class="
                  star <= review.rating ? 'text-yellow-400' : 'text-gray-300'
                "
                class="w-5 h-5"
                fill="currentColor"
                viewBox="0 0 20 20"
              >
                <path
                  d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z"
                />
              </svg>
            </div>
            <span class="text-sm text-gray-500 ml-2">{{ review.comment }}</span>
          </div>
          <div class="flex space-x-2">
            <button @click="openEditModal(review)" class="text-blue-500">
              수정
            </button>
            <button @click="deleteUserReview(review.id)" class="text-red-500">
              삭제
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 리뷰 수정 모달 -->
    <ReviewEditModal
      :is-open="isEditModalOpen"
      :review="selectedReview"
      @update="handleReviewUpdate"
      @close="closeEditModal"
    />
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import ReviewEditModal from './ReviewEditModal.vue';
import {
  fetchMatchReviews,
  submitReview,
  updateReview,
  deleteReview,
} from '@/api/match';

export default {
  components: {
    ReviewEditModal,
  },
  props: {
    matchId: {
      type: Number,
      required: true,
    },
  },
  setup(props) {
    const reviews = ref([]);
    const rating = ref(0);
    const comment = ref('');
    const loading = ref(false);
    const editingReviewId = ref(null);

    // 입력 유효성 검사
    const isValid = computed(() => {
      return rating.value > 0 && comment.value.trim().length > 0;
    });

    // 별점 평균 계산
    const averageRating = computed(() => {
      if (reviews.value.length === 0) return 0;
      const sum = reviews.value.reduce((acc, review) => acc + review.rating, 0);
      return (sum / reviews.value.length).toFixed(1);
    });

    // 리뷰 개수 텍스트
    const reviewCountText = computed(() => {
      return `${reviews.value.length}개의 리뷰`;
    });

    // 모달 관련 상태
    const isEditModalOpen = ref(false);
    const selectedReview = ref(null);

    // 리뷰 가져오기
    const loadReviews = async () => {
      if (!props.matchId) return;
      loading.value = true;
      try {
        const reviewData = await fetchMatchReviews(props.matchId);
        reviews.value = reviewData;
      } catch (error) {
        console.error('리뷰 데이터를 불러오는 중 오류 발생:', error);
      } finally {
        loading.value = false;
      }
    };

    // 리뷰 등록
    const submitUserReview = async () => {
      // 별점 체크
      if (rating.value <= 0) {
        alert('별점을 선택해주세요.');
        return;
      }

      // 코멘트 체크
      if (comment.value.trim().length === 0) {
        alert('코멘트를 입력해주세요.');
        return;
      }

      try {
        const newReview = await submitReview(
          props.matchId,
          rating.value,
          comment.value.trim()
        );
        // 새로운 리뷰를 reviews 배열에 추가
        reviews.value = [...reviews.value, newReview];
        resetForm();
      } catch (error) {
        console.error('리뷰 저장 중 오류 발생:', error);
        alert('리뷰 저장에 실패했습니다. 다시 시도해주세요.');
      }
    };

    // 모달 열기
    const openEditModal = (review) => {
      console.log('Opening edit modal with review:', review); // 디버깅용 로그
      if (!review || typeof review.id === 'undefined') {
        console.error('유효하지 않은 리뷰입니다:', review);
        return;
      }
      selectedReview.value = { ...review };
      isEditModalOpen.value = true;
    };

    // 모달 닫기
    const closeEditModal = () => {
      selectedReview.value = null;
      isEditModalOpen.value = false;
    };

    // 리뷰 수정 처리
    const handleReviewUpdate = async (updatedReview) => {
      if (!updatedReview || typeof updatedReview.id === 'undefined') {
        console.error('유효하지 않은 리뷰 정보입니다.');
        return;
      }

      try {
        await updateReview(
          props.matchId,
          updatedReview.id,
          updatedReview.rating,
          updatedReview.comment.trim()
        );
        // 로컬 상태 업데이트
        reviews.value = reviews.value.map((review) =>
          review.id === updatedReview.id ? updatedReview : review
        );
        closeEditModal();
      } catch (error) {
        console.error('리뷰 수정 중 오류 발생:', error);
        alert('리뷰 수정에 실패했습니다. 다시 시도해주세요.');
      }
    };

    // 리뷰 삭제
    const deleteUserReview = async (reviewId) => {
      console.log('Deleting review with ID:', reviewId); // 디버깅용 로그
      if (typeof reviewId === 'undefined') {
        console.error('삭제할 리뷰 ID가 없습니다.');
        return;
      }

      if (!confirm('정말로 이 리뷰를 삭제하시겠습니까?')) return;

      try {
        await deleteReview(props.matchId, reviewId);
        // 로컬 상태 업데이트
        reviews.value = reviews.value.filter(
          (review) => review.id !== reviewId
        );
      } catch (error) {
        console.error('리뷰 삭제 실패:', error);
        alert('리뷰 삭제에 실패했습니다. 다시 시도해주세요.');
      }
    };

    // 입력 폼 초기화
    const resetForm = () => {
      rating.value = 0;
      comment.value = '';
    };

    onMounted(loadReviews);

    return {
      reviews,
      rating,
      comment,
      loading,
      isValid,
      isEditModalOpen,
      selectedReview,
      editingReviewId,
      averageRating,
      reviewCountText,
      submitUserReview,
      openEditModal,
      closeEditModal,
      handleReviewUpdate,
      deleteUserReview,
    };
  },
};
</script>
