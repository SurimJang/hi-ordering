<script setup>

import SalesStat from "@/components/SalesStat.vue";
import SalesChart from "@/components/SalesChart.vue";
import {onMounted} from "vue";
onMounted(() => {
  const storeName = localStorage.getItem('store_name');
  if (!storeName) {
    alert("매출 정보를 불러오지 못했습니다. 로그인해서 가게를 알려주세요.")
    window.location.href = '/login';
  }
});
</script>

<template>
  <SalesStat/>
  <SalesChart class="mt-8"/>
</template>