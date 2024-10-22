import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useMenuStore = defineStore('menu', () => {
    // 活跃菜单项索引
    const activeIndex = ref<string>('0');

    return {
        activeIndex
    };
})