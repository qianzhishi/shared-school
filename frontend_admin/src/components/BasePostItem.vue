<script lang="ts" setup>
    import { ref } from 'vue'
    import { UserFilled } from '@element-plus/icons-vue'
    import { WarnTriangleFilled } from '@element-plus/icons-vue'
    import { ElMessage } from 'element-plus'
    import api from '@/api/system'

    // 定义事件
    const emit = defineEmits(['delete']);

    type post = {
        cardId: number;
        type: number;
        userId: number;
        name: string;
        avatar: string;
        title: string;
        content: string;
        time: string;
        images: Array<string>;
    }

    // 传入参数
    const props = defineProps({
        info: {
            type: Object as () => post,
            required: true,
        },
    })

    // 标签颜色列表
    const tagColorList = [
        'primary', 'warning', 'danger', 'success', 'info'
    ]

    // 帖子类型
    const types = [
        '学习', '娱乐', '表白',
    ]

    // 信息
    const authorAvatar = props['info'].avatar;
    const cardID = props['info'].cardId;
    const cardType = props['info'].type;
    const authorID = props['info'].userId;
    const authorName = props['info'].name;

    // 是否显示dialog
    const dialogVisible = ref(false);

    // 帖子详情
    const postTitle = props['info'].title;
    const postContent = props['info'].content;
    const dateObj = new Date(props['info'].time);
    const updateTime = `${dateObj.toLocaleDateString()} ${dateObj.getHours()}:${("0" + dateObj.getMinutes()).slice(-2)}:${("0" + dateObj.getSeconds()).slice(-2)}`;
    const postImages = props['info'].images;

    // 删除帖子
    async function deletePost() {
        await api.delPostApi({ cardId: cardID })
            .then((res:any) => {
                if (res.code == 200) {
                    emit('delete');
                    ElMessage.success('删除成功！');
                }
            })
    }
</script>

<template>
    <div class="user-item">
        <div class="info-bar">
            <el-avatar :size="50" :src="authorAvatar" v-if="authorAvatar != null && authorAvatar != ''" />
            <el-avatar :size="50" :icon="UserFilled" v-else />
            <div class="info">
                <div style="display: flex">
                    <h1 class="title">帖子ID：</h1>
                    <h1 class="title-text">{{ cardID }}</h1>
                </div>
                <div style="display: flex">
                    <h1 class="title">作者ID：</h1>
                    <h1 class="title-text">{{ authorID }}</h1>
                </div>
            </div>
            <div>
                <el-tag effect="dark" :type="tagColorList[cardType - 1]">{{ types[cardType - 1] }}</el-tag>
            </div>
        </div>
        <div class="btn-bar">
            <el-button type="primary" @click="dialogVisible = true;">查看帖子</el-button>
            <el-popconfirm width=" 220" confirm-button-text="确定" confirm-button-type="danger" cancel-button-text="取消"
                :icon="WarnTriangleFilled" icon-color="#F56C6C" title="你确定要删除吗？" @confirm="deletePost()">
                <template #reference>
                    <el-button type="danger">删除帖子</el-button>
                </template>
            </el-popconfirm>
        </div>
        <el-dialog v-model="dialogVisible" title="详情信息" class="content" style="width: 82vw;">
            <div class="post-content">
                <!-- 帖子标题栏 -->
                <div class="post-content-header">
                    <div class="user-info">
                        <div>
                            <el-avatar class="avatar" :src="authorAvatar" v-if="authorAvatar != null" />
                            <el-avatar class="avatar" :icon="UserFilled" v-else />
                        </div>
                        <label class="user-name">{{ authorName }}</label>
                    </div>
                    <div class="user-info">
                        <label class="update-time">更新时间：{{ updateTime}}</label>
                    </div>
                </div>
                <!-- 帖子图片 -->
                <div class="post-content-main">
                    <div>{{ postTitle }}</div>
                    <div v-html="postContent"></div>
                    <div class="post-content-images" v-if="postImages == null || postImages.length !== 0">
                        <div v-for="(img, index) in postImages" :key="index">
                            <el-image class="post-img" :src="img" :zoom-rate="1.2" :max-scale="7" :min-scale="0.2"
                                :preview-src-list="postImages" :initial-index="index" fit="contain" />
                        </div>
                    </div>
                </div>
            </div>
        </el-dialog>
    </div>
</template>

<style scoped>
    .content {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        width: 90vw;
        min-height: 60vh;
        background: white;
    }

    .user-item {
        display: flex;
        justify-content: space-between;
        min-width: 80%;
        min-height: 10vh;
        border-radius: 2vh;
        border: 1px solid lightgray;
        margin: 2vh 0;
        padding: 0 2vw;
        transition: background-color 0.5s ease;
    }

    .user-item:hover {
        background-color: #d9ecff;
    }

    .info-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        color: black;
    }

    .info {
        margin: 0 1vw;
    }

    .title {
        color: gray;
        font-style: bold;
    }

    .title-text {
        min-width: 8vw;
        max-width: 8vw;
        white-space: nowrap;
        overflow: hidden;
        text-overflow: ellipsis;
    }

    .btn-bar {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .post-content {
        width: 80vw;
        min-height: 30vh;
        margin-top: 5vh;
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

    .tag-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 1vw;
    }
</style>
