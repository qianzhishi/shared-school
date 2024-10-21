<script lang="ts" setup>
    import { Plus } from '@element-plus/icons-vue'
    import { ref, reactive, onMounted, computed } from 'vue'
    import { Camera } from '@element-plus/icons-vue'
    import type {  FormInstance, FormRules } from 'element-plus'
    import { ElMessage } from 'element-plus'
    import {  onBeforeRouteUpdate, useRoute } from 'vue-router'
    import api from '@/api/user'
    import { useUserStore } from '@/stores/user';
    
    const userStore = useUserStore();

    // 路由
    const route = useRoute()
    

    // 当前访问者的用户id
    const visitorId = Number(localStorage.getItem('userId'))
    // 当前被访问用户的id
    const visitedId = ref(Number(route.query.id) || visitorId)
    // 记录用户是否访问自己的个人中心
    const isOwn = computed(()=>{
        return (visitedId.value == visitorId ? true : false)
    }) 

    let dialogVisible = ref(false);

   
    let oldImageUrl = ref('')
    let imageUrl = ref('');


    interface RuleForm {
        id: number
        username: string
        avatar: string
        age: number
        gender: number
        intro: string
        contact: string
    }


    const formData = ref<RuleForm>({
        id: visitedId.value,
        username: userStore.name,
        avatar: userStore.avatar,
        age: userStore.age,
        gender: userStore.gender,
        intro: userStore.intro,
        contact: userStore.contact,
    })

    // 编辑用户信息
    const editUserInfo = ()=> {
        api.editApi(formData.value)
            .then((res: any) => {
            console.log(res)
            if (res.code == 200)
            {
                userStore.fetchUserInfo(visitedId.value)
                ElMessage.success('保存成功！')
            }
        })
    }

    

    const ruleFormRef = ref<FormInstance>()

    const submitForm = async (formEl: FormInstance | undefined) => {
        if (!formEl) return
        await formEl.validate((valid) => {
            if (valid) {   
                removeImage()
                editUserInfo()
            } else {
                ElMessage.error('输入错误！驳回')
            }
        })
    } 

    const rules = reactive<FormRules<RuleForm>>({
        username: [
            { required: true, message: '姓名不能为空', trigger: 'blur' },
            { min: 1, max: 10, message: '姓名不能超过10个字', trigger: 'blur' },
        ],
        age: [
            { required: true, message: '年龄不能为空', trigger: 'blur' },
            { type: 'number', message: '年龄必须是一个数字', trigger: 'blur' },
            { pattern: /^\d{1,2}$/, message: '年龄应为1至2位数字', trigger: 'blur' },
        ],
        contact: [
            { min: 0, max: 30, message: '联系方式不能超过30字', trigger: 'blur' },
        ],
        intro: [
            { min: 0, max: 30, message: '个性签名不能超过30字', trigger: 'blur' },
        ],
    })

    function confirm() {
        if(imageUrl.value !='')
        {
            oldImageUrl.value = formData.value.avatar
            formData.value.avatar = imageUrl.value

        }
        dialogVisible.value = false
    }

    const beforeUpload = (rawFile:any)=>{
        if (rawFile.type !== "image/jpeg" && rawFile.type !== "image/png" && rawFile.type !== "image/jpg") {
            ElMessage.error('图片类型只能是JPEG,JPG,PNG格式')
            return false
        } else if (rawFile.size / 1024 / 1024 > 2) {
            ElMessage.error('上传图片大小不能超过2MB!!')
            return false
        }
        return true       
    }
    // 删除用户上传的头像
    const removeImage = ()=>{
        if(oldImageUrl.value !='')
        {
            api.removeImageApi({userId: visitorId})
                .then(() => {
            })

        }      
    }

    // 上传头像
    const uploadFile = async(item:any)=> {
        const data = new FormData();
        data.append('file', item.file)
        data.append('authorId', localStorage.getItem('userId') as string)
        data.append('type','1')
                                    
        api.uploadImageApi(data)
        .then((res: any) => {
            if(res.code == 200) {
                imageUrl.value = res.data
                ElMessage.success('上传成功！')
            }
        })
}

    // 当路由更新时重新获取数据
    onBeforeRouteUpdate(async(from) => {
        await userStore.fetchUserInfo(Number(from.query.id))
            formData.value.id = visitedId.value
            formData.value.username = userStore.name
            formData.value.avatar = userStore.avatar
            formData.value.age = userStore.age
            formData.value.gender = userStore.gender
            formData.value.intro = userStore.intro
            formData.value.contact = userStore.contact
    });

    onMounted(async () => {
        
        await userStore.fetchUserInfo(visitedId.value)
        formData.value.id = visitedId.value
        formData.value.username = userStore.name
        formData.value.avatar = userStore.avatar
        formData.value.age = userStore.age
        formData.value.gender = userStore.gender
        formData.value.intro = userStore.intro
        formData.value.contact = userStore.contact
        
    })

</script>

<template>

    <div class="edit-box" v-if="isOwn" >

        <div class="title">编辑资料</div>
        <div style="width: 100%; border:1px solid #999999"></div>

        <el-avatar 
            :size="120"
            :src='formData.avatar'
            style="margin-top: 10px;"
        />

        <el-button  
            color="#626aef" :icon="Camera" size:large plain 
            style="margin-top: 20px;"
            @click="dialogVisible = true;"
        >
            修改头像
        </el-button>

        <el-dialog
            title="修改头像"
            v-model="dialogVisible"
            center
        >

            <div class="change-avatar">
                <el-upload
                    class="avatar-uploader"
                    :limit="1"
                    accept=".jpg,.png,.jepg"
                    :show-file-list="false"
                    :before-upload="beforeUpload"
                    :http-request="uploadFile"
                >
                    <img v-if="imageUrl" :src="imageUrl" class="imageUrl" />
                    <el-icon v-else class="avatar-uploader-icon"><Plus /></el-icon>
                </el-upload>

                <div style="height: 100%; width: 1%; background-color: #999999;"></div>

                <div style="display: flex; flex-direction: column; align-items: center;">
                    <el-avatar 
                        :size="120"
                        :src='imageUrl'
                        style=""
                    />
                    <div style="margin-top: 20px;">头像预览</div>
                </div>


            </div>

            <div class="tips">请选择图片上传: 大小180 * 180像素支持JPG、PNG格式, 图片需小于2M</div>

        <template #footer>
            <el-button type="primary" @click="confirm">确认</el-button>
        </template>
        </el-dialog>


        <el-form
            ref="ruleFormRef"
            :model="formData"
            :rules="rules"
            status-icon
            label-position="right"
            label-width="auto"
            style="width: 75%; margin-top: 20px;"
        >
            <el-form-item label="呢称" prop="name">
                <el-input v-model="formData.username" />
            </el-form-item>

            <el-form-item label="年龄" prop="age">
                <el-input v-model.number="formData.age" />
            </el-form-item>

            <el-form-item label="性别" prop="gender">
                <el-radio-group v-model="formData.gender">
                    <el-radio :value=1>男</el-radio>
                    <el-radio :value=0>女</el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item label="联系方式" prop="contact_info">
                <el-input v-model="formData.contact" type="textarea"/>
            </el-form-item>

            <el-form-item label="个性签名" prop="intro">
                <el-input v-model="formData.intro" type="textarea"/>
            </el-form-item>
        </el-form>

        <el-button style="margin: 30px;" size="large" @click="submitForm(ruleFormRef)">保存</el-button>

    </div>

    <div class="edit-box" v-else>
        <div class="title">资料</div>
        <div style="width: 100%; border:1px solid #999999"></div>

        <el-avatar 
            :size="120"
            :src='formData.avatar'
            style="margin-top: 20px;"
        />

        <el-form
            :model="formData"
            status-icon
            label-position="right"
            label-width="auto"
            style="width: 75%; margin-top: 20px;"
            disabled="true"
        >
            <el-form-item label="呢称">
                <el-input v-model="formData.username" />
            </el-form-item>

            <el-form-item label="年龄">
                <el-input v-model.number="formData.age" />
            </el-form-item>

            <el-form-item label="性别">
                <el-radio-group v-model="formData.gender">
                    <el-radio :value=1>男</el-radio>
                    <el-radio :value=0>女</el-radio>
                </el-radio-group>
            </el-form-item>

            <el-form-item label="联系方式">
                <el-input v-model="formData.contact" type="textarea"/>
            </el-form-item>

            <el-form-item label="个性签名">
                <el-input v-model="formData.intro" type="textarea"/>
            </el-form-item>
        </el-form>       

    </div>



</template>
  
<style scoped>

    .edit-box {
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

    .change-avatar {
        display: flex; 
        align-items: center; 
        justify-content: space-between;

        width: 50%;
        height: 250px;
        margin: 25px auto;

    }

    .avatar-uploader {
        width: 180px;
        height: 180px;
        display: flex;
        align-items: center;
        justify-content: center;
        border: 1px dashed var(--el-border-color);
        border-radius: 6px;
        cursor: pointer;
        position: relative;
        overflow: hidden;
        transition: var(--el-transition-duration-fast);        
    }

    .avatar-uploader:hover {
        border-color: var(--el-color-primary);
    }

    .avatar-uploader-icon {
        font-size: 50px;
        color: #8c939d;
        width: 180px;
        height: 180px;
    }

    .tips {
        text-align: center;
        color: 179, 171, 200;
        margin-bottom: 25px;
    }

</style>