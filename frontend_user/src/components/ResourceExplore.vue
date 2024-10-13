<script lang="ts" setup>
    import { onMounted, ref } from 'vue';
    import { useRouter } from 'vue-router'
    import { Download, Back, Refresh, DArrowRight } from '@element-plus/icons-vue';
    import { ElMessage, ElMessageBox } from 'element-plus'
    import api from '@/api/upload'

    // 路由
    const router = useRouter();

    const defaultProps = {
        children: 'children',
        label: 'name',
    }
    
    interface Folder {
        id: number;
        name: string;
        children: Folder[] | null;
    }

    //获得户信息
    const store = window.localStorage;

    const routerData= ref<Folder[]>([]);
    const routerCur= ref<number | null>();
    const path= ref<any[]>([]);
    const paths= ref<any[]>([]);
    const routerlist = ref<Folder[]| null>([]);
    const filelist = ref<any[]>([]);
    const list = ref<any[]>([]);
    const hasFile = ref(true);


     onMounted(async ()=>{
        api.pathApi()
        .then((res:any)=>{
            if(res.code == 1 && Array.isArray(res.path))
            {
                routerData.value = res.path;
                console.log(routerData.value)
                routerlist.value = routerData.value;
                list.value = routerData.value ? routerData.value.map((obj:any) => ({...obj, type: '文件夹'})) : [];
                setbread(routerData.value, []);
            }
            else
            {
                ElMessage.error('服务器接受失败，请稍后再试');
            }
        })
    })

    function setbread(data: Folder[], idPath: any[]){
        if (!data) return;
        data.forEach((item:any) =>{
            //console.log(item);
            idPath.push({id:item.id, name:item.name, children:item.children});
            //console.log([...idPath]);
            //console.log(idPath.length);
            paths.value.push({id:item.id, name:item.name, path:[...idPath]});
            //console.log(paths.value);

            //console.log(item.children)
            if(item.children !== null){
                setbread(item.children,[...idPath]);
            }
            idPath.pop();
        })
    }

    function back(){
        //console.log(path.value.length);
        if(path.value.length > 1){
            const father = path.value[path.value.length-2];
            setlist(father);
        }else{
            path.value = [];
            routerlist.value = routerData.value;
            list.value = routerData.value ? routerData.value.map((obj:any) => ({...obj, type: '文件夹'})) : [];
        }

    }

    function refresh(){
        const now = path.value[path.value.length-1];
        setlist(now);
    }

    function toHome(){
        path.value = [];
        routerlist.value = routerData.value;
        list.value = routerData.value ? routerData.value.map((obj:any) => ({...obj, type: '文件夹'})) : [];
    }

    function toPath(item:any){
        //console.log(item);
        setlist(item);
    }

    function handleNodeClick(data: any){
        //console.log(data);
        setlist(data);
    }

    async function download(row: any){
        console.log(row.name);
        console.log(path.value.map(item => item.name).join('/'));

        const fileurl = 'http://8.140.241.231:63000/api/frontend/download/files?name=' + row.name + '&path=' + path.value.map(item => item.name).join('/');
        //window.open(fileurl, '_blank');
        const link = document.createElement('a');
        link.href = fileurl;
        link.download = '1.pdf'; // 自定义下载文件名和扩展名
        document.body.appendChild(link);
        link.click();
        document.body.removeChild(link);        
        
    }

    function changeList(data: any){
        //console.log(data);
        if(data.type === '文件夹'){
            setlist(data);
        }
    }

    async function setlist(data: any){
        routerCur.value = data.id;
        // 确保 routerList.value 不是 null
        if (data.children) {
            routerlist.value = data.children.map((obj: Folder) => ({ ...obj, type: '文件夹' }));
        } else {
            routerlist.value = [];
        }       
        api.filedataApi({id:data.id})
        .then((res:any)=>{
            if(res.code == 1)
            {
                filelist.value = res.res.map((obj:any) => ({...obj, type: obj.name.split('.').pop() + '文件'}));
            }
            else
            {
                ElMessage.error('服务器接受失败，请稍后再试');
            }
        })
        list.value = [...(routerlist.value) || [], ...filelist.value];
        //console.log(routerlist.value);
        path.value = paths.value.find(item => item.id === data.id)?.path || [];
        //console.log(path.value);
        hasFile.value = list.value.length > 0;
    }

    function toUplode(){
        //console.log(store)
        if(store.isLogin === 'false'){
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

</script>

<template>
  <div class="resource-head">
      <div class="resource-head-button">
          <el-button type="primary" :icon="Back" @click="back"/>
          <el-button type="primary" :icon="Refresh" @click="refresh"/>
      </div>

      <div class="resource-head-bread">
          <div class="head-breadcrumb-border">
              <div class="head-breadcrumb">
                  <el-scrollbar >
                      <div class="head-breadcrumb">
                          <el-button link @click="toHome" style="height: 5vh;">HOME</el-button> 
                          <el-button v-for="(item, index) in path" link @click="toPath(item)">
                              <el-icon><DArrowRight /></el-icon>
                              {{item.name}}
                          </el-button>
                      </div>
                  </el-scrollbar>
              </div>
          </div>
      </div>

      <div class="resource-head-search">
          
      </div>
    </div>

  <div class="resource">
      <div class="selector">
          <el-tree
          style="font-size: 100%;"
          :data="routerData"
          :props="defaultProps"
          @node-click="handleNodeClick"
          />
      </div>
      <div class="select-content">
          <div v-if="hasFile" style="width: 100%;">
              <el-table :data="list" 
                      style="width: 100%; height: 70vh; border-bottom-right-radius:1vh; border-bottom:0.3vh solid; border-color: gray;"
                      @row-dblclick="changeList"
              >
                  <el-table-column fixed prop="name" label="名称" style="width: 40%;" />
                  <el-table-column prop="type" label="类型" style="width: 30%;" />
                  <el-table-column prop="date" label="时间" style="width: 30%;" />
                  <el-table-column prop="author" label="作者" style="width: 30%;" />
                  <el-table-column fixed="right" label="操作" style="width: 30%;">
                      <template #default="scope" >
                          <el-button v-if="scope.row.type != '文件夹'" type="primary" :icon="Download" size="small" @click="download(scope.row)" />
                      </template>
                  </el-table-column>
              </el-table>
          </div>
          <div v-if="!hasFile" style="width: 100%; position: relative;">
              <img
                  src="@/assets/images/filenull.png"
                  style="width: 100%; height: 100%; border-bottom-right-radius: 1vh;"
              />
              <el-button
                  type="primary"
                  link
                  style="position: absolute; top: 95%; left: 80%;"
                  @click="toUplode"
              >
                  投稿中心
              </el-button>
          </div>
      </div>
  </div>
</template>

<style>
    .resource-head{
        display: flex;
        width: 100%;
        height: 7vh;
        border-bottom: 0.15vh solid;
        border-top-left-radius:1vh;
        border-top-right-radius:1vh;
        border-color: gray;
        background-color: white;
    }

    .resource-head-button{
        width: 20%;
        height: 100%;
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center; /* 垂直居中 */
    }

    .resource-head-bread{
        width: 60%;
        height: 100%;
        padding: 0.5vh;
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center; /* 垂直居中 */
    }
    .head-breadcrumb-border{
        width: 100%;
        height: 100%;
        border: 1px solid;
        border-color: lightgray;
        border-radius: 5vh;
        padding: 0 2.5vh;
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center; /* 垂直居中 */
    }

    .head-breadcrumb{
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: left; /* 水平靠左 */
        align-items: center; /* 垂直居中 */
    }

    .resource-head-search{
        width: 20%;
        height: 100%;
        display: flex;
        justify-content: center; /* 水平居中 */
        align-items: center; /* 垂直居中 */
    }

    .resource {
        display: flex;
        width: 100%;
        height: 70vh;
        border-top: 0.15vh solid;
        border-bottom-left-radius:1vh;
        border-bottom-right-radius:1vh;
        border-color: gray;
        background-color: white;
    }

    .selector {
        width: 20%;
        overflow-y: scroll;
        padding: 0.5vh 0.1vw;
        border-right:0.1vw solid;
        border-bottom-left-radius:1vh;
        border-color: lightgrey;
        background-color: white;
    }

    .select-content {
        width: 80%;
        display: flex;
        border-bottom-right-radius:1vh;
        justify-content: center; /* 水平居中 */
        background-color: white;
    }
</style>