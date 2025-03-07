<template>
  <main class="container mx-auto py-8 px-4">
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
