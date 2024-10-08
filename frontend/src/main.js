import { createApp } from 'vue'
import { createPinia } from 'pinia'

import App from './App.vue'
import router from './router'
import './index.css'
import axios from 'axios';
import VueApexCharts from 'vue3-apexcharts';


const app = createApp(App)

axios.defaults.withCredentials = true;
app.use(createPinia());
app.use(router);
app.config.globalProperties.axios=axios;

app.use(VueApexCharts);
app.component('apexchart', VueApexCharts);

app.mount('#app')
