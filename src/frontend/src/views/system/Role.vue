<script setup lang="ts">
import { ref, onMounted, computed } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElMessage, ElPagination, ElTree } from 'element-plus'
import { getRoleList, createRole, updateRole, deleteRole, getRoleById, type SysRole } from '@/api/role'
import { getPermissionTree, type SysPermission } from '@/api/permission'

const tableData = ref<SysRole[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<SysRole>({ roleName: '', roleCode: '', status: 1, permissionIds: [] })
const permissions = ref<SysPermission[]>([])
const searchForm = ref({ roleName: '' })
const checkedKeys = ref<number[]>([])

function fetchRoleList() {
  getRoleList(page.value, size.value, searchForm.value.roleName)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function fetchPermissions() { 
  getPermissionTree().then(res => { 
    permissions.value = res.data 
  }) 
}

function handleAdd() { 
  form.value = { roleName: '', roleCode: '', status: 1, permissionIds: [] }
  checkedKeys.value = []
  dialogVisible.value = true 
}

function handleEdit(row: SysRole) {
  getRoleById(row.id!).then(res => {
    form.value = res.data
    checkedKeys.value = res.data.permissionIds || []
    dialogVisible.value = true
  })
}

function handleSubmit() {
  const roleData = { ...form.value, permissionIds: checkedKeys.value }
  const action = dialogTitle.value === '新增角色' ? createRole(roleData) : updateRole(form.value.id!, roleData)
  action.then(() => { 
    ElMessage.success('操作成功')
    dialogVisible.value = false
    fetchRoleList() 
  }).catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  if (confirm('确定删除?')) {
    deleteRole(id).then(() => { ElMessage.success('删除成功'); fetchRoleList() })
      .catch(err => ElMessage.error(err.message))
  }
}

function handleCheckChange(data: any, checked: boolean) {
  if (checked) {
    if (!checkedKeys.value.includes(data.id)) {
      checkedKeys.value.push(data.id)
    }
  } else {
    checkedKeys.value = checkedKeys.value.filter(id => id !== data.id)
  }
}

function generateTreeData(ps: SysPermission[]): any[] {
  return ps.map(p => ({ id: p.id, label: p.permissionName, children: p.children ? generateTreeData(p.children) : [] }))
}

const dialogTitle = computed(() => form.value.id ? '编辑角色' : '新增角色')

onMounted(() => { fetchRoleList(); fetchPermissions() })
</script>

<template>
  <div class="page-container">
    <ElInput v-model="searchForm.roleName" placeholder="角色名称" style="width: 200px;" />
    <ElButton type="primary" @click="fetchRoleList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="roleName" label="角色名称" />
      <ElTableColumn prop="roleCode" label="角色编码" />
      <ElTableColumn prop="description" label="描述" />
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">{{ scope.row.status === 1 ? '启用' : '禁用' }}</template>
      </ElTableColumn>
      <ElTableColumn prop="createTime" label="创建时间" />
      <ElTableColumn label="操作" width="150" fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <ElButton size="small" @click="handleEdit(scope.row as SysRole)">编辑</ElButton>
            <ElButton size="small" type="danger" @click="handleDelete((scope.row as SysRole).id!)">删除</ElButton>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchRoleList()" @current-page-change="page = $event; fetchRoleList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" :title="dialogTitle" width="600px">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="角色名称" required><ElInput v-model="form.roleName" /></ElFormItem>
        <ElFormItem label="角色编码" required><ElInput v-model="form.roleCode" /></ElFormItem>
        <ElFormItem label="描述"><ElInput v-model="form.description" type="textarea" /></ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status">
            <ElOption label="启用" :value="1" />
            <ElOption label="禁用" :value="0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="权限">
          <ElTree :data="generateTreeData(permissions)" show-checkbox node-key="id"
            :default-checked-keys="checkedKeys"
            :checked-keys="checkedKeys"
            @check-change="handleCheckChange"
            style="height: 300px; overflow-y: auto;" />
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
