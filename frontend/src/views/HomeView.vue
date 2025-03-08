<template>
  <main class="container mx-auto py-8 px-4">
    <h1 class="text-center mt-2 text-3xl md:text-4xl font-bold mb-4">
      LCK 경기 일정
    </h1>
    <div class="space-y-2">
      <MatchList />
    </div>
  </main>
</template>

<script>
import { defineComponent } from 'vue';
import MatchList from '@/components/MatchList.vue';

export default defineComponent({
  name: 'HomeView',
  components: {
    MatchList,
  },
});
</script>
