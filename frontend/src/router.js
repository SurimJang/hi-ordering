
import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router);


import OrderOrderManager from "./components/listers/OrderOrderCards"
import OrderOrderDetail from "./components/listers/OrderOrderDetail"

import ManagementShopManager from "./components/listers/ManagementShopCards"
import ManagementShopDetail from "./components/listers/ManagementShopDetail"

import PaymentPaymentManager from "./components/listers/PaymentPaymentCards"
import PaymentPaymentDetail from "./components/listers/PaymentPaymentDetail"

import UserUserManager from "./components/listers/UserUserCards"
import UserUserDetail from "./components/listers/UserUserDetail"

import MenuMenuManager from "./components/listers/MenuMenuCards"
import MenuMenuDetail from "./components/listers/MenuMenuDetail"

import CategoryCategoryManager from "./components/listers/CategoryCategoryCards"
import CategoryCategoryDetail from "./components/listers/CategoryCategoryDetail"


export default new Router({
    // mode: 'history',
    base: process.env.BASE_URL,
    routes: [
            {
                path: '/orders/orders',
                name: 'OrderOrderManager',
                component: OrderOrderManager
            },
            {
                path: '/orders/orders/:id',
                name: 'OrderOrderDetail',
                component: OrderOrderDetail
            },

            {
                path: '/managements/shops',
                name: 'ManagementShopManager',
                component: ManagementShopManager
            },
            {
                path: '/managements/shops/:id',
                name: 'ManagementShopDetail',
                component: ManagementShopDetail
            },

            {
                path: '/payments/payments',
                name: 'PaymentPaymentManager',
                component: PaymentPaymentManager
            },
            {
                path: '/payments/payments/:id',
                name: 'PaymentPaymentDetail',
                component: PaymentPaymentDetail
            },

            {
                path: '/users/users',
                name: 'UserUserManager',
                component: UserUserManager
            },
            {
                path: '/users/users/:id',
                name: 'UserUserDetail',
                component: UserUserDetail
            },

            {
                path: '/menus/menus',
                name: 'MenuMenuManager',
                component: MenuMenuManager
            },
            {
                path: '/menus/menus/:id',
                name: 'MenuMenuDetail',
                component: MenuMenuDetail
            },

            {
                path: '/categories/categories',
                name: 'CategoryCategoryManager',
                component: CategoryCategoryManager
            },
            {
                path: '/categories/categories/:id',
                name: 'CategoryCategoryDetail',
                component: CategoryCategoryDetail
            },



    ]
})
