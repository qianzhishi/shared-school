
<script lang="ts" setup>
    import  bgUrl  from "@/assets/images/user_bg2.png"
    import { computed, onMounted,ref } from 'vue'
    import { More } from '@element-plus/icons-vue'
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useRoute, useRouter } from "vue-router";
    import api from '@/api/user'

    const router = useRouter()
    const route = useRoute()

    // 本地数据存储
    const store = window.localStorage

    const dialogVisible = ref(false)

    // 当前访问者的用户id
    const visitorId = Number(store.getItem('userId'))
    // 当前被访问用户的id
    const visitedId = ref(Number(route.query.id) || visitorId)
    // 记录用户是否访问自己的个人中心
    const isOwn = computed(()=>{
        return (visitedId.value == visitorId ? true : false)
    }) 
    // 记录被访问用户的评论查看权限
    const commentFlag = ref(true)

    // 记录当前帖子列表是否有数据
    let dataFlag = ref(false)

    type commentData = {
        comment_id: number,
        card_id: number,
        author_id: number,
        avatar: string,
        name: string,
        content: string,
        num_like: number,
        time: string,
        flag: boolean,
    }

    const commentsList = ref<commentData[]>([])
    //要查看的评论在对象数组里的索引值
    let index = ref(0)

    // 获取用户评论列表
    const getComments = async()=> {
        api.commentApi({id: visitedId.value, user_id: visitorId})
        .then((res: any) => {
            if(res.list.length > 0)
            {
                dataFlag.value = true
                commentsList.value = res.list               
            }
            else
                dataFlag.value = false
        })
        .catch((error: any) => {
            console.log(error)
        })
    }
    
    // 获取用户隐私设置
    const getSettings = async()=> {
        api.settingsApi({id: visitedId.value})
        .then((res: any) => {
            commentFlag.value = res.data.comment ? true : false
        })
        .catch((error: any) => {
            console.log(error)
        })
    } 

    const showTip = (commentId:number) => {
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
            deleteComments(commentId)
        })
    }

    // 删除评论
    const deleteComments = (commentId:number)=> {
        api.delcommentApi({comment_id: commentId})
        .then((res: any) => {
            if(res.code)
            {
                commentsList.value = commentsList.value.filter(comment => comment.comment_id !== commentId);
                ElMessage.success('删除成功')
            }
        })
        .catch((error: any) => {
            console.log(error)
        })
    }
    
    function getDetail(commentId:number) {
        index.value = commentsList.value.findIndex((item: { comment_id: any; }) => item.comment_id == commentId);
        dialogVisible.value = true
    }

    function likeCmt() {
        api.likeApi({
            id: commentsList.value[index.value].comment_id,
            user: visitorId,
            state: commentsList.value[index.value].flag ? 1:0,
            type: 1,
        })
        .then((res: any) => {
            if(res.code)
            {
                if(commentsList.value[index.value].flag)
                {
                    commentsList.value[index.value].flag = false
                }
                else
                {
                    commentsList.value[index.value].flag = true           
                }
                commentsList.value[index.value].num_like = res.count
                ElMessage.success('操作成功')
            }
        })    
    }



    onMounted(async()=>{
        if(!isOwn.value)
            await getSettings()       
        //有权限再获取数据
        if(commentFlag.value) {
            getComments()
        }
    })

</script>

<template>
    <div class="comment-box" v-if="commentFlag">

        <div class="title" v-if="isOwn">我的评论</div>
        <div class="title" v-else>评论</div>
        <div style="width: 100%; border:1px solid #999999"></div>

        <div style="width: 100%;" v-if="!dataFlag">
            <el-empty :image-size=250 :image="bgUrl" description="暂无评论！"/>
        </div>
        <el-scrollbar class="scrollbar" v-else>
            <div v-for="item in commentsList" :key="item.comment_id" class="item" @click="getDetail(item.comment_id)">
                <div style="display: flex; width: 100%; justify-content: space-between;">
                    <div style="display: flex;">
                        <img class="icon" src="@/assets/icons/time.svg" alt="time" width="30">
                        <el-text  type="info" style="margin-left: 20px; font-size: 20px;">{{ item.time }}</el-text>
                    </div>
                    <el-dropdown style="margin-right: 20px;">
                        <el-button  plain>
                            <el-icon><More /></el-icon>
                        </el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item @click="showTip(item.comment_id)">删除</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>    

                <el-text  class="content" truncated >{{ item.content }}</el-text>

            </div>
            <el-text  type="info" style="margin: 30px; font-size: 20px;">没有更多数据了</el-text>
        </el-scrollbar>

    </div>

    <div style="width: 100%;" v-else>
        <div class="title">评论</div>
        <div style="width: 100%; border:1px solid #999999"></div>
        <el-empty :image-size=250 :image="bgUrl" description="用户已设为隐私"/>
    </div>

    <el-dialog
        v-model="dialogVisible"
        width="800"
        align-center
    >
        <template #header>
            <div style="height: 40px; border-bottom: solid #efefef; font-size: 20px; font-weight: bold">
                查看评论
            </div>
        </template>

        <div class="dialog">
            <div style="display: flex;">
                <el-avatar
                    :src="commentsList[index].avatar"
                    :size="60"
                    class="avatar"
                />
                <div 
                    style="display: flex; flex-direction: column; 
                    justify-content: space-between; margin-left: 10px;"
                >
                    <div style="font-size: 24px; color: black">{{ commentsList[index].name }}</div>
                    <div style="font-size: 16px; color: #ccd0db;">{{ commentsList[index].time }}</div>
                </div>                  
            </div>

            <p class="comment-content">{{ commentsList[index].content }}</p>

            <div style="display: flex; width: 60%; justify-content: space-between; margin-top: 10px; margin-left: 70px;">
                <span 
                    style="color: #29cdff; font-size: 20px;"
                    @click="router.push(`/detail?id=${commentsList[index].card_id}`)"
                >
                    查看原帖
                </span>

                <div style="display: flex; justify-content: center;">
                    <img v-if="commentsList[index].flag"
                        class="icon" 
                        src="@/assets/icons/likes.svg"
                        @click="likeCmt()"
                    >
                    <img v-else
                        class="icon" 
                        src="@/assets/icons/unlikes.svg"
                        @click="likeCmt()"
                    >
                    <span style="margin-left: 10px; font-size: 20px;">{{ commentsList[index].num_like }}</span>                    
                </div>

            </div>
        </div>

  </el-dialog>

</template>
  
<style scoped>
    .comment-box {
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
    }


    .item {
        display: flex;
        flex-direction: column;
        align-items: flex-start;
        justify-content: space-evenly;
        height: 100px;
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

    .content {
        width: 90%;
        margin-left: 20px;
        font-size: 22px;
        color: black;
    }

    .post-title {
        display: flex;
        justify-content: flex-start;
        align-items: center;

        width: 80%;
        height: 30px;
        padding-left: 20px;
        margin-left: 20px;

        color: black;
        font-size: 18px;

        background-color: rgba(156, 156, 156, 0.201);
        
    }

    .dialog {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: flex-start;

        max-height: 400px;
        overflow-y: auto;
    }

    .comment-content {

        margin-top: 10px;
        margin-left: 70px;
        font-size: 20px;
    }

    .icon {
        width: 30px;
    }

</style>