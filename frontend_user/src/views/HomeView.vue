<script setup lang="ts">
    import { ref, onMounted, onUnmounted } from 'vue'
    import { useRouter } from 'vue-router'
    import Header from '@/components/NavBar.vue'
    import Footer from '@/components/BaseFooter.vue'
    import Nav from '@/components/BaseHeader.vue'
import { ElMessage } from 'element-plus'

    // 本地数据存储
    const store = window.localStorage;

    if (!('isLogin' in store)) {
        store.setItem('isLogin', 'false');
    }
    if (!('userId' in store)) {
        store.setItem('userId', '0');
    }
    if (!('userAvatar' in store)) {
        store.setItem('userAvatar', 'https://small-event-bucket1.oss-cn-hangzhou.aliyuncs.com/avatarDefault.jpg');
    }

    // 路由
    const router = useRouter();

    // 路由跳转
    function redirect(url: string) {
        router.push(url);
    }

    // 防抖函数类型定义
    type Debouncer<T extends (...args: any[]) => void> = {
        (...args: Parameters<T>): void;
        flush: () => void;
    };

    // 创建防抖函数
    function createDebouncer<T extends (...args: any[]) => void>(func: T, wait: number): Debouncer<T> {
        let timeout: ReturnType<typeof setTimeout> | null = null;
        const debounced: Debouncer<T> = (...args) => {
            if (timeout) {
                clearTimeout(timeout);
            }
            timeout = setTimeout(() => {
                func(...args);
                timeout = null;
            }, wait);
        };
        debounced.flush = () => {
            if (timeout) {
                clearTimeout(timeout);
                func();
            }
        };
        return debounced;
    }

    // 滚动显示导航栏参数
    const scrollTop = ref(0);
    const navVisible = ref(false);
    const topDistance = 100; //当滚动到100px时显示

    // 滚动逻辑
    const onScroll = () => {
        scrollTop.value = window.scrollY || document.documentElement.scrollTop || document.body.scrollTop;
        if (scrollTop.value > topDistance) {
            navVisible.value = true;
        }
        else {
            navVisible.value = false;
        }
    }

    const debouncedScroll = createDebouncer(onScroll, 100);

    onMounted(() => {
        window.addEventListener('scroll', debouncedScroll);
        ElMessage.success('欢迎来到共享学园！');
    });

    onUnmounted(() => {
        window.removeEventListener('scroll', debouncedScroll);
    })
</script>

<template>
    <div class="container">
        <el-backtop target=".container" :right="100" :bottom="80" :visibility-height="0" />
        <el-affix :offset="0">
            <Nav class="nav" v-show="navVisible"></Nav>
        </el-affix>
        <Header></Header>
        <div class="main">
            <div class="top-bar" id="module-nav-bar">
                <el-button class="btn-route" type="primary" round
                    @click="redirect('/home/recommend-part')">推荐</el-button>
                <el-button class="btn-route" type="primary" round
                    @click="redirect('/home/friend-part')">知趣情缘</el-button>
                <el-button class="btn-route" type="primary" round
                    @click="redirect('/home/resource-part')">资源中心</el-button>
            </div>
            <div class="center">
                <el-divider style="width: 80%; border-top: 3px solid gray" />
            </div>
            <RouterView />
        </div>
        <Footer></Footer>
    </div>
</template>

<style scoped>
    .nav {
        position: fixed;
        top: 0;
    }

    .grid {
        display: grid;
        place-items: center;
    }

    .center {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .top-bar {
        display: flex;
        justify-content: center;
        align-items: center;
        gap: 2vw;
    }

    .container {
        width: 100%;
        height: 100%;
        margin: 0 auto;
    }

    .main {
        padding: 30px 30px;
        width: 100%;
        background-color: white;
    }

    .btn-route {
        width: 15vw;
        height: 8vh;
        font-size: 4vb;
        background-image: none;
        transition: background-color 0.5s ease, transform 0.3s ease;
    }

    .btn-route:hover {
        background-color: #79bbff;
        transform: scale(1.1);
    }
</style>

