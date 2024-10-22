<script lang="ts" setup>
  import { ref } from 'vue'
  import { UserFilled } from '@element-plus/icons-vue'
  import { ElMessage } from 'element-plus'
  import { WarnTriangleFilled } from '@element-plus/icons-vue'
  import api from '@/api/system'

  // 显示对话框标识符
  const detailDialogVisible = ref(false);

  type user = {
    userId: number;
    username: string;
    avatar: string;
    intro: string;
  }

  // 传入参数
  const props = defineProps({
    info: {
      type: Object as () => user,
      required: true,
    },
  })

  // 定义事件
  const emit = defineEmits(['delete']);

  // 用户信息
  const userAvatar = props['info'].avatar;
  const userID = props['info'].userId;
  const userName = props['info'].username;
  const userIntro = props['info'].intro;

  // 删除用户
 async function deleteUser() {
    await api.delUserApi({ id: userID })
        .then((res:any) => {
          if (res.code == 200) {
            emit('delete');
            ElMessage.success('删除成功！');
          }
        })
  }

</script>

<template>
  <div class="user-item">
    <div class="info-bar">
      <el-avatar :size="50" :src="userAvatar" v-if="userAvatar != null && userAvatar != ''" />
      <el-avatar :size="50" :icon="UserFilled" v-else />
      <div class="info">
        <div style="display: flex">
          <h1 class="title">用户ID：</h1>
          <h1 class="title-text">{{ userID }}</h1>
        </div>
        <div style="display: flex">
          <h1 class="title">用户名：</h1>
          <h1 class="title-text">{{ userName }}</h1>
        </div>
      </div>
    </div>
    <div class="btn-bar">
      <el-button type="primary" @click="detailDialogVisible = true;">查看信息</el-button>
      <el-popover placement="right" :width="'14vw'" trigger="click">
      </el-popover>
      <el-popconfirm width="220" confirm-button-text="确定" confirm-button-type="danger" cancel-button-text="取消"
        :icon="WarnTriangleFilled" icon-color="#F56C6C" title="你确定要删除吗？" @confirm="deleteUser()">
        <template #reference>
          <el-button type="danger">删除用户</el-button>
        </template>
      </el-popconfirm>
    </div>

    <!-- 显示用户详情对话框 -->
    <el-dialog v-model="detailDialogVisible" title="用户详情" width="30vw" center>
      <div class="detail-dialog-container">
        <el-avatar :size="100" :src="userAvatar" v-if="userAvatar != null && userAvatar != ''" />
        <el-avatar :size="50" :icon="UserFilled" v-else />
        <el-descriptions style="width:25vw;" title="用户信息" :column="1" :size="'default'" :border="true">
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <Connection />
                </el-icon>
                用户ID
              </div>
            </template>
            {{ userID }}
          </el-descriptions-item>
          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <user />
                </el-icon>
                用户名
              </div>
            </template>
            {{ userName }}
          </el-descriptions-item>

          <el-descriptions-item>
            <template #label>
              <div class="cell-item">
                <el-icon>
                  <Postcard />
                </el-icon>
                简介
              </div>
            </template>
            {{ userIntro }}
          </el-descriptions-item>
        </el-descriptions>
      </div>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="primary" @click="detailDialogVisible = false">确定</el-button>
        </div>
      </template>
    </el-dialog>

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

  /* 显示用户详情对话框部分 */
  .detail-dialog-container {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    gap: 1.5vh;
    margin: 2vh 0;
  }
</style>
