import { createRouter, createWebHistory } from 'vue-router'
import type { RouteRecordRaw } from 'vue-router'

const routes: RouteRecordRaw[] = [
  {
    path: '/',
    redirect: '/system/user'
  },
  {
    path: '/system',
    name: 'System',
    meta: { title: '系统设置' },
    children: [
      {
        path: 'user',
        name: 'User',
        meta: { title: '用户管理' },
        component: () => import('@/views/system/User.vue')
      },
      {
        path: 'role',
        name: 'Role',
        meta: { title: '角色管理' },
        component: () => import('@/views/system/Role.vue')
      },
      {
        path: 'permission',
        name: 'Permission',
        meta: { title: '权限管理' },
        component: () => import('@/views/system/Permission.vue')
      },
      {
        path: 'dept',
        name: 'Dept',
        meta: { title: '部门管理' },
        component: () => import('@/views/system/Dept.vue')
      },
      {
        path: 'post',
        name: 'Post',
        meta: { title: '岗位管理' },
        component: () => import('@/views/system/Post.vue')
      },
      {
        path: 'dict',
        name: 'Dict',
        meta: { title: '字典管理' },
        component: () => import('@/views/system/Dict.vue')
      }
    ]
  },
  {
    path: '/material',
    name: 'Material',
    meta: { title: '物资管理' },
    children: [
      {
        path: 'info',
        name: 'MaterialInfo',
        meta: { title: '物资信息' },
        component: () => import('@/views/material/MaterialInfo.vue')
      },
      {
        path: 'category',
        name: 'MaterialCategory',
        meta: { title: '分类管理' },
        component: () => import('@/views/material/MaterialCategory.vue')
      },
      {
        path: 'warehouse',
        name: 'Warehouse',
        meta: { title: '仓库管理' },
        component: () => import('@/views/material/Warehouse.vue')
      },
      {
        path: 'inbound',
        name: 'Inbound',
        meta: { title: '入库管理' },
        component: () => import('@/views/material/Inbound.vue')
      },
      {
        path: 'outbound',
        name: 'Outbound',
        meta: { title: '出库管理' },
        component: () => import('@/views/material/Outbound.vue')
      },
      {
        path: 'apply',
        name: 'Apply',
        meta: { title: '领用申请' },
        component: () => import('@/views/material/Apply.vue')
      },
      {
        path: 'stock',
        name: 'Stock',
        meta: { title: '库存查询' },
        component: () => import('@/views/material/Stock.vue')
      }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router
