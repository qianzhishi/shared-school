<script lang="ts" setup>
    import { onMounted, ref } from 'vue'
    import { useRouter } from 'vue-router';
    import { UserFilled } from '@element-plus/icons-vue'
    import type { commentInfo} from '@/types/types'
    import api from '@/api/content'
    import { ElMessage } from 'element-plus'

    function likeCheck() {
        if (localStorage.getItem('isLogin') == 'true') {
            likeComment();
        }
        else {
            ElMessage.error('您尚未登录，暂不支持该操作！')
        }
    }

    // 路由
    const router = useRouter();

    // 传入参数
    const props = defineProps({
        info: {
            type: Object as () => commentInfo,
            required: true,
        },
        father: {
            type: Number,
            required: true,
        }
    })

    // 评论发布者信息
    const userId = ref();
    const userAvatar = ref();
    const userName = ref('用户名');

    // 评论信息
    const commentId = ref();
    const commentContent = ref("sbyl");
    const commentLike = ref(0)
    const updateTime = ref("2024/05/12 20:23:09")
    const isLike = ref(false)

    // 是否点赞状态码转换
    function flagTranslate(flag: number) {
        if (flag == 1) {
            return true;
        }
        else {
            return false;
        }
    }

    // bool转换
    function boolTranslate(flag: boolean) {
        if (flag) {
            return 1;
        }
        else {
            return 0;
        }
    }

    // 点赞评论
    const likeComment = async()=> {
        await api.likeCommentApi({
            commentId: commentId.value,
            userId: Number(localStorage.getItem('userId')),
            state: boolTranslate(isLike.value),
        })
        .then((res: any) => {
            if (res.code == 200) {
                if (isLike.value) {
                    ElMessage.success("取消点赞成功！")
                }
                else {
                    ElMessage.success("点赞成功！")
                }
                isLike.value = !isLike.value;
                commentLike.value = res.data;
            }
      })
    }

    onMounted(() => {
        commentId.value = props['info'].commentId;
        userId.value = props['info'].userId;
        userAvatar.value = props['info'].avatar;
        userName.value = props['info'].name;
        commentContent.value = props['info'].content;
        commentLike.value = props['info'].likeNum;
        updateTime.value = props['info'].time;
        isLike.value = flagTranslate(props['info'].likeFlag);
    })
</script>

<template>
    <div class="comment-item">
        <div class="header">
            <div class="user-info">
                <div @click.stop="router.push({ path: 'user', query: { id: userId } })">
                    <el-avatar class="avatar" :src="userAvatar" v-if="userAvatar != null" />
                    <el-avatar class="avatar" :icon="UserFilled" v-else />
                </div>
                <label class="user-name">{{ userName }}</label>
            </div>
        </div>
        <div class="content">
            <div v-html="commentContent"></div>
        </div>
        <div class="footer">
            <div class="info-bar">
                <div class="like-bar">
                    <div class="info-element" @click.stop="likeCheck()">
                        <svg t="1715352765087" class="like-icon" viewBox="0 0 1024 1024" version="1.1"
                            xmlns="http://www.w3.org/2000/svg" p-id="10822" width="200" height="200" v-if="isLike">
                            <path
                                d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                fill="#d81e06" p-id="10823"></path>
                            <path
                                d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                fill="#d81e06" p-id="10824"></path>
                        </svg>
                        <svg t="1715353376137" class="like-icon" viewBox="0 0 1024 1024" version="1.1"
                            xmlns="http://www.w3.org/2000/svg" p-id="11033" width="200" height="200" v-else>
                            <path
                                d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                fill="#8a8a8a" p-id="11034"></path>
                            <path
                                d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                fill="#8a8a8a" p-id="11035"></path>
                        </svg>
                    </div>
                    <label class="data-text">{{ commentLike }}</label>
                </div>
            </div>
            <div>
                <label class="update-time">评论时间：{{ new Date(updateTime as string).toLocaleString() }}</label>
            </div>
        </div>
    </div>
</template>

<style scoped>
    .comment-item {
        padding: 2vh 0;
        padding-left: 5vw;
        border-top: 1px solid lightgray;
    }

    .header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .avatar {
        transition: transform 0.5s ease, box-shadow 0.5s ease;
    }

    .avatar:hover {
        transform: scale(1.1);
        box-shadow: 5px 5px 5px lightgrey;
    }

    .user-info {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 1vw;
    }

    .user-name {
        font-size: 2.5vb;
    }

    .update-time {
        color: gray;
        font-size: 2vb;
    }

    .content {
        height: fit-content;
        margin: 1vh 0;
        padding: 1vh 1vw;
        font-size: 2vb;
        background: white;
    }

    .footer {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .info-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 2vw;
    }

    .info-element {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 0.5vw;
    }

    .data-text {
        color: gray;
        font-size: 2vb;
    }

    .like-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 0.5vw;
    }

    .like-icon {
        width: 1.5vw;
        height: 1.5vw;
        transition: transform 0.3s ease;
    }

    .like-icon:hover {
        transform: scale(1.2);
    }

    .mar {
        width: 100%;
        display: flex;
        justify-content: center;
        margin-bottom: 2vh;
        margin-top: 2vh;
    }
</style>