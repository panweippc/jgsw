<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTree, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage } from 'element-plus'
import { getDeptTree, createDept, updateDept, deleteDept, type SysDept } from '@/api/dept'

const depts = ref<SysDept[]>([])
const dialogVisible = ref(false)
const form = ref<SysDept>({ parentId: 0, deptCode: '', deptName: '', status: 1 })

function fetchDepts() { getDeptTree().then(res => { depts.value = res.data }) }

function handleAdd(parentId = 0) {
  form.value = { parentId, deptCode: '', deptName: '', status: 1 }
  dialogVisible.value = true
}

function handleSubmit() {
  const action = form.value.id ? updateDept(form.value.id, form.value) : createDept(form.value)
  action.then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; fetchDepts() })
    .catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deleteDept(id).then(() => { ElMessage.success('删除成功'); fetchDepts() })
    .catch(err => ElMessage.error(err.message))
}

function generateTreeData(ds: SysDept[]): any[] {
  return ds.map(d => ({ id: d.id, label: d.deptName, children: d.children ? generateTreeData(d.children) : [] }))
}

onMounted(() => fetchDepts())
</script>

<template>
  <div class="page-container">
    <ElButton type="success" @click="handleAdd(0)">新增部门</ElButton>
    <ElTree :data="generateTreeData(depts)" node-key="id" default-expand-all>
      <template #default="{ data }">
        <span>{{ data.label }}</span>
        <ElButton size="small" @click="handleAdd(data.id)">新增</ElButton>
        <ElButton size="small" type="danger" @click="handleDelete(data.id)">删除</ElButton>
      </template>
    </ElTree>
    <ElDialog v-model="dialogVisible" title="部门设置">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="编码" required><ElInput v-model="form.deptCode" /></ElFormItem>
        <ElFormItem label="名称" required><ElInput v-model="form.deptName" /></ElFormItem>
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
