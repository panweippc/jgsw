<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElPagination } from 'element-plus'
import { getMaterialList, createMaterial, updateMaterial, deleteMaterial, getCategoryTree, type Material, type MaterialCategory } from '@/api/material'

const tableData = ref<Material[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<Material>({ materialCode: '', materialName: '', status: 1 })
const searchForm = ref({ materialName: '', categoryId: undefined })
const categories = ref<MaterialCategory[]>([])

function fetchList() {
  getMaterialList(page.value, size.value, searchForm.value.materialName, searchForm.value.categoryId)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function fetchCategories() { getCategoryTree().then(res => { categories.value = res.data }) }

function handleAdd() { form.value = { materialCode: '', materialName: '', status: 1 }; dialogVisible.value = true }

function handleSubmit() {
  const action = form.value.id ? updateMaterial(form.value.id, form.value) : createMaterial(form.value)
  action.then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deleteMaterial(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

onMounted(() => { fetchList(); fetchCategories() })
</script>

<template>
  <div class="page-container">
    <ElInput v-model="searchForm.materialName" placeholder="物资名称" style="width: 200px;" />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="materialCode" label="编码" />
      <ElTableColumn prop="materialName" label="名称" />
      <ElTableColumn prop="specification" label="规格" />
      <ElTableColumn prop="unit" label="单位" />
      <ElTableColumn prop="price" label="价格" />
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">{{ scope.row.status === 1 ? '启用' : '禁用' }}</template>
      </ElTableColumn>
      <ElTableColumn label="操作">
        <template #default="scope">
          <ElButton size="small" @click="form = { ...scope.row }; dialogVisible = true">编辑</ElButton>
          <ElButton size="small" type="danger" @click="handleDelete(scope.row.id!)">删除</ElButton>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="物资信息">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="编码" required><ElInput v-model="form.materialCode" /></ElFormItem>
        <ElFormItem label="名称" required><ElInput v-model="form.materialName" /></ElFormItem>
        <ElFormItem label="规格"><ElInput v-model="form.specification" /></ElFormItem>
        <ElFormItem label="单位"><ElInput v-model="form.unit" /></ElFormItem>
        <ElFormItem label="价格"><ElInput v-model.number="form.price" type="number" /></ElFormItem>
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
