<script lang="ts" setup>
    import Header from '@/components/BaseHeader.vue'
    import Footer from '@/components/BaseFooter.vue'
    import { computed, onMounted, reactive, ref } from 'vue'
    import { RouterView, onBeforeRouteUpdate, useRoute, useRouter } from 'vue-router'
    import { ElMessage, ElMessageBox } from 'element-plus'
    import api from '@/api/user'

    // 路由
    const router = useRouter()
    const route = useRoute()


    // 本地数据存储
    const store = window.localStorage

    // 判断是否登录
    let isLogin = true
    // 当前访问者的用户id
    let visitorId = Number(store.getItem('userId'))
    // 当前被访问用户的id
    const visitedId = ref(Number(route.query.id) || visitorId)

    if (store.getItem('isLogin') == 'false') {
        visitorId = -1
        isLogin = false
    }

    // 记录是否访问自己的个人中心
    const isOwn = computed(() => {
        return (visitedId.value == visitorId ? true : false)
    })

    // 保存被访问用户的数据
    const userData = reactive({
        id: visitedId.value,
        name: '',
        avatarUrl: '',
        gender: 0,
        age: 0,
        bio: '',
        contact: '',
        subs: 0,
        fans: 0,
    })

    // 获取用户个人信息
    const getUserInfo = async () => {
        await api.infoApi({ id: visitedId.value })
            .then((res: any) => {
                userData.name = res.name
                userData.avatarUrl = res.avatar
                userData.fans = res.fans
                userData.subs = res.subs 
                userData.bio = res.intro
                userData.age = res.age
                userData.gender = res.sex
                userData.contact = res.contact
            })
            .catch((error: any) => {
                console.log(error)
            })
    }

    // 路由跳转
    function redirect(url: string) {
        router.push({ path: url, query: { id: visitedId.value } });
    }

    // 退出登录
    function signOut() {
        store.setItem('isLogin', 'false');
        router.push('/login')
    }

    // 存放当前登录用户是否关注此人
    const isFollowing = ref(false)

    // 判断当前登录用户是否关注此人
    const getFocus = () => {
        if (isLogin) {
            api.subsApi({ id: visitorId })
                .then((res:any) => {

                    if(res.subs != null)
                    {
                        const neededData = res.subs.map((item: any) => ({
                            id: item.id,
                        }))
                        isFollowing.value = neededData.some((item: { id: number }) => item.id == visitedId.value)
                    }
                    else
                        isFollowing.value = false
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
            id: visitedId.value,
            user: visitorId,
            state: 0
        })
        .then((res: any) => {
            if (res.code) {
                ElMessage.success('关注成功')
                isFollowing.value = true
            }
            else
            {
                let msg = res.data.msg;
                ElMessage.error(msg)
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
                    id: visitedId.value,
                    user: visitorId,
                    state: 1
                })
                .then((res: any) => {
                    if (res.code) {
                        ElMessage.success('取消成功')
                        isFollowing.value = false
                    }
                    else
                    {
                        let msg = res.data.msg;
                        ElMessage.error(msg)
                    }
                })
            })
    }

    onBeforeRouteUpdate((from) => {
        if (from.query.id == null) {
            visitedId.value = visitorId
            getUserInfo()

        }
        else {
            visitedId.value = Number(from.query.id)
            getUserInfo()
        }
    })

    onMounted(async () => {
        await getUserInfo()
        if (!isOwn.value)
            getFocus()
    })

</script>

<template>
    <div class="container">
        <Header></Header>

        <div class="user-card">
            <div class="box1">
                <el-avatar :src="userData.avatarUrl" :size="120" class="avatar" />

                <div class="user-info">
                    <el-text class="name" tag="p">{{ userData.name }}</el-text>
                    <el-text class="id" type="info" tag="p">ID: {{ userData.id }}</el-text>
                    <img class="gender-img" src="@/assets/images/male.png" alt="male" v-show="userData.gender == 1">
                    <img class="gender-img" src="@/assets/images/female.png" alt="Female" v-show="userData.gender == 0">
                    <el-text class="age" size="large">{{ userData.age }}</el-text>
                    <el-text class="bio" size="large" tag="p">{{ userData.bio }}</el-text>
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
                <el-text class="text"> <span>{{ userData.fans }}</span>粉丝</el-text>
                <el-text class="text"> <span>{{ userData.subs }}</span>关注</el-text>
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