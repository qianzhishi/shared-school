import { defineStore } from 'pinia';
import { ref } from 'vue';
import api from '@/api/user'
import { ElMessage } from 'element-plus';

export const useUserStore = defineStore('user', () => {
    const userId = ref(0);
    const name = ref('');
    const gender = ref(0);
    const age = ref(0);
    const avatar = ref('');
    const intro = ref('');
    const contact = ref('');
    const likeNum = ref(0);
    const focusNum = ref(0);
    const fansNum = ref(0);
    const showPosts = ref(0);
    const showComments = ref(0);
    const showLikes = ref(0);
    const showFans = ref(0);
    

    // 定义获取用户信息的方法
    const fetchUserInfo = async (id: number) => { 
        await api.infoApi({id:id})
        .then((res:any) => {
            if(res.code == 200)
            {
                userId.value = res.data.userId
                name.value = res.data.username
                gender.value = res.data.gender
                age.value = res.data.age
                avatar.value = res.data.avatar
                intro.value = res.data.intro
                contact.value = res.data.contact
                likeNum.value = res.data.likeNum
                focusNum.value = res.data.focusNum
                fansNum.value = res.data.fansNum
                showPosts.value = res.data.showPosts
                showComments.value = res.data.showComments
                showLikes.value = res.data.showLikes
                showFans.value = res.data.showFans
            }
            else {
            let msg = res.msg
            ElMessage.error(msg);
            }
        })
    };

    // 定义登出方法
    const logout = () => {
        userId.value = 0
        name.value = ''
        gender.value = 0
        age.value = 0
        avatar.value = 'https://small-event-bucket1.oss-cn-hangzhou.aliyuncs.com/avatarDefault.jpg'
        intro.value = ''
        contact.value = ''
        likeNum.value = 0
        focusNum.value = 0
        fansNum.value = 0
        showPosts.value = 0
        showComments.value = 0
        showLikes.value = 0
        showFans.value = 0
    };

    // 返回所有需要暴露给组件使用的属性和方法
    return {
        userId,
        name,
        gender,
        age,
        avatar,
        intro,
        contact,
        likeNum,
        focusNum,
        fansNum,
        showPosts,
        showComments,
        showLikes,
        showFans,
        fetchUserInfo,
        logout,
    }
});