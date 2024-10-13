<script lang="ts" setup>
    import { onMounted, ref } from 'vue'
    import { useRouter } from 'vue-router';
    import { UserFilled } from '@element-plus/icons-vue'
    import type { commentInfo} from '@/types/types'
    import api from '@/api/content'
    import { useCommentStore } from '@/stores/comment';
    import { ElMessage } from 'element-plus'

    // 本地仓库存储
    const store = window.localStorage;

    // 评论仓库
    const commentStore = useCommentStore()

    function toComment() {
        if (store.getItem('isLogin') == 'true') {
            commentStore.commentCommentDiaglogInit(commentStore.postId, props['father']);
        }
        else {
            ElMessage.error('您尚未登录，暂不支持该操作！')
        }
    }

    function likeCheck() {
        if (store.getItem('isLogin') == 'true') {
            like();
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
    const commentReply = ref(0)
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
  const like = async()=> {
      await api.likeApi({
            id: commentId.value,
            user: Number(store.getItem('userId')),
            state: boolTranslate(isLike.value),
            type: 1,
      })
      .then((res: any) => {
          console.log(res)
          // 状态码
          let code = res.code;
          if (code == 1) {

            // 消息提示
            if (isLike.value) {
                ElMessage.success("取消点赞成功！")
            }
            else {
                ElMessage.success("点赞成功！")
            }
            isLike.value = !isLike.value;
            commentLike.value = res.count;
          }
          else {
              let msg = res.data.msg;
              // 消息提示
              ElMessage.error(msg)
          }
      })
  }

    onMounted(() => {
        commentId.value = props['info'].id;
        userId.value = props['info'].author_id;
        userAvatar.value = props['info'].avatar;
        userName.value = props['info'].name;
        commentContent.value = props['info'].content;
        commentLike.value = props['info'].num_like;
        commentReply.value = props['info'].num_comment;
        updateTime.value = props['info'].time;
        isLike.value = flagTranslate(props['info'].flag);
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
            <div>
                {{ commentContent }}
            </div>
        </div>
        <div class="footer">
            <div class="info-bar">
                <div class="like-bar">
                    <div class="info-element" @click.stop="toComment()">
                        <svg t="1715517345008" class="like-icon" viewBox="0 0 1024 1024" version="1.1"
                            xmlns="http://www.w3.org/2000/svg" p-id="15300" width="200" height="200">
                            <path
                                d="M628.363636 605.090909c-18.618182 0-34.909091-16.290909-34.909091-34.909091S609.745455 535.272727 628.363636 535.272727h41.890909c18.618182 0 34.909091 16.290909 34.909091 34.909091s-13.963636 34.909091-34.909091 34.909091H628.363636z m-325.818181 0c-18.618182 0-34.909091-16.290909-34.909091-34.909091S283.927273 535.272727 302.545455 535.272727h160.581818c18.618182 0 34.909091 16.290909 34.909091 34.909091s-16.290909 34.909091-34.909091 34.909091H302.545455z m0-209.454545c-18.618182 0-34.909091-16.290909-34.909091-34.909091S283.927273 325.818182 302.545455 325.818182h372.363636c18.618182 0 34.909091 16.290909 34.909091 34.909091S693.527273 395.636364 674.909091 395.636364H302.545455z m202.472727 507.345454l-104.727273 69.818182c-48.872727 32.581818-114.036364 18.618182-144.290909-30.254545-2.327273-4.654545-4.654545-6.981818-6.981818-11.636364l-18.618182-39.563636c-111.709091-13.963636-195.490909-107.054545-195.490909-218.763637V272.290909C34.909091 162.909091 116.363636 69.818182 225.745455 53.527273c95.418182-13.963636 190.836364-18.618182 286.254545-18.618182s190.836364 6.981818 286.254545 18.618182c109.381818 16.290909 190.836364 109.381818 190.836364 218.763636v400.290909c0 116.363636-90.763636 214.109091-207.127273 221.090909-90.763636 4.654545-179.2 9.309091-269.963636 9.309091h-6.981818zM474.763636 837.818182c4.654545-4.654545 11.636364-6.981818 18.618182-6.981818h16.290909c88.436364 0 176.872727-2.327273 265.309091-9.309091 79.127273-4.654545 141.963636-72.145455 141.963637-151.272728V269.963636c0-76.8-55.854545-139.636364-130.327273-148.945454-90.763636-9.309091-181.527273-16.290909-274.618182-16.290909s-183.854545 6.981818-276.945455 18.618182c-74.472727 9.309091-130.327273 74.472727-130.327272 148.945454v400.290909c0 79.127273 62.836364 146.618182 141.963636 151.272727h9.309091c11.636364 0 23.272727 6.981818 27.927273 18.618182l27.927272 55.854546c0 2.327273 2.327273 2.327273 2.327273 4.654545 11.636364 16.290909 32.581818 20.945455 48.872727 9.309091l111.709091-74.472727z"
                                fill="#8a8a8a" p-id="15301"></path>
                        </svg>
                    </div>
                    <label class="data-text">评论</label>
                </div>
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
                <label class="update-time">评论时间：{{ updateTime }}</label>
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