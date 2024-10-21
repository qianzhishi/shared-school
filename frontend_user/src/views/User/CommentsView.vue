
<script lang="ts" setup>
    import  bgUrl  from "@/assets/images/user_bg2.png"
    import { computed, onMounted,ref } from 'vue'
    import { More } from '@element-plus/icons-vue'
    import { ElMessage, ElMessageBox } from "element-plus";
    import { useRoute, useRouter } from "vue-router";
    import api from '@/api/user'
    import { useUserStore } from '@/stores/user';
    
    const userStore = useUserStore();

    const router = useRouter()
    const route = useRoute()


    const dialogVisible = ref(false)

    // 当前访问者的用户id
    const visitorId = Number(localStorage.getItem('userId'))
    // 当前被访问用户的id
    const visitedId = ref(Number(route.query.id) || visitorId)
    // 记录用户是否访问自己的个人中心
    const isOwn = computed(()=>{
        return (visitedId.value == visitorId ? true : false)
    }) 
    const commentFlag = ref(true)
    // 记录被访问用户的评论查看权限
    if (!isOwn.value)
    {
        commentFlag.value = userStore.showComments ? true : false
    }


    // 记录当前评论列表是否有数据
    let dataFlag = ref(false)

    type commentData = {
        commentId: number,
        fatherId: number,
        userId: number,
        avatar: string,
        name: string,
        content: string,
        likeNum: number,
        time: string,
        likeFlag: number,
    }

    const commentsList = ref<commentData[]>([])
    //要查看的评论在对象数组里的索引值
    let index = ref(0)

    // 获取用户评论列表
    const getComments = async()=> {
        api.commentListApi({visitorId: visitorId, visitedId: visitedId.value, type:1})
            .then((res: any) => {
                if (res.code == 200)
                {
                    if(res.data && res.data.length > 0)
                    {
                        dataFlag.value = true
                        commentsList.value = res.data          
                    }
                    else
                        dataFlag.value = false
                }
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
        api.delCommentApi({commentId: commentId})
        .then((res: any) => {
            if(res.code == 200)
            {
                commentsList.value = commentsList.value.filter(comment => comment.commentId !== commentId);
                ElMessage.success('删除成功')
            }
        })
    }
    
    function getDetail(commentId:number) {
        index.value = commentsList.value.findIndex((item: { commentId: any; }) => item.commentId == commentId);
        dialogVisible.value = true
    }

    function likeCmt() {
        api.likeCommentApi({
            fatherId: commentsList.value[index.value].commentId,
            userId: visitorId,
            state: commentsList.value[index.value].likeFlag ? 1:0,
        })
        .then((res: any) => {
            if(res.code == 200)
            {
                if(commentsList.value[index.value].likeFlag)
                {
                    commentsList.value[index.value].likeFlag = 0
                }
                else
                {
                    commentsList.value[index.value].likeFlag = 1          
                }
                commentsList.value[index.value].likeNum = res.count
                ElMessage.success('操作成功')
            }
        })    
    }



    onMounted(async()=>{     
        //有权限再获取数据
        if(commentFlag.value || isOwn.value) {
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
            <div v-for="item in commentsList" :key="item.commentId" class="item" @click="getDetail(item.commentId)">
                <div style="display: flex; width: 100%; justify-content: space-between;">
                    <div style="display: flex;">
                        <img class="icon" src="@/assets/icons/time.svg" alt="time" width="30">
                        <el-text  type="info" style="margin-left: 20px; font-size: 20px;">{{ new Date(item.time as string).toLocaleString() }}</el-text>
                    </div>
                    <el-dropdown style="margin-right: 20px;">
                        <el-button  plain>
                            <el-icon><More /></el-icon>
                        </el-button>
                        <template #dropdown>
                            <el-dropdown-menu>
                                <el-dropdown-item @click="showTip(item.commentId)">删除</el-dropdown-item>
                            </el-dropdown-menu>
                        </template>
                    </el-dropdown>
                </div>    

                <el-text  class="content" truncated v-html="item.content"></el-text>

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

            <p class="comment-content" v-html="commentsList[index].content"></p>

            <div style="display: flex; width: 60%; justify-content: space-between; margin-top: 10px; margin-left: 70px;">
                <span 
                    style="color: #29cdff; font-size: 20px;"
                    @click="router.push(`/detail?id=${commentsList[index].fatherId}`)"
                >
                    查看原帖
                </span>

                <div style="display: flex; justify-content: center;">
                    <img v-if="commentsList[index].likeFlag"
                        class="icon" 
                        src="@/assets/icons/likes.svg"
                        @click="likeCmt()"
                    >
                    <img v-else
                        class="icon" 
                        src="@/assets/icons/unlikes.svg"
                        @click="likeCmt()"
                    >
                    <span style="margin-left: 10px; font-size: 20px;">{{ commentsList[index].likeNum }}</span>                    
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
        position: relative;
        object-fit: contain;
        height: fit-content;
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
        height: fit-content;
        margin-left: 20px;
        font-size: 24px;
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