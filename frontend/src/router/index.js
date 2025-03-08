import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import MatchDetailView from '@/views/MatchDetailView.vue';
import TeamListView from '@/views/TeamListView.vue';
import TeamDetailView from '@/views/TeamDetailView.vue';

const routes = [
  {
    path: '/',
    name: 'home',
    component: HomeView,
  },
  {
    path: '/matches/:id',
    name: 'match-detail',
    component: MatchDetailView,
  },
  {
    path: '/teams',
    name: 'TeamList',
    component: TeamListView,
  },
  {
    path: '/teams/:teamId(\\d+)',
    name: 'TeamDetailById',
    component: TeamDetailView,
    props: true,
  },
  {
    path: '/teams/name/:teamName',
    name: 'TeamDetailByName',
    component: TeamDetailView,
    props: true,
  },
];

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes,
});

export default router;
