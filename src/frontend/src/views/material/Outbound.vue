<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElPagination } from 'element-plus'
import { getOutboundList, createOutbound, updateOutbound, deleteOutbound, type MaterialOutbound } from '@/api/material'

const tableData = ref<MaterialOutbound[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<MaterialOutbound>({ materialId: 0, warehouseId: 0, quantity: 0 })
const searchForm = ref({ outboundNo: '', materialId: undefined })

function fetchList() {
  getOutboundList(page.value, size.value, searchForm.value.outboundNo, searchForm.value.materialId)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function handleAdd() { form.value = { materialId: 0, warehouseId: 0, quantity: 0 }; dialogVisible.value = true }

function handleSubmit() {
  const action = form.value.id ? updateOutbound(form.value.id, form.value) : createOutbound(form.value)
  action.then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deleteOutbound(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

onMounted(() => fetchList())
</script>

<template>
  <div class="page-container">
    <ElInput v-model="searchForm.outboundNo" placeholder="出库单号" style="width: 200px;" />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增出库</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="outboundNo" label="出库单号" />
      <ElTableColumn prop="materialId" label="物资" />
      <ElTableColumn prop="warehouseId" label="仓库" />
      <ElTableColumn prop="quantity" label="数量" />
      <ElTableColumn prop="receiver" label="接收人" />
      <ElTableColumn prop="operator" label="操作员" />
      <ElTableColumn prop="createTime" label="出库时间" />
      <ElTableColumn label="操作">
        <template #default="scope">
          <ElButton size="small" @click="form = { ...scope.row }; dialogVisible = true">编辑</ElButton>
          <ElButton size="small" type="danger" @click="handleDelete(scope.row.id!)">删除</ElButton>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="出库管理">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="物资"><ElInput v-model.number="form.materialId" type="number" /></ElFormItem>
        <ElFormItem label="仓库"><ElInput v-model.number="form.warehouseId" type="number" /></ElFormItem>
        <ElFormItem label="数量"><ElInput v-model.number="form.quantity" type="number" /></ElFormItem>
        <ElFormItem label="接收人"><ElInput v-model="form.receiver" /></ElFormItem>
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
