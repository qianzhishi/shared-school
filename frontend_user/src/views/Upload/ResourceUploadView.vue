<script lang="ts" setup>
  import { ref, onMounted } from 'vue';
    import { ElMessage, ElMessageBox } from 'element-plus';
  import { UploadFilled, DocumentAdd } from '@element-plus/icons-vue'
  import { useRouter } from 'vue-router';
  import api from '@/api/upload'

    // 路由
    const router = useRouter();

    interface Folder {
        pathPreFix: string;
        label: string;
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

    const filePathList = ref<string[]>([]);

    //获得用户信息
    const store = window.localStorage;

    const routerData= ref<Folder[]>();

    //文件路径前缀（相当于所在的文件目录）
    const selectPath= ref('');
    const fullPath = ref('');

    const fileList = ref<any[]>([]);
    const files = ref<any[]>([]);

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
                        label: part,
                        children: null
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

  onMounted(async ()=>{
      api.getFileListApi()
      .then((res:any)=>{
          if(res.code == 200)
            {
                const data = res.data as Resource[];
                filePathList.value = data.map(item => item.resourcePath);
                // 构建树形结构
                routerData.value = buildTree(filePathList.value);
              
            }
      })
  })


    async function handleNodeClick(data: any) {
        selectPath.value = data.pathPreFix // 更新路径前缀
        fullPath.value = data.pathPreFix
    }

    function uploadFile(file: any) {
        files.value.push(file.file)
    }

    function handleRemove() {
        files.value.pop()
    }

    async function submitUpload(){
        const names = files.value.map(item => item.name);
        console.log(names);
        if (fullPath.value === '') {
            ElMessage.warning('未选择路径');
        }else{                        
          const isfail = ref(false);
          const failFiles = ref<any[]>([])
          ElMessageBox({
              title: '是否发布',
              message:'确认',
              confirmButtonText: '发布',
              cancelButtonText: '取消',
              type: 'info',
              beforeClose: async (action, instance, done) => {
                  if (action === 'confirm') {
                      instance.confirmButtonLoading = true
                      instance.confirmButtonText = 'Loading...'
                      files.value.forEach(async (file) => {
                          const formData = new FormData();
                          formData.append('file', file);
                          formData.append('authorId', store.userId);
                          formData.append('filePathPreFix',fullPath.value);
                          api.uploadFileApi(formData)
                          .then((res:any)=>{
                              if(res.code !== 200){
                                isfail.value = true;
                                failFiles.value.push(file);
                              }
                          })
                      });
                      instance.confirmButtonLoading = false
                      done(); // 关闭对话框
                  } else {
                      done()
                  }
              },
          })
          .then(()=>{
              if (isfail.value === false) {
                  ElMessage.success('上传成功');
                  router.push('/home');
              }else{
                  files.value = failFiles.value;
              }
          })
          .catch(() => {
              ElMessage({
                  type: 'info',
                  message: '取消发布',
              })
          });
      }
  }
</script>

<template>
  <div class="container">
      <div style="font-weight: bold; width: 80%;">
          请选择资源发送路径: 
          <el-tree-select
              v-model="selectPath"
              :data="routerData"
              check-strictly
              :render-after-expand="false"
              @node-click="handleNodeClick"
          />
      </div>

      <el-divider />

      <div style="font-weight: bold; width: 80%;">
          当前资源发送路径: 
          <el-input
              v-model="fullPath"
              disabled
              placeholder="请选择资源发送路径"
          />
      </div>

      <el-divider />

      <el-upload
          v-model:file-list="fileList"
          class="upload-demo"
          :on-remove="handleRemove"
          :http-request="uploadFile"
      >
        <template #trigger>
            <el-button type="primary" size="large">
                选择文件
            <el-icon class="el-icon--right"><DocumentAdd /></el-icon></el-button>
        </template>

        <el-button class="ml-4" type="primary" size="large" @click="submitUpload">
            上传<el-icon class="el-icon--right"><UploadFilled /></el-icon>
        </el-button>

        <template #tip>
            <div class="el-upload__tip" style="margin-left: 40px;">
                上传文件大小需不大于100MB
            </div> 
        </template>
      </el-upload>    
  </div>
</template>

<style scoped>
  .container {
      width: 90%;
      display:flex;
      justify-content: center; /* 水平居中 */
      align-items: center; /* 垂直居中 */
      background-color: white;
      min-height: 80vh;
      flex-direction: column; /* 如果内容不止一行，确保垂直排列 */

  }

  .ml-4 {
      margin-left: 40px;
  }
  

</style>
