import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

// 本地数据存储
const store = window.localStorage;

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/home'
    },
    {
      path: '/home',
      name: 'home',
      redirect:'/home/recommend-part',
      component: HomeView,
      meta: {
        title: '主页'
      },
      children: [
        {
          path: 'recommend-part',
          name: 'recommend-part',
          component: () => import('../views/RecommendPartView.vue')
        },
        {
          path: 'friend-part',
          name: 'friend-part',
          redirect:'/home/friend-part/study',
          component: () => import('../views/FriendPartView.vue'),
          children: [
            {
              path: 'study',
              name: 'friend-part-study',
              component: () => import('../views/FriendPart/StudyFriendView.vue')
            },
            {
              path: 'play',
              name: 'friend-part-play',
              component: () => import('../views/FriendPart/PlayFriendView.vue')
            },
            {
              path: 'love',
              name: 'friend-part-love',
              component: () => import('../views/FriendPart/LoveFriendView.vue')
            }
          ]
        },
        {
          path: 'resource-part',
          name: 'resource-part',
          component: () => import('../views/ResourcePartView.vue'),
        },
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    },
    {
      path: '/upload',
      name: 'upload',
      redirect:'/upload/beign',
      component: () => import('../views/UploadView.vue'),
      beforeEnter: () => {
        if (store.getItem('isLogin') === 'true') {
          return true;
        }
        else {
          return { path: '/login' };
        }
      },
      children: [
        {
          path: 'beign',
          name: 'upload_beign',
          component: () => import('../views/Upload/BeginUploadView.vue'),
        },
        {
          path: 'play',
          name: 'upload_play',
          component: () => import('../views/Upload/PlayUploadView.vue'),
        },
        {
          path: 'resource',
          name: 'upload_resource',
          component: () => import('../views/Upload/ResourceUploadView.vue'),
        },
        {
          path: 'study',
          name: 'upload_study',
          component: () => import('../views/Upload/StudyUploadView.vue'),
        },
        {
          path: 'love',
          name: 'upload_love',
          component: () => import('../views/Upload/LoveUploadView.vue'),
        },
      ]
    },
    {
      path: '/detail',
      name: 'detail',
      component: () => import('../views/PostDetailView.vue')
    },
    {
      path: '/search',
      name: 'search',
      component: () => import('../views/SearchView.vue')
    },
    {
      path: '/update',
      name: 'update',
      component: () => import('../views/UpdateView.vue'),
      beforeEnter: () => {
        if (store.getItem('isLogin') === 'true') {
          return true;
        }
        else {
          return { path: '/login' };
        }
      },
    },
    {
      path: '/user',
      name: 'user',
      redirect: '/user/edit',
      component: () => import('../views/UserView.vue'),
      beforeEnter: () => {
        if (store.getItem('isLogin') === 'true') {
          return true;
        }
        else {
          return { path: '/login' };
        }
      },
      children: [
        {
          path: 'edit',
          name: 'edit',
          component: () => import('../views/User/EditView.vue'),
        },
        {
          path: 'postings',
          name: 'postings',
          component: () => import('../views/User/PostingsView.vue'),
        },
        {
          path: 'comments',
          name: 'comments',
          component: () => import('../views/User/CommentsView.vue'),
        },
        {
          path: 'fans',
          name: 'fans',
          component: () => import('../views/User/FansView.vue'),
        },
        {
          path: 'focus',
          name: 'focus',
          component: () => import('../views/User/FocusView.vue'),
        },
        {
          path: 'settings',
          name: 'settings',
          component: () => import('../views/User/SettingsView.vue'),
        }
      ]
    },
    {
      path: '/friend-news',
      name: 'friend-news',
      component: () => import('../views/FriendNewsView.vue'),
      beforeEnter: () => {
        if (store.getItem('isLogin') === 'true') {
          return true;
        }
        else {
          return { path: '/login' };
        }
      },
    },
  ]
})

export default router
