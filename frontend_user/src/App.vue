<script setup lang="ts">
  import { RouterView } from 'vue-router'
</script>

<template>
  <RouterView />
</template>

<style scoped>
  html,
  body {
    width: 100%;
    height: 100%;
    margin: 0 auto;
    padding: 0;
    background-color: white;
    overflow-x: hidden;
  }

  #app {
    width: 100%;
    height: 100%;
    margin: 0 auto;
    padding: 0;
    background-color: black;
  }
</style>
