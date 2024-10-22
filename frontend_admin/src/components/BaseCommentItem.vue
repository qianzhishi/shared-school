<script lang="ts" setup>
    import { ref } from 'vue'
    import { UserFilled } from '@element-plus/icons-vue'
    import { WarnTriangleFilled } from '@element-plus/icons-vue'
    import { ElMessage } from 'element-plus'
    import api from '@/api/system'


    const session = window.sessionStorage;

    // 定义事件
    const emit = defineEmits(['delete']);

    // 对话框显示标识符
    const detailDialogVisible = ref(false);

    type comment = {
        commentId: number;
        userId: number;
        name: string;
        avatar: string;
        content: string;
        time: string;
    }

    // 传入参数
    const props = defineProps({
        info: {
            type: Object as () => comment,
            required: true,
        },
    })

    // 评论信息
    const authorAvatar = props['info'].avatar;
    const commentID = props['info'].commentId
    const authorID = props['info'].userId;
    const authorName = props['info'].name;
    const commentDetail = props['info'].content;
    const dateObj = new Date(props['info'].time);
    const updateTime = `${dateObj.toLocaleDateString()} ${dateObj.getHours()}:${("0" + dateObj.getMinutes()).slice(-2)}:${("0" + dateObj.getSeconds()).slice(-2)}`;

    // 删除评论
    async function deleteComment() {
        await api.delCommentApi({ commentId: commentID })
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
                    <h1 class="title">评论ID：</h1>
                    <h1>{{ commentID }}</h1>
                </div>
                <div style="display: flex">
                    <h1 class="title">作者ID：</h1>
                    <h1>{{ authorID }}</h1>
                </div>
            </div>
        </div>
        <div class="btn-bar">
            <el-button type="primary" @click="detailDialogVisible = true;">查看评论</el-button>
            <el-popconfirm width=" 220" confirm-button-text="确定" confirm-button-type="danger" cancel-button-text="取消"
                :icon="WarnTriangleFilled" icon-color="#F56C6C" title="你确定要删除吗？" @confirm="deleteComment()">
                <template #reference>
                    <el-button type="danger">删除评论</el-button>
                </template>
            </el-popconfirm>
        </div>

        <!-- 显示评论详情对话框 -->
        <el-dialog v-model="detailDialogVisible" title="评论详情" width="30vw" center>
            <div class="detail-dialog-container">
                <el-avatar :size="100" :src="authorAvatar" v-if="authorAvatar != null && authorAvatar != ''" />
                <el-avatar :size="50" :icon="UserFilled" v-else />
                <el-descriptions style="width:25vw;" title="评论信息" :column="1" :size="'default'" :border="true">
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon>
                                    <Postcard />
                                </el-icon>
                                评论ID
                            </div>
                        </template>
                        {{ commentID }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon>
                                    <Connection />
                                </el-icon>
                                发布者ID
                            </div>
                        </template>
                        {{ authorID }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon>
                                    <user />
                                </el-icon>
                                发布者用户名
                            </div>
                        </template>
                        {{ authorName }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label>
                            <div class="cell-item">
                                <el-icon>
                                    <Notification />
                                </el-icon>
                                发布时间
                            </div>
                        </template>
                        {{ updateTime }}
                    </el-descriptions-item>
                    <el-descriptions-item>
                        <template #label >
                            <div class="cell-item">
                                <el-icon>
                                    <Stamp />
                                </el-icon>
                                评论内容
                            </div>
                        </template>
                        <div v-html="commentDetail"></div>
                    </el-descriptions-item>
                </el-descriptions>
            </div>
            <template #footer>
                <div class="dialog-footer">
                    <el-button type="primary" @click="detailDialogVisible = false">确定</el-button>
                </div>
            </template>
        </el-dialog>
    </div>
</template>

<style scoped>
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

    .btn-bar {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    /* 显示评论详情对话框部分 */
    .detail-dialog-container {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        gap: 1.5vh;
        margin: 2vh 0;
    }
</style>
