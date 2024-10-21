<script lang="ts" setup>
  import  bgUrl  from "@/assets/images/user_bg2.png"
  import Header from '@/components/BaseHeader.vue' 
  import { onMounted, ref } from 'vue';
  import api from '@/api/content'  
  import { useRouter } from "vue-router";
  import { ElMessage } from "element-plus";
  import type { cardInfo } from "@/types/types";

  // 路由
  const router = useRouter() 
  // 当前用户id
  const userId = Number(localStorage.getItem('userId')) 
  let isLoading = ref(true)
  let dataFlag = ref(false)

  let postsList = ref<cardInfo[]>([])

  // 获取关注圈发布的内容列表
  const getPosts = async()=> {
      await api.likeNewsApi({userId: userId})
      .then((res:any) => {
        if(res.code == 200)
        {
          if(res.data && res.data.length > 0)
          {
            dataFlag.value = true
            postsList.value = res.data
            isLoading.value = false
          }
          else
            isLoading.value = false
        }
      })
  }

  // 前往帖子详情页面
  function toDetail(cardId:Number) {
      router.push(`/detail?id=${cardId}`)
  }

  //点赞/取消点赞帖子
  function likePost(index:number) {
        api.likePostApi({
            cardId: postsList.value[index].cardId,
            userId: userId,
            state: postsList.value[index].likeFlag,
        })
        .then((res: any) => {
            if(res.code == 200)
            {
                if(postsList.value[index].likeFlag)
                {
                  postsList.value[index].likeFlag = 0
                  ElMessage.success("取消点赞成功！")
                }
                else
                {
                  postsList.value[index].likeFlag = 1
                  ElMessage.success("点赞成功！")           
                }
                postsList.value[index].likeNum =res.data
            }
        })      
    }

  onMounted(async()=>{
    await getPosts()
  })

</script>

<template>
  <div class="container">
      <Header style="z-index: 1;"></Header>

      <div class="content">

        <div class="loading" v-if="isLoading">
          <div class="spinner-box">
            <div class="circle-border">
              <div class="circle-core"></div>
            </div>
          </div>
        </div>

        <div class="middle" v-else>
          <div class="news-title">我的关注圈动态</div>
          <div v-if="!dataFlag" style="display: flex; height: 100%; align-items: center; justify-content: center;">
            <el-empty :image-size=400 :image="bgUrl" description="暂无动态！"/>
          </div>
          
          <div v-else v-for="(item,index) in postsList" :key="item.cardId" class="item">
            <div style="display: flex; align-items: center; ">
                <el-avatar
                    :src="item.avatar"
                    :size="60"
                    @click="router.push({ path: '/user', query: { id: item.userId } })"
                />
              <div style="display: flex; flex-direction: column; margin-left: 10px;">
                <span class="post-name" @click="router.push({ path: '/user', query: { id: item.userId } })">{{ item.name }}</span>
                <span style="color: #a7abb0; font-size: 16px" >{{ new Date(item.time).toLocaleString() }}</span>
              </div>
            </div>

            <div class="post-title" @click="toDetail(item.cardId)">{{ item.title }}</div>
            <p v-html="item.content" class="post-content" @click="toDetail(item.cardId)"></p>
            

            <div class="post-bottom">
              <div style="display: flex; align-items: center;">
                <img width="40" src="@/assets/icons/views.svg">
                <span class="icon-text">({{ item.viewNum }})</span>
              </div>
              <div style="display: flex; align-items: center;" @click="toDetail(item.cardId)">
                <img width="40" src="@/assets/icons/comment.svg">
                <span class="icon-text">({{ item.commentNum }})</span>
              </div>
              <div style="display: flex; align-items: center;">
                <img v-show="item.likeFlag" width="40" src="@/assets/icons/likes.svg" @click="likePost(index)">
                <img v-show="!item.likeFlag" width="40" src="@/assets/icons/unlikes.svg" @click="likePost(index)">
                <span class="icon-text">{{ item.likeNum }}</span>
              </div>
            </div>
          </div>
        </div>

        <div class="left"></div>
        <div class="right"></div>  

      </div>
    </div>
</template>

<style scoped>
  .container {
    display: flex;
    flex-direction: column;
    
    width: 100vw;
    min-height: 100vh;

    overflow-y: auto;
    overflow-x: hidden;

  }

  .content {
    display: flex;
    flex-wrap: nowrap;

    background: linear-gradient(to bottom,#f1f4f9,#dff1ff) ;
  }

  .loading {
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    order: 2;
    flex: 1 1 0 ;
    min-height: 100vh;  

  }

  .spinner-box {
    width: 40vw;
    min-height: 100vh;;
    display: flex;
    justify-content: center;
    align-items: center;
    background-color: white;
  }

  .circle-border {
    width: 150px;
    height: 150px;
    padding: 3px;
    display: flex;
    justify-content: center;
    align-content: center;
    border-radius: 50%;
    background: linear-gradient(0deg,rgb(0 0 0 / 0%) 33%,rgb(255 0 106) 100%);
    animation: spin 0.8s linear infinite;
  }

  .circle-core {
    width: 100%;
    height: 100%;
    background: white;
    border-radius: 50%;
  }

  @keyframes spin {
    0%{
      transform: rotate(0deg);
    }
    100%{
      transform: rotate(360deg);
    }
  }

  .middle {
    display: flex;
    flex-direction: column;
    min-height: 100vh;
    order: 2;
    flex: auto 1 0 ;
    align-items: center;

  }

  .item {
    position: relative;
    display: flex;
    flex-direction: column;
    justify-content: center;
    width: 40vw;
    margin-bottom: 50px;
    padding: 10px;
    background-color: white;
    border-radius: 10px;
    box-shadow: 0 4px 6px rgba(0, 0, 0, 0.1);
    transition: transform 0.3s ease;
  }

  .item:hover {
    transform: scale(1.04);
    background: linear-gradient(to bottom right, #f0f8ff, #e6e6fa);
  }

  .post-name {
    color: black;
    font-size: 20px;
    font-weight: bold;
  }

  .post-name:hover {
    color: #40c5f1;
  }

  .post-title {
    color: black;
    font-size: 22px;
    text-align: center;
  }

  .post-content {

    margin-left: 70px;
    color: #18191c;
    font-size: 18px;

  }

  .post-bottom {
    display: flex; 
    justify-content: space-between; 
    width: 80%; 
    margin-top: 20px;
    margin-left: 70px;
    
  }

  .icon-text {
    margin-left: 10px;
    font-size: 20px;
    color: #8A8B8B;

  }

  .news-title {
    font-size: 34px;
    margin-top: center;
    margin-top: 30px;
    margin-bottom: 50px;
    background-image: -webkit-linear-gradient(left,#e19ea5,#bcc0c1 10%,#6dc7db 20%,#ae9dd9 30%,
    #87c6d8 40%,#e39ea5 50%,#b9c2bf 60%,#70c6db 70%,#ae9dd9 80%,#87c6d8 90%, #e9a5a5);

    color: transparent;
    background-clip: text;

    background-size: 200% 100%;
    
    animation: a 5s infinite linear;
  }

  @keyframes a {
    0% {
      background-position: 0 0;
    }

    100% {
      background-position: -100% 0;
    }
  }


  .left {
      order: 1;
      flex: 20% 0 0;
      background: linear-gradient(to right, rgba(172,182,229,0.9),rgba(134, 253, 232,0.5));
      filter: blur(150px);
  }

  .right {
      order: 3;
      flex: 20% 0 0;
      background: linear-gradient(to right, rgba(101, 153, 153,0.1),rgba(255,192,203, 0.8));
      filter: blur(150px);
  }



</style>