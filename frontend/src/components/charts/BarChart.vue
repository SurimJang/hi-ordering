<template>
  <div>
    <apexchart type="bar" :options="chartOptions" :series="series"></apexchart>
  </div>
</template>

<script>
import {defineComponent, watchEffect, reactive, watchSyncEffect} from 'vue';
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
        data: [{x:'test',y:100}]
      }],
      chartOptions: {
        chart: {
          type: 'bar',
          height: 350
        },
        plotOptions: {
          bar: {
            horizontal: true,
            columnWidth: '55%',
            endingShape: 'rounded'
          },
        },
        dataLabels: {
          enabled: false
        },
        stroke: {
          show: true,
          width: 2,
          colors: ['transparent']
        },
        xaxis: {
          type: 'category',
          categories: [],
          labels: {
            style: {
              fontSize: '12px',
              colors: ['#000']
            },
            formatter: function (val) {
              return val;
            },
            rotate: -45,
            maxHeight: 60
          }
        },
        yaxis: {
          title: {}
        },
        fill: {
          opacity: 1
        },
        tooltip: {
          y: {
            formatter: function (val) {
              return formatStat(val) + "원";
            }
          }
        }
      }
    });

    const formatStat = (value) => {
      return Number(value).toLocaleString();
    };

    watchSyncEffect(() => {
      const formattedData = props.salesData.value.map((value, index) => ({
        x: props.salesLabel.value[index],
        y: value
      }));

      state.series[0].data = formattedData;
    });
    return {
      ...state
    };
  }
});
</script>
