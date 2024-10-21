<script lang="ts" setup>
    import Header from '@/components/BaseHeader.vue'
    import Footer from '@/components/BaseFooter.vue'
    import { ref, onMounted } from 'vue'
    import Card from '@/components/BaseCard.vue'
    import { ElMessage, ElMessageBox } from 'element-plus'
    import { Search } from '@element-plus/icons-vue'
    import type { cardInfo } from '@/types/types'
    import { useRouter, useRoute } from 'vue-router';
    import api from '@/api/content'

    // 卡片列表
    const cardList = ref<Array<cardInfo>>([]);
    const cardCount = ref(0);

    // 是否正在加载
    const loading = ref(true);

    // 当前页数
    const currentPage = ref(1);

    // 处理页面变换
    const handleCurrentChange = (newPage: number) => {
        currentPage.value = newPage;
        getCardInfo();
    };

    // 路由
    const router = useRouter();
    const route = useRoute();

    // 搜索框内容
    const input = ref('');
    const type = ref();
    const isFruit = ref(false);

    // 获取卡片信息
    const getCardInfo = async()=> {
        loading.value = true;
        await api.searchApi({
            userId: Number(localStorage.getItem('userId')),
            keyword: input.value,
            type: type.value,
            size: 10,
            page: currentPage.value,
        })
        .then((res: any) => {
            if (res.code == 200) {
                
                cardCount.value = res.data.total;
                cardList.value = res.data.list;
                if(cardList.value.length !== 0){
                    isFruit.value = true;
                }
                else{
                    isFruit.value = false;
                }
                loading.value = false;
                ElMessage.success("搜索成功");
            }

        })
    }

    function search() {
        if (input.value !== '' && typeof type.value !== 'undefined') {
            router.push({ path: '/search', query: { input: input.value, type: type.value } })
                .then(() => {
                    // 重新刷新页面
                    location.reload()
                });
        }
    }

    function toUplode(){
        if(localStorage.isLogin === 'false'){
            ElMessageBox.confirm(
                '没登录不能投稿哦，快去登录吧',
                'Warning',
                {
                    confirmButtonText: '登录',
                    cancelButtonText: '取消',
                    type: 'warning',
                }
            )
            .then(() => {
                router.push('/login');
            })
            .catch(() => {
                ElMessage({
                    type: 'warning',
                    message: '记得登录哦亲~~~',
                })
            })
        }else{
           router.push('/upload'); 
        }
        
    }

    onMounted(() => {
        if (route.query.input !== null) {
            input.value = route.query.input.toString();
            type.value = route.query.type;
        }
        loading.value = true;
        getCardInfo();
    })
</script>

<template>
    <div class="contain">
        <div class="header">
            <Header></Header>
        </div>
        <div class="content" style="margin-bottom: 5vh;">
            <div class="center gap" style="margin-top: 5vh;">
                <el-input style="width: 30vw" :prefix-icon="Search" v-model="input"
                    v-on:keyup.enter="search"
                    placeholder="来问问我们吧，我们这什么都有"></el-input>
                <el-button style="width: 6vw" type="primary" @click="search" round>搜索</el-button>
                <el-select v-model="type" placeholder="类型" style="width: 6vw;">
                    <el-option label="全局" value="0" />
                    <el-option label="学习" value="1" />
                    <el-option label="娱乐" value="2" />
                    <el-option label="表白" value="3" />
                </el-select>
            </div>
            <div style="width: 100%;">
                <div class="container" v-loading="loading" v-if="isFruit">
                    <div v-for="card in cardList" :key="card.cardId">
                        <Card :info="card">
                            <!-- 图标 -->
                            <template #icon v-if="card.type === 1">
                                <svg t="1716613194352" class="icon" viewBox="0 0 1036 1024" version="1.1"
                                    xmlns="http://www.w3.org/2000/svg" p-id="10877" width="200" height="200">
                                    <path
                                        d="M1008 329.6c-153.6-67.2-307.2-131.2-460.8-198.4-16-6.4-32-6.4-48 0-150.4 67.2-304 131.2-454.4 195.2-16 6.4-32 12.8-32 35.2 0 22.4 16 28.8 32 35.2 140.8 60.8 281.6 118.4 422.4 182.4 38.4 19.2 73.6 22.4 112 0 19.2-12.8 41.6-19.2 64-28.8 89.6-38.4 176-76.8 268.8-115.2v323.2c0 19.2 6.4 25.6 25.6 25.6 19.2 0 25.6-3.2 25.6-25.6 0-96 3.2-195.2-3.2-291.2 0-38.4 6.4-64 48-73.6 16-3.2 28.8-12.8 28.8-32s-12.8-25.6-28.8-32z"
                                        fill="#ffffff" p-id="10878"></path>
                                    <path
                                        d="M848 547.2c0-22.4-6.4-22.4-22.4-16-86.4 38.4-172.8 73.6-256 112-32 12.8-57.6 12.8-89.6 0-86.4-38.4-172.8-73.6-259.2-112-16-6.4-19.2-3.2-19.2 12.8v128c-9.6 86.4 35.2 144 108.8 176 147.2 67.2 294.4 64 438.4-6.4 67.2-35.2 105.6-86.4 99.2-169.6-3.2-41.6 0-83.2 0-124.8z"
                                        fill="#ffffff" p-id="10879"></path>
                                </svg>
                            </template>
                            <template #icon v-else-if="card.type === 2">
                                <svg t="1716613598815" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                    xmlns="http://www.w3.org/2000/svg" p-id="13597" width="200" height="200">
                                    <path
                                        d="M947.36 582.21l-64.55-212.48c-12.59-41.42-42.34-75.88-83.79-97.03-40.67-20.75-87.95-26.23-133.16-15.46-24.31 5.79-46.45 15.91-65.93 30.12H426.07c-19.49-14.21-41.63-24.32-65.93-30.12-45.2-10.78-92.49-5.29-133.15 15.46-41.45 21.14-71.21 55.6-83.79 97.03L78.64 582.21c-11.84 38.96-7.03 80.14 13.53 115.97 23 40.09 63.96 69.7 112.38 81.25 14.95 3.56 29.34 5.31 43.09 5.31 67.27 0 118.88-41.88 141.95-117.8l0.13-0.43h246.83l0.13 0.43c12.97 42.69 35.55 75.75 65.29 95.61 32.75 21.87 74.07 27.7 119.49 16.88 48.42-11.54 89.38-41.15 112.38-81.25 20.55-35.83 25.36-77.01 13.52-115.97zM324 564.29c-61.69 0-111.87-50.19-111.87-111.87 0-61.69 50.19-111.87 111.87-111.87s111.87 50.19 111.87 111.87c0 61.68-50.19 111.87-111.87 111.87z m344.61-39.96c-19.86 0-35.96-16.1-35.96-35.96s16.1-35.96 35.96-35.96 35.96 16.1 35.96 35.96-16.1 35.96-35.96 35.96z m17.98-131.85c0-19.86 16.1-35.96 35.96-35.96s35.96 16.1 35.96 35.96-16.1 35.96-35.96 35.96-35.96-16.1-35.96-35.96z m89.89 131.85c-19.86 0-35.96-16.1-35.96-35.96s16.1-35.96 35.96-35.96 35.96 16.1 35.96 35.96-16.1 35.96-35.96 35.96z"
                                        fill="#ffffff" p-id="13598"></path>
                                    <path
                                        d="M324 452.41m-55.94 0a55.94 55.94 0 1 0 111.88 0 55.94 55.94 0 1 0-111.88 0Z"
                                        fill="#ffffff" p-id="13599"></path>
                                    <path
                                        d="M324 452.41m-55.94 0a55.94 55.94 0 1 0 111.88 0 55.94 55.94 0 1 0-111.88 0Z"
                                        fill="#ffffff" p-id="13600"></path>
                                </svg>
                            </template>
                            <template #icon v-else-if="card.type === 3">
                                <svg t="1716613736578" class="icon" viewBox="0 0 1024 1024" version="1.1"
                                    xmlns="http://www.w3.org/2000/svg" p-id="20549" width="200" height="200">
                                    <path
                                        d="M244.506122 67.918367c-59.559184 9.195102-105.116735 32.182857-146.285714 73.978776C20.48 220.26449-13.583673 335.830204 6.269388 452.440816c2.298776 13.792653 5.433469 29.257143 7.105306 34.481633l2.716735 9.404082 86.517551 0.417959 86.517551 0.417959 37.616326-41.586939c45.557551-50.573061 49.110204-53.289796 68.963265-53.289796 12.956735 0 15.673469 0.626939 24.032654 6.687347 8.150204 5.642449 12.956735 12.956735 36.989387 55.379592 15.25551 26.958367 28.839184 50.573061 30.302041 52.453878 2.298776 3.134694 7.941224-7.523265 40.333061-77.11347 20.68898-44.512653 39.706122-84.009796 42.422857-87.771428 6.896327-10.44898 20.48-17.554286 33.227756-17.554286 14.001633 0 23.823673 4.388571 32.391836 14.419592 4.179592 5.01551 24.241633 44.094694 49.737143 97.17551 23.405714 48.901224 43.049796 89.025306 43.885714 89.025306 0.835918 0 7.523265-12.956735 15.046531-28.839184 15.25551-32.182857 21.733878-41.16898 34.063674-46.602449 7.941224-3.761633 17.763265-3.970612 173.87102-3.970612H1017.730612l1.044898-4.80653c8.150204-35.73551 6.269388-92.786939-4.388571-141.479184-21.942857-99.683265-76.69551-175.751837-155.062857-216.084898-49.528163-25.286531-116.401633-33.854694-183.275102-23.196735-55.170612 8.777143-100.310204 31.346939-146.703674 73.560817l-17.136326 15.673469-15.255511-14.419592c-44.303673-41.586939-89.443265-64.783673-146.494693-75.023673-25.49551-4.597551-81.92-5.433469-105.952654-1.880817z"
                                        p-id="20550" fill="#ffffff"></path>
                                    <path
                                        d="M502.595918 480.444082c-2.716735 8.568163-72.306939 156.107755-75.859591 161.123265-9.822041 13.374694-34.272653 19.017143-50.782041 11.911837-11.911837-5.01551-18.808163-14.837551-53.080817-75.650613-18.181224-32.182857-33.645714-59.141224-34.481632-60.186122-0.626939-0.835918-13.374694 11.702857-28.421225 28.003265-18.181224 20.062041-29.675102 30.92898-35.317551 33.436735-7.523265 3.552653-15.46449 3.970612-89.443265 3.970612H54.125714l8.777143 12.956735c14.419592 21.524898 32.809796 45.348571 51.2 66.45551 21.106939 24.450612 74.187755 76.69551 102.191021 100.519184 47.438367 40.75102 212.114286 167.183673 238.654693 183.484081 25.077551 15.46449 48.065306 20.48 73.142858 16.091429 22.360816-3.970612 33.227755-9.613061 72.724898-38.243265C732.682449 828.604082 798.093061 775.314286 863.085714 710.530612c59.977143-59.768163 95.294694-105.534694 120.999184-156.316734l6.478367-12.956735-134.164898 0.417959-133.955918 0.626939-27.794286 58.514286c-15.25551 32.182857-30.51102 62.066939-34.272653 66.24653-7.732245 9.404082-24.450612 15.25551-36.989388 12.956735-11.493878-1.880816-24.659592-10.03102-29.466122-18.181225-2.298776-3.970612-19.017143-38.034286-36.989388-75.859591-18.181224-37.825306-37.616327-77.949388-43.049796-89.443266-7.732245-16.300408-10.03102-19.644082-11.284898-16.091428z"
                                        p-id="20551" fill="#ffffff"></path>
                                </svg>
                            </template>
                        </Card>
                    </div>
                </div>
                <div class="center" v-if="!isFruit">
                    <el-empty
                        image="src/assets/images/upload_begin.png"
                        description="暂时没有数据哦 快点去上传吧！"
                    >
                        <el-button type="primary" @click="toUplode">上传</el-button>
                    </el-empty>
                </div>
                <div class="center">
                    <el-pagination background layout="prev, pager, next" :total="cardCount" hide-on-single-page
                        @current-change="handleCurrentChange" />
                </div>
            </div>
        </div>
        <div class="footer">
            <Footer></Footer>
        </div>
    </div>
</template>

<style scoped>
    .contain {
        color: black;
    }

    .header {
        min-height: fit-content;
    }

    .center {
        display: flex;
        justify-content: center;
        align-items: center;
    }

    .gap {
        display: flex;
        gap: 1vw;
    }

    .content {
        display: flex;
        flex-direction: column;
        justify-content: center;
        align-items: center;
        min-height: 60vh;
        background: white;
    }

    .footer {
        min-height: 30vh;
    }

    .container {
        display: grid;
        grid-template-columns: repeat(2, 50%);
        place-items: center;
        min-height: 60vh;
        height: fit-content;
        margin: 5vh 0;
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

    .icon {
        width: 1.5vw;
        height: 1.5vw;
    }

</style>
