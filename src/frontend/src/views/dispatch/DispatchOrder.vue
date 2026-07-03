<template>
  <div class="page-container">
    <ElInput v-model="searchForm.orderNo" placeholder="派车单号" style="width: 200px;" />
    <ElSelect v-model="searchForm.status" placeholder="状态" style="width: 120px;" clearable>
      <ElOption label="待审批" :value="0" />
      <ElOption label="已批准" :value="1" />
      <ElOption label="已拒绝" :value="2" />
      <ElOption label="已派车" :value="3" />
      <ElOption label="已完成" :value="4" />
      <ElOption label="已取消" :value="5" />
    </ElSelect>
    <ElDatePicker v-model="searchForm.useDate" type="date" placeholder="用车日期" style="width: 180px;" clearable />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增</ElButton>
    <ElButton type="warning" @click="handleExport">导出</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="orderNo" label="派车单号" />
      <ElTableColumn prop="plateNumber" label="车牌号" />
      <ElTableColumn prop="driverName" label="驾驶员" />
      <ElTableColumn prop="applicantName" label="申请人" />
      <ElTableColumn prop="applyDept" label="申请部门" />
      <ElTableColumn prop="startPlace" label="出发地点" />
      <ElTableColumn prop="endPlace" label="目的地" />
      <ElTableColumn prop="useDate" label="用车日期" />
      <ElTableColumn prop="purpose" label="用车事由" />
      <ElTableColumn prop="passengerCount" label="乘车人数" width="100" />
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">{{ getStatusLabel(scope.row.status) }}</template>
      </ElTableColumn>
      <ElTableColumn prop="actualMileage" label="实际里程" width="100" />
      <ElTableColumn label="操作" width="300" fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <template v-if="scope.row.status === 0">
              <ElButton size="small" @click="handleEdit(scope.row as DispatchOrder)">编辑</ElButton>
              <ElButton size="small" type="success" @click="handleApprove(scope.row as DispatchOrder, 1)">通过</ElButton>
              <ElButton size="small" type="danger" @click="handleApprove(scope.row as DispatchOrder, 2)">拒绝</ElButton>
            </template>
            <template v-else-if="scope.row.status === 3">
              <ElButton size="small" type="success" @click="handleComplete(scope.row as DispatchOrder)">完成</ElButton>
              <ElButton size="small" type="warning" @click="handleCancel(scope.row as DispatchOrder)">取消</ElButton>
            </template>
            <template v-else-if="scope.row.status !== 4">
              <ElButton size="small" type="warning" @click="handleCancel(scope.row as DispatchOrder)">取消</ElButton>
            </template>
            <template v-if="scope.row.status !== 4">
              <ElButton size="small" type="danger" @click="handleDelete((scope.row as DispatchOrder).id!)">删除</ElButton>
            </template>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="派车单信息">
      <ElForm :model="form" label-width="120px">
        <ElFormItem label="车辆" required>
          <ElSelect v-model="form.vehicleId" placeholder="请选择车辆">
            <ElOption v-for="v in vehicles" :key="v.id" :label="v.plateNumber + ' (' + v.brand + ' ' + v.model + ')'" :value="v.id || 0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="驾驶员" required>
          <ElSelect v-model="form.driverId" placeholder="请选择驾驶员">
            <ElOption v-for="d in drivers" :key="d.id" :label="d.driverName + ' (' + d.licenseType + ')'" :value="d.id || 0" />
          </ElSelect>
        </ElFormItem>
        <ElFormItem label="申请人"><ElInput v-model="form.applicantName" /></ElFormItem>
        <ElFormItem label="申请部门"><ElInput v-model="form.applyDept" /></ElFormItem>
        <ElFormItem label="出发地点" required><ElInput v-model="form.startPlace" /></ElFormItem>
        <ElFormItem label="目的地" required><ElInput v-model="form.endPlace" /></ElFormItem>
        <ElFormItem label="用车日期" required><ElDatePicker v-model="form.useDate" type="date" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="出发时间"><ElTimePicker v-model="form.startTime" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="预计返回时间"><ElTimePicker v-model="form.endTime" style="width: 100%;" /></ElFormItem>
        <ElFormItem label="用车事由"><ElInput v-model="form.purpose" type="textarea" /></ElFormItem>
        <ElFormItem label="乘车人数"><ElInput v-model.number="form.passengerCount" type="number" /></ElFormItem>
        <ElFormItem label="备注"><ElInput v-model="form.remark" type="textarea" /></ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="dialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmit">确定</ElButton>
      </template>
    </ElDialog>
    <ElDialog v-model="approveVisible" title="审批意见">
      <ElForm :model="approveForm" label-width="100px">
        <ElFormItem label="审批意见"><ElInput v-model="approveForm.remark" type="textarea" :rows="4" /></ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="approveVisible = false">取消</ElButton>
        <ElButton type="primary" @click="submitApprove">确定</ElButton>
      </template>
    </ElDialog>
    <ElDialog v-model="completeVisible" title="完成派车">
      <ElForm :model="completeForm" label-width="100px">
        <ElFormItem label="实际里程(公里)"><ElInput v-model.number="completeForm.actualMileage" type="number" /></ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="completeVisible = false">取消</ElButton>
        <ElButton type="primary" @click="submitComplete">确定</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElMessageBox, ElPagination, ElSelect, ElOption, ElDatePicker, ElTimePicker } from 'element-plus'
import { getOrderList, createOrder, updateOrder, approveOrder, completeOrder, cancelOrder, deleteOrder, exportOrders, getAvailableVehicles, getAvailableDrivers, type DispatchOrder, type Vehicle, type Driver } from '@/api/dispatch'

const tableData = ref<DispatchOrder[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const approveVisible = ref(false)
const completeVisible = ref(false)
const form = ref<DispatchOrder>({ vehicleId: 0, driverId: 0, startPlace: '', endPlace: '', useDate: '' })
const searchForm = ref({ orderNo: '', status: undefined, useDate: '' })
const vehicles = ref<Vehicle[]>([])
const drivers = ref<Driver[]>([])
const approveForm = ref({ remark: '' })
const completeForm = ref({ actualMileage: 0 })
const currentApproveId = ref(0)
const currentApproveStatus = ref(0)
const currentCompleteId = ref(0)

function fetchList() {
  getOrderList(page.value, size.value, searchForm.value.orderNo, searchForm.value.status, searchForm.value.useDate)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function fetchVehicles() {
  getAvailableVehicles().then(res => { vehicles.value = res.data })
}

function fetchDrivers() {
  getAvailableDrivers().then(res => { drivers.value = res.data })
}

function handleAdd() {
  form.value = { vehicleId: 0, driverId: 0, startPlace: '', endPlace: '', useDate: '', passengerCount: 1 }
  dialogVisible.value = true
}

function handleEdit(row: DispatchOrder) {
  form.value = { ...row }
  dialogVisible.value = true
}

function handleSubmit() {
  const submitData = { ...form.value }
  if (submitData.useDate && submitData.useDate instanceof Date) {
    submitData.useDate = formatDate(submitData.useDate)
  }
  const action = form.value.id ? updateOrder(form.value.id, submitData) : createOrder(submitData)
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
  deleteOrder(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleApprove(row: DispatchOrder, status: number) {
  currentApproveId.value = row.id!
  currentApproveStatus.value = status
  approveForm.value = { remark: '' }
  approveVisible.value = true
}

function submitApprove() {
  approveOrder(currentApproveId.value, currentApproveStatus.value, approveForm.value.remark)
    .then(() => { ElMessage.success('审批成功'); approveVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleComplete(row: DispatchOrder) {
  currentCompleteId.value = row.id!
  completeForm.value = { actualMileage: 0 }
  completeVisible.value = true
}

function submitComplete() {
  completeOrder(currentCompleteId.value, completeForm.value.actualMileage)
    .then(() => { ElMessage.success('派车单已完成'); completeVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleCancel(row: DispatchOrder) {
  ElMessageBox.confirm('确定取消此派车单吗？', '提示', { type: 'warning' }).then(() => {
    cancelOrder(row.id!).then(() => { ElMessage.success('已取消'); fetchList() })
      .catch(() => ElMessage.error('取消失败'))
  })
}

function handleExport() {
  exportOrders(searchForm.value.status).then(res => {
    const blob = new Blob([res.data], { type: 'text/csv;charset=UTF-8' })
    const url = URL.createObjectURL(blob)
    const link = document.createElement('a')
    link.href = url
    link.download = `派车单_${new Date().toISOString().split('T')[0]}.csv`
    link.click()
    URL.revokeObjectURL(url)
    ElMessage.success('导出成功')
  }).catch(() => ElMessage.error('导出失败'))
}

function getStatusLabel(status?: number) {
  if (!status) return '待审批'
  const map: Record<number, string> = { 0: '待审批', 1: '已批准', 2: '已拒绝', 3: '已派车', 4: '已完成', 5: '已取消' }
  return map[status] || '未知'
}

onMounted(() => { fetchList(); fetchVehicles(); fetchDrivers() })
</script>