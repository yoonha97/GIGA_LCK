<template>
  <div class="min-h-screen bg-gray-100 py-10">
    <TeamDetail />
  </div>
</template>

<script>
import TeamDetail from '@/components/TeamDetail.vue';

export default {
  name: 'TeamDetailView',
  components: {
    TeamDetail,
  },
};
</script>
