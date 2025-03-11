import { defineStore } from 'pinia';
import {
  fetchAllTeams,
  fetchTeamById,
  fetchTeamByName,
  fetchTeamRecentMatchesById,
  fetchTeamRecentMatchesByName,
} from '@/api/team';

export const useTeamStore = defineStore('teamStore', {
  state: () => ({
    teams: [],
    currentTeam: null,
    recentMatches: [],
    loading: false,
    matchesLoading: false,
    error: null,
    matchesError: null,
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

    async loadRecentMatchesById(teamId) {
      this.matchesLoading = true;
      this.matchesError = null;
      this.recentMatches = [];

      try {
        const matches = await fetchTeamRecentMatchesById(teamId);
        console.log('Matches data structure:', matches);
        if (matches && matches.length > 0) {
          console.log('First match sample:', matches[0]);
          console.log('Available match properties:', Object.keys(matches[0]));
        }
        this.recentMatches = matches;
      } catch (err) {
        this.matchesError =
          '팀의 최근 경기 정보를 불러오는 중 오류가 발생했습니다.';
        console.error(err);
      } finally {
        this.matchesLoading = false;
      }
    },

    async loadRecentMatchesByName(teamName) {
      this.matchesLoading = true;
      this.matchesError = null;
      this.recentMatches = [];

      try {
        const matches = await fetchTeamRecentMatchesByName(teamName);
        console.log('Matches data structure:', matches);
        if (matches && matches.length > 0) {
          console.log('First match sample:', matches[0]);
          console.log('Available match properties:', Object.keys(matches[0]));
        }
        this.recentMatches = matches;
      } catch (err) {
        this.matchesError =
          '팀의 최근 경기 정보를 불러오는 중 오류가 발생했습니다.';
        console.error(err);
      } finally {
        this.matchesLoading = false;
      }
    },

    clearCurrentTeam() {
      this.currentTeam = null;
      this.recentMatches = [];
    },
  },
});
