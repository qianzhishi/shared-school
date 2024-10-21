<script lang="ts" setup>

    import  bgUrl  from "@/assets/images/user_bg2.png"
    import { computed, onMounted,ref } from 'vue'
    import { More } from '@element-plus/icons-vue'
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useRouter } from "vue-router";
    import { useRoute } from 'vue-router'
    import api from '@/api/user'
    import { useUserStore } from '@/stores/user';
    
    const userStore = useUserStore();


    // 路由
    const router = useRouter()
    const route = useRoute()


    // 当前访问者的用户id
    const visitorId = Number(localStorage.getItem('userId'))
    // 当前被访问用户的id
    const visitedId = ref(Number(route.query.id) || visitorId)
    // 记录用户是否访问自己的个人中心
    const isOwn = computed(()=>{
        return (visitedId.value == visitorId ? true : false)
    })

    // 记录被访问用户的评论查看权限
    const postFlag = ref(true)
    if(!isOwn.value)
        postFlag.value = userStore.showPosts ? true : false

    // 记录当前帖子列表是否有数据
    let dataFlag = ref(false)

    type postData = {
        cardId: number,
        title: string,
        date: string,
        content: string
    }

    const postingsList = ref<postData[]>([]);

    // 获取用户帖子列表
    const getPostings = async()=> {
        await api.contentApi({userId: visitedId.value})
            .then((res: any) => {
            if (res.code == 200) {
                if (res.data && res.data.length > 0)
                {
                    dataFlag.value = true
                    // const neededData = res.list.map((item:any) => ({
                    //     cardId: item.card,
                    //     title: item.title, 
                    //     time: item.time.substring(0, 10),
                    //     content: item.content
                    // }));
                    // postingsList.value = neededData
                    postingsList.value = res.data
                    
                }
            }
            else
                dataFlag.value = false
        })
    }

 
    const showTip = (cardId:number) => {
        ElMessageBox.confirm(
            '确定要删除吗?',
            '提示',
            {
                confirmButtonText: '确认',
                cancelButtonText: '取消',
                type: 'warning',
            }
        )
        .then(() => {
            deletePosting(cardId)
        })
    }

    // 删除帖子
    const deletePosting = (cardId:number)=> {
        api.delPostApi({cardId: cardId})
        .then((res: any) => {
            if(res.code == 200)
            {
                postingsList.value = postingsList.value.filter(post => post.cardId != cardId);
                ElMessage.success('删除成功')
            }
        })
    }

    // 前往帖子详情页面
    function toDetail(cardId:Number) {
        router.push(`/detail?id=${cardId}`)
    }
    

    onMounted(async()=>{
        if(postFlag.value || isOwn.value)
            await getPostings()
    })

</script>

<template>

    <div class="post-box" v-if="postFlag">

        <div class="title" v-if="isOwn">我的帖子</div>
        <div class="title" v-else>发帖</div>
        <div style="width: 100%; border:1px solid #999999"></div>

        <div style="width: 100%;" v-if="!dataFlag">
            <el-empty :image-size=250 :image="bgUrl" description="暂无帖子"/>
        </div>
        <el-scrollbar class="scrollbar" v-else>
            <div v-for="item in postingsList" :key="item.cardId" class="item" @click="toDetail(item.cardId)">
                <div style="display: flex; width: 100%; justify-content: space-between;">
                    <div style="display: flex;">
                        <img class="icon" src="@/assets/icons/time.svg" alt="time" width="30">
                        <el-text  type="info" style="margin-left: 20px; font-size: 20px;">{{ new Date(item.date as string).toLocaleString() }}</el-text>
                    </div>
                    <el-dropdown style="margin-right: 20px;" v-if="isOwn">
                        <el-button  plain>
                            <el-icon><More /></el-icon>
                        </el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item @click="showTip(item.cardId)">删除</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>
                
                <div>
                    <el-text  class="post-title" truncated >{{ item.title }}</el-text>
                </div>

                <div  class="content" truncated v-html="item.content"></div>

            </div>
            <el-text  type="info" style=" font-size: 20px;">没有更多数据了</el-text>
        </el-scrollbar>

    </div>

    <div style="width: 100%;" v-else>
        <div class="title">发帖</div>
        <div style="width: 100%; border:1px solid #999999"></div>
        <el-empty :image-size=250 :image="bgUrl" description="用户已设为隐私"/>
    </div>


</template>
  
<style scoped>

    .post-box {
        display: flex;
        flex-direction: column;
        align-items: center;

        width: 100%;
        max-height: 700px;

        border-radius: 10px;
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
        height: 100%;
        overflow: hidden;
    }


    .item {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        justify-content: space-evenly;
        height: 150px;
        margin: 20px auto;
        background: linear-gradient(to right,rgba(131, 96, 195,0.1),rgba(192, 108, 132,0.1));
        border-radius: 10px;
    }

    .item:hover {
        background: linear-gradient(to left, rgba(131, 96, 195, 0.2), rgba(192, 108, 132, 0.2));
    }

    .icon {
        margin-left: 20px;
    }

    .post-title {
        margin-left: 20px;
        font-size: 26px;
        color: black;
    }

    .content {
        width: 600px;
        margin-left: 20px;
        font-size: 20px;
        color: black;
        white-space: nowrap;
        overflow: hidden; 
        text-overflow: ellipsis;
    }

</style>