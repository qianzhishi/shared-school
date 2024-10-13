<script lang="ts" setup>
    import { ref } from 'vue';
    import { Plus } from '@element-plus/icons-vue';
    import type { UploadProps, UploadUserFile } from 'element-plus';
    import { ElMessage, ElMessageBox } from 'element-plus'
    import EmojiContent from './EmojiContent.vue'
    import { useRouter } from 'vue-router'
    import api from '@/api/upload'

    // 路由
    const router = useRouter();

    //获得用户信息
    const store = window.localStorage;

    const props = defineProps(['type']);

    const fileList = ref<UploadUserFile[]>([]);
    const dialogImageUrl = ref('');
    const dialogVisible = ref(false);
    const hasDate = ref(false);
    const title = ref('');
    const content = ref('');

    const uploadPicture = async(file:any)=> {
        const data = new FormData();
        data.append('pictureData', file.file)                                    
        api.loadPictureApi(data)
        .then((res: any) => {
            if(res.code) {
                fileList.value.forEach(item => {if (item.name === file.file.name) { item.url = res.data.picture_url;}});
            }
        })
    }     

    const handleRemove: UploadProps['onRemove'] = async (uploadFile) => {
        api.removePictureApi({picture_url: uploadFile.url})
            .then((res:any) => {
                if(res.code == 1)
                {
                    ElMessage.success('删除成功');
                }
                else
                {
                    ElMessage.error('服务器接受失败，请稍后再试');
                }
            })
    }

    const handlePictureCardPreview: UploadProps['onPreview'] = (uploadFile) => {
        dialogImageUrl.value = uploadFile.url!;
        dialogVisible.value = true;
    }

    async function publish(routerUrl: string){
        if(title.value === ''){
            ElMessage({
                type: 'error',
                message: '无标题',
            })
        }else if(content.value === ''){
            ElMessage({
                type: 'error',
                message: '无内容',
            })
        }else{
            const loading = ref(false); // 新增：用于控制加载状态
            const result = await  ElMessageBox.confirm(
                '是否发布',
                '确认',
                {
                    confirmButtonText: '发布',
                    cancelButtonText: '取消',
                    type: 'info',
                }
            )
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: '取消发布',
                })
            });

            if (result === 'confirm') {
                try {
                    loading.value = true; // 开始加载
                    api.postApi({
                        id:Number(store.getItem('userId')),
                        type:Number(props.type),
                        title: title.value, 
                        content: content.value, 
                        images: fileList.value.map(item => item.url),
                    })
                    .then((res:any) => {
                        console.log(res)
                        loading.value = false;
                        if(res.code == 1)
                        {
                            ElMessage.success('发布成功');
                            router.push(routerUrl);
                        }
                        else
                        {
                          ElMessage.error('糟糕，出错了，请稍后再试');
                        }
                    })
                } catch (error: any) {
                    loading.value = false; // 确保在错误处理后关闭loading
                    ElMessage.error('发送失败: ' + error.message);
                }
            }
        }
    }
</script>

<template>
    <div class="container">
        <!-- 标题输入框 -->
        <div style="font-weight: bold;" class="mar">
            *标题: 
            <el-input v-model="title" placeholder="请输入标题" style="width: 90%;"/>
        </div>

        <!-- 内容输入框 -->
        <div class="mar">
            <EmojiContent v-model:inputContent="content"/>
        </div>

        <!-- 图片上传组件 -->
        <div class="mar">
            <el-upload
                v-model:file-list="fileList"
                list-type="picture-card"
                accept="image/jpeg,image/gif,image/png,image/jpg"
                :http-request="uploadPicture"
                :on-preview="handlePictureCardPreview"
                :on-remove="handleRemove"
                :limit="5"
            >
                <el-icon><Plus /></el-icon>
            </el-upload>
        </div>

        <div>
            <el-dialog v-model="dialogVisible" >
                <img :src="dialogImageUrl" alt="Preview Image" style="width: 100%; height: auto;"/>
            </el-dialog>
        </div>

        <!-- 发布按钮 -->
        <div class="mar">
            <el-button type="primary" @click="publish('/home')">发布</el-button>
        </div>

    </div>
</template>

<style scoped>
    .container {
        background-color: white; 
        width: 100%; 
        min-height: 50vh;
    }
    .mar{
        width: 100%; 
        display:flex;
        justify-content: center; /* 水平居中 */
        margin-bottom: 2vh;
        margin-top: 2vh;
    }
    .preview {
        /* 根据需要自定义预览区域的样式 */
        border: 1px solid #ddd;
        padding: 1rem;
        margin-bottom: 2vh;
    }
</style>