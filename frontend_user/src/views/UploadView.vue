<script lang="ts" setup>
  import Header from '@/components/BaseHeader.vue'
  import Footer from '@/components/BaseFooter.vue'
  import { ref, onMounted } from 'vue';
  import { useRoute, useRouter } from 'vue-router';
  import { Back } from '@element-plus/icons-vue'

  const currentActiveIndex = ref('');

  const router = useRouter();

  onMounted(() => {
      // 在组件挂载时，根据当前路由恢复选中状态
      const route = useRoute();
      const pathToIndexMap: { [key: string]: string | undefined } = {
          '/upload/study': '1-1',
          '/upload/play': '1-2',
          '/upload/love': '1-3',
          '/upload/resource': '2',
      };
      currentActiveIndex.value = pathToIndexMap[route.path] || '';
  });

</script>

<template>
  <div class="container">
      <Header></Header>
      <div class="back-nav">
          <el-button :icon="Back" style="margin-left:12.5vw" @click="router.back()">返回</el-button>
      </div>
      <div class="main">
          <div class="upload">
              <div class="selector">
                  <el-menu :default-active="currentActiveIndex">
                      <el-sub-menu index="1">
                          <template #title>
                              <span>知趣情缘</span>
                          </template>
                          <RouterLink to="/upload/study">
                              <el-menu-item index="1-1">学习</el-menu-item>
                          </RouterLink>
                          <RouterLink to="/upload/play">
                              <el-menu-item index="1-2">娱乐</el-menu-item>
                          </RouterLink>
                          <RouterLink to="/upload/love">
                              <el-menu-item index="1-3">表白</el-menu-item>
                          </RouterLink>
                      </el-sub-menu>
                      <RouterLink to="/upload/resource">
                          <el-menu-item index="2">
                              <span>上传资源</span>
                          </el-menu-item>
                      </RouterLink>
                  </el-menu>
              </div>

              <div class="select-content">
                  <RouterView />
              </div>
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

  .back-nav {
      display: flex;
      align-items: center;
      height: 10vh;
      width: 100vw;
  }

  .main {
      display: flex;
      justify-content: center;
      /* 水平居中 */
      width: 100%;
      background-color: white;
  }

  .upload {
      display: flex;
      width: 75vw;
      border: 0.1vw solid;
      border-color: black;
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
      border: 0.1vw solid;
      border-color: lightgrey;
      background-color: white;
  }


  .select-content {
      width: 80%;
      display: flex;
      justify-content: center;
      /* 水平居中 */
      background-color: white;
  }
</style>
