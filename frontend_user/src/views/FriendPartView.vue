<script lang="ts" setup>
  import { useRouter, RouterView } from 'vue-router'

  // 路由
  const router = useRouter();

  // 路由跳转
  function redirect(url: string) {
    router.push(url);
  }
</script>

<template>
  <div class="top-bar">
    <el-button class="btn-route" type="primary" round @click="redirect('/home/friend-part/study')">学习</el-button>
    <el-button class="btn-route" type="primary" round @click="redirect('/home/friend-part/play')">娱乐</el-button>
    <el-button class="btn-route" type="primary" round @click="redirect('/home/friend-part/love')">表白</el-button>
  </div>
  <RouterView />
</template>

<style scoped>
  .center {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .top-bar {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 2vw;
  }

  .btn-route {
    width: 15vw;
    height: 8vh;
    font-size: 4vb;
    transition: background-color 0.5s ease, transform 0.3s ease;
  }

  .btn-route:hover {
    background-color: #79bbff;
    transform: scale(1.1);
  }
</style>
