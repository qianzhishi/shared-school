<script lang="ts" setup>
    import { ElMessage } from 'element-plus'
    import { onMounted,reactive,ref } from 'vue'
    import api from '@/api/user'
    import router from '@/router';

    // 本地数据存储
    const store = window.localStorage
    // 当前登录用户的id
    const userId = Number(store.getItem('userId'))

    // 保存被访问用户的个人空间的权限数据
    const privilegesData = reactive({
        id: userId,
        post: true,
        comment: true,
        subs: true,
        fans: true
    })

    const dialogFormVisible = ref(false)

    const formLabelWidth = '140px'

    const form = reactive({
            oldPassword: '',
            password: '',
            confirmPassword: '',
        })

    // 获取用户隐私设置
    const getSettings = ()=> {
        api.settingsApi({id: userId})
        .then((res: any) => {
            privilegesData.post = res.post ? true : false
            privilegesData.comment = res.comment ? true : false
            privilegesData.subs = res.subs ? true : false
            privilegesData.fans = res.fans ? true : false
        })
    }

    // 更新用户隐私设置
    const updatedSettings = ()=> {
        api.setApi({
            id: userId,
            post: privilegesData.post ? 1 : 0,
            comment: privilegesData.comment ? 1 : 0,
            subs: privilegesData.subs ? 1 : 0,
            fans: privilegesData.fans ? 1 : 0,
        })
        .then((res: any) => {
            if(res.code == 1)
                ElMessage.success('更改成功')
        })
    }

    // 修改用户密码
    const editPwd = (oldValue:string, newValue:string)=> {
        api.editPwdApi({
            id: userId,
            oldpwd: oldValue,
            newpwd: newValue
        })
        .then((res: any) => {
            if(res.code == 1)
            {
                ElMessage.success('更改成功')
                store.setItem('isLogin', 'false')
                router.push('/login')                
            }
        })
        .catch((error: any) => {
            console.log(error)
        })
    }

    function setPassword() {
        if(form.password.length < 8 || form.password.length > 16 )
        {
            ElMessage.error('错误, 密码需8~16位! ')
        }
        else if(form.password == form.confirmPassword) 
        {
            dialogFormVisible.value = false
            editPwd(form.oldPassword,form.password)
        }
        else 
        {
            ElMessage.error('错误，两次输入密码不一致！')
        }
    }

    onMounted(()=>{
        getSettings()
    })

</script>

<template>
    <div class="title">安全设置</div>
    <div style="width: 100%; border:1px solid #999999"></div>
    
    <div class="box">
        <span class="text">在个人中心展示我发布的帖子</span>
        <el-switch
            v-model="privilegesData.post"
            size="large"
            inline-prompt
            active-text="是"
            inactive-text="否"
        />
    </div>

    <div class="box">
        <span class="text">在个人中心展示我的评论</span>
        <el-switch
            v-model="privilegesData.comment"
            size="large"
            inline-prompt
            active-text="是"
            inactive-text="否"
        />
    </div>

    <div class="box">
        <span class="text">在个人中心展示我的关注</span>
        <el-switch
            v-model="privilegesData.subs"
            size="large"
            inline-prompt
            active-text="是"
            inactive-text="否"
        />
    </div>

    <div class="box">
        <span class="text">在个人中心展示我的粉丝</span>
        <el-switch
            v-model="privilegesData.fans"
            size="large"
            inline-prompt
            active-text="是"
            inactive-text="否"
        />
    </div>

    <el-button 
        color="#626aef" plain round
        style="margin-top: 40px; font-size: 20px;"
        @click="updatedSettings()"
    >
        保存设置
    </el-button>
    
    <el-button 
        color="#626aef" plain round
        style="margin: 40px auto; font-size: 20px;"
        @click="dialogFormVisible = true"
    >
        修改密码
    </el-button>

    <el-dialog v-model="dialogFormVisible" title="修改密码" width="500">
            <el-form :model="form">
                <el-form-item label="旧密码" :label-width="formLabelWidth">
                    <el-input 
                    v-model.lazy="form.oldPassword" 
                    placeholder="Please input old password"
                    show-password/>
                </el-form-item>
                <el-form-item label="密码" :label-width="formLabelWidth">
                    <el-input 
                    v-model.lazy="form.password" 
                    placeholder="Please input password"
                    show-password/>
                </el-form-item>
                <el-form-item label="确认密码" :label-width="formLabelWidth">
                    <el-input 
                    v-model.lazy="form.confirmPassword" 
                    placeholder="Please confirm password"
                    show-password/>
                </el-form-item>
            </el-form>
            <template #footer>
                <div class="dialog-footer">
                    <el-button @click="dialogFormVisible = false">取消</el-button>
                    <el-button type="primary" @click="setPassword">
                        确认
                    </el-button>
                </div>
            </template>
        </el-dialog>


</template>
  
<style scoped>

    .title {
        width: 100%;
        margin-top: 10px;
        margin-bottom: 10px;
        text-align: center;
        font-size: 25px;
    }

    .box {
        display: flex;
        align-items: center;
        justify-content: space-between;
        width: 80%;
        height: 50px;
        margin-top: 25px;
    }

    .text {
        font-size: 20px;
    }


</style>