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
        <h3 class="text-lg font-bold mb-4">응원 댓글 수정</h3>

        <form @submit.prevent="handleSubmit">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2"
              >댓글</label
            >
            <textarea
              v-model="localComment"
              rows="3"
              class="w-full px-3 py-2 border rounded-lg focus:outline-none focus:ring-2 focus:ring-blue-500"
              placeholder="팀을 응원하는 댓글을 남겨주세요..."
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
  name: 'SupportEditModal',
  props: {
    isOpen: {
      type: Boolean,
      required: true,
    },
    support: {
      type: Object,
      required: true,
      validator: (value) => {
        // 백엔드에서는 supportId로 오지만,
        // SupportSection에서 id 속성을 추가하여 전달함
        return (
          value &&
          (typeof value.id !== 'undefined' ||
            typeof value.supportId !== 'undefined')
        );
      },
    },
  },
  emits: ['update', 'close'],
  setup(props, { emit }) {
    const localComment = ref('');

    // Initialize local state when support changes
    watch(
      () => props.support,
      (newSupport) => {
        if (newSupport && typeof newSupport === 'object') {
          // ID가 없으면 에러
          if (
            typeof newSupport.id === 'undefined' &&
            typeof newSupport.supportId === 'undefined'
          ) {
            console.error('응원 댓글 객체에 ID가 없습니다:', newSupport);
            emit('close');
            return;
          }
          localComment.value = newSupport.supportComment || '';
        }
      },
      { immediate: true }
    );

    const isValid = computed(() => {
      return localComment.value.trim().length > 0;
    });

    const handleSubmit = () => {
      // 댓글 내용 체크
      if (localComment.value.trim().length === 0) {
        alert('댓글 내용을 입력해주세요.');
        return;
      }

      // ID 존재 여부 확인 (id 또는 supportId)
      const supportId = props.support?.id || props.support?.supportId;
      if (!supportId) {
        console.error('유효하지 않은 응원 댓글 정보입니다.');
        return;
      }

      emit('update', {
        id: supportId,
        supportComment: localComment.value.trim(),
      });
    };

    const closeModal = () => {
      emit('close');
    };

    return {
      localComment,
      isValid,
      handleSubmit,
      closeModal,
    };
  },
};
</script>
