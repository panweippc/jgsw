<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElPagination } from 'element-plus'
import { getApplyList, createApply, approveApply, deleteApply, type MaterialApply } from '@/api/material'

const tableData = ref<MaterialApply[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<MaterialApply>({ materialId: 0, warehouseId: 0, quantity: 0 })
const searchForm = ref({ applyNo: '', status: undefined })

function fetchList() {
  getApplyList(page.value, size.value, searchForm.value.applyNo, searchForm.value.status)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function handleAdd() { form.value = { materialId: 0, warehouseId: 0, quantity: 0 }; dialogVisible.value = true }

function handleSubmit() {
  createApply(form.value).then(() => { ElMessage.success('申请成功'); dialogVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleApprove(id: number, status: number) {
  approveApply(id, status).then(() => { ElMessage.success(status === 1 ? '审批通过' : '审批拒绝'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deleteApply(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function getStatusLabel(status: number): string {
  const labels: Record<number, string> = { 0: '待审批', 1: '已通过', 2: '已拒绝' }
  return labels[status] || '未知'
}

onMounted(() => fetchList())
</script>

<template>
  <div class="page-container">
    <ElInput v-model="searchForm.applyNo" placeholder="申请单号" style="width: 200px;" />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增申请</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="applyNo" label="申请单号" />
      <ElTableColumn prop="materialId" label="物资" />
      <ElTableColumn prop="warehouseId" label="仓库" />
      <ElTableColumn prop="quantity" label="数量" />
      <ElTableColumn prop="applicant" label="申请人" />
      <ElTableColumn prop="deptId" label="部门" />
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">{{ getStatusLabel(scope.row.status!) }}</template>
      </ElTableColumn>
      <ElTableColumn prop="createTime" label="申请时间" />
      <ElTableColumn label="操作">
        <template #default="scope">
          <template v-if="scope.row.status === 0">
            <ElButton size="small" type="success" @click="handleApprove(scope.row.id!, 1)">通过</ElButton>
            <ElButton size="small" type="danger" @click="handleApprove(scope.row.id!, 2)">拒绝</ElButton>
          </template>
          <ElButton size="small" type="danger" @click="handleDelete(scope.row.id!)">删除</ElButton>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="领用申请">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="物资"><ElInput v-model.number="form.materialId" type="number" /></ElFormItem>
        <ElFormItem label="仓库"><ElInput v-model.number="form.warehouseId" type="number" /></ElFormItem>
        <ElFormItem label="数量"><ElInput v-model.number="form.quantity" type="number" /></ElFormItem>
        <ElFormItem label="申请人"><ElInput v-model="form.applicant" /></ElFormItem>
        <ElFormItem label="部门"><ElInput v-model.number="form.deptId" type="number" /></ElFormItem>
        <ElFormItem label="备注"><ElInput v-model="form.remark" /></ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmit">提交申请</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<style scoped>
.page-container { background: #fff; padding: 20px; border-radius: 8px; }
</style>
