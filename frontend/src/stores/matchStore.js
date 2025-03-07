import { defineStore } from 'pinia';
import { fetchMatchesByMonth } from '@/api/match';

export const useMatchStore = defineStore('matchStore', {
  state: () => ({
    matches: [],
  }),
  actions: {
    async loadMatches(year, month) {
      this.matches = await fetchMatchesByMonth(year, month);
    },
  },
});
