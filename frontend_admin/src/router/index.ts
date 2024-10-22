import { createRouter, createWebHistory } from 'vue-router'
import HomeView from '../views/HomeView.vue'

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      redirect: '/login'
    },
    {
      path: '/home',
      name: 'home',
      component: HomeView,
      beforeEnter: (to, from) => {
      if (localStorage.getItem('isLogin') === 'true') {
        return true;
      }
      else {
        return { path: '/login' };
      }
    },
      children: [
        {
          path: '/',
          redirect: '/home/welcome'
        },
        {
          path: 'welcome',
          name: 'welcome',
          component: () => import('../views/Home/WelcomeView.vue')
        },
        {
          path: 'user',
          name: 'user',
          component: () => import('../views/Home/UserView.vue')
        },
        {
          path: 'admin',
          name: 'admin',
          component: () => import('../views/Home/AdminView.vue')
        },
        {
          path: 'upload-post',
          name: 'upload-post',
          component: () => import('../views/Home/Upload/PostUploadView.vue')
        },
        {
          path: 'upload-resource',
          name: 'upload-resource',
          component: () => import('../views/Home/Upload/ResourceUploadView.vue')
        },
        {
          path: 'comment',
          name: 'comment',
          component: () => import('../views/Home/CommentView.vue')
        }
      ]
    },
    {
      path: '/login',
      name: 'login',
      component: () => import('../views/LoginView.vue')
    }
  ]
})

export default router
