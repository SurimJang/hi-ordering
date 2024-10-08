<template>
  <div class="divide-y divide-gray-200 overflow-hidden rounded-lg bg-gray-200 shadow sm:grid sm:grid-cols-2 sm:gap-px sm:divide-y-0">
    <div v-for="(action, actionIdx) in actions" :key="action.title" :class="[actionIdx === 0 ? 'rounded-tl-lg rounded-tr-lg sm:rounded-tr-none' : '', actionIdx === 1 ? 'sm:rounded-tr-lg' : '', actionIdx === actions.length - 2 ? 'sm:rounded-bl-lg' : '', actionIdx === actions.length - 1 ? 'rounded-bl-lg rounded-br-lg sm:rounded-bl-none' : '', 'group relative bg-white p-6 focus-within:ring-2 focus-within:ring-inset focus-within:ring-indigo-500']">
      <div>
        <span :class="[action.iconBackground, action.iconForeground, 'inline-flex rounded-lg p-3 ring-4 ring-white']">
          <component :is="action.icon" class="h-6 w-6" aria-hidden="true" />
        </span>
      </div>
      <div class="mt-8">
        <h3 class="text-base font-semibold leading-6 text-gray-900">
          <a :href="action.href" class="focus:outline-none">
            <span class="absolute inset-0" aria-hidden="true" />
            {{ action.title }}
          </a>
        </h3>
        <component :is="action.chart" :salesData="action.salesData" :salesLabel="action.salesLabel"/>
      </div>
      <span class="pointer-events-none absolute right-6 top-6 text-gray-300 group-hover:text-gray-400" aria-hidden="true">
        <svg class="h-6 w-6" fill="currentColor" viewBox="0 0 24 24">
          <path d="M20 4h1a1 1 0 00-1-1v1zm-1 12a1 1 0 102 0h-2zM8 3a1 1 0 000 2V3zM3.293 19.293a1 1 0 101.414 1.414l-1.414-1.414zM19 4v12h2V4h-2zm1-1H8v2h12V3zm-.707.293l-16 16 1.414 1.414 16-16-1.414-1.414z" />
        </svg>
      </span>
    </div>
  </div>
</template>

<script setup>
import {
  AcademicCapIcon,
  BanknotesIcon,
  CheckBadgeIcon,
  ClockIcon,
  ReceiptRefundIcon,
  UsersIcon,
} from '@heroicons/vue/24/outline';
import PieChart from "@/components/charts/PieChart.vue";
import BarChart from "@/components/charts/BarChart.vue";
import LineChart from "@/components/charts/LineChart.vue";
import { onMounted, ref } from "vue";
import axios from "axios";

const HOST = "http://localhost:8080";
const dailySalesData = ref([]);
const dailySalesLabel = ref([]);
onMounted(() => {
  axios.get(`${HOST}/api/sales/daily`)
      .then(response => {
        if (response.status === 200) {
          const weeklySales = {};
          response.data.forEach(entry => {
            const weekKey = `${entry.year}-${entry.month}-${entry.week}`;
            if (!weeklySales[weekKey]) {
              weeklySales[weekKey] = {
                year: entry.year,
                month: entry.month,
                week: entry.week,
                total_sales_amount: 0,
                total_sales_count: 0
              };
            }
            weeklySales[weekKey].total_sales_amount += entry.sales_amount;
            weeklySales[weekKey].total_sales_count += entry.sales_count;
          });

          const weeklySalesArray = Object.values(weeklySales);
          const data = [];
          const label = [];
          weeklySalesArray.forEach(e => {
            data.push(e.total_sales_amount);
            label.push(`${e.month}월 ${e.week}주`);
          });
          dailySalesData.value = data;
          dailySalesLabel.value = label;
        }
      })
      .catch(error => {
        console.error('get category error: ', error);
      });

});

const timeSalesData = ref([]);
const timeSalesLabel = ref([]);
onMounted(() => {
  axios.get(`${HOST}/api/sales/time`)
      .then(response => {
        if (response.status === 200) {
          // Create arrays for data and labels
          const data = [];
          const labels = [];
          for (let hour = 0; hour <= 23; hour++) {
            const hourStr = hour.toString().padStart(2, '0'); // Format hour with leading zero
            const amountKey = `hour_${hourStr}_amount`;
            if(response.data[amountKey] === undefined){
              data.push(0)
            }else{
              data.push(response.data[amountKey]);
            }

            labels.push(`${hourStr}시`);
          }
          console.log(data)
          timeSalesData.value = data;
          timeSalesLabel.value = labels;
        }
      })
      .catch(error => {
        console.error('get category error: ', error);
      });
});

const monthlySalesData = ref([]);
const monthlySalesLabel = ref([]);
onMounted(() => {
  axios.get(`${HOST}/api/sales/monthly/all`)
      .then(response => {
        if (response.status === 200) {
          console.log(response.data);
          const data = [];
          const label = [];
          response.data.forEach(e => {
            data.push(e.sales_amount);
            label.push(`${e.month}월`);
          });
          monthlySalesData.value = data;
          monthlySalesLabel.value = label;
        }
      })
      .catch(error => {
        console.error('get category error: ', error);
      });
});
const actions = [
  {
    title: '주차별 매출',
    href: '#',
    icon: ClockIcon,
    chart: BarChart,
    iconForeground: 'text-teal-700',
    iconBackground: 'bg-teal-50',
    salesData: dailySalesData,
    salesLabel: dailySalesLabel
  },
  {
    title: '메뉴별 매출',
    href: '#',
    icon: CheckBadgeIcon,
    chart: PieChart,
    iconForeground: 'text-purple-700',
    iconBackground: 'bg-purple-50',
  },
  {
    title: '시간별 매출',
    href: '#',
    icon: UsersIcon,
    chart: LineChart,
    iconForeground: 'text-sky-700',
    iconBackground: 'bg-sky-50',
    salesData: timeSalesData,
    salesLabel: timeSalesLabel
  },
  {
    title: '월별 매출',
    href: '#',
    icon: BanknotesIcon,
    chart: BarChart,
    iconForeground: 'text-yellow-700',
    iconBackground: 'bg-yellow-50',
    salesData: monthlySalesData,
    salesLabel: monthlySalesLabel
  }
];
</script>
