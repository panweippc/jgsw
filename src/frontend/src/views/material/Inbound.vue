<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElPagination } from 'element-plus'
import { getInboundList, createInbound, updateInbound, deleteInbound, getAllWarehouses, getMaterialList, type MaterialInbound, type MaterialWarehouse, type Material } from '@/api/material'

const tableData = ref<MaterialInbound[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<MaterialInbound>({ materialId: 0, warehouseId: 0, quantity: 0 })
const searchForm = ref({ inboundNo: '', materialId: undefined })
const warehouses = ref<MaterialWarehouse[]>([])
const materials = ref<Material[]>([])

function fetchList() {
  getInboundList(page.value, size.value, searchForm.value.inboundNo, searchForm.value.materialId)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function fetchOptions() {
  getAllWarehouses().then(res => { warehouses.value = res.data })
  getMaterialList(1, 100).then(res => { materials.value = res.data.records })
}

function handleAdd() { form.value = { materialId: 0, warehouseId: 0, quantity: 0 }; dialogVisible.value = true }

function handleSubmit() {
  const action = form.value.id ? updateInbound(form.value.id, form.value) : createInbound(form.value)
  action.then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deleteInbound(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

onMounted(() => { fetchList(); fetchOptions() })
</script>

<template>
  <div class="page-container">
    <ElInput v-model="searchForm.inboundNo" placeholder="入库单号" style="width: 200px;" />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增入库</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="inboundNo" label="入库单号" />
      <ElTableColumn prop="materialId" label="物资" />
      <ElTableColumn prop="warehouseId" label="仓库" />
      <ElTableColumn prop="quantity" label="数量" />
      <ElTableColumn prop="supplier" label="供应商" />
      <ElTableColumn prop="operator" label="操作员" />
      <ElTableColumn prop="createTime" label="入库时间" />
      <ElTableColumn label="操作" width="150" fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <ElButton size="small" @click="form = { ...(scope.row as MaterialInbound) }; dialogVisible = true">编辑</ElButton>
            <ElButton size="small" type="danger" @click="handleDelete((scope.row as MaterialInbound).id!)">删除</ElButton>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="入库管理">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="物资"><ElInput v-model.number="form.materialId" type="number" /></ElFormItem>
        <ElFormItem label="仓库"><ElInput v-model.number="form.warehouseId" type="number" /></ElFormItem>
        <ElFormItem label="数量"><ElInput v-model.number="form.quantity" type="number" /></ElFormItem>
        <ElFormItem label="供应商"><ElInput v-model="form.supplier" /></ElFormItem>
        <ElFormItem label="操作员"><ElInput v-model="form.operator" /></ElFormItem>
        <ElFormItem label="备注"><ElInput v-model="form.remark" /></ElFormItem>
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
