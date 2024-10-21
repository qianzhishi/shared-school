<script lang="ts" setup>
  import { ref, onMounted } from 'vue'
  import Card from '@/components/BaseCard.vue'
  import { ElMessage } from 'element-plus'
  import type { cardInfo } from '@/types/types'
  import api from '@/api/content'

  // 是否正在加载
  const loading = ref(true);

  // 卡片列表
  const cardList = ref<Array<cardInfo>>();
  const cardCount = ref(0);

  // 当前页数
  const currentPage = ref(1);

  // TODO：切换页面后回到顶部
  // 处理页面变换
  const handleCurrentChange = (newPage: number) => {
    const element = document.getElementById("module-nav-bar");
    if (element) {
      element.scrollIntoView({
        behavior: "smooth"
      });
    }
    currentPage.value = newPage;
    getCardInfo();
  };

  // 本地数据存储
  const store = window.localStorage;

  // 获取卡片信息
  const getCardInfo = async()=> {
    await api.postListApi({
        userId: Number(store.getItem('userId')),
        type: 1,
        size: 10,
        page: currentPage.value,
    })
    .then((res: any) => {
        if (res.code == 200) {
          // 导入卡片信息
          cardCount.value = res.total;
          cardList.value = res.data.list;

          loading.value = false;
        }
    })
  }

  onMounted(() => {
    loading.value = true;
    getCardInfo();
  })
</script>

<template>
  <div>
    <div class="container" v-loading="loading">
      <div v-for="card in cardList" :key="card.cardId">
        <Card :info="card">
          <template #icon>
            <svg t="1716613194352" class="icon" viewBox="0 0 1036 1024" version="1.1" xmlns="http://www.w3.org/2000/svg"
              p-id="10877" width="200" height="200">
              <path
                d="M1008 329.6c-153.6-67.2-307.2-131.2-460.8-198.4-16-6.4-32-6.4-48 0-150.4 67.2-304 131.2-454.4 195.2-16 6.4-32 12.8-32 35.2 0 22.4 16 28.8 32 35.2 140.8 60.8 281.6 118.4 422.4 182.4 38.4 19.2 73.6 22.4 112 0 19.2-12.8 41.6-19.2 64-28.8 89.6-38.4 176-76.8 268.8-115.2v323.2c0 19.2 6.4 25.6 25.6 25.6 19.2 0 25.6-3.2 25.6-25.6 0-96 3.2-195.2-3.2-291.2 0-38.4 6.4-64 48-73.6 16-3.2 28.8-12.8 28.8-32s-12.8-25.6-28.8-32z"
                fill="#ffffff" p-id="10878"></path>
              <path
                d="M848 547.2c0-22.4-6.4-22.4-22.4-16-86.4 38.4-172.8 73.6-256 112-32 12.8-57.6 12.8-89.6 0-86.4-38.4-172.8-73.6-259.2-112-16-6.4-19.2-3.2-19.2 12.8v128c-9.6 86.4 35.2 144 108.8 176 147.2 67.2 294.4 64 438.4-6.4 67.2-35.2 105.6-86.4 99.2-169.6-3.2-41.6 0-83.2 0-124.8z"
                fill="#ffffff" p-id="10879"></path>
            </svg>
          </template>
        </Card>
      </div>
    </div>
    <div class="center">
      <el-pagination background layout="prev, pager, next" :total="cardCount" hide-on-single-page
        @current-change="handleCurrentChange" />
    </div>
  </div>
</template>

<style scoped>
  .center {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .container {
    display: grid;
    grid-template-columns: repeat(2, 50%);
    place-items: center;
    min-height: 60vh;
    height: fit-content;
    margin: 5vh 0;
  }

  .tip-area {
    display: grid;
    place-items: center;
  }

  .type-tip {
    font-size: 1.5vw;
  }

  .contact-tip {
    font-weight: bold;
    font-size: 2.2vw;
    color: #409EFF;
  }

  .icon {
    width: 1.5vw;
    height: 1.5vw;
  }
</style>
