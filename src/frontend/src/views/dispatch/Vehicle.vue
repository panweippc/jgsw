<template>
  <div class="page-container">
    <ElInput v-model="searchForm.plateNumber" placeholder="车牌号" style="width: 200px;" />
    <ElSelect v-model="searchForm.vehicleType" placeholder="车辆类型" style="width: 150px;" clearable>
      <ElOption label="轿车" value="轿车" />
      <ElOption label="SUV" value="SUV" />
      <ElOption label="商务车" value="商务车" />
      <ElOption label="货车" value="货车" />
      <ElOption label="面包车" value="面包车" />
      <ElOption label="客车" value="客车" />
    </ElSelect>
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增</ElButton>
    <ElButton type="warning" @click="handleExport">导出</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="plateNumber" label="车牌号" />
      <ElTableColumn prop="vehicleType" label="车辆类型" />
      <ElTableColumn prop="brand" label="品牌" />
      <ElTableColumn prop="model" label="车型" />
      <ElTableColumn prop="color" label="颜色" />
      <ElTableColumn prop="seatCount" label="座位数" />
      <ElTableColumn prop="mileage" label="里程(公里)" />
      <ElTableColumn prop="fuelType" label="燃油类型" />
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">{{ getStatusLabel(scope.row.status) }}</template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="180" fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <ElButton size="small" @click="handleEdit(scope.row as Vehicle)">编辑</ElButton>
            <ElButton size="small" type="danger" @click="handleDelete((scope.row as Vehicle).id!)">删除</ElButton>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="车辆信息">
      <ElForm :model="form" label-width="120px">
        <ElFormItem label="车牌号" required><ElInput v-model="form.plateNumber" /></ElFormItem>
        <ElFormItem label="车辆类型">
          <ElSelect v-model="form.vehicleType" placeholder="请选择车辆类型">
            <ElOption label="轿车" value="轿车" />
            <ElOption label="SUV" value="SUV" />
            <ElOption label="商务车" value="商务车" />
            <ElOption label="货车" value="货车" />
            <ElOption label="面包车" value="面包车" />
            <ElOption label="客车" value="客车" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="品牌"><ElInput v-model="form.brand" /></ElFormItem>
        <ElFormItem label="车型"><ElInput v-model="form.model" /></ElFormItem>
        <ElFormItem label="颜色"><ElInput v-model="form.color" /></ElFormItem>
        <ElFormItem label="座位数"><ElInput v-model.number="form.seatCount" type="number" /></ElFormItem>
        <ElFormItem label="购置日期"><ElDatePicker v-model="form.purchaseDate" type="date" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="当前里程"><ElInput v-model.number="form.mileage" type="number" placeholder="公里" /></ElFormItem>
        <ElFormItem label="燃油类型">
          <ElSelect v-model="form.fuelType" placeholder="请选择燃油类型">
            <ElOption label="汽油" value="汽油" />
            <ElOption label="柴油" value="柴油" />
            <ElOption label="混动" value="混动" />
            <ElOption label="电动" value="电动" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="发动机号"><ElInput v-model="form.engineNo" /></ElFormItem>
        <ElFormItem label="车架号"><ElInput v-model="form.vin" /></ElFormItem>
        <ElFormItem label="保险到期"><ElDatePicker v-model="form.insuranceExpire" type="date" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="年检到期"><ElDatePicker v-model="form.inspectionExpire" type="date" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="状态">
          <ElSelect v-model="form.status" placeholder="请选择状态">
            <ElOption label="停用" :value="0" />
            <ElOption label="在用" :value="1" />
            <ElOption label="维修中" :value="2" />
            <ElOption label="已报废" :value="3" />
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
import { getVehicleList, createVehicle, updateVehicle, deleteVehicle, exportVehicles, type Vehicle } from '@/api/dispatch'

const tableData = ref<Vehicle[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<Vehicle>({ plateNumber: '' })
const searchForm = ref({ plateNumber: '', vehicleType: '' })

function fetchList() {
  getVehicleList(page.value, size.value, searchForm.value.plateNumber, searchForm.value.vehicleType)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function handleAdd() {
  form.value = { plateNumber: '', vehicleType: '', brand: '', model: '', color: '', seatCount: 5, fuelType: '汽油', status: 1 }
  dialogVisible.value = true
}

function handleEdit(row: Vehicle) {
  form.value = { ...row }
  dialogVisible.value = true
}

function handleSubmit() {
  const submitData = { ...form.value }
  if (submitData.purchaseDate && submitData.purchaseDate instanceof Date) {
    submitData.purchaseDate = formatDate(submitData.purchaseDate)
  }
  if (submitData.insuranceExpire && submitData.insuranceExpire instanceof Date) {
    submitData.insuranceExpire = formatDate(submitData.insuranceExpire)
  }
  if (submitData.inspectionExpire && submitData.inspectionExpire instanceof Date) {
    submitData.inspectionExpire = formatDate(submitData.inspectionExpire)
  }
  const action = form.value.id ? updateVehicle(form.value.id, submitData) : createVehicle(submitData)
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
  deleteVehicle(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleExport() {
  exportVehicles(searchForm.value.plateNumber, searchForm.value.vehicleType).then(res => {
    const blob = new Blob([res.data], { type: 'text/csv;charset=UTF-8' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `车辆信息_${new Date().toISOString().split('T')[0]}.csv`
    link.click()
    URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  }).catch(() => ElMessage.error('导出失败'))
}

function getStatusLabel(status?: number) {
  if (!status) return '停用'
  const map: Record<number, string> = { 0: '停用', 1: '在用', 2: '维修中', 3: '已报废' }
  return map[status] || '未知'
}

onMounted(() => { fetchList() })
</script>