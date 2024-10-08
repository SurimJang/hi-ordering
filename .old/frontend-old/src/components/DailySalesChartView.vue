<template>

    <v-data-table
        :headers="headers"
        :items="dailySalesChart"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'DailySalesChartView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            dailySalesChart : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/dailySalesCharts'))

            temp.data._embedded.dailySalesCharts.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.dailySalesChart = temp.data._embedded.dailySalesCharts;
        },
        methods: {
        }
    }
</script>

