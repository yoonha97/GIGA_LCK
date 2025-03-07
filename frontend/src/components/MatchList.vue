<template>
  <div class="p-6 bg-gray-100">
    <!-- 연도 및 월 선택 -->
    <div class="flex items-center justify-between mb-4 border-b pb-2">
      <div class="flex items-center space-x-4">
        <button @click="selectedYear--" class="px-2 text-lg">◀</button>
        <span class="font-bold text-xl">{{ selectedYear }}</span>
        <button @click="selectedYear++" class="px-2 text-lg">▶</button>
      </div>

      <div class="flex space-x-2">
        <button
          v-for="month in months"
          :key="month.value"
          @click="selectedMonth = month.value"
          :class="[
            'px-3 py-2 rounded',
            selectedMonth === month.value
              ? 'bg-blue-500 text-white'
              : 'text-gray-600 bg-gray-200',
          ]"
        >
          {{ month.label }}
        </button>
      </div>
    </div>

    <!-- 경기 목록 -->
    <div v-if="loading" class="text-center py-4 text-gray-600">로딩 중...</div>
    <div v-else-if="error" class="text-center text-red-500">{{ error }}</div>
    <div
      v-else-if="matchStore.matches.length === 0"
      class="text-center text-gray-600"
    >
      경기 데이터가 없습니다.
    </div>

    <div v-else>
      <div v-for="(group, date) in groupedMatches" :key="date" class="mb-6">
        <div class="font-bold text-lg py-2 border-b bg-gray-200 px-4">
          {{ date }}
        </div>
        <MatchCard
          v-for="match in group"
          :key="match.matchId"
          :matchId="match.matchId"
          :time="match.time"
          :stage="match.stage"
          :homeTeam="match.homeTeam"
          :score="match.score"
          :awayTeam="match.awayTeam"
          :replayLink="match.replayLink"
        />
      </div>
    </div>
  </div>
</template>

<script>
import { ref, computed, onMounted, watch } from 'vue';
import { useMatchStore } from '@/stores/matchStore';
import MatchCard from '@/components/MatchCard.vue';

export default {
  components: {
    MatchCard,
  },
  setup() {
    const matchStore = useMatchStore();
    const selectedYear = ref(new Date().getFullYear());
    const selectedMonth = ref(new Date().getMonth());
    const loading = ref(false);
    const error = ref(null);

    const months = ref([
      { value: 1, label: '1월' },
      { value: 2, label: '2월' },
      { value: 3, label: '3월' },
      { value: 4, label: '4월' },
      { value: 5, label: '5월' },
      { value: 6, label: '6월' },
      { value: 7, label: '7월' },
      { value: 8, label: '8월' },
      { value: 9, label: '9월' },
      { value: 10, label: '10월' },
      { value: 11, label: '11월' },
      { value: 12, label: '12월' },
    ]);

    const fetchMatches = async () => {
      loading.value = true;
      error.value = null;
      try {
        await matchStore.loadMatches(selectedYear.value, selectedMonth.value);
      } catch (err) {
        error.value = '데이터를 불러오는 중 오류가 발생했습니다.';
      } finally {
        loading.value = false;
      }
    };

    const groupedMatches = computed(() => {
      return matchStore.matches.reduce((acc, match) => {
        const dateKey = new Date(match.date).toLocaleDateString('ko-KR', {
          month: 'long',
          day: 'numeric',
          weekday: 'short',
        });
        if (!acc[dateKey]) acc[dateKey] = [];
        acc[dateKey].push(match);
        return acc;
      }, {});
    });

    watch([selectedYear, selectedMonth], fetchMatches);
    onMounted(fetchMatches);

    return {
      matchStore,
      selectedYear,
      selectedMonth,
      loading,
      error,
      months,
      groupedMatches,
    };
  },
};
</script>

<style scoped>
button {
  cursor: pointer;
}
</style>
