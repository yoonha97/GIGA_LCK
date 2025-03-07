import { createRouter, createWebHistory } from 'vue-router';
import HomeView from '@/views/HomeView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'home',
      component: HomeView,
    },
    // 나중에 필요한 라우트를 여기에 추가할 수 있습니다.
    // {
    //   path: '/match/:id',
    //   name: 'match-detail',
    //   component: () => import('@/views/MatchDetailView.vue'),
    // },
  ],
});

export default router;
