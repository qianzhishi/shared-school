export type cardInfo = {
    cardId: number;
    type: number;
    userId: number;
    avatar: string;
    name: string;
    title: string;
    content: string;
    viewNum: number;
    commentNum: number;
    likeNum: number;
    time: string;
    likeFlag: number;
};

export type recommendCardInfo = {
    cardId: number;
    userId: number;
    type: number;
    avatar: string;
    name: string;
    title: string;
    content: string;
    images:Array<string>;
    viewNum: number;
    likeNum: number;
    commentNum: number;
    time: string;
    likeFlag: number;
};

  // 评论信息类型
export type commentInfo = {
    commentId: number,
    fatherId: number,
    userId: number,
    name: string,
    avatar: string,
    content: string,
    likeNum: number,
    time: string,
    likeFlag: number,
}

    // 卡片信息类型
export type detailInfo = {
    cardInfo:{
        cardId: number;
        userId: number;
        type: number;
        title: string;
        content: string;
        date: string;
        viewNum: number;
        likeNum: number;
        commentNum: number;
        images: Array<string>;
    }
    name: string;
    avatar: string;
    likeFlag: number;
    commentList: Array<commentInfo>;
}
