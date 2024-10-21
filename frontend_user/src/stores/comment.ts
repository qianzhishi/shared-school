import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useCommentStore = defineStore('comment', () => {
    // 父级(帖子或一级评论)ID
    const fatherId = ref<number>(0);

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
        fatherId.value = object;
        userId.value = 0;
        postContent.value = '';
        postCommentDialogVisiable.value = true;
    }
    
    // 评论的评论对话框初始化
    function commentCommentDiaglogInit(object:number) {
        fatherId.value = object;
        userId.value = 0;
        commentContent.value = '';
        commentCommentDialogVisiable.value = true;
    }

    // 帖子的评论对话框关闭
    function postCommentDialogClose() { 
        fatherId.value = 0;
        userId.value = 0;
        postContent.value = '';
        postCommentDialogVisiable.value = false;
    }
    
    // 评论的评论对话框关闭
    function commentCommentDialogClose() { 
        fatherId.value = 0;
        userId.value = 0;
        commentContent.value = '';
        commentCommentDialogVisiable.value = false;
    }

    return {
        fatherId,
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