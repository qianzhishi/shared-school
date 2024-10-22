<script setup lang="ts">
  import { useRouter, RouterView } from 'vue-router'
  import {  onMounted, ref } from 'vue'
  import Header from '@/components/TheHeader.vue'
  import { useMenuStore } from '@/stores/menu'

  // 路由
  const router = useRouter()

  // 本地数据存储
  const session = window.sessionStorage;

  // 仓库
  const menuStore = useMenuStore();

  // 当前登录的管理员ID
  const adminId = ref()

  // 更新当前菜单索引
  function handleSelect(key: string) {
    session.setItem('activeIndex', key);
    menuStore.activeIndex = key;
  }

  // 路由跳转
  function redirect(url: string) {
    router.push(url)
  }

  // 获取当前登录的管理员ID
  function setAdminId() {
    if (session.getItem('adminId') != undefined) {
      adminId.value = session.getItem('adminId')
    } else {
      adminId.value = 'undefined'
    }
  }

  // 页面载入时的操作
  onMounted(() => {
    if (session.getItem('activeIndex') !== null) {
      menuStore.activeIndex = String(session.getItem('activeIndex'));
    }
    else {
      menuStore.activeIndex = '0';
    }
    setAdminId();
  })
</script>

<template>
  <el-container>
    <el-header class="header">
      <Header></Header>
    </el-header>

    <el-container>
      <el-aside class="nav">
        <el-menu class="nav-bar" @select="handleSelect" :default-active="menuStore.activeIndex">
          <el-menu-item index="0" @click="redirect('/home/welcome');" :class="{ active: menuStore.activeIndex == '0' }">
            <template #title>
              <el-icon>
                <House />
              </el-icon>
              <span>主页</span>
            </template>
          </el-menu-item>

          <el-menu-item index="1" @click="redirect('/home/user');;" :class="{ active: menuStore.activeIndex == '1' }">
            <template #title>
              <el-icon>
                <User />
              </el-icon>
              <span>用户管理</span>
            </template>
          </el-menu-item>

          <el-menu-item index="2" @click="redirect('/home/admin');" :class="{ active: menuStore.activeIndex == '2' }">
            <el-icon>
              <Service />
            </el-icon>
            <span>管理员管理</span>
          </el-menu-item>

          <el-sub-menu index="3" :class="{ active: menuStore.activeIndex == '3' }">
            <template #title>
              <el-icon>
                <Document />
              </el-icon>
              <span>投稿管理</span>
            </template>
            <el-menu-item index="3-1" @click="redirect('/home/upload-post');"
              :class="{ active: menuStore.activeIndex == '3-1' }">帖子管理</el-menu-item>
            <el-menu-item index="3-2" @click="redirect('/home/upload-resource');"
              :class="{ active: menuStore.activeIndex == '3-2' }">资源管理</el-menu-item>
          </el-sub-menu>

          <el-menu-item index="4" @click="redirect('/home/comment');" :class="{ active: menuStore.activeIndex == '4' }">
            <el-icon>
              <ChatDotRound />
            </el-icon>
            <span>评论管理</span>
          </el-menu-item>

        </el-menu>
      </el-aside>

      <el-main class="content">
        <RouterView />
      </el-main>
    </el-container>
  </el-container>
</template>

<style scoped>

  a,
  .green {
    color: black;
    background-color: transparent;
    width: 100%;
    height: fit-content;
  }

  .center {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .header {
    display: flex;
    justify-content: space-between;
    width: 100vw;
    height: 10vh;
    background: white;
    padding: 1vh 3vw;
    border-bottom: 1px solid lightgrey;
  }

  .nav {
    width: 18vw;
    height: 90vh;
    background: white;
  }

  .nav-bar {
    height: 90vh;
    background: white;
  }

  .active {
    background: #409eff;
    color: white;
  }

  .content {
    min-width: 82vw;
    height: 90vh;
    background: white;
  }
</style>
