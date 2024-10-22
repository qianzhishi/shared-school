<script lang="ts" setup>
  import { onMounted, ref } from 'vue'
  import { Search } from '@element-plus/icons-vue'
  import UserItem from '@/components/BaseUserItem.vue'
  import api from '@/api/system'
import { ElMessage } from 'element-plus';

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
    getUserList();
  };

  // 删除指定元素
  function removeItem(index: number) {
    userList.value.splice(index, 1);
  }

  type user = {
    userId: number;
    username: string;
    avatar: string;
    intro: string;
  }

  // 用户列表
  const userList = ref<Array<user>>([]);
  const userCount = ref(0)

  // 获取用户列表
  async function getUserList() {
    await api.userListApi({ page: currentPage.value, size: perPage })
      .then((res: any) => {
        if (res.code == 200) {
            userList.value = res.data.list;
            userCount.value = res.data.total;
            loading.value = false;
        }
      })
  }
    // 搜索用户
async function searchUser() {
  // 如果输入框为空或不为数字，则不进行搜索
  if (!input.value || isNaN(Number(input.value))) {
      ElMessage.error('请输入正确的用户ID')
      return;
  }
  await api.getUserApi({ id: Number(input.value) })
    .then((res: any) => {
      if (res.code == 200) {
          userList.value = [res.data]
          userCount.value = 1
          loading.value = false;
      }
    })
  }

  onMounted(() => {
    loading.value = true;
    getUserList();
  })
</script>

<template>

  <div class="header">
    <div class="search-bar">
      <el-input v-model="input" style="width: 40%; padding-right: 1vw;" placeholder="请输入用户ID" :prefix-icon="Search"
        clearable />
      <el-button type="primary" @click="searchUser()">搜索</el-button>
    </div>
  </div>
  <div class="content" v-loading="loading">
    <transition-group name="list" tag="AdminItem">
      <UserItem v-for="(item, index) in userList" :key="item.userId" :info="item" @delete="removeItem(index)">
      </UserItem>
    </transition-group>
  </div>
  <div class="center">
    <el-pagination background layout="prev, pager, next" :total="userCount" :default-page-size="perPage"
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
