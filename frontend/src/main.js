import './assets/main.css';
import { createApp } from 'vue';
import { createPinia } from 'pinia';
import App from './App.vue';

// 환경 변수 로딩 확인
console.log('API Key loaded:', !!import.meta.env.VITE_RIOT_API_KEY);
console.log(
  'API Key prefix:',
  import.meta.env.VITE_RIOT_API_KEY?.substring(0, 4)
);

const app = createApp(App);
app.use(createPinia());
app.mount('#app');
