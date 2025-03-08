<template>
  <div class="min-h-screen bg-gray-100">
    <nav class="bg-white shadow-sm">
      <div class="container mx-auto px-4">
        <div class="flex justify-between h-16 items-center">
          <div class="h-20 w-20 flex items-center justify-center">
            <img :src="logo" alt="Logo" class="h-16 w-auto object-contain" />
          </div>
          <div class="flex items-center space-x-4">
            <RouterLink
              to="/"
              class="text-xl font-bold text-gray-800 hover:text-gray-600 transition-colors"
            >
              LCK Matches
            </RouterLink>
            <RouterLink
              to="/teams"
              class="text-xl font-bold text-gray-800 hover:text-gray-600 transition-colors"
            >
              LCK Teams
            </RouterLink>
          </div>
          <!-- 나중에 필요한 네비게이션 링크를 여기에 추가할 수 있습니다 -->
        </div>
      </div>
    </nav>

    <RouterView />
  </div>
</template>

<script>
import { defineComponent } from 'vue';
import { RouterLink, RouterView } from 'vue-router';
import Logo from '@/assets/logo.svg';

export default defineComponent({
  name: 'App',
  components: {
    RouterLink,
    RouterView,
  },
  data() {
    return {
      logo: Logo,
    };
  },
});
</script>
