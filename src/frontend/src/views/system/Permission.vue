<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTree, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElMessage } from 'element-plus'
import { getPermissionTree, createPermission, updatePermission, deletePermission, type SysPermission } from '@/api/permission'

const permissions = ref<SysPermission[]>([])
const dialogVisible = ref(false)
const form = ref<SysPermission>({ parentId: 0, permissionName: '', permissionCode: '', type: 1, status: 1 })

function fetchPermissions() {
  getPermissionTree().then(res => { permissions.value = res.data })
}

function handleAdd(parentId = 0) {
  form.value = { parentId, permissionName: '', permissionCode: '', type: parentId === 0 ? 1 : 2, status: 1 }
  dialogVisible.value = true
}

function handleSubmit() {
  const action = form.value.id ? updatePermission(form.value.id, form.value) : createPermission(form.value)
  action.then(() => {
    ElMessage.success('操作成功')
    dialogVisible.value = false
    fetchPermissions()
  }).catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deletePermission(id).then(() => { ElMessage.success('删除成功'); fetchPermissions() })
    .catch(err => ElMessage.error(err.message))
}

function generateTreeData(ps: SysPermission[]): any[] {
  return ps.map(p => ({ id: p.id, label: p.permissionName, children: p.children ? generateTreeData(p.children) : [] }))
}

onMounted(() => fetchPermissions())
</script>

<template>
  <div class="page-container">
    <ElButton type="success" @click="handleAdd(0)">新增</ElButton>
    <ElTree :data="generateTreeData(permissions)" node-key="id" default-expand-all>
      <template #default="{ data }">
        <span>{{ data.label }}</span>
        <ElButton size="small" @click="handleAdd(data.id)">新增</ElButton>
        <ElButton size="small" type="danger" @click="handleDelete(data.id)">删除</ElButton>
      </template>
    </ElTree>
    <ElDialog v-model="dialogVisible" title="权限设置">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="名称" required><ElInput v-model="form.permissionName" /></ElFormItem>
        <ElFormItem label="编码" required><ElInput v-model="form.permissionCode" /></ElFormItem>
        <ElFormItem label="类型">
          <ElSelect v-model="form.type">
            <ElOption label="模块" :value="1" />
            <ElOption label="菜单" :value="2" />
            <ElOption label="按钮" :value="3" />
          </ElSelect>
        </ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmit">确定</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<style scoped>
.page-container { background: #fff; padding: 20px; border-radius: 8px; }
</style>
