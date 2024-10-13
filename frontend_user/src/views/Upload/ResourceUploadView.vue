<script lang="ts" setup>
  import { ref, onMounted } from 'vue';
  import type { UploadUserFile } from 'element-plus';
  import { ElMessage, ElMessageBox } from 'element-plus';
  import { useRouter } from 'vue-router';
  import api from '@/api/upload'

  // 路由
  const router = useRouter();

  const defaultProps = {
      value: 'id',
      children: 'children',
      label: 'name',
  }

  //获得用户信息
  const store = window.localStorage;

  const routerData= ref();
  const routerID = ref();
  const fullPath = ref('');

  const path= ref<any[]>([]);
  const paths= ref<any[]>([]);

  const Filelist = ref<any[]>([]);
  const fileList = ref<UploadUserFile[]>([]);
  const files = ref<any[]>([]);

  onMounted(async ()=>{
      api.pathApi()
      .then((res:any)=>{
          if(res.code == 1)
          {
              routerData.value = res.path;
              setbread(routerData.value, []);
          }
          else
          {
            ElMessage.error('服务器接受失败，请稍后再试');
          }
      })
  })

  function setbread(date:any[], idPath:object[]){
      date.forEach((item:any) =>{
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

  async function onCheckChange (node:any){
      path.value = paths.value.find(item => item.id === node).path;
      //console.log(path.value);
      fullPath.value = path.value.map(item => item.name).join('/');

      api.filedataApi({id:node})
      .then((res:any)=>{
          if(res.code == 1)
          {
              Filelist.value = res.res;
          }
          else
          {
            ElMessage.error('服务器接受失败，请稍后再试');
          }
      })
      //console.log(Filelist.value);
  }

  function uploadFile(file:any){
      console.log(file.file);
      //console.log(fileList.value);
      // console.log(Filelist.value.some(item => item.name !== file.file.name));
      if(files.value.length !== 0 || Filelist.value.some(item => item.name === file.file.name)){
          // 检查是否有重复文件，有的话删除新选择的文件     
          if(files.value.some(item => item.name === file.file.name) || Filelist.value.some(item => item.name === file.file.name)){
              ElMessage.error(file.file.name + "文件已存在");
              fileList.value.pop();
          }else{
              files.value.push(file.file);
          }
      }else{
          files.value.push(file.file);
      }
  }

  async function submitUpload(){
      const names = files.value.map(item => item.name);
      console.log(names);
      if(fullPath.value === ''){
          ElMessage({
              type: 'error',
              message: '未选择路径',
          })
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
                      //console.log(fileList.value);
                      //console.log(files.value);
                      files.value.forEach(async (file) => {
                          const formData = new FormData();
                          formData.append('res', file);
                          formData.append('id', store.userId);
                          formData.append('path',fullPath.value);
                          //console.log(formData.get("res"));
                          api.resourceApi(formData)
                          .then((res:any)=>{
                              if(res.code == 1)
                              {
                                ElMessage.success(file.name + '发送成功');
                              }
                              else
                              {
                                ElMessage.error('服务器接受失败，请稍后再试');
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
              if(isfail.value === false){
                  router.push('/upload/success');
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
          资源发送路径: 
          <el-input
              v-model="fullPath"
              disabled
              placeholder="请选择资源发送路径"
          />
          <el-tree-select
              v-model="routerID"
              placeholder=" 请选择资源发送路径"
              :data="routerData"
              :props="defaultProps"
              check-strictly
              :render-after-expand="false"
              @change="onCheckChange"
          />
      </div>

      <el-divider />

      <el-upload
          v-model:file-list="fileList"
          class="upload-demo"
          action=""
          :http-request="uploadFile"
      >
          <template #trigger>
          <el-button type="primary">选择文件</el-button>
          </template>

          <el-button class="ml-3" type="success" @click="submitUpload">
              上传
          </el-button>

          <template #tip>
          <div class="el-upload__tip">
               files with a size less than 100mb
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

</style>
