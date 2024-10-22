<script lang="ts" setup>
    import { onMounted, ref } from 'vue';
    import { DeleteFilled, Back, Refresh, DArrowRight } from '@element-plus/icons-vue';
    import { ElMessage } from 'element-plus'
    import api from '@/api/system';

    interface Folder {
        pathPreFix: string;
        name: string;
        children: Folder[] | null;
    }
    // 定义接口
    interface Resource {
        resourceId: number;
        resourceName: string;
        resourceUrl: string;
        resourcePath: string;
        resourceType: number;
        uploadTime: string;
        authorId: number;
    }


    const defaultProps = {
        children: 'children',
        label: 'name',
    }

    const filePathList = ref<string[]>([]);
    const routerData = ref<Folder[]>([]);
    const path = ref<any[]>([]);
    const paths = ref<any[]>([]);
    const routerlist = ref<Folder[]| null>([]);
    const filelist = ref<any[]>([]);
    const tableData = ref<any[]>([]);
    const hasFile = ref(false);

    function buildTree(paths: string[]): Folder[] {
        const root: Folder[] = [];
        const pathMap: { [key: string]: Folder } = {};

        paths.forEach(path => {
            const parts = path.split('/');
            let currentPath = '';
            for (let i = 0; i < parts.length - 1; i++) {
                const part = parts[i];
                currentPath += (currentPath ? '/' : '') + part;

                if (!pathMap[currentPath]) {
                    const newFolder: Folder = {
                        pathPreFix: currentPath + '/',
                        name: part,
                        children: null,
                    };
                    pathMap[currentPath] = newFolder;

                    if (i === 0) {
                        root.push(newFolder);
                    } else {
                        const parentPath = currentPath.substring(0, currentPath.lastIndexOf('/'));
                        if (pathMap[parentPath]) {
                            if (!pathMap[parentPath].children) {
                            pathMap[parentPath].children = [];
                            }
                            pathMap[parentPath].children.push(newFolder);
                        }
                    }
                }
            }
        });
        return root;
    }

    function setbread(data:any[], o:object[]){
        data.forEach((item:any) =>{
            o.push({pathPreFix:item.pathPreFix, name:item.name, children:item.children});
            paths.value.push({pathPreFix:item.pathPreFix, name:item.name, path:[...o]});
            if(item.children !== null){
                setbread(item.children,[...o]);
            }
            o.pop();
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
            tableData.value = routerData.value ? routerData.value.map((obj:any) => ({...obj, type: '文件夹'})) : [];
        }

    }

    function refresh() {
        const now = path.value[path.value.length - 1];
        setlist(now);
    }

    function toHome(){
        path.value = [];
        routerlist.value = routerData.value;
        tableData.value = routerData.value ? routerData.value.map((obj:any) => ({...obj, type: '文件夹'})) : [];
    }

    function toPath(item: any) {
        setlist(item);
    }

    function handleNodeClick(data: any) {
        setlist(data);
    }

    async function remove(row: any) {
        await api.delResourceApi({ filePath: row.pathUrl })
            .then((res: any) => {
                if (res.code == 200) {
                    ElMessage.success(row.name + '成功删除');
                    refresh();
                }
            })
    }

    function changeList(data: any) {
        //console.log(data);
        if (data.type === '文件夹') {
            setlist(data);
        }
    }

async function setlist(data: any) {
        // 确保 routerList.value 不是 null
        if (data.children) {
            routerlist.value = data.children.map((obj: Folder) => ({ ...obj, type: '文件夹' }));
        } else {
            routerlist.value = [];
        }       
        await api.getFileByPathApi({path:data.pathPreFix})
            .then((res: any) => {
                if(res.code == 200)
                {
                    if (res.data.length > 0) {
                        filelist.value = res.data.map((obj: any) => ({ ...obj, type: obj.name.split('.').pop() + '文件' }));
                        filelist.value.forEach((item: any) => {
                            item.date = new Date(item.date).toLocaleDateString();
                        })
                    }
                    else {
                        filelist.value = [];
                    }
                        
                }
            })
        if (routerlist.value == null)
            routerlist.value = [];
        tableData.value = [...routerlist.value, ...filelist.value];
        path.value = paths.value.find(item => item.pathPreFix === data.pathPreFix)?.path || [];
        hasFile.value = tableData.value.length > 0;
    }

    onMounted(async ()=>{
        await api.getFileListApi()
        .then((res:any)=>{
            if(res.code == 200)
            {
                const data = res.data as Resource[];
                filePathList.value = data.map(item => item.resourcePath);
                // 构建树形结构
                routerData.value = buildTree(filePathList.value);
                routerlist.value = routerData.value;
                tableData.value = routerData.value ? routerData.value.map((obj:any) => ({...obj, type: '文件夹'})) : [];
                setbread(routerData.value, []);
            }
        })
    })

</script>

<template>
    <div class="resource-head">
        <div class="resource-head-button">
            <el-button type="primary" :icon="Back" @click="back" />
            <el-button type="primary" :icon="Refresh" @click="refresh" />
        </div>

        <div class="resource-head-bread">
            <div class="head-breadcrumb-border">
                <div class="head-breadcrumb">
                    <el-scrollbar>
                        <div class="head-breadcrumb">
                            <el-button link @click="toHome" style="height: 5vh;">HOME</el-button>
                            <el-button v-for="(item, index) in path" link @click="toPath(item)">
                                <el-icon>
                                    <DArrowRight />
                                </el-icon>
                                {{ item.name }}
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
            <el-tree style="font-size: 100%;" :data="routerData" :props="defaultProps" @node-click="handleNodeClick" />
        </div>
        <div class="select-content">
            <div v-if="hasFile" style="width: 100%;">
                <el-table :data="tableData" style="width: 100%; height: 69.5vh; border-bottom-right-radius:1vh;"
                    @row-dblclick="changeList">
                    <el-table-column fixed prop="name" label="名称" style="width: 40%;" />
                    <el-table-column prop="type" label="类型" style="width: 30%;" />
                    <el-table-column prop="date" label="时间" style="width: 30%;" />
                    <el-table-column prop="author" label="作者" style="width: 30%;" />
                    <el-table-column fixed="right" label="操作" style="width: 30%;">
                        <template #default="scope">
                            <el-button v-if="scope.row.type != '文件夹'" type="primary" :icon="DeleteFilled" size="small"
                                @click="remove(scope.row)" />
                        </template>
                    </el-table-column>
                </el-table>
            </div>
            <div v-if="!hasFile" style="width: 100%; position: relative;">
                <el-empty description="暂无内容" style="min-height: 100%" />
            </div>
        </div>
    </div>
</template>

<style>
    .resource-head {
        display: flex;
        width: 100%;
        height: 7vh;
        border: 0.3vh solid;
        border-bottom: 0.15vh solid;
        border-top-left-radius: 1vh;
        border-top-right-radius: 1vh;
        border-color: gray;
        background-color: white;
    }

    .resource-head-button {
        width: 20%;
        height: 100%;
        display: flex;
        justify-content: center;
        /* 水平居中 */
        align-items: center;
        /* 垂直居中 */
    }

    .resource-head-bread {
        width: 60%;
        height: 100%;
        padding: 0.5vh;
        display: flex;
        justify-content: center;
        /* 水平居中 */
        align-items: center;
        /* 垂直居中 */
    }

    .head-breadcrumb-border {
        width: 100%;
        height: 100%;
        border: 1px solid;
        border-color: lightgray;
        border-radius: 5vh;
        padding: 0 2.5vh;
        display: flex;
        justify-content: center;
        /* 水平居中 */
        align-items: center;
        /* 垂直居中 */
    }

    .head-breadcrumb {
        width: 100%;
        height: 100%;
        display: flex;
        justify-content: left;
        /* 水平靠左 */
        align-items: center;
        /* 垂直居中 */
    }

    .resource-head-search {
        width: 20%;
        height: 100%;
        display: flex;
        justify-content: center;
        /* 水平居中 */
        align-items: center;
        /* 垂直居中 */
    }

    .resource {
        display: flex;
        width: 100%;
        height: 70vh;
        border: 0.3vh solid;
        border-top: 0.15vh solid;
        border-bottom-left-radius: 1vh;
        border-bottom-right-radius: 1vh;
        border-color: gray;
        background-color: white;
    }

    .selector {
        width: 20%;
        overflow-y: scroll;
        padding: 0.5vh 0.1vw;
        border-right: 0.1vw solid;
        border-bottom-left-radius: 1vh;
        border-color: lightgrey;
        background-color: white;
    }

    .select-content {
        width: 80%;
        display: flex;
        border-bottom-right-radius: 1vh;
        justify-content: center;
        /* 水平居中 */
        background-color: white;
    }
</style>
