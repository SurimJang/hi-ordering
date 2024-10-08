<template>

    <v-data-table
        :headers="headers"
        :items="menuList"
        :items-per-page="5"
        class="elevation-1"
    ></v-data-table>

</template>

<script>
    const axios = require('axios').default;

    export default {
        name: 'MenuListView',
        props: {
            value: Object,
            editMode: Boolean,
            isNew: Boolean
        },
        data: () => ({
            headers: [
                { text: "id", value: "id" },
            ],
            menuList : [],
        }),
          async created() {
            var temp = await axios.get(axios.fixUrl('/menuLists'))

            temp.data._embedded.menuLists.map(obj => obj.id=obj._links.self.href.split("/")[obj._links.self.href.split("/").length - 1])

            this.menuList = temp.data._embedded.menuLists;
        },
        methods: {
        }
    }
</script>

