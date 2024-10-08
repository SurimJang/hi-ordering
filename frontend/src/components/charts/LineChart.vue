<template>
  <div>
    <apexchart type="line" :options="chartOptions" :series="series"></apexchart>
  </div>
</template>

<script>
import {defineComponent, reactive, watch} from 'vue';

export default defineComponent({

  props: {
    salesData: {
      type: Array,
      required: true
    },
    salesLabel: {
      type: Array,
      required: true
    }
  },
  setup(props) {
    const state = reactive({
      series: [{
        name: 'Sales',
        data: [
          { x: new Date('2023-01-01').getTime(), y: 30 },
          { x: new Date('2023-01-02').getTime(), y: 40 },
          { x: new Date('2023-01-03').getTime(), y: 35 },
          // 더 많은 데이터 포인트 추가
        ]
      }],
      chartOptions: {
        chart: {
          type: 'line',
          height: 350,
          zoom: {
            enabled: true
          }
        },
        xaxis: {
          type: 'category',
          categories: []
        },
        tooltip: {
          x: {
            format: 'HH시'
          }
        }
      }
    });
    const processSalesData = (salesData, salesLabel) => {

      state.chartOptions.xaxis.categories = salesLabel;
      const formattedData = salesData.value.map((amount, index) => ({
        x: `${index}시`, // 시간 레이블
        y: amount
      }));
      state.series[0].data = formattedData;
    };

    watch(
        () => ({salesData: props.salesData, salesLabel: props.salesLabel}),
        (newVal) => {
          processSalesData(newVal.salesData, newVal.salesLabel);
        },
        {immediate: true, deep: true}
    );

    return {
      ...state
    };
  }
});
</script>
