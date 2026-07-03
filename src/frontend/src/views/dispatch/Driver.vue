<template>
  <div class="page-container">
    <ElInput v-model="searchForm.driverName" placeholder="驾驶员姓名" style="width: 200px;" />
    <ElInput v-model="searchForm.department" placeholder="所属部门" style="width: 150px;" />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增</ElButton>
    <ElButton type="warning" @click="handleExport">导出</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="driverName" label="姓名" />
      <ElTableColumn prop="phone" label="手机号" />
      <ElTableColumn prop="driverLicense" label="驾驶证号" />
      <ElTableColumn prop="licenseType" label="准驾车型" />
      <ElTableColumn prop="licenseExpire" label="驾驶证到期" />
      <ElTableColumn prop="hireDate" label="入职日期" />
      <ElTableColumn prop="department" label="所属部门" />
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">{{ getStatusLabel(scope.row.status) }}</template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="180" fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <ElButton size="small" @click="handleEdit(scope.row as Driver)">编辑</ElButton>
            <ElButton size="small" type="danger" @click="handleDelete((scope.row as Driver).id!)">删除</ElButton>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="驾驶员信息">
      <ElForm :model="form" label-width="120px">
        <ElFormItem label="姓名" required><ElInput v-model="form.driverName" /></ElFormItem>
        <ElFormItem label="手机号"><ElInput v-model="form.phone" /></ElFormItem>
        <ElFormItem label="驾驶证号" required><ElInput v-model="form.driverLicense" /></ElFormItem>
        <ElFormItem label="准驾车型">
          <ElSelect v-model="form.licenseType" placeholder="请选择准驾车型">
            <ElOption label="C1" value="C1" />
            <ElOption label="C2" value="C2" />
            <ElOption label="B1" value="B1" />
            <ElOption label="B2" value="B2" />
            <ElOption label="A1" value="A1" />
            <ElOption label="A2" value="A2" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="驾驶证到期"><ElDatePicker v-model="form.licenseExpire" type="date" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="入职日期"><ElDatePicker v-model="form.hireDate" type="date" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="所属部门"><ElInput v-model="form.department" /></ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status" placeholder="请选择状态">
            <ElOption label="停用" :value="0" />
            <ElOption label="在职" :value="1" />
            <ElOption label="离职" :value="2" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="备注"><ElInput v-model="form.remark" type="textarea" /></ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmit">确定</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElPagination, ElSelect, ElOption, ElDatePicker } from 'element-plus'
import { getDriverList, createDriver, updateDriver, deleteDriver, exportDrivers, type Driver } from '@/api/dispatch'

const tableData = ref<Driver[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<Driver>({ driverName: '', driverLicense: '' })
const searchForm = ref({ driverName: '', department: '' })

function fetchList() {
  getDriverList(page.value, size.value, searchForm.value.driverName, searchForm.value.department)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function handleAdd() {
  form.value = { driverName: '', driverLicense: '', licenseType: 'C1', department: '后勤保障科', status: 1 }
  dialogVisible.value = true
}

function handleEdit(row: Driver) {
  form.value = { ...row }
  dialogVisible.value = true
}

function handleSubmit() {
  const submitData = { ...form.value }
  if (submitData.licenseExpire && submitData.licenseExpire instanceof Date) {
    submitData.licenseExpire = formatDate(submitData.licenseExpire)
  }
  if (submitData.hireDate && submitData.hireDate instanceof Date) {
    submitData.hireDate = formatDate(submitData.hireDate)
  }
  const action = form.value.id ? updateDriver(form.value.id, submitData) : createDriver(submitData)
  action.then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function formatDate(date: Date): string {
  const year = date.getFullYear()
  const month = String(date.getMonth() + 1).padStart(2, '0')
  const day = String(date.getDate()).padStart(2, '0')
  return `${year}-${month}-${day}`
}

function handleDelete(id: number) {
  deleteDriver(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleExport() {
  exportDrivers(searchForm.value.driverName, searchForm.value.department).then(res => {
    const blob = new Blob([res.data], { type: 'text/csv;charset=UTF-8' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `驾驶员信息_${new Date().toISOString().split('T')[0]}.csv`
    link.click()
    URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  }).catch(() => ElMessage.error('导出失败'))
}

function getStatusLabel(status?: number) {
  if (!status) return '停用'
  const map: Record<number, string> = { 0: '停用', 1: '在职', 2: '离职' }
  return map[status] || '未知'
}

onMounted(() => { fetchList() })
</script>