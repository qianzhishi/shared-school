<script lang="ts" setup>
  import { onMounted, ref } from 'vue'
  import { Search } from '@element-plus/icons-vue'
  import AdminItem from '@/components/BaseAdminItem.vue'
  import { ElMessage } from 'element-plus'
  import api from '@/api/system'

  // 搜索框输入内容
  const input = ref('')

  // 新管理员信息
  const newAdminName = ref('');

  // 显示添加管理员对话框标识符
  const addDialogVisible = ref<boolean>(false);

  // 是否正在加载
  const loading = ref(true);

  // 当前页数
  const currentPage = ref(1);
  const perPage = 20;

  // 处理页面变换
  const handleCurrentChange = (newPage: number) => {
    currentPage.value = newPage;
    getAdminList();
  };

  // 删除指定元素
  function removeItem(index: number) {
    adminList.value.splice(index, 1);
  }

  type admin = {
    adminId: number;
    adminName: string;
  }

  // 管理员列表
  const adminList = ref<Array<admin>>([]);
  const adminCount = ref(0)

  // 添加管理员
  async function addAdmin() {
      await api.addAdminApi({
        name: newAdminName.value,
        password: '12345678',
      }).then((res:any) => {
        if (res.code == 200) {
          ElMessage.success('添加成功!')
        }
      })
  }

  // 获取管理员列表
  async function getAdminList() {
      await api.adminListApi({
        page: currentPage.value,
        size: perPage,
      }).then((res: any) => {
        if (res.code == 200) {
          adminList.value = res.data.list;
          adminCount.value = res.data.total;
          loading.value = false;
        }
      })
  }

  onMounted(() => {
    loading.value = true;
    getAdminList();
  })
</script>

<template>
  <div class="header">
    <div class="search-bar">
      <el-input v-model="input" style="width: 40%; padding-right: 1vw" placeholder="请输入管理员ID" :prefix-icon="Search"
        clearable />
      <el-button type="primary">搜索</el-button>
    </div>
    <div style="display: flex">
      <el-button style="margin-right: 1vw" type="primary"
        @click="addDialogVisible = true; newAdminName = ''">添加管理员</el-button>
    </div>
  </div>
  <div class="content" v-loading="loading">
    <transition-group name="list" tag="AdminItem">
      <AdminItem v-for="(item, index) in  adminList " :key="item.adminId" :info="item" @delete=" removeItem(index)">
      </AdminItem>
    </transition-group>
  </div>
  <div class="center">
    <el-pagination background layout="prev, pager, next" :total="adminCount" :default-page-size="perPage"
      hide-on-single-page @current-change="handleCurrentChange" />
  </div>

  <!-- 添加管理员对话框 -->
  <el-dialog v-model="addDialogVisible" title="添加管理员" width="500" center>
    <div class="add-dialog-container">
      <el-avatar :size="100" :src="'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'" />
      <div>
        <label class="add-dialog-label">姓名：</label>
        <el-input v-model="newAdminName" style="width: 12vw" placeholder="请输入管理员姓名" clearable />
      </div>
      <div>
        <label style="text-align:center">默认初始密码：<label style="color: red;font-weight: bold;">12345678</label></label>
      </div>
    </div>
    <template #footer>
      <div class="dialog-footer">
        <el-button type="info" plain @click="addDialogVisible = false">取消</el-button>
        <el-button type="primary" @click="addAdmin(); addDialogVisible = false"
          :disabled="newAdminName === ''">添加</el-button>
      </div>
    </template>
  </el-dialog>
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

  /* 添加管理员对话框部分 */
  .add-dialog-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1.5vh;
    margin: 2vh 0;
  }

  .add-dialog-label {
    text-align: center;
    min-width: 5vw;
  }
</style>
