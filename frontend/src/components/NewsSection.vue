<template>
  <div class="bg-white rounded-lg shadow p-6">
    <h2 class="text-xl font-bold mb-4">관련 뉴스</h2>
    <div v-if="newsList.length === 0" class="text-gray-500 text-center">
      관련 뉴스를 찾을 수 없습니다.
    </div>
    <div v-else class="space-y-4">
      <article
        v-for="news in newsList"
        :key="news.link"
        class="border-b last:border-b-0 pb-4 last:pb-0"
      >
        <a
          :href="news.link"
          target="_blank"
          class="block hover:bg-gray-50 p-2 rounded transition-colors"
        >
          <h3 class="font-semibold text-lg mb-1" v-html="news.title"></h3>
          <p class="text-gray-600 text-sm mb-2" v-html="news.description"></p>
          <div class="text-xs text-gray-500">
            {{ news.pubDate }} | {{ news.source }}
          </div>
        </a>
      </article>
    </div>
  </div>
</template>

<script>
import { defineComponent } from 'vue';

export default defineComponent({
  name: 'NewsSection',
  props: {
    newsList: {
      type: Array,
      required: true,
    },
  },
});
</script>
