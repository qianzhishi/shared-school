<script lang="ts" setup>
  import { onMounted, ref, reactive } from 'vue'
  import type { FormInstance, FormRules } from 'element-plus'
  import { useRouter } from 'vue-router'
  import { ElMessage } from 'element-plus'
  import api from '@/api/system'


  // 本地数据存储
  const session = window.sessionStorage;

  // 路由
  const router = useRouter()

  // 管理员ID
  const adminId = ref()

  // 表单引用
  const updateInfoFormRef = ref<FormInstance>()

  // 修改表单
  const admin_name = ref('')
  const new_pwd = ref('')
  const new_check = ref('')

  const dialogFormVisible = ref(false)
  const updateInfoForm = reactive({
    admin_name,
    new_pwd,
    new_check,
  })

  // 密码验证器
  const validatePass = (rule: any, value: any, callback: any) => {
    if (value === '') {
      callback(new Error('密码不能为空！'))
    } else if (value.length < 8 || value.length > 16) {
      callback(new Error('密码长度为8-16位！'))
    }
    else {
      callback()
    }
  }
  const validateCheck = (rule: any, value: string, callback: any) => {
    if (value === '') {
      callback(new Error('密码不能为空！'))
    } else if (value.length < 8 || value.length > 16) {
      callback(new Error('密码长度为8-16位！'))
    } else {
      callback()
    }
  }

  // TODO：密码还没有校验
  // 表单规则
  const updateInfoRules = reactive<FormRules<typeof updateInfoForm>>({
    new_pwd: [{ validator: validatePass, trigger: 'blur' }],
    new_check: [{ validator: validateCheck, trigger: 'blur' }],
  })

  // 登录标识符
  const isLogin = ref(false)

  // 登录
  function login() {
    router.push('/login')
  }

  // 退出登录
  function logout() {
    setLoginState()
    router.push('/login');
  }

  // 获取登录状态
  function setLoginState() {
    if (session.getItem('isLogin') == 'true') {
      isLogin.value = true
    } else {
      isLogin.value = false
    }
  }

  // 获取管理员ID
  function getAdminId() {
    adminId.value = session.getItem('adminId')
  }

  // 修改管理员信息
  async function updateInfo() {
    await api.updateAdminApi({ adminId: adminId.value, name: admin_name.value, password: new_pwd.value })
    .then((res:any) => {
      if (res.code == 200) {
        dialogFormVisible.value = false;
        ElMessage.success('修改成功!')
      }
    })
  }

  // 页面初始化
  onMounted(() => {
    setLoginState();
    getAdminId();
  })
</script>

<template>
  <div class="header-logo-bar">
    <img style="display: inline; width: 60px" src="@/assets/images/logo.png" alt="logo" />
    <h1 style="display: inline" class="title">共享学园</h1>
    <h1 style="display: inline" class="title">后台管理系统</h1>
  </div>
  <div class="header-operation-bar">
    <el-avatar style="margin-right: 1vw; border: 1px solid gray" @click="dialogFormVisible = true"
      src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" v-if="isLogin" />

    <!-- 修改信息 -->
    <el-dialog v-model="dialogFormVisible" title="个人信息" width="500" center>
      <div class="center">
        <el-avatar class="avatar"
        @click="dialogFormVisible = true"
          src="https://cube.elemecdn.com/0/88/03b0d39583f48206768a7534e55bcpng.png" :size="100" v-if="isLogin" />
      </div>
      <div class="center">
        <h1>管理员ID：{{ adminId }}</h1>
      </div>
      <el-form ref="updateInfoFormRef" :model="updateInfoForm" :rules="updateInfoRules"
        style="margin: 3vh auto; display: grid; place-items: center">
        <el-form-item label="用户名">
          <el-input v-model="admin_name" style="width: 240px" placeholder="请输入用户名" />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="new_pwd" style="width: 240px" type="password" placeholder="请输入新密码" show-password />
        </el-form-item>
        <el-form-item label="确认密码">
          <el-input v-model="new_check" style="width: 240px" type="password" placeholder="请确认新密码" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <div class="dialog-footer">
          <el-button type="success" @click="updateInfo()"> 修改信息 </el-button>
        </div>
      </template>
    </el-dialog>

    <el-button type="danger" round v-if="isLogin" @click="logout()">退出登录</el-button>
    <el-button type="primary" round v-if="!isLogin" @click="login()">登录</el-button>
  </div>
</template>

<style scoped>
  .center {
    display: flex;
    justify-content: center;
    align-items: center;
  }

  a,
  .green {
    color: white;
    background-color: transparent;
  }

  .header-logo-bar {
    display: flex;
    justify-content: center;
    align-items: center;
    gap: 1vw;
  }

  .title {
    color: black;
  }

  .header-operation-bar {
    display: flex;
    justify-content: center;
    align-items: center;
  }


</style>
