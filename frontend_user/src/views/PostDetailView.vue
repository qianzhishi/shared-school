<script lang="ts" setup>
    import { onMounted, ref } from 'vue'
    import Header from '@/components/BaseHeader.vue'
    import Footer from '@/components/BaseFooter.vue'
    import Comment from '@/components/BaseCommentCard.vue'
    import EmojiContent from '@/components/EmojiContent.vue'
    import { useRouter, useRoute } from 'vue-router'
    import { UserFilled, ChatDotRound, Back } from '@element-plus/icons-vue'
    import { useCommentStore } from '@/stores/comment'
    import { ElMessage } from 'element-plus'
    import type {detailInfo} from '@/types/types'
    import api from '@/api/content'
    
    // 评论仓库
    const commentStore = useCommentStore();

    // 路由
    const route = useRoute()
    const router = useRouter();


    function toComment() {
        if (localStorage.getItem('isLogin') == 'true') {
            commentStore.postCommentDiaglogInit(Number(postId.value));
        }
        else {
            ElMessage.error('您尚未登录，暂不支持该操作！')
        }
    }

    function likeCheck() {
        if (localStorage.getItem('isLogin') == 'true') {
            like();
        }
        else {
            ElMessage.error('您尚未登录，暂不支持该操作！')
        }
    }

    // 帖子信息
    const postDetail = ref<detailInfo>()
    const postId = ref<number>(0)
    const authorId = ref<number>()
    const authorAvatar = ref<string>()
    const authorName = ref<string>()
    const postContent = ref<string>()
    const postImages = ref<Array<string>>()
    const readCount = ref<number>(0)
    const commentCount = ref<number>(0)
    const likeCount = ref<number>(0)
    const updateTime = ref<string>("2024/05/11 16:22:08")
    const isLike = ref<boolean>(false)

    // 前往修改页面
    function toPostEditor() {
        router.push({ path: '/update', query: { id: postId.value, type: route.query.type } });
    }

    // 判断是否为自己的帖子
    function checkAuthor() {
        if (localStorage.userId == authorId.value) {
            return true;
        }
        else {
            return false;
        }
    }

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

    // 删除帖子
    const deletePost = async()=> {
        await api.delPostApi({cardId: postId.value})
        .then((res: any) => {
            if (res.code == 200) {
                ElMessage.success("删除成功！")
                router.back()
            }
        })
    }

    // 评论
    const comment = async(type: number)=> {
        let commentContent = '';
        if (type == 1) {
            commentContent = commentStore.postContent;
        }
        else {
            commentContent = commentStore.commentContent;
        }
        await api.commentApi({
            userId: Number(localStorage.getItem('userId')),
            content: commentContent,
            fatherId: commentStore.fatherId,
            type: type         
        })
        .then((res: any) => {
            if (res.code == 200) {
                // 消息提示
                ElMessage.success("评论成功！")
                commentCount.value = res.data;
            }
        })
    }

    // 点赞帖子
    const like = async()=> {
        await api.likePostApi({
            cardId: postId.value,
            userId: Number(localStorage.getItem('userId')),
            state: boolTranslate(isLike.value),
        })
        .then((res: any) => {
            if (res.code == 200) {
                // 消息提示
                if (isLike.value) {
                    ElMessage.success("取消点赞成功！")
                }
                else {
                    ElMessage.success("点赞成功！")
                }
                isLike.value = !isLike.value;
                likeCount.value = res.data;
            }
      })
    }

    // 获取卡片信息
    const getPostInfo = async()=> {
        await api.detailApi({
            userId: Number(localStorage.getItem('userId')),
            cardId: Number(route.query.id)
        })
        .then((res: any) => {
            if (res.code == 200) {
                // 导入卡片信息
                postDetail.value = res.data;
                postId.value = Number(route.query['id']);
                authorId.value = res.data.cardInfo.id;
                authorAvatar.value = res.data.avatar;
                authorName.value = res.data.name;
                postContent.value = res.data.cardInfo.content;
                postImages.value = res.data.cardInfo.images;
                readCount.value = res.data.cardInfo.viewNum;
                commentCount.value = res.data.cardInfo.commentNum;
                updateTime.value = res.data.cardInfo.date;
                isLike.value = flagTranslate(res.data.likeFlag)
                likeCount.value = res.data.cardInfo.likeNum;
            }
      })
    }

    onMounted(() => {
        commentStore.fatherId = Number(route.query.id);
        getPostInfo();
    })
</script>

<template>
  <div class="container">
      <div class="header">
          <Header></Header>
      </div>
      <div class="back-nav">
          <el-button :icon="Back" style="margin-left:10vw" @click="router.back()">返回</el-button>
      </div>
      <div class="content">
          <!-- 帖子内容 -->
          <div class="post-content">
              <!-- 帖子标题栏 -->
              <div class="post-content-header">
                  <div class="user-info">
                      <div @click="router.push({ path: '/user', query: { id: authorId } })">
                          <el-avatar class="avatar" :src="authorAvatar" v-if="authorAvatar != null" />
                          <el-avatar class="avatar" :icon="UserFilled" v-else />
                      </div>
                      <label class="user-name">{{ authorName }}</label>
                  </div>
                  <div class="user-info">
                      <div class="info-element">
                          <svg t="1715593642941" class="like-icon" viewBox="0 0 1024 1024" version="1.1"
                              xmlns="http://www.w3.org/2000/svg" p-id="16548" width="200" height="200">
                              <path
                                  d="M759.600191 263.253706c-14.437827-9.465582-33.785477-5.429663-43.25106 8.987698-9.455349 14.427594-5.429663 33.795711 8.987698 43.25106 113.344464 74.322729 172.030052 183.955662 171.714873 221.87939-0.620124 74.14979-152.803151 288.444435-385.216455 288.444435-244.969271 0-385.211339-226.475058-385.211339-288.180422 0-38.473243 58.665121-148.675134 170.804131-222.134193 14.437827-9.455349 18.463514-28.814256 9.018398-43.240827-9.455349-14.427594-28.814256-18.473747-43.240827-9.018398C144.135748 341.236801 64.155159 468.094024 64.155159 537.637913c0 101.133352 178.419576 350.648149 447.679066 350.648149 272.125771 0 446.809255-246.372224 447.684182-350.383112C960.097598 468.957695 879.899044 342.141404 759.600191 263.253706z"
                                  fill="#8a8a8a" p-id="16549"></path>
                              <path
                                  d="M389.948223 136.356574c-17.253966 0-31.233352 13.980409-31.233352 31.233352l0 30.470989c0 17.253966 13.980409 31.233352 31.233352 31.233352s31.233352-13.980409 31.233352-31.233352l0-30.470989C421.182598 150.336984 407.202189 136.356574 389.948223 136.356574z"
                                  fill="#8a8a8a" p-id="16550"></path>
                              <path
                                  d="M633.728412 136.356574c-17.253966 0-31.233352 13.980409-31.233352 31.233352l0 30.470989c0 17.253966 13.980409 31.233352 31.233352 31.233352s31.233352-13.980409 31.233352-31.233352l0-30.470989C664.961764 150.336984 650.982378 136.356574 633.728412 136.356574z"
                                  fill="#8a8a8a" p-id="16551"></path>
                              <path
                                  d="M511.524163 337.555969c43.963281 0 75.105558-30.308283 86.583984-48.223305 9.222035-14.396895 5.042853-33.348526-9.242502-42.753733-14.275121-9.42465-33.57263-5.409197-43.159986 8.744151-0.132006 0.203638-13.614066 19.765159-34.18252 19.765159-19.989263 0-32.43369-18.117636-33.267685-19.378349-9.181103-14.407128-28.285207-18.819624-42.834574-9.750061-14.650675 9.099239-19.155269 28.356838-10.044774 43.007513C436.591543 307.014372 467.347011 337.555969 511.524163 337.555969z"
                                  fill="#8a8a8a" p-id="16552"></path>
                              <path
                                  d="M511.838318 733.499743c99.893104 0 181.170223-81.277118 181.170223-181.170223s-81.277118-181.170223-181.170223-181.170223-181.170223 81.277118-181.170223 181.170223S411.945213 733.499743 511.838318 733.499743zM511.838318 433.627025c65.456804 0 118.702496 53.245691 118.702496 118.702496s-53.245691 118.702496-118.702496 118.702496-118.702496-53.245691-118.702496-118.702496S446.381513 433.627025 511.838318 433.627025z"
                                  fill="#8a8a8a" p-id="16553"></path>
                          </svg>
                          <label class="update-time">浏览：{{ readCount }}</label>
                      </div>
                      <label class="update-time">更新时间：{{ new Date(updateTime as string).toLocaleString() }}</label>
                  </div>
              </div>
              <!-- 帖子图片 -->
              <div class="post-content-main">
                  <div v-html="postContent"></div>
                  <div class="post-content-images">
                      <div v-for="(img, index) in postImages" :key="index">
                          <el-image class="post-img" :src="img" :zoom-rate="1.2" :max-scale="7" :min-scale="0.2"
                              :preview-src-list="postImages" :initial-index="index" fit="contain" />
                      </div>
                  </div>
              </div>
              <!-- 帖子操作区 -->
              <div class="post-content-footer">
                  <el-affix position="bottom" :offset="20">
                      <div class="info-bar">
                          <div class="user-info">
                              <div class="info-element">
                                  <svg t="1715517345008" class="comment-icon" viewBox="0 0 1024 1024" version="1.1"
                                      xmlns="http://www.w3.org/2000/svg" p-id="15300" width="200" height="200">
                                      <path
                                          d="M628.363636 605.090909c-18.618182 0-34.909091-16.290909-34.909091-34.909091S609.745455 535.272727 628.363636 535.272727h41.890909c18.618182 0 34.909091 16.290909 34.909091 34.909091s-13.963636 34.909091-34.909091 34.909091H628.363636z m-325.818181 0c-18.618182 0-34.909091-16.290909-34.909091-34.909091S283.927273 535.272727 302.545455 535.272727h160.581818c18.618182 0 34.909091 16.290909 34.909091 34.909091s-16.290909 34.909091-34.909091 34.909091H302.545455z m0-209.454545c-18.618182 0-34.909091-16.290909-34.909091-34.909091S283.927273 325.818182 302.545455 325.818182h372.363636c18.618182 0 34.909091 16.290909 34.909091 34.909091S693.527273 395.636364 674.909091 395.636364H302.545455z m202.472727 507.345454l-104.727273 69.818182c-48.872727 32.581818-114.036364 18.618182-144.290909-30.254545-2.327273-4.654545-4.654545-6.981818-6.981818-11.636364l-18.618182-39.563636c-111.709091-13.963636-195.490909-107.054545-195.490909-218.763637V272.290909C34.909091 162.909091 116.363636 69.818182 225.745455 53.527273c95.418182-13.963636 190.836364-18.618182 286.254545-18.618182s190.836364 6.981818 286.254545 18.618182c109.381818 16.290909 190.836364 109.381818 190.836364 218.763636v400.290909c0 116.363636-90.763636 214.109091-207.127273 221.090909-90.763636 4.654545-179.2 9.309091-269.963636 9.309091h-6.981818zM474.763636 837.818182c4.654545-4.654545 11.636364-6.981818 18.618182-6.981818h16.290909c88.436364 0 176.872727-2.327273 265.309091-9.309091 79.127273-4.654545 141.963636-72.145455 141.963637-151.272728V269.963636c0-76.8-55.854545-139.636364-130.327273-148.945454-90.763636-9.309091-181.527273-16.290909-274.618182-16.290909s-183.854545 6.981818-276.945455 18.618182c-74.472727 9.309091-130.327273 74.472727-130.327272 148.945454v400.290909c0 79.127273 62.836364 146.618182 141.963636 151.272727h9.309091c11.636364 0 23.272727 6.981818 27.927273 18.618182l27.927272 55.854546c0 2.327273 2.327273 2.327273 2.327273 4.654545 11.636364 16.290909 32.581818 20.945455 48.872727 9.309091l111.709091-74.472727z"
                                          fill="#8a8a8a" p-id="15301"></path>
                                  </svg>
                                  <label style="color:gray;">评论（{{ commentCount }}）</label>
                                  <el-input v-model="commentStore.postContent" style="width: 25vw"
                                      placeholder="网络绝非法外之地，请谨言慎行" :prefix-icon="ChatDotRound" @click="toComment()" />
                              </div>

                              <!-- 帖子的评论对话框 -->
                              <el-dialog v-model="commentStore.postCommentDialogVisiable" title="评论" width="500"
                                  center align-center @close="commentStore.postCommentDialogClose()">
                                  <div class="mar">
                                      <EmojiContent v-model:inputContent="commentStore.postContent" />
                                  </div>
                                  <template #footer>
                                      <div class="dialog-footer">
                                          <el-button type="primary"
                                              @click="comment(1); commentStore.postCommentDialogClose()">
                                              提交评论
                                          </el-button>
                                      </div>
                                  </template>
                              </el-dialog>

                              <!-- 评论的评论对话框 -->
                              <el-dialog v-model="commentStore.commentCommentDialogVisiable" title="评论" width="500"
                                  center align-center @close="commentStore.commentCommentDialogClose()">
                                  <div class="mar">
                                      <EmojiContent v-model:inputContent="commentStore.commentContent" />
                                  </div>
                                  <template #footer>
                                      <div class="dialog-footer">
                                          <el-button type="primary"
                                              @click="comment(2); commentStore.commentCommentDialogClose()">
                                              提交评论
                                          </el-button>
                                      </div>
                                  </template>
                              </el-dialog>
                          </div>
                          <div class="user-info">
                              <!-- 点赞 -->
                              <div class="like-bar">
                                  <div class="info-element">
                                      <div @click="likeCheck()">
                                          <svg t="1715352765087" class="like-icon" viewBox="0 0 1024 1024"
                                              version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="10822"
                                              width="200" height="200" v-if="isLike">
                                              <path
                                                  d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                                  fill="#d81e06" p-id="10823"></path>
                                              <path
                                                  d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                                  fill="#d81e06" p-id="10824"></path>
                                          </svg>
                                          <svg t="1715353376137" class="like-icon" viewBox="0 0 1024 1024"
                                              version="1.1" xmlns="http://www.w3.org/2000/svg" p-id="11033"
                                              width="200" height="200" v-else>
                                              <path
                                                  d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                                  fill="#8a8a8a" p-id="11034"></path>
                                              <path
                                                  d="M838.762667 153.173333c145.706667 99.904 160.853333 309.610667 41.557333 452.906667l-1.642667 1.813333-296.96 305.749334-0.704 0.704a83.2 83.2 0 0 1-117.653333 0.32C302.784 754.944 203.690667 652.48 165.077333 606.08 45.781333 462.784 60.906667 253.077333 206.634667 153.173333c101.738667-69.738667 228.48-60.501333 316.053333 23.978667 87.573333-84.48 214.336-93.717333 316.074667-23.957333z m-486.698667 92.224c-74.56 0-128 60.437333-128 139.221334a32 32 0 0 0 64 0c0-45.077333 26.666667-75.221333 64-75.221334a32 32 0 1 0 0-64z"
                                                  fill="#8a8a8a" p-id="11035"></path>
                                          </svg>
                                      </div>
                                      <label>{{ likeCount }}</label>
                                  </div>
                              </div>

                              <el-dropdown v-if="checkAuthor()">
                                  <el-button type="info" plain round>
                                      更多
                                  </el-button>
                                  <template #dropdown>
                                      <el-dropdown-menu>
                                          <el-dropdown-item @click="toPostEditor()">修改</el-dropdown-item>
                                          <el-dropdown-item @click="deletePost()">删除</el-dropdown-item>
                                      </el-dropdown-menu>
                                  </template>
                              </el-dropdown>
                          </div>
                      </div>
                  </el-affix>
              </div>
          </div>
          <!-- 评论区 -->
          <div class="comment-area">
              <div v-for="(comment, index) in postDetail?.commentList || []" :key="index">
                  <Comment :info="comment" />
              </div>
              <div class="bottom-tip">
                  <label class="bottom-tip-text">没有更多内容了</label>
              </div>
          </div>
      </div>
      <div class="footer">
          <Footer></Footer>
      </div>
  </div>
  <el-backtop target=".content" :right="100" :bottom="100" />
</template>

<style scoped>
    .container {
        color: black;
    }

    .center {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .back-nav {
        display: flex;
        align-items: center;
        height: 10vh;
        width: 100vw;
    }

    .header {
        min-height: fit-content;
    }

    .title {
        color: black;
        font-size: 2em;
        text-align: center;
        text-justify: center;
    }

    .content {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        min-height: 60vh;
        background: white;
    }

    .post-content {
        width: 80vw;
        min-height: 30vh;
        margin-bottom: 2vh;
        padding: 3vh 3vw;
        border: 2px solid lightgrey;
        border-radius: 2vw;
        background-color: #ecf5ff;
    }

    .post-content-header {
        display: flex;
        justify-content: space-between;
        align-items: center;
    }

    .user-info {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 2vw;
    }

    .user-name {
        font-size: 2.5vb;
    }

    .update-time {
        font-size: 2.5vb;
        color: gray;
    }

    .post-content-main {
        margin: 5vh 0;
    }

    .post-content-images {
        display: grid;
        grid-template-columns: repeat(4, 25%);
        place-items: center;
        margin-top: 5vh;
    }

    .post-img {
        width: 15vw;
        height: 15vw;
        overflow: hidden;
        border: 1px solid lightgray;
        border-radius: 1vw;
        background-color: white;
    }

    .post-img img {
        max-width: 100%;
        max-height: 100%;
        display: block;
        margin: auto;
        /* 居中图片 */
    }

    .post-content-footer {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .info-bar {
        display: flex;
        justify-content: space-between;
        align-items: center;
        gap: 2vw;
        min-width: 75vw;
        height: 8vh;
        padding: 0 2vw;
        border-radius: 3vh;
        border: 1px solid lightgrey;
        background-color: white;
        box-shadow: 5px 5px 5px lightgray;
    }

    .info-element {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 0.5vw;
    }

    .like-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 0.5vw;
    }

    .comment-icon {
        width: 2vw;
        height: 2vw;
    }

    .like-icon {
        width: 2vw;
        height: 2vw;
        transition: transform 0.3s ease;
    }

    .like-icon:hover {
        transform: scale(1.2);
    }

    .comment-area {
        width: 80vw;
        min-height: 10vh;
        margin-top: 2vh;
        margin-bottom: 5vh;
        padding: 3vh 3vw;
        border: 2px solid lightgrey;
        border-radius: 2vw;
        background-color: #ecf5ff;
    }

    .footer {
        min-height: 30vh;
    }

    .mar {
        width: 100%;
        display: flex;
        justify-content: center;
        margin-bottom: 2vh;
        margin-top: 2vh;
    }

    .avatar {
        transition: transform 0.5s ease, box-shadow 0.5s ease;
    }

    .avatar:hover {
        transform: scale(1.1);
        box-shadow: 5px 5px 5px lightgrey;
    }

    .bottom-tip {
        display: flex;
        justify-content: center;
        align-items: center;
        padding: 1vh 0;
    }

    .bottom-tip-text {
        color: gray;
        font-size: 2vh;
    }

    .tip-area {
        display: grid;
        place-items: center;
    }

    .type-tip {
        font-size: 1.5vw;
    }

    .contact-tip {
        font-weight: bold;
        font-size: 2.2vw;
        color: #409EFF;
    }

    /* 按钮基础样式 */
    .base-btn {
        width: 6vw;
        height: 5vh;
    }

    /* 各模块按钮样式 */
    .study-btn {
        background-color: #409EFF;
        border: none;
        transition: all 0.3s ease;
    }

    .study-btn:hover {
        opacity: 0.6;
    }

    .play-btn {
        background-color: #E6A23C;
        border: none;
        transition: all 0.3s ease;
    }

    .play-btn:hover {
        opacity: 0.6;
    }

    .love-btn {
        background-color: #f89898;
        border: none;
        transition: all 0.3s ease;
    }

    .love-btn:hover {
        opacity: 0.6;
    }

    .icon {
        width: 1.5vw;
        height: 1.5vw;
    }
</style>
