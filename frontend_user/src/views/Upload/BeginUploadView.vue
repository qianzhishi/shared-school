
<script lang="ts" setup>

</script>

<template>
  <div class="container">
      <img src="@/assets/images/upload_begin.png"></img>
      <div class="title">欢迎来到投稿中心</div>
  </div>
</template>

<style scoped>
  .container {
      width: 90%;
      display:flex;
      justify-content: center; /* 水平居中 */
      align-items: center; /* 垂直居中 */
      background-color: white;
      min-height: 80vh;
      flex-direction: column; /* 如果内容不止一行，确保垂直排列 */
  }

  .mar{
      text-align: center; /* 使文本在容器内居中 */
  }

  .title {
    font-size: 34px;
    margin-top: center;
    margin-top: 30px;
    margin-bottom: 50px;
    background-image: -webkit-linear-gradient(left,#e19ea5,#bcc0c1 10%,#6dc7db 20%,#ae9dd9 30%,
    #87c6d8 40%,#e39ea5 50%,#b9c2bf 60%,#70c6db 70%,#ae9dd9 80%,#87c6d8 90%, #e9a5a5);

    color: transparent;
    background-clip: text;

    background-size: 200% 100%;
    
    animation: a 5s infinite linear;
  }

  @keyframes a {
    0% {
      background-position: 0 0;
    }

    100% {
      background-position: -100% 0;
    }
  }

</style>