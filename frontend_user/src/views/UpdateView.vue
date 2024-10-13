<script lang="ts" setup>
    import Header from '@/components/BaseHeader.vue'
    import Footer from '@/components/BaseFooter.vue'
    import Update from '@/components/BaseUpdate.vue'
    import { useRoute } from 'vue-router';

    const route = useRoute();

</script>

<template>
    <div class="container">
        <Header></Header>
        <div class="main">
            <div class="update">
                <Update :type="route.query.type" :id="route.query.id"></Update>
            </div>
        </div>
        <Footer></Footer>
    </div>
</template>

<style scoped>
    .container {
        width: 100%;
        background-color: white;
    }

    .main {
        display: flex;
        justify-content: center; /* 水平居中 */
        width: 100%;
        background-color: white;
    }

    .update {
        display: flex;
        width: 75vw;
        border:0.1vw solid;
        border-color: black;
        margin-top: 5vh;
        margin-bottom: 5vh;
        background-color: white;
    }

    a, 
    .green {
        text-decoration: none;
        color: hsla(160, 100%, 37%, 1);
        transition: 0.4s;
        padding: 0px;
    }

    .selector {
        width: 20%;
        border:0.1vw solid;
        border-color: lightgrey;
        background-color: white;
    }


    .select-content {
        width: 80%;
        display: flex;
        justify-content: center; /* 水平居中 */
        background-color: white;
    }
</style>

