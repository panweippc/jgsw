<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElContainer, ElAside, ElMain, ElMenu, ElMenuItem, ElSubMenu } from 'element-plus'
import { useRouter, useRoute } from 'vue-router'

const router = useRouter()
const route = useRoute()

const collapsed = ref(false)

const menuItems = [
  {
    name: 'system',
    label: '系统设置',
    icon: 'Setting',
    children: [
      { name: 'user', label: '用户管理', path: '/system/user' },
      { name: 'role', label: '角色管理', path: '/system/role' },
      { name: 'permission', label: '权限管理', path: '/system/permission' },
      { name: 'dept', label: '部门管理', path: '/system/dept' },
      { name: 'post', label: '岗位管理', path: '/system/post' },
      { name: 'dict', label: '字典管理', path: '/system/dict' }
    ]
  },
  {
    name: 'material',
    label: '物资管理',
    icon: 'Package',
    children: [
      { name: 'info', label: '物资信息', path: '/material/info' },
      { name: 'category', label: '分类管理', path: '/material/category' },
      { name: 'warehouse', label: '仓库管理', path: '/material/warehouse' },
      { name: 'inbound', label: '入库管理', path: '/material/inbound' },
      { name: 'outbound', label: '出库管理', path: '/material/outbound' },
      { name: 'apply', label: '领用申请', path: '/material/apply' },
      { name: 'stock', label: '库存查询', path: '/material/stock' }
    ]
  },
  {
    name: 'dispatch',
    label: '派车管理',
    icon: 'Car',
    children: [
      { name: 'vehicle', label: '车辆信息', path: '/dispatch/vehicle' },
      { name: 'driver', label: '驾驶员信息', path: '/dispatch/driver' },
      { name: 'order', label: '派车单管理', path: '/dispatch/order' }
    ]
  }
]

function handleMenuClick(path: string) {
  router.push(path)
}

onMounted(() => {
  if (!route.path || route.path === '/') {
    router.push('/system/user')
  }
})
</script>

<template>
  <ElContainer class="app-container">
    <ElAside :width="collapsed ? '64px' : '200px'" class="sidebar">
      <div class="logo">
        <span v-if="!collapsed">机关事务管理系统</span>
        <span v-else>JG</span>
      </div>
      <ElMenu
        :default-active="route.path"
        :collapse="collapsed"
        router
        class="sidebar-menu"
      >
        <ElSubMenu
          v-for="item in menuItems"
          :key="item.name"
          :index="item.name"
        >
          <template #title>
            <span>{{ item.label }}</span>
          </template>
          <ElMenuItem
            v-for="child in item.children"
            :key="child.name"
            :index="child.path"
            @click="handleMenuClick(child.path)"
          >
            {{ child.label }}
          </ElMenuItem>
        </ElSubMenu>
      </ElMenu>
    </ElAside>
    <ElContainer>
      <ElMain class="main-content">
        <router-view />
      </ElMain>
    </ElContainer>
  </ElContainer>
</template>

<style scoped>
.app-container {
  height: 100vh;
}

.sidebar {
  background-color: #001529;
  color: #fff;
}

.logo {
  height: 60px;
  line-height: 60px;
  text-align: center;
  font-size: 16px;
  font-weight: bold;
  background-color: #1890ff;
}

.sidebar-menu {
  height: calc(100vh - 60px);
  border-right: none;
}

.main-content {
  background-color: #f0f2f5;
  padding: 20px;
}
</style>
