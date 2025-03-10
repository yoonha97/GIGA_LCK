<template>
  <div class="container mx-auto py-8 px-4">
    <div class="mb-8 flex justify-between items-center">
      <h1 class="text-3xl font-bold">선수 관리</h1>
      <button
        @click="showAddModal = true"
        class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition-colors"
      >
        새 선수 등록
      </button>
    </div>

    <!-- 로딩 상태 -->
    <div v-if="loading" class="text-center py-12">
      <div
        class="w-16 h-16 border-4 border-blue-500 border-t-transparent rounded-full animate-spin mx-auto mb-6"
      ></div>
      <p class="text-gray-600 text-lg">선수 정보를 불러오는 중...</p>
    </div>

    <!-- 에러 메시지 -->
    <div
      v-else-if="error"
      class="bg-red-100 border border-red-300 text-red-700 px-4 py-3 rounded mb-6"
    >
      {{ error }}
      <button
        @click="fetchPlayers"
        class="ml-4 bg-red-500 text-white px-3 py-1 rounded text-sm hover:bg-red-600"
      >
        다시 시도
      </button>
    </div>

    <!-- 데이터 없음 -->
    <div
      v-else-if="players.length === 0"
      class="text-center py-12 bg-gray-50 rounded-lg"
    >
      <p class="text-gray-500 text-lg mb-4">등록된 선수가 없습니다.</p>
      <button
        @click="showAddModal = true"
        class="bg-blue-500 text-white px-4 py-2 rounded hover:bg-blue-600 transition-colors"
      >
        선수 등록하기
      </button>
    </div>

    <!-- 선수 목록 -->
    <div v-else>
      <div class="mb-4 flex justify-between">
        <div class="flex items-center">
          <span class="mr-2">필터:</span>
          <select
            v-model="positionFilter"
            class="border rounded px-3 py-1.5 mr-4"
          >
            <option value="">All Positions</option>
            <option value="Top">Top</option>
            <option value="Jungle">Jungle</option>
            <option value="Mid">Mid</option>
            <option value="Bot">Bot</option>
            <option value="Support">Support</option>
          </select>

          <input
            v-model="searchQuery"
            type="text"
            placeholder="선수 이름 검색..."
            class="border rounded px-3 py-1.5"
          />
        </div>
      </div>

      <div class="bg-white rounded-lg shadow overflow-hidden">
        <table class="min-w-full divide-y divide-gray-200">
          <thead class="bg-gray-50">
            <tr>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                선수명
              </th>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                포지션
              </th>
              <th
                class="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                팀
              </th>
              <th
                class="px-6 py-3 text-right text-xs font-medium text-gray-500 uppercase tracking-wider"
              >
                관리
              </th>
            </tr>
          </thead>
          <tbody class="bg-white divide-y divide-gray-200">
            <tr v-for="player in filteredPlayers" :key="player.id">
              <td class="px-6 py-4 whitespace-nowrap">
                <div class="flex items-center">
                  <div
                    class="flex-shrink-0 h-10 w-10 bg-blue-500 rounded-full flex items-center justify-center text-white font-bold"
                  >
                    {{ player.playerName ? player.playerName.charAt(0) : '' }}
                  </div>
                  <div class="ml-4">
                    <div class="text-sm font-medium text-gray-900">
                      {{ player.playerName }}
                    </div>
                    <div v-if="player.realName" class="text-sm text-gray-500">
                      {{ player.realName }}
                    </div>
                  </div>
                </div>
              </td>
              <td class="px-6 py-4 whitespace-nowrap">
                <span
                  class="px-2 inline-flex text-xs leading-5 font-semibold rounded-full"
                  :class="getPositionColor(player.position)"
                >
                  {{ player.position }}
                </span>
              </td>
              <td class="px-6 py-4 whitespace-nowrap text-sm text-gray-500">
                {{ getTeamName(player.teamId) || 'FA' }}
              </td>
              <td
                class="px-6 py-4 whitespace-nowrap text-right text-sm font-medium"
              >
                <button
                  @click="editPlayer(player)"
                  class="text-blue-600 hover:text-blue-900 mr-4"
                >
                  수정
                </button>
                <button
                  @click="confirmDelete(player)"
                  class="text-red-600 hover:text-red-900"
                >
                  삭제
                </button>
              </td>
            </tr>
          </tbody>
        </table>
      </div>
    </div>

    <!-- 선수 추가/수정 모달 -->
    <div
      v-if="showAddModal || showEditModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
        <div class="flex justify-between items-center mb-4">
          <h2 class="text-xl font-bold">
            {{ showEditModal ? '선수 정보 수정' : '새 선수 등록' }}
          </h2>
          <button
            @click="closeModals"
            class="text-gray-500 hover:text-gray-700"
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

        <form @submit.prevent="handleSubmit">
          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">
              선수명
            </label>
            <input
              v-model="playerForm.playerName"
              type="text"
              class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            />
          </div>

          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">
              실명
            </label>
            <input
              v-model="playerForm.realName"
              type="text"
              class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            />
          </div>

          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">
              포지션
            </label>
            <select
              v-model="playerForm.position"
              class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
              required
            >
              <option value="">포지션 선택</option>
              <option value="Top">Top</option>
              <option value="Jungle">Jungle</option>
              <option value="Mid">Mid</option>
              <option value="Bot">Bot</option>
              <option value="Support">Support</option>
            </select>
          </div>

          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">
              소속 팀
            </label>
            <select
              v-model="playerForm.teamId"
              class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            >
              <option :value="null">팀 없음 (FA)</option>
              <option
                v-for="team in teams"
                :key="team.id || team.teamId"
                :value="team.id || team.teamId"
              >
                {{ team.teamName }}
              </option>
            </select>
          </div>

          <div class="mb-4">
            <label class="block text-gray-700 text-sm font-bold mb-2">
              선수 소개
            </label>
            <textarea
              v-model="playerForm.description"
              rows="3"
              class="w-full px-3 py-2 border rounded focus:outline-none focus:ring-2 focus:ring-blue-500"
            ></textarea>
          </div>

          <div class="flex justify-end space-x-3">
            <button
              type="button"
              @click="closeModals"
              class="px-4 py-2 text-gray-600 hover:text-gray-800"
            >
              취소
            </button>
            <button
              type="submit"
              class="px-4 py-2 bg-blue-500 text-white rounded hover:bg-blue-600 transition-colors"
              :disabled="formLoading"
            >
              <span v-if="formLoading" class="flex items-center">
                <svg
                  class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                  xmlns="http://www.w3.org/2000/svg"
                  fill="none"
                  viewBox="0 0 24 24"
                >
                  <circle
                    class="opacity-25"
                    cx="12"
                    cy="12"
                    r="10"
                    stroke="currentColor"
                    stroke-width="4"
                  ></circle>
                  <path
                    class="opacity-75"
                    fill="currentColor"
                    d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                  ></path>
                </svg>
                처리 중...
              </span>
              <span v-else>
                {{ showEditModal ? '수정 완료' : '등록하기' }}
              </span>
            </button>
          </div>
        </form>
      </div>
    </div>

    <!-- 삭제 확인 모달 -->
    <div
      v-if="showDeleteModal"
      class="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50"
    >
      <div class="bg-white rounded-lg shadow-lg max-w-md w-full p-6">
        <h2 class="text-xl font-bold mb-4">선수 삭제 확인</h2>
        <p class="mb-6">
          정말 <strong>{{ playerToDelete?.playerName }}</strong> 선수를
          삭제하시겠습니까? 이 작업은 되돌릴 수 없습니다.
        </p>

        <div class="flex justify-end space-x-3">
          <button
            @click="showDeleteModal = false"
            class="px-4 py-2 text-gray-600 hover:text-gray-800"
          >
            취소
          </button>
          <button
            @click="deletePlayerConfirmed"
            class="px-4 py-2 bg-red-500 text-white rounded hover:bg-red-600 transition-colors"
            :disabled="deleteLoading"
          >
            <span v-if="deleteLoading" class="flex items-center">
              <svg
                class="animate-spin -ml-1 mr-2 h-4 w-4 text-white"
                xmlns="http://www.w3.org/2000/svg"
                fill="none"
                viewBox="0 0 24 24"
              >
                <circle
                  class="opacity-25"
                  cx="12"
                  cy="12"
                  r="10"
                  stroke="currentColor"
                  stroke-width="4"
                ></circle>
                <path
                  class="opacity-75"
                  fill="currentColor"
                  d="M4 12a8 8 0 018-8V0C5.373 0 0 5.373 0 12h4zm2 5.291A7.962 7.962 0 014 12H0c0 3.042 1.135 5.824 3 7.938l3-2.647z"
                ></path>
              </svg>
              삭제 중...
            </span>
            <span v-else>삭제하기</span>
          </button>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { ref, reactive, computed, onMounted } from 'vue';
import {
  fetchAllPlayers,
  createPlayer,
  updatePlayer,
  deletePlayer,
} from '@/api/player';
import { fetchAllTeams } from '@/api/team';

export default {
  name: 'PlayerAdminView',
  setup() {
    // 상태 변수
    const players = ref([]);
    const teams = ref([]);
    const loading = ref(false);
    const formLoading = ref(false);
    const deleteLoading = ref(false);
    const error = ref('');
    const positionFilter = ref('');
    const searchQuery = ref('');

    // 모달 상태
    const showAddModal = ref(false);
    const showEditModal = ref(false);
    const showDeleteModal = ref(false);
    const playerToDelete = ref(null);

    // 폼 데이터
    const playerForm = reactive({
      id: null,
      playerName: '',
      realName: '',
      position: '',
      teamId: null,
      description: '',
    });

    // 선수 목록 필터링
    const filteredPlayers = computed(() => {
      return players.value.filter((player) => {
        // 포지션 필터링 - 값이 있으면 정확히 일치하는지 확인
        const matchesPosition = positionFilter.value
          ? player.position === positionFilter.value
          : true;

        // 검색어 필터링 - 선수명이나 실명에 포함되는지 확인
        const matchesSearch = searchQuery.value
          ? player.playerName
              ?.toLowerCase()
              .includes(searchQuery.value.toLowerCase()) ||
            player.realName
              ?.toLowerCase()
              .includes(searchQuery.value.toLowerCase())
          : true;

        return matchesPosition && matchesSearch;
      });
    });

    // 선수 목록 불러오기
    const fetchPlayers = async () => {
      loading.value = true;
      error.value = '';

      try {
        const data = await fetchAllPlayers();
        players.value = data;

        // 선수 데이터 구조 디버깅
        if (data && data.length > 0) {
          console.log('첫 번째 선수 데이터 구조:', data[0]);
          console.log(
            '선수 데이터의 teamId 필드:',
            data.map((p) => ({
              id: p.playerId,
              name: p.playerName,
              teamId: p.teamId,
            }))
          );
        }
      } catch (err) {
        console.error('선수 목록을 불러오는 중 오류 발생:', err);
        error.value = '선수 목록을 불러오는데 실패했습니다. 다시 시도해주세요.';
      } finally {
        loading.value = false;
      }
    };

    // 팀 목록 불러오기
    const fetchTeams = async () => {
      try {
        const data = await fetchAllTeams();
        teams.value = data;
        // 팀 데이터 구조 디버깅
        if (data && data.length > 0) {
          console.log('팀 데이터 구조 확인:', data[0]);
        }
      } catch (err) {
        console.error('팀 목록을 불러오는 중 오류 발생:', err);
      }
    };

    // 포지션별 색상
    const getPositionColor = (position) => {
      // 포지션명에 따른 색상 매핑 (첫 글자만 대문자)
      const colors = {
        Top: 'bg-red-100 text-red-800',
        Jungle: 'bg-green-100 text-green-800',
        Mid: 'bg-blue-100 text-blue-800',
        Bot: 'bg-yellow-100 text-yellow-800',
        Support: 'bg-purple-100 text-purple-800',
      };
      return colors[position] || 'bg-gray-100 text-gray-800';
    };

    // 팀 이름 가져오기
    const getTeamName = (teamId) => {
      if (!teamId) return null;

      // 디버깅 로그 추가
      if (teams.value.length > 0 && teamId) {
        console.log(
          `Finding team for teamId: ${teamId}, type: ${typeof teamId}`
        );
        console.log(
          'Available teams:',
          teams.value.map((t) => ({
            id: t.id,
            teamId: t.teamId,
            name: t.teamName,
          }))
        );
      }

      // API에서 받아온 팀 객체의 필드가 id가 아닐 수 있으므로 teamId로도 비교
      // 숫자형과 문자열 형태 모두 처리
      const team = teams.value.find((t) => {
        return (
          String(t.id) === String(teamId) || String(t.teamId) === String(teamId)
        );
      });

      return team ? team.teamName : null;
    };

    // 모달 닫기
    const closeModals = () => {
      showAddModal.value = false;
      showEditModal.value = false;
      resetForm();
    };

    // 폼 초기화
    const resetForm = () => {
      playerForm.id = null;
      playerForm.playerName = '';
      playerForm.realName = '';
      playerForm.position = '';
      playerForm.teamId = null;
      playerForm.description = '';
    };

    // 선수 수정 모달 열기
    const editPlayer = async (player) => {
      console.log('Opening edit modal for player:', player);

      // 팀 목록 새로고침
      await fetchTeams();

      // 선수 데이터를 폼에 복사 - playerId 사용
      playerForm.id = player.playerId; // playerId를 id 필드에 매핑
      playerForm.playerName = player.playerName || '';
      playerForm.realName = player.realName || '';
      playerForm.position = player.position || '';

      // teamId 처리 - 디버깅 로그 추가
      console.log(
        'Setting teamId in form:',
        player.teamId,
        typeof player.teamId
      );
      playerForm.teamId = player.teamId; // null이어도 괜찮음

      playerForm.description = player.description || '';

      showEditModal.value = true;
    };

    // 선수 삭제 확인 모달
    const confirmDelete = (player) => {
      console.log('Opening delete confirmation for player:', player);
      playerToDelete.value = player;
      showDeleteModal.value = true;
    };

    // 선수 삭제 실행
    const deletePlayerConfirmed = async () => {
      // playerId 사용
      if (!playerToDelete.value || !playerToDelete.value.playerId) {
        console.error('삭제할 선수 ID가 없습니다:', playerToDelete.value);
        alert('삭제할 선수 정보가 유효하지 않습니다.');
        return;
      }

      console.log(
        'Confirming deletion of player ID:',
        playerToDelete.value.playerId
      );
      deleteLoading.value = true;

      try {
        // playerId 사용
        await deletePlayer(playerToDelete.value.playerId);
        console.log('Player deleted successfully. Updating UI...');

        // 목록에서 제거 - playerId 사용
        players.value = players.value.filter(
          (p) => p.playerId !== playerToDelete.value.playerId
        );
        showDeleteModal.value = false;
        alert('선수가 성공적으로 삭제되었습니다.');
      } catch (err) {
        console.error('선수 삭제 중 오류 발생:', err);
        alert('선수 삭제에 실패했습니다. 다시 시도해주세요.');
      } finally {
        deleteLoading.value = false;
      }
    };

    // 폼 제출 처리
    const handleSubmit = async () => {
      console.log('Submitting form with data:', { ...playerForm });
      console.log(
        'Current team selection:',
        playerForm.teamId,
        typeof playerForm.teamId
      );

      // 선택된 팀 정보 로깅
      if (playerForm.teamId !== null) {
        const selectedTeam = teams.value.find(
          (t) => String(t.id || t.teamId) === String(playerForm.teamId)
        );
        console.log('Selected team:', selectedTeam);
      }

      if (!playerForm.playerName || !playerForm.position) {
        alert('선수명과 포지션은 필수 입력 항목입니다.');
        return;
      }

      formLoading.value = true;

      try {
        if (showEditModal.value) {
          // 선수 정보 수정
          if (!playerForm.id) {
            throw new Error('수정할 선수의 ID가 없습니다');
          }

          // ID를 숫자로 변환
          const playerId = Number(playerForm.id);
          console.log('Updating player ID:', playerId);

          const updatedPlayer = await updatePlayer(playerId, playerForm);
          console.log('Player updated successfully:', updatedPlayer);

          // 목록 업데이트 - playerId 사용
          const index = players.value.findIndex((p) => p.playerId === playerId);
          if (index !== -1) {
            // 기존 객체의 속성을 유지하면서 업데이트된 속성을 병합
            players.value[index] = {
              ...players.value[index],
              ...updatedPlayer,
              teamId: updatedPlayer.teamId, // 명시적으로 teamId 업데이트
            };
            console.log('Updated player in array:', players.value[index]);
          }
          alert('선수 정보가 성공적으로 수정되었습니다.');
        } else {
          // 새 선수 등록
          console.log('Creating new player...');
          const newPlayer = await createPlayer(playerForm);
          console.log('Player created successfully:', newPlayer);
          players.value.push(newPlayer);
          alert('새 선수가 성공적으로 등록되었습니다.');
        }

        closeModals();
      } catch (err) {
        console.error('선수 정보 저장 중 오류 발생:', err);
        alert(
          showEditModal.value
            ? '선수 정보 수정에 실패했습니다. 서버 응답을 확인하세요.'
            : '선수 등록에 실패했습니다. 서버 응답을 확인하세요.'
        );
      } finally {
        formLoading.value = false;
      }
    };

    // 초기 데이터 로드
    onMounted(() => {
      fetchPlayers();
      fetchTeams();
    });

    return {
      players,
      teams,
      loading,
      formLoading,
      deleteLoading,
      error,
      positionFilter,
      searchQuery,
      filteredPlayers,
      showAddModal,
      showEditModal,
      showDeleteModal,
      playerForm,
      playerToDelete,
      fetchPlayers,
      getPositionColor,
      getTeamName,
      editPlayer,
      confirmDelete,
      deletePlayerConfirmed,
      closeModals,
      handleSubmit,
    };
  },
};
</script>
