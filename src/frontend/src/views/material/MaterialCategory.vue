<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTree, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage } from 'element-plus'
import { getCategoryTree, createCategory, updateCategory, deleteCategory, type MaterialCategory } from '@/api/material'

const categories = ref<MaterialCategory[]>([])
const dialogVisible = ref(false)
const form = ref<MaterialCategory>({ parentId: 0, categoryCode: '', categoryName: '', status: 1 })

function fetchData() { getCategoryTree().then(res => { categories.value = res.data }) }

function handleAdd(parentId = 0) {
  form.value = { parentId, categoryCode: '', categoryName: '', status: 1 }
  dialogVisible.value = true
}

function handleSubmit() {
  const action = form.value.id ? updateCategory(form.value.id, form.value) : createCategory(form.value)
  action.then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; fetchData() })
    .catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deleteCategory(id).then(() => { ElMessage.success('删除成功'); fetchData() })
    .catch(err => ElMessage.error(err.message))
}

function generateTreeData(cs: MaterialCategory[]): any[] {
  return cs.map(c => ({ id: c.id, label: c.categoryName, children: c.children ? generateTreeData(c.children) : [] }))
}

onMounted(() => fetchData())
</script>

<template>
  <div class="page-container">
    <ElButton type="success" @click="handleAdd(0)">新增分类</ElButton>
    <ElTree :data="generateTreeData(categories)" node-key="id" default-expand-all>
      <template #default="{ data }">
        <span>{{ data.label }}</span>
        <ElButton size="small" @click="handleAdd(data.id)">新增</ElButton>
        <ElButton size="small" type="danger" @click="handleDelete(data.id)">删除</ElButton>
      </template>
    </ElTree>
    <ElDialog v-model="dialogVisible" title="分类设置">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="编码" required><ElInput v-model="form.categoryCode" /></ElFormItem>
        <ElFormItem label="名称" required><ElInput v-model="form.categoryName" /></ElFormItem>
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
