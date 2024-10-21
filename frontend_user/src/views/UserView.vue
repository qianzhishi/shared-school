<script lang="ts" setup>
    import Header from '@/components/BaseHeader.vue'
    import Footer from '@/components/BaseFooter.vue'
    import { computed, onMounted, reactive, ref } from 'vue'
    import { RouterView, onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router'
    import { ElMessage, ElMessageBox } from 'element-plus'
    import api from '@/api/user'
    import { useUserStore } from '@/stores/user';
    
    const userStore = useUserStore();
    // 路由
    const router = useRouter()
    const route = useRoute()

    let isLogin = ref(true)
    // 当前访问者的用户id
    let visitorId = Number(localStorage.getItem('userId'))
    // 当前被访问用户的id
    const visitedId = ref(Number(route.query.id) || visitorId)

    if (localStorage.getItem('isLogin') == 'false') {
        visitorId = -1
        isLogin.value = false
    }

    // 记录是否访问自己的个人中心
    const isOwn = computed(() => {
        return (visitedId.value == visitorId ? true : false)
    })


    // 路由跳转
    function redirect(url: string) {
        router.push({ path: url, query: { id: visitedId.value } });
    }

    // 退出登录
    function signOut() {
        localStorage.setItem('isLogin', 'false')
        userStore.logout()
        router.push('/login')
    }

    // 存放当前登录用户是否关注此人
    const isFollowing = ref(false)

    // 判断当前登录用户是否关注此人
    const getFocus = () => {
        if (isLogin) {
            api.subsApi({ userId: visitorId })
                .then((res:any) => {
                    if (res.code == 200)
                    {
                    
                        if(res.data !== null && res.data !== undefined)
                        {
                            const neededData = res.data.map((item: any) => ({
                                id: item.userId,
                            }))
                            isFollowing.value = neededData.some((item: { id: number }) => item.id == visitedId.value)
                        }
                        else
                            isFollowing.value = false
                    }
                })
        }
        else
            isFollowing.value = false
    }

    // 关注用户
    const follow = () => {
        if (!isLogin) {
            ElMessage.info('请先登录后再进行此操作！')
            return
        }
        api.subscribeApi({
            focusId: visitorId,
            focusedId: visitedId.value,
            state: 0
        })
        .then((res: any) => {
            if (res.code == 200) {
                ElMessage.success('关注成功')
                isFollowing.value = true
            }
        })
    }

    // 取消关注方法
    const unFollow = () => {
        ElMessageBox.confirm(
            '确认要取消关注吗?',
            '提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
            .then(() => {
                api.subscribeApi({
                    focusId: visitorId,
                    focusedId: visitedId.value,
                    state: 1
                })
                .then((res: any) => {
                    if (res.code == 200) {
                        ElMessage.success('取消成功')
                        isFollowing.value = false
                    }
                })
            })
    }

    onBeforeRouteUpdate((from) => {
        if (from.query.id == null) {
            visitedId.value = visitorId
            userStore.fetchUserInfo(visitorId)
        }
        else {
            visitedId.value = Number(from.query.id)
            userStore.fetchUserInfo(Number(from.query.id))
        }
    })

    onMounted(async () => {
        await userStore.fetchUserInfo(Number(route.query.id))
        if (!isOwn.value)
            getFocus()
    })

</script>

<template>
    <div class="container">
        <Header></Header>

        <div class="user-card">
            <div class="box1">
                <el-avatar :src="userStore.avatar" :size="120" class="avatar" />

                <div class="user-info">
                    <el-text class="name" tag="p">{{ userStore.name }}</el-text>
                    <el-text class="id" type="info" tag="p">ID: {{ userStore.userId }}</el-text>
                    <img class="gender-img" src="@/assets/images/male.png" alt="male" v-show="userStore.gender == 1">
                    <img class="gender-img" src="@/assets/images/female.png" alt="Female" v-show="userStore.gender == 0">
                    <el-text class="age" size="large">{{ userStore.age }}</el-text>
                    <el-text class="bio" size="large" tag="p">{{ userStore.intro }}</el-text>
                </div>

                <div v-if="!isOwn">
                    <div class="edit" v-if="isFollowing">
                        <el-button color="#626aef" size="large" plain round style="margin-right: 20px;"
                            v-show="isFollowing" @click="unFollow()">
                            已关注
                        </el-button>
                    </div>

                    <div class="edit" v-else>
                        <el-button color="#626aef" size="large" round style="margin-right: 20px;" v-show="!isFollowing"
                            @click="follow()">
                            关注
                        </el-button>
                    </div>

                </div>

            </div>

            <div class="box2"></div>

            <div class="box3">
                <el-text class="text"> <span>{{ userStore.likeNum }}</span>获赞</el-text>
                <el-text class="text"> <span>{{ userStore.fansNum }}</span>粉丝</el-text>
                <el-text class="text"> <span>{{ userStore.focusNum }}</span>关注</el-text>
            </div>
        </div>

        <div class="user-content">

            <div class="selector" v-if="isOwn">

                <div class="box">
                    <img class="icon" src="@/assets/icons/more.svg" alt="more">
                    <span>个人中心</span>
                </div>
                <div class="box" @click="redirect('/user/edit')">
                    <img class="icon" src="@/assets/icons/edit.svg" alt="edit">
                    <span>编辑资料</span>
                </div>
                <div class="box" @click="redirect('/user/postings')">
                    <img class="icon" src="@/assets/icons/post.svg" alt="posttings">
                    <span>我的发帖</span>
                </div>
                <div class="box" @click="redirect('/user/comments')">
                    <img class="icon" src="@/assets/icons/comment.svg" alt="comments">
                    <span>我的评论</span>
                </div>
                <div class="box" @click="redirect('/user/focus')">
                    <img class="icon" src="@/assets/icons/focus.svg" alt="focus">
                    <span>我的关注</span>
                </div>
                <div class="box" @click="redirect('/user/fans')">
                    <img class="icon" src="@/assets/icons/fan.svg" alt="fans">
                    <span>我的粉丝</span>
                </div>
                <div class="box" @click="redirect('/user/settings')">
                    <img class="icon" src="@/assets/icons/settings.svg" alt="settings">
                    <span>安全设置</span>
                </div>
                <div class="box" @click="signOut()">
                    <img class="icon" src="@/assets/icons/exit.svg" alt="exit">
                    <span>退出登录</span>
                </div>

            </div>

            <div class="selector" v-else>
                <div class="box">
                    <img class="icon" src="@/assets/icons/more.svg" alt="more">
                    <span>个人中心</span>
                </div>
                <div class="box" @click="redirect('/user/edit')">
                    <img class="icon" src="@/assets/icons/edit.svg" alt="edit">
                    <span>资料</span>
                </div>
                <div class="box" @click="redirect('/user/postings')">
                    <img class="icon" src="@/assets/icons/post.svg" alt="posttings">
                    <span>发帖</span>
                </div>
                <div class="box" @click="redirect('/user/comments')">
                    <img class="icon" src="@/assets/icons/comment.svg" alt="comments">
                    <span>评论</span>
                </div>
                <div class="box" @click="redirect('/user/focus')">
                    <img class="icon" src="@/assets/icons/focus.svg" alt="focus">
                    <span>关注</span>
                </div>
                <div class="box" @click="redirect('/user/fans')">
                    <img class="icon" src="@/assets/icons/fan.svg" alt="fans">
                    <span>粉丝</span>
                </div>

            </div>


            <div class="select-content">
                <RouterView />
            </div>


        </div>

        <div class="footer">
            <Footer></Footer>
        </div>

    </div>
</template>

<style scoped>

    .container {
        position: relative;
        display: flex;
        flex-direction: column;

        align-items: center;
        background: linear-gradient(to bottom, #f1f4f9, #dff1ff);

        width: 100vw;
        max-height: 200vh;
        overflow-y: auto;
        overflow-x: hidden;
    }

    .user-card {
        position: relative;
        width: 60%;
        height: 220px;
        margin-top: 50px;
        border: 1px solid;
        border-radius: 10px;
        background: url('@/assets/images/user_bg1.jpg') center / cover no-repeat;
    }

    .box1 {
        display: flex;
    }

    .avatar {

        width: 120px;
        height: 120px;
        margin-left: 20px;
        margin-top: 20px;
        border-radius: 50%;
    }

    .user-info {
        margin-left: 20px;
        margin-top: 20px;
    }

    .name {

        color: #000000;
        font-size: 24px;
        font-weight: bold;
    }

    .gender-img {

        width: 20px;
    }

    .age {
        margin-left: 5px;
        font-size: 20px;
    }


    .box2 {

        width: 80%;
        margin-top: 20px;
        margin-left: auto;
        margin-right: auto;
        border: 1px solid #999999;
    }

    .box3 {
        display: flex;
        justify-content: space-between;

        width: 30%;
        margin-top: 10px;
        margin-left: auto;
        margin-right: auto;
    }


    .text {
        font-size: 25px;
    }

    .text span {
        margin-right: 5px;

        color: #4e77ff;
    }

    .edit {
        position: absolute;
        top: 20px;
        right: 20px;
    }

    .btn {
        width: 125px;
        height: 40px;
        font-size: 20px;
        background:
            linear-gradient(90deg, #03a9f4, #ffdde1, #ffeb3b, #03a9f4);
        background-size: 400%;
    }

    .btn:hover {
        animation: animate 8s linear infinite;
    }

    @keyframes animate {
        from {
            background-position: 0%;
        }

        to {
            background-position: 400%;
        }
    }

    .user-content {
        display: flex;
        align-items: flex-start;
        justify-content: space-between;
        width: 60%;
        margin-top: 50px;
    }

    .selector {
        display: flex;
        flex-direction: column;
        align-items: center;
        justify-content: center;

        width: 100px;
        max-height: 700px;

        border-radius: 10px;
        transition: .3s;
        background: linear-gradient(to right, rgba(172, 182, 229, 0.1), rgba(134, 253, 232, 0.1));
        overflow: hidden;
    }

    .box {
        display: flex;
        align-items: center;
        justify-content: flex-start;

        height: 70px;
        width: 85%;
        margin: 9px;

        border-radius: 5px;
        position: relative;
        transition: .3s;
    }

    .icon {
        width: 30px;
        position: absolute;
        margin-left: 25px;
    }

    .box:nth-child(1)::before,
    .box:nth-child(3)::before,
    .box:nth-child(5)::before,
    .box:nth-child(7)::before {
        content: '';
        display: block;
        width: 100%;
        height: 2px;
        background-color: rgb(55, 65, 81);
        position: absolute;
        bottom: -10px;
    }

    .box span {
        position: relative;
        margin: auto;
        font: 500 20px '';
        opacity: 0;
        transition: .1s;
    }

    .box:first-child span {
        font-weight: bold;
    }

    .selector:hover {
        width: 250px;
    }

    .selector:hover span {
        opacity: 1;
    }

    .box:hover {
        background-color: rgb(209, 213, 219);
        color: rgb(241, 159, 159);
    }

    .select-content {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 70%;
        max-height: 800px;
        border-radius: 10px;
        background: linear-gradient(to right, rgba(101, 153, 153, 0.1), rgba(244, 121, 31, 0.1))
    }

    .footer {
        width: 100vw;
        margin-top: 50px;
    }

</style>