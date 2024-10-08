<template>
  <div>
    <apexchart type="donut" :options="chartOptions" :series="series"></apexchart>
  </div>
</template>

<script>
import { defineComponent, reactive } from 'vue';

export default defineComponent({
  setup() {
    const state = reactive({
      series: [178, 121, 60, 68, 69],
      chartOptions: {
        chart: {
          type: 'donut',
          height: 350
        },
        plotOptions: {
          pie: {
            startAngle: -90,
            endAngle: 90,
            offsetY: 10,
          }
        },
        dataLabels: {
          enabled: true
        },
        fill: {
          type: 'gradient'
        },
        legend: {
          position: 'bottom'
        },
        labels: ['한돈 수제돈가츠', '붓가케 우동(국물없는 우동)', '미니우동&돈가츠', '자루소바', '치즈가츠'], // TOP 5 메뉴
        responsive: [{
          breakpoint: 480,
          options: {
            chart: {
              width: 200,
              height: 350,
            },
            legend: {
              position: 'bottom'
            }
          }
        }]
      }
    });

    return {
      ...state
    };
  }
});
</script>
