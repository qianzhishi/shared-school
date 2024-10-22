<script lang="ts" setup>
  import { reactive, ref, computed, onMounted } from 'vue'
  import type { FormInstance, FormRules } from 'element-plus'
  import { ElMessage } from 'element-plus'
  import api from '@/api/system'
  import { useRouter } from 'vue-router';

  // 本地数据存储
  const session = window.sessionStorage;


  const router = useRouter();

  // 判断输入是否合法
  const idValid = computed(() => {
    if (loginRuleForm.userid === '' || !stringToNumber(loginRuleForm.userid)) {
      return false;
    }
    return true;
  });
  const loginPasswordValid = computed(() => {
    if (loginRuleForm.password === '' || (loginRuleForm.password.length < 8 || loginRuleForm.password.length > 16)) {
      return false;
    }
    return true;
  });
  const loginAble = computed(() => {
    return idValid.value && loginPasswordValid.value;
  })

  // 表单引用
  const loginRuleFormRef = ref<FormInstance>()

  // 账号验证器
  const validateId = (rule: any, value: any, callback: any) => {
    if (value === '') {
      callback(new Error('账号不能为空！'))
    }
    else if (!stringToNumber(value)) {
      callback(new Error('账号格式不正确！'))
    }
    else {
      callback()
    }
  }

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

  // 登录表单
  const loginRuleForm = reactive({
    userid: '',
    password: ''
  })

  const loginRules = reactive<FormRules<typeof loginRuleForm>>({
    userid: [{ validator: validateId, trigger: 'blur' }],
    password: [{ validator: validatePass, trigger: 'blur' }]
  })

  // 验证账号是否为数字
  function stringToNumber(str: string) {
    return /^\d+$/.test(str)
  };

  // 提交登录
  async function login() {
      await api.loginApi({adminId:loginRuleForm.userid, password:loginRuleForm.password})
        .then((res: any) => {
        if (res.code == 200) {
          session.setItem('isLogin', 'true')
          session.setItem('adminId', loginRuleForm.userid)
          ElMessage.success(res.data+'，欢迎您!')
          router.push('/home')
        }
      })
  }

  onMounted(() => {
    session.clear;
    session.setItem('activeIndex', '0');
    session.setItem('isLogin', 'false')
  })
</script>

<template>
  <div class="wrapper">
    <div class="login-panel">
      <!-- 登录版块 -->
      <div class="content">
        <div>
          <h1 class="title">登录</h1>
        </div>
        <el-form ref="loginRuleFormRef" :model="loginRuleForm" :rules="loginRules" label-width="auto"
          class="demo-ruleForm login-form">
          <el-form-item label="账号" prop="userid">
            <el-input v-model="loginRuleForm.userid" placeholder="请输入账号" clearable />
          </el-form-item>
          <el-form-item label="密码" prop="password">
            <el-input v-model="loginRuleForm.password" type="password" placeholder="请输入密码" show-password />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="login()" round :disabled="!loginAble">登录</el-button>
          </el-form-item>
        </el-form>
      </div>
    </div>
  </div>
</template>

<style scoped>
  .tip-area {
    display: grid;
    place-items: center;
  }

  .user-id-tip {
    font-size: 1.5vw;
  }

  .user-id {
    font-weight: bold;
    font-size: 2.2vw;
    color: #409EFF;
  }

  .wrapper {
    display: flex;
    justify-content: center;
    align-items: center;
    margin: 0;
    width: 100vw;
    height: 100vh;
    background-image: url('@/assets/images/login_background.jpg');
    background-repeat: round;
  }

  .login-panel {
    display: grid;
    place-items: center;
    height: 50vh;
    width: 40vw;
    border-radius: 5vb;
    border-width: 1px;
    border-style: solid;
    border-color: #999;
    padding: 20px 50px;
    box-shadow: 5px 5px 10px #999;
    background-color: rgba(255, 255, 255, 0.8);
    overflow: hidden;
    position: relative;
  }

  .login-form {
    display: grid;
    place-items: center;
  }

  .title {
    font-size: 5vb;
  }

  .login-input {
    display: grid;
    place-items: center;
  }

  h1 {
    color: black;
    text-align: center;
  }

  .el-input {
    width: 20vw;
  }

  .el-button {
    width: 15vw;
    font-size: 3vb;
  }

  .content {
    position: absolute;
    left: 0;
    right: 0;
    transition: transform 0.5s;
  }
</style>
