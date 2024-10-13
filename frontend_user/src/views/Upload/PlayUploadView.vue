<script lang="ts" setup>
  import Upload from '@/components/BaseUpload.vue'

</script>

<template>
  <div class="container">
      <Upload :type="2"></Upload>
  </div>
</template>

<style scoped>
  .container {
      width: 90%;
      background-color: white;
  }
</style>