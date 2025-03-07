<template>
  <div class="w-full">
    <div v-if="videoId" class="relative w-full" style="padding-bottom: 56.25%">
      <iframe
        :src="embedUrl"
        class="absolute top-0 left-0 w-full h-full"
        frameborder="0"
        allow="accelerometer; autoplay; clipboard-write; encrypted-media; gyroscope; picture-in-picture"
        allowfullscreen
      ></iframe>
    </div>
    <div v-else class="bg-gray-100 rounded-lg p-4 text-center text-gray-500">
      동영상을 찾을 수 없습니다.
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
    embedUrl() {
      return `https://www.youtube.com/embed/${this.videoId}`;
    },
  },
});
</script>
