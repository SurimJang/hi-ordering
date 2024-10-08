import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './index.css'
import axios from 'axios';
import VueApexCharts from 'vue3-apexcharts';

const app = createApp(App)

axios.defaults.withCredentials = true;

// backend host url
axios.backend = null; // 초기 값 null로 설정

// axios의 fixUrl 메서드 정의
axios.fixUrl = function(original) {
  if (!axios.backend && original.startsWith("/")) return original; // HOST가 설정되지 않았을 경우

  let url;

  try {
    url = new URL(original); // URL 객체로 변환
  } catch (e) {
    // original이 URL 형식이 아닌 경우
    if (axios.backend) {
      url = new URL(axios.backend + original); // HOST를 붙여서 URL 객체 생성
    } else {
      console.error('Invalid URL and no backend URL defined');
      return original; // 잘못된 URL이면 원래의 값을 반환
    }
  }

  // backend URL이 설정되어 있는 경우
  if (axios.backend) {
    url.hostname = new URL(axios.backend).hostname; // backend 호스트 이름
    url.port = new URL(axios.backend).port; // backend 포트
  }

  return url.href; // 완성된 URL 반환
}

// Vue 앱 설정
app.use(createPinia());
app.use(router);
app.config.globalProperties.axios=axios;
app.use(VueApexCharts);

// Vue 컴포넌트 등록
app.component('apexchart', VueApexCharts);

app.mount('#app')