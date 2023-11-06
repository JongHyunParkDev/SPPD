import { RouteRecordRaw } from 'vue-router';

const routes: RouteRecordRaw[] = [
    {
        path: '/login',
        component: () => import('../LoginApp.vue')
    },
    {
        path: '/',
        component: () => import('layouts/MainLayout.vue'),
        children: [{ path: '', component: () => import('pages/IndexPage.vue') }],
    },
    {
        path: '/AccountBook',
        component: () => import('layouts/MainLayout.vue'),
        children: [{ path: '', component: () => import('pages/AccountBookPage.vue') }],
    },
    {
        path: '/AccountBookDetail',
        component: () => import('layouts/MainLayout.vue'),
        children: [{ path: '', component: () => import('pages/AccountBookDetailPage.vue') }],
    },
    {
        path: '/Arduino',
        component: () => import('layouts/MainLayout.vue'),
        children: [{ path: '', component: () => import('pages/ArduinoPage.vue') }],
    },

    // Always leave this as last one,
    // but you can also remove it
    {
        path: '/:catchAll(.*)*',
        component: () => import('pages/ErrorNotFound.vue'),
    },
];

export default routes;
