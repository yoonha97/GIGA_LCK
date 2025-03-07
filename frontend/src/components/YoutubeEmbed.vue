<template>
  <div class="w-full">
    <div
      v-if="isValidVideo"
      class="relative w-full"
      style="padding-bottom: 56.25%"
    >
      <iframe
        :src="embedUrl"
        class="absolute top-0 left-0 w-full h-full"
        frameborder="0"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowfullscreen
      ></iframe>
    </div>
    <div v-else class="bg-gray-100 rounded-lg p-4 text-center text-gray-500">
      <p>유효한 YouTube 동영상을 찾을 수 없습니다.</p>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'YoutubeEmbed',
  props: {
    videoId: {
      type: String,
      required: true,
    },
  },
  computed: {
    extractedVideoId() {
      // 입력이 이미 유효한 비디오 ID인지 확인
      if (this.isValidYoutubeId(this.videoId)) {
        return this.videoId;
      }

      // URL에서 비디오 ID 추출 시도
      try {
        // 다양한 YouTube URL 형식 처리
        // 1. youtube.com/watch?v=VIDEO_ID
        // 2. youtu.be/VIDEO_ID
        // 3. youtube.com/embed/VIDEO_ID
        // 4. youtube.com/v/VIDEO_ID

        // URL 객체 생성 시도
        let url;
        try {
          url = new URL(this.videoId);
        } catch (e) {
          // URL이 아닌 경우 null 반환
          return null;
        }

        // youtu.be 단축 URL 처리
        if (url.hostname === 'youtu.be') {
          const id = url.pathname.slice(1);
          return this.isValidYoutubeId(id) ? id : null;
        }

        // youtube.com 도메인 처리
        if (url.hostname.includes('youtube.com')) {
          // /watch?v=VIDEO_ID 형식
          if (url.pathname === '/watch') {
            const params = new URLSearchParams(url.search);
            const id = params.get('v');
            return this.isValidYoutubeId(id) ? id : null;
          }

          // /embed/VIDEO_ID 또는 /v/VIDEO_ID 형식
          if (
            url.pathname.startsWith('/embed/') ||
            url.pathname.startsWith('/v/')
          ) {
            const id = url.pathname.split('/')[2];
            return this.isValidYoutubeId(id) ? id : null;
          }
        }

        return null;
      } catch (e) {
        console.error('YouTube 비디오 ID 추출 중 오류:', e);
        return null;
      }
    },

    isValidVideo() {
      return this.extractedVideoId !== null;
    },

    embedUrl() {
      if (!this.isValidVideo) return '';
      return `https://www.youtube.com/embed/${this.extractedVideoId}`;
    },
  },

  methods: {
    isValidYoutubeId(id) {
      // YouTube 비디오 ID는 일반적으로 11자리이며 특정 문자만 포함합니다
      const youtubeIdRegex = /^[a-zA-Z0-9_-]{11}$/;
      return id && youtubeIdRegex.test(id);
    },
  },
});
</script>
