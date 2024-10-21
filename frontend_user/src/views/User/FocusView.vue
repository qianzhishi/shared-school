<script lang="ts" setup>
    import  bgUrl  from "@/assets/images/user_bg2.png"
    import { computed, onMounted,ref } from 'vue'
    import { ElMessage, ElMessageBox } from 'element-plus'
    import api from '@/api/user'
    import { useRoute, useRouter } from "vue-router";
    import { useUserStore } from '@/stores/user';
    
    const userStore = useUserStore();

    const route = useRoute()
    const router = useRouter()

    // 当前访问者的用户id
    const visitorId = Number(localStorage.getItem('userId'))
    // 当前被访问用户的id
    const visitedId = ref(Number(route.query.id) || visitorId)
    // 记录用户是否访问自己的个人中心
    const isOwn = computed(()=>{
        return (visitedId.value == visitorId ? true : false)
    })

    // 记录被访问用户的关注列表查看权限
    const focusFlag = ref(true)
    if(!isOwn.value)
    focusFlag.value = userStore.showLikes ? true : false

    // 记录当前关注列表是否有数据
    let dataFlag = ref(true)

    type focusData = { 
        userId: string, 
        avatar:string, 
        username: string, 
        intro:string, 
        isFollowed: boolean
    }

    const focusList = ref<focusData[]>([])

    // 获取用户关注列表
    const getFocus = async()=> {
        await api.subsApi({userId: visitedId.value})
            .then((res: any) => {
                if (res.code == 200)
                {
                    if (res.data && res.data.length > 0)
                    {
                        dataFlag.value = true
                        const neededData = res.data.map((item:any) => ({
                            userId: item.userId,
                            username: item.username, 
                            avatar: item.avatar,
                            intro: item.intro,
                            isFollowed: false
                        }))
                        focusList.value = neededData
                        getIsFollwed()
                    }
                    else
                        dataFlag.value = false
                }
        })
    }

    // 判断当前登录用户是否关注此人的关注列表里的用户
    const  getIsFollwed = async () => {
        if(localStorage.getItem('isLogin') == 'false')
            return  
        api.subsApi({userId: visitorId})
            .then((res: any) => {
                if (res.code == 200)
                {
                    if(res.data && res.data.length > 0)
                    {
                        const neededData = res.data.map((item:any) => ({
                            id: item.userId,
                        }))
                        focusList.value.forEach(focusUser => {
                            if (neededData.some((data: { id: any; }) => data.id == focusUser.userId)) {
                                focusUser.isFollowed = true;
                            }
                        })
                        
                    }
                }
        })
    }
    
    // 关注用户
    const follow = (item:focusData)=> {
        if(localStorage.getItem('isLogin') == 'false')
        {
            ElMessage.info('请先登录后再进行此操作！')
            return            
        }
        if(visitorId == Number(item.userId))
        {
            ElMessage.info('不能关注自己哦！')
            return             
        }
        api.subscribeApi({
            focusedId: Number(item.userId),
            focusId: visitorId,
            state: 0
        })
        .then((res: any) => {
            if(res.code == 200) {
                ElMessage.success('关注成功')
                item.isFollowed = true
            }
        })
    }

    // 取消关注方法
    const unFollow = (item:focusData) => {
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
                focusedId: Number(item.userId),
                focusId: visitorId,
                state: 1
            })
            .then((res: any) => {
                if(res.code == 200) {
                    ElMessage.success('取消成功')
                    item.isFollowed = false
                }
            })
        })
    }

    onMounted(async()=>{
        if(focusFlag.value || isOwn.value) {
            await getFocus()
        }
    })
</script>

<template>

    <div class="viewSubs" v-if="focusFlag">

        <div class="title" v-if="isOwn">我的关注</div>
        <div class="title" v-else>关注</div>
        <div style="width: 100%; border:1px solid #999999"></div>

        <div style="width: 100%;" v-if="!dataFlag">
            <el-empty :image-size=250 :image="bgUrl" description="暂无关注"/>
        </div>
        <el-scrollbar class="scrollbar" v-else>
            <div v-for="item in focusList" :key="item.userId" class="item">
                <el-avatar
                    :src="item.avatar"
                    :size="60"
                    class="avatar"
                    @click="router.push({ path: '/user', query: { id: item.userId } })"
                />
                <div class="box">
                    <el-text 
                        class="name" 
                        @click="router.push({ path: '/user', query: { id: item.userId } })">
                        {{item.username}}
                    </el-text>

                    <el-text class="bio" 
                        size="large" line-clamp="1" 
                        style="width: 100%; text-align: left;"
                    >
                        {{item.intro}}
                    </el-text>               
                </div>

                <el-button 
                    color="#626aef" size="large" plain round
                    style="margin-right: 20px;"
                    v-show="item.isFollowed"
                    @click="unFollow(item)"
                >
                    已关注
                </el-button>
                <el-button 
                    color="#626aef" size="large" round
                    style="margin-right: 20px;"
                    v-show="!item.isFollowed" 
                    @click="follow(item)"
                >
                    关注
                </el-button>

            </div>
            <el-text  type="info" style="margin: 30px; font-size: 20px;">没有更多数据了</el-text>
        </el-scrollbar>

    </div>

    <div style="width: 100%;" v-else>
        <div class="title">关注</div>
        <div style="width: 100%; border:1px solid #999999"></div>
        <el-empty :image-size=250 :image="bgUrl" description="用户已设为隐私"/>
    </div>


</template>
  
<style scoped>

    .viewSubs {
        display: flex;
        flex-direction: column;
        align-items: center;
        width: 100%;
        max-height: 700px;
    }

    .title {
        width: 100%;
        margin-top: 10px;
        margin-bottom: 10px;
        text-align: center;
        font-size: 25px;
    }

    .scrollbar {
        display: flex;
        flex-direction: column;
        text-align: center;
        width: 100%;
    }


    .item {
        display: flex;
        align-items: center;
        width: 100%;
        min-height: 100px;
        margin: 20px auto;
        border-radius: 10px;
        background: linear-gradient(to right,rgba(131, 96, 195,0.1),rgba(192, 108, 132,0.1))
    }

    .box {
        display: flex;
        flex-direction: column;
        flex-grow: 1;
        align-items: center;
        margin-left: 20px;
    }

    .name {
        width: 100%;
        text-align: left;
        font-size: 22px;
        color: black;
    }

    .name:hover {
        color: #4f78ff;
    }

    .avatar {
        margin-left: 20px;
    }

</style>