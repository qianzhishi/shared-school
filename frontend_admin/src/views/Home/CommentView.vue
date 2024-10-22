<script lang="ts" setup>
  import { onMounted, ref } from 'vue'
  import { Search } from '@element-plus/icons-vue'
  import CommentItem from '@/components/BaseCommentItem.vue'
  import { storeToRefs } from 'pinia';
  import axios from 'axios'
  import { ElMessage } from 'element-plus'
  import api from '@/api/system'

  // 搜索框输入内容
  const input = ref('')

  // 是否正在加载
  const loading = ref(true);

  // 本地数据存储
  const session = window.sessionStorage;

  // 当前页数
  const currentPage = ref(1);
  const perPage = 20;

  // 处理页面变换
  const handleCurrentChange = (newPage: number) => {
    currentPage.value = newPage;
    getCommentList();
  };

  // 删除指定元素
  function removeItem(index: number) {
    commentList.value.splice(index, 1);
  }

  type comment = {
      commentId: number;
      userId: number;
      name: string;
      avatar: string;
      content: string;
      time: string;
  }

  // 评论列表
  const commentList = ref<Array<comment>>([]);
  const commentCount = ref(0)

  // 获取评论列表
  async function getCommentList() {
    await api.commentListApi({ page: currentPage.value, size: perPage })
        .then((res:any) => {
          if (res.code == 200) {
            commentList.value = res.data.list;
            commentCount.value = res.data.total;
            loading.value = false;
          }
        })
  }

  onMounted(() => {
    loading.value = true;
    getCommentList();
  })
</script>

<template>
  <div class="header">
    <div class="search-bar">
      <el-input v-model="input" style="width: 40%; padding-right: 1vw" placeholder="请输入评论ID" :prefix-icon="Search"
        clearable />
      <el-button type="primary">搜索</el-button>
    </div>
  </div>
  <div class="content" v-loading="loading">
    <transition-group name="list" tag="AdminItem">
      <CommentItem v-for="(item, index) in commentList " :key="item.commentId" :info="item"
        @delete=" removeItem(index)">
      </CommentItem>
    </transition-group>
  </div>
  <div class="center">
    <el-pagination background layout="prev, pager, next" :total="commentCount" :default-page-size="perPage"
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
