import { defineStore } from 'pinia';
import { fetchMatchesByMonth, fetchMatchesByTeam } from '@/api/match';

export const useMatchStore = defineStore('matchStore', {
  state: () => ({
    matches: [],
  }),
  actions: {
    async loadMatches(year, month) {
      this.matches = await fetchMatchesByMonth(year, month);
    },
    async loadTeamMatches(teamId) {
      this.matches = await fetchMatchesByTeam(teamId);
    },
  },
});
