<script lang="ts" setup>
  import { WarnTriangleFilled } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  import api from '@/api/system';

  // 定义事件
  const emit = defineEmits(['delete']);

  type admin = {
    adminId: number;
    adminName: string;
  }

  // 传入参数
  const props = defineProps({
    info: {
      type: Object as () => admin,
      required: true,
    },
  })


  // 管理员信息
  const avatar = 'https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png'
  const adminID = props['info'].adminId
  const adminName = props['info'].adminName;

    // 删除管理员
  async function deleteAdmin() {
    await api.delAdminApi({ adminId: adminID })
          .then((res:any) => {
            if (res.code == 200) {
              emit('delete')
              ElMessage.success('删除成功!')
            }
          })
  }
</script>

<template>
  <div class="user-item">
    <div class="info-bar">
      <el-avatar :size="50" :src="avatar" />
      <div class="info">
        <div style="display: flex">
          <h1 class="title">管理员ID：</h1>
          <h1 class="title-text">{{ adminID }}</h1>
        </div>
        <div style="display: flex">
          <h1 class="title">姓名：</h1>
          <h1 class="title-text">{{ adminName }}</h1>
        </div>
      </div>
    </div>
    <div class="btn-bar">
      <el-popconfirm width=" 220" confirm-button-text="确定" confirm-button-type="danger" cancel-button-text="取消"
        :icon="WarnTriangleFilled" icon-color="#F56C6C" title="你确定要删除吗？" @confirm="deleteAdmin()">
        <template #reference>
          <el-button type="danger">删除管理员</el-button>
        </template>
      </el-popconfirm>
    </div>
  </div>
</template>

<style scoped>
  .user-item {
    display: flex;
    justify-content: space-between;
    min-width: 80%;
    min-height: 10vh;
    border-radius: 2vh;
    border: 1px solid lightgray;
    margin: 2vh 0;
    padding: 0 2vw;
    transition: background-color 0.5s ease;
  }

  .user-item:hover {
    background-color: #d9ecff;
  }

  .info-bar {
    display: flex;
    justify-content: center;
    align-items: center;
    color: black;
  }

  .info {
    margin: 0 1vw;
  }

  .title {
    color: gray;
    font-style: bold;
  }

  .title-text {
    min-width: 8vw;
    max-width: 8vw;
    white-space: nowrap;
    overflow: hidden;
    text-overflow: ellipsis;
  }

  .btn-bar {
    display: flex;
    justify-content: center;
    align-items: center;
  }
</style>
