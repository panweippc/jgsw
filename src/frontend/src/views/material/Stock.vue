<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElInput, ElButton, ElPagination } from 'element-plus'
import { getStockList, type MaterialStock } from '@/api/material'

const tableData = ref<MaterialStock[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const searchForm = ref({ materialId: undefined, warehouseId: undefined })

function fetchList() {
  getStockList(page.value, size.value, searchForm.value.materialId, searchForm.value.warehouseId)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

onMounted(() => fetchList())
</script>

<template>
  <div class="page-container">
    <ElInput v-model.number="searchForm.materialId" placeholder="物资ID" style="width: 150px;" />
    <ElInput v-model.number="searchForm.warehouseId" placeholder="仓库ID" style="width: 150px;" />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="materialId" label="物资" />
      <ElTableColumn prop="warehouseId" label="仓库" />
      <ElTableColumn prop="quantity" label="总数量" />
      <ElTableColumn prop="availableQuantity" label="可用数量" />
      <ElTableColumn prop="lockedQuantity" label="锁定数量" />
      <ElTableColumn prop="lastInboundTime" label="最后入库时间" />
      <ElTableColumn prop="lastOutboundTime" label="最后出库时间" />
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
  </div>
</template>

<style scoped>
.page-container { background: #fff; padding: 20px; border-radius: 8px; }
</style>
