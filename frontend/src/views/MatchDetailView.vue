<template>
  <div class="container mx-auto py-8 px-4">
    <div class="space-y-8">
      <!-- 매치 정보 -->
      <div class="bg-white rounded-lg shadow p-6">
        <MatchDetailCard
          :date="match.date"
          :time="match.time"
          :stage="match.stage"
          :homeTeam="match.homeTeam"
          :score="match.score"
          :awayTeam="match.awayTeam"
        />
      </div>

      <div class="grid grid-cols-1 md:grid-cols-2 gap-8">
        <div class="space-y-6">
          <!-- 하이라이트 영상 -->
          <YoutubeEmbed v-if="highlightVideo" :videoId="highlightVideo" />
          <!-- 리뷰 섹션 -->
          <ReviewSection v-if="matchId" :matchId="matchId" />
        </div>
        <div>
          <!-- 관련 뉴스 -->
          <NewsSection v-if="news.length > 0" :newsList="news" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, computed, onMounted } from 'vue';
import {
  fetchYoutubeHighlight,
  fetchMatchNews,
  fetchMatchById,
} from '@/api/match';
import MatchDetailCard from '@/components/MatchDetailCard.vue';
import YoutubeEmbed from '@/components/YoutubeEmbed.vue';
import NewsSection from '@/components/NewsSection.vue';
import ReviewSection from '@/components/ReviewSection.vue';

export default defineComponent({
  name: 'MatchDetailView',
  components: {
    MatchDetailCard,
    YoutubeEmbed,
    NewsSection,
    ReviewSection,
  },
  setup() {
    const matchId = ref(null);
    const match = ref({});
    const highlightVideo = ref('');
    const news = ref([]);

    onMounted(async () => {
      matchId.value = window.location.pathname.split('/').pop(); // URL에서 matchId 추출
      match.value = await fetchMatchById(matchId.value);
      highlightVideo.value = await fetchYoutubeHighlight(matchId.value);
      news.value = await fetchMatchNews(matchId.value);
    });

    return { match, matchId, highlightVideo, news };
  },
});
</script>
