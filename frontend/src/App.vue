<script setup>
import { ref, onMounted, computed } from 'vue';
import { useEsportsStore } from './stores/esports';
import moment from 'moment';
import MatchCard from './components/MatchCard.vue';

const store = useEsportsStore();

const loading = computed(() => store.loading);
const error = computed(() => store.error);
const leagues = computed(() => store.leagues);
const selectedLeague = ref('');
const filteredSchedule = computed(() => store.filteredSchedule);
const scheduleByLeague = computed(() => store.scheduleByLeague);

const handleLeagueChange = () => {
  store.setSelectedLeague(selectedLeague.value);
};

const formatDate = (date) => {
  return moment(date).format('MMM D, YYYY HH:mm');
};

onMounted(async () => {
  await Promise.all([
    store.fetchSchedule(),
    store.fetchLeagues(),
    store.fetchTeams(),
  ]);
});
</script>

<template>
  <div class="min-h-screen bg-gray-100">
    <nav class="bg-white shadow-lg">
      <div class="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
        <div class="flex justify-between h-16">
          <div class="flex">
            <div class="flex-shrink-0 flex items-center">
              <h1 class="text-2xl font-bold text-gray-900">
                LoL Esports Schedule
              </h1>
            </div>
          </div>
        </div>
      </div>
    </nav>

    <main class="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
      <!-- League Selector -->
      <div class="mb-6">
        <select
          v-model="selectedLeague"
          @change="handleLeagueChange"
          class="mt-1 block w-full pl-3 pr-10 py-2 text-base border-gray-300 focus:outline-none focus:ring-indigo-500 focus:border-indigo-500 sm:text-sm rounded-md"
        >
          <option value="">All Leagues</option>
          <option v-for="league in leagues" :key="league.id" :value="league.id">
            {{ league.name }}
          </option>
        </select>
      </div>

      <!-- Loading State -->
      <div v-if="loading" class="text-center py-10">
        <div
          class="animate-spin rounded-full h-12 w-12 border-b-2 border-indigo-500 mx-auto"
        ></div>
      </div>

      <!-- Error State -->
      <div v-else-if="error" class="text-center py-10 text-red-600">
        {{ error }}
      </div>

      <!-- Schedule Grid -->
      <div v-else>
        <template v-if="selectedLeague">
          <!-- 선택된 리그의 일정 -->
          <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
            <div
              v-for="event in filteredSchedule"
              :key="event.id"
              class="bg-white overflow-hidden shadow rounded-lg"
            >
              <MatchCard :event="event" />
            </div>
          </div>
        </template>
        <template v-else>
          <!-- 리그별로 그룹화된 일정 -->
          <div
            v-for="(events, leagueName) in scheduleByLeague"
            :key="leagueName"
            class="mb-8"
          >
            <h2 class="text-xl font-bold text-gray-900 mb-4">
              {{ leagueName }}
            </h2>
            <div class="grid grid-cols-1 gap-4 sm:grid-cols-2 lg:grid-cols-3">
              <div
                v-for="event in events"
                :key="event.id"
                class="bg-white overflow-hidden shadow rounded-lg"
              >
                <MatchCard :event="event" />
              </div>
            </div>
          </div>
        </template>
      </div>
    </main>
  </div>
</template>

<style>
header {
  line-height: 1.5;
}

.logo {
  display: block;
  margin: 0 auto 2rem;
}

@media (min-width: 1024px) {
  header {
    display: flex;
    place-items: center;
    padding-right: calc(var(--section-gap) / 2);
  }

  .logo {
    margin: 0 2rem 0 0;
  }

  header .wrapper {
    display: flex;
    place-items: flex-start;
    flex-wrap: wrap;
  }
}
</style>
