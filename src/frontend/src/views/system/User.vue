<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption, ElSwitch, ElMessage, ElPagination } from 'element-plus'
import { getUserList, createUser, updateUser, deleteUser, resetPassword, type SysUser } from '@/api/user'
import { getAllRoles } from '@/api/role'
import { getDeptTree } from '@/api/dept'
import { getAllPosts } from '@/api/post'
import type { SysRole } from '@/api/role'
import type { SysDept } from '@/api/dept'
import type { SysPost } from '@/api/post'

const tableData = ref<SysUser[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const loading = ref(false)

const dialogVisible = ref(false)
const dialogTitle = ref('新增用户')
const form = ref<SysUser>({
  username: '',
  password: '',
  realName: '',
  phone: '',
  email: '',
  deptId: undefined,
  postId: undefined,
  isAdmin: false,
  status: 1,
  roleIds: []
})

const roles = ref<SysRole[]>([])
const depts = ref<SysDept[]>([])
const posts = ref<SysPost[]>([])

const searchForm = ref({
  username: '',
  realName: ''
})

function fetchUserList() {
  loading.value = true
  getUserList(page.value, size.value, searchForm.value.username, searchForm.value.realName)
    .then(res => {
      tableData.value = res.data.records
      total.value = res.data.total
    })
    .finally(() => {
      loading.value = false
    })
}

function fetchOptions() {
  getAllRoles().then(res => { roles.value = res.data })
  getDeptTree().then(res => { depts.value = res.data })
  getAllPosts().then(res => { posts.value = res.data })
}

function handleAdd() {
  dialogTitle.value = '新增用户'
  form.value = {
    username: '',
    password: '',
    realName: '',
    phone: '',
    email: '',
    deptId: undefined,
    postId: undefined,
    isAdmin: false,
    status: 1,
    roleIds: []
  }
  dialogVisible.value = true
}

function handleEdit(row: SysUser) {
  dialogTitle.value = '编辑用户'
  form.value = { ...row }
  dialogVisible.value = true
}

function handleSubmit() {
  if (dialogTitle.value === '新增用户') {
    createUser(form.value)
      .then(() => {
        ElMessage.success('新增成功')
        dialogVisible.value = false
        fetchUserList()
      })
      .catch(err => {
        ElMessage.error(err.message)
      })
  } else {
    updateUser(form.value.id!, form.value)
      .then(() => {
        ElMessage.success('更新成功')
        dialogVisible.value = false
        fetchUserList()
      })
      .catch(err => {
        ElMessage.error(err.message)
      })
  }
}

function handleDelete(id: number) {
  if (confirm('确定要删除该用户吗？')) {
    deleteUser(id)
      .then(() => {
        ElMessage.success('删除成功')
        fetchUserList()
      })
      .catch(err => {
        ElMessage.error(err.message)
      })
  }
}

function handleResetPassword(id: number) {
  if (confirm('确定要重置该用户密码吗？')) {
    resetPassword(id)
      .then(() => {
        ElMessage.success('密码已重置为123456')
      })
      .catch(err => {
        ElMessage.error(err.message)
      })
  }
}

function handleSearch() {
  page.value = 1
  fetchUserList()
}

function handleSizeChange(val: number) {
  size.value = val
  fetchUserList()
}

function handlePageChange(val: number) {
  page.value = val
  fetchUserList()
}

function flattenDepts(depts: SysDept[], prefix = ''): SysDept[] {
  const result: SysDept[] = []
  depts.forEach(dept => {
    result.push({ ...dept, deptName: prefix + dept.deptName })
    if (dept.children) {
      result.push(...flattenDepts(dept.children, prefix + '├─'))
    }
  })
  return result
}

onMounted(() => {
  fetchUserList()
  fetchOptions()
})
</script>

<template>
  <div class="page-container">
    <div class="search-bar">
      <ElInput
        v-model="searchForm.username"
        placeholder="用户名"
        style="width: 200px; margin-right: 10px;"
      />
      <ElInput
        v-model="searchForm.realName"
        placeholder="真实姓名"
        style="width: 200px; margin-right: 10px;"
      />
      <ElButton type="primary" @click="handleSearch">搜索</ElButton>
      <ElButton type="success" @click="handleAdd">新增</ElButton>
    </div>

    <ElTable :data="tableData" :loading="loading" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="username" label="用户名" />
      <ElTableColumn prop="realName" label="真实姓名" />
      <ElTableColumn prop="phone" label="手机号" />
      <ElTableColumn prop="email" label="邮箱" />
      <ElTableColumn prop="isAdmin" label="是否管理员">
        <template #default="scope">
          <span>{{ scope.row.isAdmin ? '是' : '否' }}</span>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">
          <span :class="scope.row.status === 1 ? 'status-active' : 'status-inactive'">
            {{ scope.row.status === 1 ? '启用' : '禁用' }}
          </span>
        </template>
      </ElTableColumn>
      <ElTableColumn prop="createTime" label="创建时间" />
      <ElTableColumn label="操作" width="220" fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <ElButton size="small" @click="handleEdit(scope.row as SysUser)">编辑</ElButton>
            <ElButton size="small" type="warning" @click="handleResetPassword((scope.row as SysUser).id!)">重置密码</ElButton>
            <ElButton size="small" type="danger" @click="handleDelete((scope.row as SysUser).id!)">删除</ElButton>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>

    <div class="pagination-container">
      <ElPagination
        @size-change="handleSizeChange"
        @current-change="handlePageChange"
        :current-page="page"
        :page-sizes="[10, 20, 50, 100]"
        :page-size="size"
        :total="total"
        layout="total, sizes, prev, pager, next, jumper"
      />
    </div>

    <ElDialog v-model="dialogVisible" :title="dialogTitle" width="500px">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="用户名" required>
          <ElInput v-model="form.username" />
        </ElFormItem>
        <ElFormItem v-if="dialogTitle === '新增用户'" label="密码" required>
          <ElInput v-model="form.password" type="password" />
        </ElFormItem>
        <ElFormItem label="真实姓名" required>
          <ElInput v-model="form.realName" />
        </ElFormItem>
        <ElFormItem label="手机号">
          <ElInput v-model="form.phone" />
        </ElFormItem>
        <ElFormItem label="邮箱">
          <ElInput v-model="form.email" />
        </ElFormItem>
        <ElFormItem label="所属部门">
          <ElSelect v-model="form.deptId" placeholder="请选择部门">
            <ElOption v-for="dept in flattenDepts(depts)" :key="dept.id" :label="dept.deptName" :value="dept.id || 0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="岗位">
          <ElSelect v-model="form.postId" placeholder="请选择岗位">
            <ElOption v-for="post in posts" :key="post.id" :label="post.postName" :value="post.id || 0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="是否管理员">
          <ElSwitch v-model="form.isAdmin" />
        </ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status">
            <ElOption label="启用" :value="1" />
            <ElOption label="禁用" :value="0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="角色">
          <ElSelect v-model="form.roleIds" multiple placeholder="请选择角色">
            <ElOption v-for="role in roles" :key="role.id" :label="role.roleName" :value="role.id || 0" />
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
.page-container {
  background: #fff;
  padding: 20px;
  border-radius: 8px;
}

.search-bar {
  display: flex;
  align-items: center;
}

.pagination-container {
  display: flex;
  justify-content: flex-end;
  margin-top: 20px;
}

.status-active {
  color: #67c23a;
}

.status-inactive {
  color: #f56c6c;
}
</style>
