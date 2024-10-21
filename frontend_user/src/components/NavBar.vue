<script lang="ts" setup>
    import { onMounted, ref } from 'vue'
    import { Search } from '@element-plus/icons-vue'
    import { useRouter } from 'vue-router';
    import { UserFilled } from '@element-plus/icons-vue'
    import { ElMessage } from 'element-plus'
    import { useUserStore } from '@/stores/user';
    
    const userStore = useUserStore();

    // 路由
    const router = useRouter();

    // 搜索框内容
    const input = ref('')

    // 登录标识符
    const isLogin = ref(false);

    const type = ref('0');

    const userAvatar = ref(localStorage.getItem('userAvatar'))

    // 路由跳转
    function redirect(url: string) {
        router.push(url);
    }

    // 转至个人主页
    function toUser() {
        if (localStorage.getItem('isLogin') === 'true') {
            router.push({ path: '/user', query: { id: localStorage.getItem('userId') } })
        }
        else {
            // 消息提示
            ElMessage({
                message: '您尚未登录！',
                type: 'error',
            })
        }
    }

    // 转至投稿
    function toUpload() {
        if (localStorage.getItem('isLogin') == 'true') {
            redirect('/upload')
        }
        else {
            ElMessage.error('您尚未登录！')
           
        }
    }

    // 登录
    function login() {
        router.push('/login');
    }

    // 退出登录
    function logout() {
            userStore.logout();
            localStorage.setItem('isLogin', 'false');
            localStorage.setItem('userId', '0');
            localStorage.setItem('userAvatar', 'https://small-event-bucket1.oss-cn-hangzhou.aliyuncs.com/avatarDefault.jpg');
            userAvatar.value = 'https://small-event-bucket1.oss-cn-hangzhou.aliyuncs.com/avatarDefault.jpg';
            setLoginState();
            router.push('/home');
            ElMessage.success('您已退出登录！')
        }

    // 获取登录状态
    function setLoginState() {
        if (localStorage.getItem('isLogin') == 'true') {
            isLogin.value = true;
        }
        else {
            isLogin.value = false;
        }
    }

    // 转至动态
    function goNews() {
            if (localStorage.getItem('isLogin') == 'true') {
                router.push('/friend-news')
            }
            else {
                ElMessage.error('您尚未登录！')
            }
        }


    // 转至搜索
    function search() {
        if (input.value !== '' && typeof type.value !== 'undefined') {
            router.push({ path: '/search', query: { input: input.value, type: type.value } });
        }
    }


    onMounted(() => {
        setLoginState();
    })
</script>

<template>
  <div class="header">
      <div class="grid">
          <div class="top-bar">
              <div class="center gap">
                  <!-- APP下载 -->
                  <el-tooltip content="下载APP" placement="bottom" effect="light">
                      <el-button class="nav-circle-button" @click="ElMessage.warning('APP暂未上线,让我们一起为开发者加油')">
                          <svg t="1715606553249" class="nav-icon" viewBox="0 0 1024 1024" version="1.1"
                              xmlns="http://www.w3.org/2000/svg" p-id="39210" width="200" height="200">
                              <path
                                  d="M861.866667 725.76a21.333333 21.333333 0 0 1 21.333333 21.333333v170.24a106.666667 106.666667 0 0 1-99.669333 106.453334L776.533333 1024h-554.666666a106.666667 106.666667 0 0 1-106.453334-99.669333L115.2 917.333333v-169.6a21.333333 21.333333 0 0 1 21.333333-21.333333h42.666667a21.333333 21.333333 0 0 1 21.333333 21.333333V917.333333a21.333333 21.333333 0 0 0 17.493334 20.992L221.866667 938.666667h554.666666a21.333333 21.333333 0 0 0 20.992-17.493334l0.341334-3.84v-170.24a21.333333 21.333333 0 0 1 21.333333-21.333333h42.666667z m-298.666667 42.24a21.333333 21.333333 0 0 1 21.333333 21.333333v42.666667a21.333333 21.333333 0 0 1-21.333333 21.333333h-128a21.333333 21.333333 0 0 1-21.333333-21.333333v-42.666667a21.333333 21.333333 0 0 1 21.333333-21.333333h128z m213.333333-768a106.666667 106.666667 0 0 1 106.453334 99.669333l0.213333 6.997334v171.776a21.333333 21.333333 0 0 1-21.333333 21.333333h-42.666667a21.333333 21.333333 0 0 1-21.333333-21.333333V106.666667a21.333333 21.333333 0 0 0-17.493334-20.992L776.533333 85.333333h-554.666666a21.333333 21.333333 0 0 0-20.992 17.493334L200.533333 106.666667v170.666666a21.333333 21.333333 0 0 1-21.333333 21.333334h-42.666667a21.333333 21.333333 0 0 1-21.333333-21.333334v-170.666666A106.666667 106.666667 0 0 1 214.869333 0.213333L221.866667 0h554.666666z"
                                  fill="#3D3D3D" p-id="39211"></path>
                              <path
                                  d="M187.392 360.661333a17.066667 17.066667 0 0 1 16.128 11.392l101.717333 289.194667a17.066667 17.066667 0 0 1-16.085333 22.741333h-33.92a17.066667 17.066667 0 0 1-16.042667-11.264l-20.437333-56.576H91.008l-20.437333 56.576a17.066667 17.066667 0 0 1-16.042667 11.264H24.32a17.066667 17.066667 0 0 1-16.042667-22.912l104.832-289.152a17.066667 17.066667 0 0 1 16.042667-11.264z m331.434667 0.042667c83.712 1.493333 124.288 34.730667 121.770666 99.712-0.853333 36.48-11.52 62.336-31.914666 77.653333-20.437333 15.317333-47.104 22.698667-80.085334 22.101334H477.866667v106.752a17.066667 17.066667 0 0 1-17.066667 17.066666H426.666667a17.066667 17.066667 0 0 1-17.066667-17.066666V377.728l0.341333-3.413333a17.066667 17.066667 0 0 1 16.725334-13.653334z m345.6 0c83.712 1.493333 124.288 34.730667 121.770666 99.712-0.853333 36.48-11.52 62.336-31.914666 77.653333-20.437333 15.317333-47.104 22.698667-80.085334 22.101334H823.466667v106.752a17.066667 17.066667 0 0 1-17.066667 17.066666H772.266667a17.066667 17.066667 0 0 1-17.066667-17.066666V377.728l0.341333-3.413333a17.066667 17.066667 0 0 1 16.725334-13.653334zM157.482667 423.68h-1.706667l-47.274667 138.24h92.757334L157.44 423.68z m341.333333-8.746667H477.866667v90.965334h20.992c25.642667 0 43.52-4.138667 53.589333-12.458667 10.069333-8.32 14.933333-20.48 14.634667-36.522667-0.554667-16.042667-6.997333-27.136-19.2-33.28-12.288-6.101333-28.586667-9.002667-49.066667-8.704z m345.6 0H823.466667v90.965334h20.992c25.642667 0 43.52-4.138667 53.589333-12.458667 10.069333-8.32 14.933333-20.48 14.634667-36.522667-0.554667-16.042667-6.997333-27.136-19.2-33.28-12.288-6.101333-28.586667-9.002667-49.066667-8.704z"
                                  fill="#3379FF" p-id="39212"></path>
                          </svg>
                      </el-button>
                  </el-tooltip>
                  <!-- 实验室功能 -->
                  <el-tooltip content="实验室功能" placement="bottom" effect="light">
                      <el-button class="nav-circle-button" @click="ElMessage.warning('实验室功能暂未上线,让我们一起为开发者加油')">
                          <svg t="1715606599408" class="nav-icon" viewBox="0 0 1024 1024" version="1.1"
                              xmlns="http://www.w3.org/2000/svg" p-id="40294" width="200" height="200">
                              <path
                                  d="M204.668416 622.2336c87.5008 124.8768 194.4064 127.9488 320.768 9.1648 97.536-91.648 142.336-84.48 280.576 41.8304 23.04 20.992 36.4544 117.6064 40.2432 289.8432H204.668416L149.679616 744.2944 204.668416 622.2336z"
                                  fill="#20C997" p-id="40295"></path>
                              <path
                                  d="M679.190016 515.7888a350.8224 350.8224 0 0 0-20.736-35.3792l-18.3296-27.392V126.8224A51.2 51.2 0 0 1 651.183616 25.6H767.868416a51.2 51.2 0 0 1 0 102.4h-25.344v293.888l1.024 1.536c20.48 30.464 38.2976 66.048 51.6096 102.8096l114.688 316.5184C945.942016 942.08 897.404416 1024 797.052416 1024H227.298816C126.946816 1024 78.409216 942.08 114.402816 842.752l114.688-316.5184a482.304 482.304 0 0 1 51.712-102.8608l0.9728-1.536V128H255.868416a51.2 51.2 0 1 1 0-102.4h120.5248a51.2 51.2 0 0 1 7.7824 101.8368v325.632l-18.3808 27.3408a380.16 380.16 0 0 0-40.448 80.7424l-12.1344 33.536c49.4592 48.9472 103.0656 43.4688 179.5072-20.1216 62.0544-51.6096 121.856-71.7312 186.4704-58.7776z m52.0704 134.656c-72.3968-50.0224-112.896-47.2064-173.056 2.8672-100.9664 83.968-199.3216 99.2768-281.9072 43.264L210.658816 877.568c-13.056 36.1472-8.3968 43.9808 16.64 43.9808h569.7024c25.088 0 29.696-7.8336 16.5888-43.9808l-82.3296-227.1744zM563.068416 307.2a51.2 51.2 0 1 1 0-102.4 51.2 51.2 0 0 1 0 102.4zM460.668416 460.8a51.2 51.2 0 1 1 0-102.4 51.2 51.2 0 0 1 0 102.4z"
                                  fill="#2C6DD2" p-id="40296"></path>
                          </svg>
                      </el-button>
                  </el-tooltip>
                  <!-- 动态 -->
                  <el-tooltip content="动态" placement="bottom" effect="light">
                      <el-button class="nav-circle-button" @click="goNews()">
                          <svg t="1715606324273" class="nav-icon" viewBox="0 0 1024 1024" version="1.1"
                              xmlns="http://www.w3.org/2000/svg" p-id="32147" width="200" height="200">
                              <path
                                  d="M307.23 450.74m120 0l351.79 0q120 0 120 120l0 146.46q0 120-120 120l-351.79 0q-120 0-120-120l0-146.46q0-120 120-120Z"
                                  fill="#E9F1FF" p-id="32148"></path>
                              <path
                                  d="M808 864H216c-83.81 0-152-68.19-152-152V312c0-83.81 68.19-152 152-152h592c83.81 0 152 68.19 152 152v400c0 83.81-68.19 152-152 152zM216 224a88.1 88.1 0 0 0-88 88v400a88.1 88.1 0 0 0 88 88h592a88.1 88.1 0 0 0 88-88V312a88.1 88.1 0 0 0-88-88z"
                                  fill="#2C5CCB" p-id="32149"></path>
                              <path
                                  d="M484.76 592h-200a92.1 92.1 0 0 1-92-92V396a92.1 92.1 0 0 1 92-92h200a92.1 92.1 0 0 1 92 92v104a92.1 92.1 0 0 1-92 92z m-200-224a28 28 0 0 0-28 28v104a28 28 0 0 0 28 28h200a28 28 0 0 0 28-28V396a28 28 0 0 0-28-28zM799.38 384h-128a32 32 0 0 1 0-64h128a32 32 0 1 1 0 64zM799.38 544.82h-0.25l-128-1a32 32 0 0 1 0.24-64h0.26l128 1a32 32 0 0 1-0.25 64zM799.72 735.88h-575a32 32 0 1 1 0-64h575a32 32 0 0 1 0 64z"
                                  fill="#2C5CCB" p-id="32150"></path>
                          </svg>
                      </el-button>
                  </el-tooltip>
              </div>
              <div class="center gap">
                  <div class="center" @click="toUser()">
                      <el-avatar class="avatar" :src="userAvatar" v-if="userAvatar != null && userAvatar != ''" />
                      <el-avatar class="avatar" :icon="UserFilled" v-else />
                  </div>
                  <el-button class="base-btn" type="danger" round @click="logout()" v-if="isLogin">退出登录</el-button>
                  <el-button class="base-btn" type="primary" round @click="login()" v-else>登录</el-button>
                  <el-button class="base-btn" type="primary" round @click="toUpload()">投稿</el-button>
              </div>
          </div>
          <div>
              <div class="center">
                  <img style="display: inline; width: 12vb" src="@/assets/images/logo.png" alt="logo" />
                  <h1 style="display: inline" class="title">共享学园</h1>
              </div>
              <div class="center gap">
                  <el-input style="width: 30vw" :prefix-icon="Search" v-model="input" v-on:keyup.enter="search"
                      placeholder="来问问我们吧，我们这什么都有"></el-input>
                  <el-button style="width: 6vw" type="primary" @click="search()" round>搜索</el-button>
                  <el-select v-model="type" placeholder="类型" style="width: 6vw;">
                      <el-option label="全局" value="0" />
                      <el-option label="学习" value="1" />
                      <el-option label="娱乐" value="2" />
                      <el-option label="表白" value="3" />
                  </el-select>
              </div>
          </div>
      </div>
  </div>

</template>

<style scoped>
    .center {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .gap {
        display: flex;
        gap: 1vw;
    }

    .grid {
        display: grid;
    }

    .header {
        display: flex;
        justify-content: center;
        height: 60vh;
        width: 100%;
        background-image: url('@/assets/images/background.png');
        background-repeat: round;
    }

    .top-bar {
        display: flex;
        justify-content: space-between;
        min-width: 80vw;
        height: 15vh;
    }

    .nav-icon {
        width: 2vw;
        height: 2vw;
    }

    .nav-circle-button {
        width: 4vw;
        height: 4vw;
        border-radius: 2vw
    }

    .title {
        color: black;
        font-size: 8vb;
        text-align: center;
        text-justify: center;
    }

    .avatar {
        width: 3rem;
        height: 3rem;
        transition: transform 0.5s ease, box-shadow 0.5s ease;
    }

    .avatar:hover {
        transform: scale(1.1);
        box-shadow: 5px 5px 5px lightgrey;
    }

    .base-btn {
        width: 6vw;
        height: 5vh;
        font-size: 1rem;
    }
</style>
