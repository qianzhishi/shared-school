<script lang="ts" setup>
    import { onMounted, ref } from 'vue'
    import { Search } from '@element-plus/icons-vue'
    import { useRouter, useRoute } from 'vue-router';
    import { UserFilled } from '@element-plus/icons-vue'
    import { ElMessage } from 'element-plus'

    // 本地数据存储
    const store = window.localStorage;

    // 路由
    const router = useRouter();
    const route = useRoute();

    // 路由跳转
    function redirect(url: string) {
        router.push(url);
    }

    // 当前活动菜单
    const activeIndex = ref('0')

    const userAvatar = ref(store.getItem('userAvatar'));

    const handleCommand = (command: string | number | object) => {
        if (command == '1') {
            if (store.getItem('isLogin') == 'true') {
                router.push({ path: '/user', query: { id: Number(store.getItem('userId')) } })
            }
            else {
                ElMessage.error("您尚未登录！")
            }
        }
        else if (command == '2') {
            store.setItem('isLogin', 'false');
            store.setItem('userId', '0');
            store.setItem('userAvatar', 'https://small-event-bucket1.oss-cn-hangzhou.aliyuncs.com/avatarDefault.jpg');
            userAvatar.value = store.getItem('userAvatar');
            setLoginState();
            router.push('/home');
            ElMessage.success("您已退出登录！")
        }
        else if (command == '3') {
            router.push('/login');
        }
    }

    // 登录标识符
    const isLogin = ref(false);

    // 获取登录状态
    function setLoginState() {
        if (store.getItem('isLogin') == 'true') {
            isLogin.value = true;
        }
        else {
            isLogin.value = false;
        }
    }

    // 搜索框内容
    const input = ref('')

    function search() {
        if (input.value !== '') {
            if (route.name === 'search') {
                router.push({ path: '/search', query: { input: input.value, type: 0 } })
                    .then(() => {
                        // 重新刷新页面
                        location.reload()
                    });
            } else {
                router.push({ path: '/search', query: { input: input.value, type: 0 } });
            }
        }
    }

    onMounted(() => {
        setLoginState();
    })
</script>

<template>
  <div class="header">
      <el-space class="center" direction="vertical" :size="30">
          <!-- 导航栏 -->
          <el-affix :offset="0">
              <el-menu class="nav-bar" :default-active="activeIndex" mode="horizontal" :ellipsis=false>
                  <div class="center" style="padding-right: 2vw;">
                      <span style="display: flex; justify-content: center; align-items: center">
                          <img style="display: inline; width: 60px" src="@/assets/images/logo.png" alt="logo" />
                          <h1 style="display: inline" class="title">共享学园</h1>
                      </span>
                  </div>
                  <el-menu-item class="nav-label" index="1" @click="redirect('/home')">主页</el-menu-item>
                  <el-sub-menu index="2">
                      <template #title><label class="nav-label">知趣情缘</label></template>
                      <el-menu-item index="2-1" @click="redirect('/home/friend-part/study')">学习</el-menu-item>
                      <el-menu-item index="2-2" @click="redirect('/home/friend-part/play')">娱乐</el-menu-item>
                      <el-menu-item index="2-3" @click="redirect('/home/friend-part/love')">表白</el-menu-item>
                  </el-sub-menu>
                  <el-menu-item class="nav-label" index="3"
                      @click="redirect('/home/resource-part')">资源中心</el-menu-item>
                  <div class="flex-grow"></div>
                  <div class="center">
                      <el-space wrap :size="20">
                          <span class="center" style="gap:0.5vw;">
                              <el-input style="width: 25vw" :prefix-icon="Search" v-model="input"
                                  v-on:keyup.enter="search" placeholder="来问问我们吧，我们这什么都有"></el-input>
                              <el-button style="width: 2vw" :icon="Search" type="primary" round
                                  @click="search"></el-button>
                          </span>
                          <span class="center">
                              <el-dropdown @command="handleCommand">
                                  <el-avatar class="avatar" :src="userAvatar"
                                      v-if="userAvatar != null && userAvatar != ''" />
                                  <el-avatar class="avatar" :icon="UserFilled" v-else />
                                  <template #dropdown>
                                      <el-dropdown-menu>
                                          <el-dropdown-item command="1">个人主页</el-dropdown-item>
                                          <el-dropdown-item command="2" v-if="isLogin == true">退出登录</el-dropdown-item>
                                          <el-dropdown-item command="3" v-else>登录</el-dropdown-item>
                                      </el-dropdown-menu>
                                  </template>
                              </el-dropdown>
                          </span>
                      </el-space>
                  </div>
              </el-menu>
          </el-affix>
      </el-space>
  </div>
</template>

<style scoped>
    .header {
        width: 100vw;
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .center {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .nav-bar {
        width: 100vw;
        padding: 0 3vw;
    }

    .flex-grow {
        flex-grow: 1;
    }

    .title {
        color: black;
        font-size: 2em;
        text-align: center;
        text-justify: center;
    }

    .nav-label {
        font-weight: bold;
    }

    .avatar {
        width: 2.5rem;
        height: 2.5rem;
        transition: transform 0.5s ease, box-shadow 0.5s ease;
    }

    .avatar:hover {
        transform: scale(1.1);
        box-shadow: 5px 5px 5px lightgrey;
    }
</style>
