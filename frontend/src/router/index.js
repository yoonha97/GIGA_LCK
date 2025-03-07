import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';
import MatchDetailView from '@/views/MatchDetailView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
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
  ],
});

export default router;
