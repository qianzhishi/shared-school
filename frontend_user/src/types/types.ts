export type cardInfo = {
    card: number;
    type: number;
    id: number;
    avatar: string;
    name: string;
    title: string;
    content: string;
    num_read: number;
    num_comment: number;
    num_like: number;
    time: string;
    flag: number;
};

export type recommendCardInfo = {
    card_id: number;
    type: number;
    author_id: number;
    author_avatar: string;
    author_name: string;
    title: string;
    content: string;
    images:Array<string>;
    num_read: number;
    num_like: number;
    num_comment: number;
    time: string;
    flag: number;
};

  // 评论信息类型
export type commentInfo = {
    id: number,
    author_id: number,
    name: string,
    avatar: string,
    content: string,
    num_comment: number,
    num_like: number,
    time: string,
    flag: number,
}

    // 卡片信息类型
export type detailInfo = {
    id: number;
    name: string;
    title: string;
    avatar: string;
    content: string;
    images: Array<string>;
    num_like: number;
    num_read: number;
    num_comment: number;
    time: string;
    list: Array<commentInfo>;
    flag: number;
}
