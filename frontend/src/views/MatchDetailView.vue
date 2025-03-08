<template>
  <div class="container mx-auto py-8 px-4">
    <!-- 로딩 상태 -->
    <div v-if="loading" class="flex justify-center items-center py-12">
      <div
        class="animate-spin rounded-full h-12 w-12 border-b-2 border-blue-500"
      ></div>
    </div>

    <!-- 에러 메시지 -->
    <div
      v-else-if="error"
      class="bg-red-100 border border-red-400 text-red-700 px-4 py-3 rounded relative my-4"
    >
      <strong class="font-bold">오류!</strong>
      <span class="block sm:inline"> {{ error }}</span>
    </div>

    <!-- 경기 정보 -->
    <div v-else class="space-y-8">
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
          <NewsSection :newsList="news" />
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { defineComponent, ref, computed, onMounted } from 'vue';
import { useRoute } from 'vue-router';
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
    const route = useRoute();
    const matchId = ref(null);
    const match = ref({});
    const highlightVideo = ref('');
    const news = ref([]);
    const loading = ref(true);
    const error = ref(null);

    onMounted(async () => {
      try {
        // URL에서 matchId 추출
        matchId.value = route.params.id;

        if (!matchId.value) {
          error.value = '경기 ID가 유효하지 않습니다.';
          return;
        }

        // 데이터 로드
        match.value = await fetchMatchById(matchId.value);
        highlightVideo.value = await fetchYoutubeHighlight(matchId.value);
        news.value = await fetchMatchNews(matchId.value);
      } catch (err) {
        console.error('경기 데이터를 불러오는 중 오류 발생:', err);
        error.value = '경기 데이터를 불러오는 중 오류가 발생했습니다.';
      } finally {
        loading.value = false;
      }
    });

    return { match, matchId, highlightVideo, news, loading, error };
  },
});
</script>
