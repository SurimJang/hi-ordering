import { createRouter, createWebHistory } from 'vue-router';
import MainPage from '@/views/MainView.vue';
import LoginPage from '@/views/LoginView.vue';

const router = createRouter({
  history: createWebHistory(import.meta.env.BASE_URL),
  routes: [
    {
      path: '/',
      name: 'Main',
      component: MainPage,
    },
    {
      path: '/login',
      name: 'Login',
      component: LoginPage,
    },
    {
      path: '/sales',
      name: 'Sales',
      component: () => import('../views/SalesView.vue'),
    },
    {
      path: '/order',
      name: 'Order',
      component: () => import('../views/OrderView.vue'),
    },
    {
      path: '/history',
      name: 'History',
      component: () => import('../views/HistoryView.vue'),
    },
    {
      path: '/payment',
      name: 'Payment',
      component: () => import('../views/Payment.vue'),
    },
    {
      path: '/tmp',
      name: 'Tmp',
      component: () => import('../views/404View.vue'),
    },
  ],
});

export default router;
