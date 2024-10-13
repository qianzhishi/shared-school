<script lang="ts" setup>
    import { reactive, ref, computed, onMounted } from 'vue'
    import type { FormInstance, FormRules } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import { useRouter } from 'vue-router'
    import api from '@/api/system'

    // 路由
    const router = useRouter();

    // 本地数据存储
    const store = window.localStorage;

    // 当前状态
    const state = ref(true);
    const dialogVisible = ref(false);
    const receiveChecked = ref(false);

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
    const registerPasswordValid = computed(() => {
        if (registerRuleForm.password === '' || (registerRuleForm.password.length < 8 || registerRuleForm.password.length > 16)) {
            return false;
        }
        return true;
    });
    const nameValid = computed(() => {
        if (registerRuleForm.username === '') {
            return false;
        }
        return true;
    });
    const checkValid = computed(() => {
        if (registerRuleForm.check === '' || registerRuleForm.check != registerRuleForm.password) {
            return false;
        }
        return true;
    });
    const loginAble = computed(() => {
        return idValid.value && loginPasswordValid.value;
    })
    const registerAble = computed(() => {
        return nameValid.value && registerPasswordValid.value && checkValid.value;
    })

    // 用户注册的ID
    const userId = ref()

    // 表单引用
    const loginRuleFormRef = ref<FormInstance>()
    const registerRuleFormRef = ref<FormInstance>()

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

    // 用户名验证器
    const validateName = (rule: any, value: any, callback: any) => {
        if (value === '') {
            callback(new Error('用户名不能为空！'))
        } else {
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

    // 密码二次验证器
    const validateCheck = (rule: any, value: any, callback: any) => {
        if (value === '') {
            callback(new Error('密码不能为空！'))
        } else if (value.length < 8 || value.length > 16) {
            callback(new Error('密码长度为8-16位！'))
        } else if (value != registerRuleForm.password) {
            callback(new Error('两次输入的密码不一致！'))
        } else {
            callback()
        }
    }

    // 登录表单
    const loginRuleForm = reactive({
        userid: '',
        password: ''
    })

    // 注册表单
    const registerRuleForm = reactive({
        username: '',
        password: '',
        check: ''
    })

    const loginRules = reactive<FormRules<typeof loginRuleForm>>({
        userid: [{ validator: validateId, trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }]
    })

    const registerRules = reactive<FormRules<typeof registerRuleForm>>({
        username: [{ validator: validateName, trigger: 'blur' }],
        password: [{ validator: validatePass, trigger: 'blur' }],
        check: [{ validator: validateCheck, trigger: 'blur' }]
    })

    // 验证账号是否为数字
    function stringToNumber(str: string) {
        return /^\d+$/.test(str)
    };

    // 提交登录
    const login = async()=> {
        await api.loginApi({id: loginRuleForm.userid, pwd: loginRuleForm.password})
        .then((res: any) => {
            // 状态码
            let code = res.code;
            if (code == 1) {
                console.log(res)
                // 存储登录状态信息
                store.setItem('isLogin', 'true');
                store.setItem('userId', loginRuleForm.userid);
                store.setItem('userAvatar', res.avatar);
                // 消息提示
                ElMessage.success('登录成功！')
                // 转至主页
                router.push('/home');
            }
            else {
                // 存储登录状态信息
                store.setItem('isLogin', 'false');
                let msg = res.data.msg;
                // 消息提示
                ElMessage.error(msg)
            }
        })
        .catch(() => {
            // 存储登录状态信息
            store.setItem('isLogin', 'false');
        })
    }    

    // 提交注册
    const register = async()=> {
        await api.registerApi({name: registerRuleForm.username,pwd: registerRuleForm.password})
        .then((res: any) => {
            console.log(res)
            // 状态码
            let code = res.code;
            if (code == 1) {
                userId.value = res.id;
                // 消息提示
                ElMessage.success('注册成功！')
                // 显示ID
                dialogVisible.value = true;
            }
            else {
                let msg = res.data.msg;
                // 消息提示
                ElMessage.error(msg)
            }
        })
    }

    // 界面切换
    const changeState = () => {
        
        if (state.value) {
            loginRuleForm.userid = ''
            loginRuleForm.password = ''
        } else {
            registerRuleForm.username = ''
            registerRuleForm.password = ''
            registerRuleForm.check = ''
        }

        state.value = !state.value
    }

    onMounted(() => {
        // 清空本地存储中的信息
        store.clear();
    })
</script>

<template>
  <div class="wrapper">
      <div class="login-panel">
          <!-- 登录版块 -->
              <div class="content" v-show="state">
                  <div>
                      <h1 class="title">登录</h1>
                  </div>
                  <el-form ref="loginRuleFormRef" :model="loginRuleForm" :rules="loginRules" label-width="auto"
                      class="demo-ruleForm login-form">
                      <el-form-item label="账号" prop="userid">
                          <el-input v-model="loginRuleForm.userid" placeholder="请输入账号" clearable />
                      </el-form-item>
                      <el-form-item label="密码" prop="password">
                          <el-input v-model="loginRuleForm.password" type="password" placeholder="请输入密码"
                              show-password />
                      </el-form-item>
                      <el-form-item>
                          <el-button type="primary" @click="login()" round :disabled="!loginAble">登录</el-button>
                      </el-form-item>
                      <el-form-item>
                          <el-button type="primary" @click="changeState()" round>注册</el-button>
                      </el-form-item>
                  </el-form>
              </div>
          <!-- 注册版块 -->
              <div class="content" v-show="!state">
                  <div>
                      <h1 class="title">注册</h1>
                  </div>
                  <el-form ref="registerRuleFormRef" :model="registerRuleForm" :rules="registerRules"
                      label-width="auto" class="demo-ruleForm login-form">
                      <el-form-item label="用户名" prop="username">
                          <el-input v-model="registerRuleForm.username" placeholder="请输入用户名" clearable />
                      </el-form-item>
                      <el-form-item label="密码" prop="password">
                          <el-input v-model="registerRuleForm.password" type="password" placeholder="请输入密码"
                              show-password />
                      </el-form-item>
                      <el-form-item label="确认密码" prop="check">
                          <el-input v-model="registerRuleForm.check" type="password" placeholder="请再次输入密码"
                              show-password />
                      </el-form-item>
                      <el-form-item>
                          <el-button type="primary" @click="register()" round :disabled="!registerAble">注册</el-button>
                      </el-form-item>
                      <el-form-item>
                          <el-button type="primary" @click="changeState()" round>返回登录</el-button>
                      </el-form-item>
                  </el-form>
              </div>

          <!-- 显示用户注册的ID -->
          <el-dialog v-model="dialogVisible" title="欢迎使用" width="500" center align-center>
              <div class="tip-area" style="margin:8vh 0;">
                  <p class="user-id-tip">
                      您的账号是<label class="user-id">{{ userId }}</label>，请妥善保存！
                  </p>
                  <div>
                      <el-checkbox v-model="receiveChecked" label="我已保存" size="large" />
                  </div>
              </div>
              <template #footer>
                  <div class="dialog-footer">
                      <el-button type="primary" @click="dialogVisible = false" :disabled="!receiveChecked">
                          确定
                      </el-button>
                  </div>
              </template>
          </el-dialog>
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
        background-image: url('/src/assets/images/login_background.png');
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