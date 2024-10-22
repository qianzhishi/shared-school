<script lang="ts" setup>
  import { onMounted, ref} from 'vue'
  import { Search } from '@element-plus/icons-vue'
  import PostItem from '@/components/BasePostItem.vue'
  import api from '@/api/system'

  // 搜索框输入内容
  const input = ref('')

  // 是否正在加载
  const loading = ref(true);

  // 当前页数
  const currentPage = ref(1);
  const perPage = 20;

  // 处理页面变换
  const handleCurrentChange = (newPage: number) => {
    currentPage.value = newPage;
    getPostList();
  };

  // 删除指定元素
  function removeItem(index: number) {
    postList.value.splice(index, 1);
  }

  type post = {
        cardId: number;
        type: number;
        userId: number;
        name: string;
        avatar: string;
        title: string;
        content: string;
        time: string;
        images: Array<string>;
  }

  // 筛选条件
  const screenCondition = ref(0)

  // 评论列表
  const postList = ref<Array<post>>([]);
  const postCount = ref(0)

  // 获取筛选条件
  const handleCommand = (command: string | number | object) => {
    screenCondition.value = Number(command)
    currentPage.value = 1;
    getPostList();
  }

  // 获取帖子列表
async function getPostList() {
    await api.postListApi({ page: currentPage.value, size: perPage, type: screenCondition.value })
        .then((res:any) => {
          if (res.code == 200) {
            postList.value = res.data.list;
            postCount.value = res.data.total;
            loading.value = false;
          }
        })
  }

  onMounted(() => {
    loading.value = true;
    getPostList();
  })
</script>

<template>
  <div class="header">
    <div class="search-bar">
      <el-input v-model="input" style="width: 40%; padding-right: 1vw" placeholder="请输入帖子ID" :prefix-icon="Search"
        clearable />
      <el-button type="primary">搜索</el-button>
    </div>
    <div>
      <el-dropdown @command="handleCommand">
        <el-button type="info">
          筛选<el-icon class="el-icon--right"><arrow-down /></el-icon>
        </el-button>
        <template #dropdown>
          <el-dropdown-menu>
            <el-dropdown-item command="0">全部</el-dropdown-item>
            <el-dropdown-item command="1">学习</el-dropdown-item>
            <el-dropdown-item command="2">娱乐</el-dropdown-item>
            <el-dropdown-item command="3">表白</el-dropdown-item>
          </el-dropdown-menu>
        </template>
      </el-dropdown>
    </div>
  </div>
  <div class="content" v-loading="loading">
    <transition-group name="list" tag="PostItem">
      <PostItem v-for="(item, index) in postList " :key="item.cardId" :info="item" @delete=" removeItem(index)">
      </PostItem>
    </transition-group>
  </div>
  <div class="center">
    <el-pagination background layout="prev, pager, next" :total="postCount" :default-page-size="perPage"
      hide-on-single-page @current-change="handleCurrentChange" />
  </div>
</template>

<style scoped>
  .center {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  .header {
    display: flex;
    justify-content: space-between;
  }

.search-bar {
  width: 50%;
  display: flex;  
  align-items: center;  
  border: 1px solid #dcdfe6;  /* 边框样式 */  
  border-radius: 4px;  /* 圆角边框 */  
  padding: 5px;  /* 内边距 */  
}  

.search-bar .el-input {  
  flex: 1;  /* 搜索框自动填充剩余空间 */  
  border: none;  /* 去除搜索框自带边框 */  
  border-radius: 0;  /* 搜索框保持直角 */  
}  

.search-bar .el-button {  
  margin-left: 10px;  /* 搜索按钮与搜索框间距 */  
}  

  .content {
    width: 100%;
    min-height: 80vh;
  }

  .list-enter-active,
  .list-leave-active {
    transition: opacity 0.5s;
  }

  .list-enter,
  .list-leave-to {
    opacity: 0;
  }
</style>
