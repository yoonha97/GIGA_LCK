import { defineStore } from 'pinia';
import { fetchAllTeams, fetchTeamById, fetchTeamByName } from '@/api/team';

export const useTeamStore = defineStore('teamStore', {
  state: () => ({
    teams: [],
    currentTeam: null,
    loading: false,
    error: null,
  }),

  actions: {
    async loadAllTeams() {
      this.loading = true;
      this.error = null;
      try {
        this.teams = await fetchAllTeams();
      } catch (err) {
        this.error = '팀 목록을 불러오는 중 오류가 발생했습니다.';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },

    async loadTeamById(teamId) {
      this.loading = true;
      this.error = null;
      try {
        this.currentTeam = await fetchTeamById(teamId);
      } catch (err) {
        this.error = '팀 정보를 불러오는 중 오류가 발생했습니다.';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },

    async loadTeamByName(teamName) {
      this.loading = true;
      this.error = null;
      try {
        this.currentTeam = await fetchTeamByName(teamName);
      } catch (err) {
        this.error = '팀 정보를 불러오는 중 오류가 발생했습니다.';
        console.error(err);
      } finally {
        this.loading = false;
      }
    },

    clearCurrentTeam() {
      this.currentTeam = null;
    },
  },
});
