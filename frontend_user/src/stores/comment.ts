import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCommentStore = defineStore('comment', () => {
    // 帖子ID
    const postId = ref<number>(0)

    // 对象ID
    const objectId = ref<number>(0);
    const objectFather = ref<number>(0)

    // 用户ID
    const userId = ref<number>(0);

    // 帖子的评论
    const postContent = ref<string>("");

    // 评论的评论
    const commentContent = ref<string>("");

    // 是否显示对话框
    const postCommentDialogVisiable = ref<boolean>(false);
    const commentCommentDialogVisiable = ref<boolean>(false);

    // 帖子的评论对话框初始化
    function postCommentDiaglogInit(object:number){
        objectId.value = object;
        objectFather.value = 0;
        userId.value = 0;
        postContent.value = '';
        postCommentDialogVisiable.value = true;
    }
    
    // 评论的评论对话框初始化
    function commentCommentDiaglogInit(object:number,father:number) {
        objectId.value = object;
        objectFather.value = father;
        userId.value = 0;
        commentContent.value = '';
        commentCommentDialogVisiable.value = true;
    }

    // 帖子的评论对话框关闭
    function postCommentDialogClose() { 
        objectId.value = 0;
        objectFather.value = 0;
        userId.value = 0;
        postContent.value = '';
        postCommentDialogVisiable.value = false;
    }
    
    // 评论的评论对话框关闭
    function commentCommentDialogClose() { 
        objectId.value = 0;
        objectFather.value = 0;
        userId.value = 0;
        commentContent.value = '';
        commentCommentDialogVisiable.value = false;
    }

    return {
        postId,
        objectId,
        objectFather,
        userId,
        postContent,
        commentContent,
        postCommentDialogVisiable,
        commentCommentDialogVisiable,
        postCommentDiaglogInit,
        commentCommentDiaglogInit,
        postCommentDialogClose,
        commentCommentDialogClose,
    };
})