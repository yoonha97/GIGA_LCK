<template>
  <div class="bg-white rounded-lg shadow p-6">
    <div class="flex items-center justify-between mb-4">
      <h2 class="text-xl font-bold">응원 댓글</h2>
      <div class="text-gray-500 text-sm">{{ commentCountText }}</div>
    </div>

    <!-- 응원 댓글 작성 폼 -->
    <form
      v-if="!editingCommentId && !error"
      @submit.prevent="submitUserSupport"
      class="mb-8"
    >
      <div class="mb-4">
        <label class="block text-gray-700 text-sm font-bold mb-2"
          >응원 댓글</label
        >
        <textarea
          v-model="comment"
          rows="3"
          class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
          placeholder="팀을 응원하는 댓글을 남겨주세요..."
        ></textarea>
      </div>

      <button
        type="submit"
        class="bg-blue-500 text-white px-4 py-2 rounded-lg hover:bg-blue-600 transition-colors"
      >
        댓글 작성
      </button>
    </form>

    <!-- 에러 메시지 -->
    <div
      v-if="error"
      class="mb-6 p-4 bg-red-100 border border-red-300 rounded-lg"
    >
      <p class="text-red-700 font-medium">{{ errorMessage }}</p>
      <button
        @click="retryLoading"
        class="mt-2 bg-red-500 text-white px-4 py-2 rounded-lg hover:bg-red-600 transition-colors"
      >
        다시 시도
      </button>
    </div>

    <!-- 응원 댓글 목록 -->
    <div class="space-y-4">
      <div v-if="loading" class="text-center py-4">
        <div
          class="animate-spin rounded-full h-8 w-8 border-b-2 border-gray-900 mx-auto"
        ></div>
      </div>

      <div
        v-else-if="supports.length === 0"
        class="text-center text-gray-500 py-4"
      >
        아직 응원 댓글이 없습니다. 첫 번째 응원 댓글을 남겨보세요!
      </div>

      <div
        v-else
        v-for="support in supports"
        :key="support.supportId"
        class="border-b last:border-b-0 pb-4 last:pb-0 pt-4"
      >
        <div class="flex items-start justify-between">
          <div class="flex-1">
            <p class="text-gray-700">{{ support.supportComment }}</p>
            <p class="text-xs text-gray-500 mt-1">
              {{ formatDate(support.createdAt) }}
            </p>
          </div>
          <div class="flex space-x-2 ml-4">
            <button
              @click="openEditModal(support)"
              class="text-blue-500 text-sm"
            >
              수정
            </button>
            <button
              @click="deleteUserSupport(support.supportId)"
              class="text-red-500 text-sm"
            >
              삭제
            </button>
          </div>
        </div>
      </div>
    </div>

    <!-- 응원 댓글 수정 모달 -->
    <SupportEditModal
      :is-open="isEditModalOpen"
      :support="selectedSupport"
      @update="handleSupportUpdate"
      @close="closeEditModal"
    />
  </div>
</template>

<script>
import { ref, onMounted, computed } from 'vue';
import SupportEditModal from './SupportEditModal.vue';
import {
  fetchSupportsByTeam,
  submitSupport,
  updateSupport,
  deleteSupport,
} from '@/api/support';

export default {
  components: {
    SupportEditModal,
  },
  props: {
    teamId: {
      type: [Number, String],
      required: true,
    },
  },
  setup(props) {
    const supports = ref([]);
    const comment = ref('');
    const loading = ref(false);
    const error = ref(false);
    const errorMessage = ref('');
    const editingCommentId = ref(null);

    // 입력 유효성 검사
    const isValid = computed(() => {
      return comment.value.trim().length > 0;
    });

    // 댓글 개수 텍스트
    const commentCountText = computed(() => {
      return `${supports.value.length}개의 응원`;
    });

    // 모달 관련 상태
    const isEditModalOpen = ref(false);
    const selectedSupport = ref(null);

    // 응원 댓글 가져오기
    const loadSupports = async () => {
      if (!props.teamId) return;
      loading.value = true;
      error.value = false;
      errorMessage.value = '';

      try {
        const supportData = await fetchSupportsByTeam(props.teamId);
        supports.value = supportData;
      } catch (err) {
        console.error('응원 댓글 데이터를 불러오는 중 오류 발생:', err);
        supports.value = [];
        error.value = true;
        errorMessage.value = `응원 댓글을 불러올 수 없습니다: ${
          err.message || '서버 연결 오류'
        }`;
      } finally {
        loading.value = false;
      }
    };

    // 다시 시도하기
    const retryLoading = () => {
      loadSupports();
    };

    // 응원 댓글 등록
    const submitUserSupport = async () => {
      // 댓글 내용 체크
      if (comment.value.trim().length === 0) {
        alert('응원 댓글을 입력해주세요.');
        return;
      }

      try {
        // API 호출 전 로딩 상태 설정
        loading.value = true;
        const newSupport = await submitSupport(
          props.teamId,
          comment.value.trim()
        );
        // 새로운 댓글을 supports 배열에 추가
        supports.value = [...supports.value, newSupport];
        resetForm();
      } catch (err) {
        console.error('응원 댓글 저장 중 오류 발생:', err);
        alert(
          `응원 댓글 저장에 실패했습니다: ${err.message || '알 수 없는 오류'}`
        );
      } finally {
        loading.value = false;
      }
    };

    // 모달 열기
    const openEditModal = (support) => {
      if (!support || typeof support.supportId === 'undefined') {
        console.error('유효하지 않은 응원 댓글입니다:', support);
        return;
      }
      selectedSupport.value = {
        ...support,
        id: support.supportId,
      };
      isEditModalOpen.value = true;
    };

    // 모달 닫기
    const closeEditModal = () => {
      selectedSupport.value = null;
      isEditModalOpen.value = false;
    };

    // 응원 댓글 수정 처리
    const handleSupportUpdate = async (updatedSupport) => {
      if (!updatedSupport || typeof updatedSupport.id === 'undefined') {
        console.error('유효하지 않은 응원 댓글 정보입니다.');
        return;
      }

      try {
        loading.value = true;
        await updateSupport(
          props.teamId,
          updatedSupport.id,
          updatedSupport.supportComment
        );
        // 로컬 상태 업데이트
        supports.value = supports.value.map((support) =>
          support.supportId === updatedSupport.id
            ? {
                ...support,
                supportComment: updatedSupport.supportComment,
              }
            : support
        );
        closeEditModal();
      } catch (err) {
        console.error('응원 댓글 수정 중 오류 발생:', err);
        alert(
          `응원 댓글 수정에 실패했습니다: ${err.message || '알 수 없는 오류'}`
        );
      } finally {
        loading.value = false;
      }
    };

    // 응원 댓글 삭제
    const deleteUserSupport = async (supportId) => {
      if (typeof supportId === 'undefined') {
        console.error('삭제할 응원 댓글 ID가 없습니다.');
        return;
      }

      console.log('삭제할 응원 댓글 ID:', supportId); // 디버깅용

      if (!confirm('정말로 이 응원 댓글을 삭제하시겠습니까?')) return;

      try {
        loading.value = true;
        await deleteSupport(props.teamId, supportId);
        // 로컬 상태 업데이트
        supports.value = supports.value.filter(
          (support) => support.supportId !== supportId
        );
      } catch (err) {
        console.error('응원 댓글 삭제 실패:', err);
        alert(
          `응원 댓글 삭제에 실패했습니다: ${err.message || '알 수 없는 오류'}`
        );
      } finally {
        loading.value = false;
      }
    };

    // 입력 폼 초기화
    const resetForm = () => {
      comment.value = '';
    };

    // 날짜 포맷팅
    const formatDate = (dateString) => {
      if (!dateString) return '';
      const date = new Date(dateString);
      return date.toLocaleString('ko-KR', {
        year: 'numeric',
        month: 'long',
        day: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
      });
    };

    onMounted(loadSupports);

    return {
      supports,
      comment,
      loading,
      error,
      errorMessage,
      isValid,
      isEditModalOpen,
      selectedSupport,
      editingCommentId,
      commentCountText,
      submitUserSupport,
      retryLoading,
      openEditModal,
      closeEditModal,
      handleSupportUpdate,
      deleteUserSupport,
      formatDate,
    };
  },
};
</script>
