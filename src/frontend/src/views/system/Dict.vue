<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElPagination } from 'element-plus'
import { getDictTypeList, createDictType, updateDictType, deleteDictType, getDictItems, createDictItem, deleteDictItem, type SysDictType, type SysDictItem } from '@/api/dict'

const typeData = ref<SysDictType[]>([])
const itemData = ref<SysDictItem[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const typeDialogVisible = ref(false)
const itemDialogVisible = ref(false)
const typeForm = ref<SysDictType>({ dictTypeCode: '', dictTypeName: '', status: 1 })
const itemForm = ref<SysDictItem>({ dictTypeId: 0, dictItemCode: '', dictItemName: '', status: 1 })
const searchForm = ref({ dictTypeName: '' })
const currentTypeId = ref<number>(0)

function fetchTypes() {
  getDictTypeList(page.value, size.value, searchForm.value.dictTypeName)
    .then(res => { typeData.value = res.data.records; total.value = res.data.total })
}

function fetchItems(typeId: number) {
  currentTypeId.value = typeId
  getDictItems(typeId).then(res => { itemData.value = res.data })
}

function handleAddType() { typeForm.value = { dictTypeCode: '', dictTypeName: '', status: 1 }; typeDialogVisible.value = true }

function handleSubmitType() {
  const action = typeForm.value.id ? updateDictType(typeForm.value.id, typeForm.value) : createDictType(typeForm.value)
  action.then(() => { ElMessage.success('操作成功'); typeDialogVisible.value = false; fetchTypes() })
    .catch(err => ElMessage.error(err.message))
}

function handleDeleteType(id: number) {
  deleteDictType(id).then(() => { ElMessage.success('删除成功'); fetchTypes() })
    .catch(err => ElMessage.error(err.message))
}

function handleAddItem() { itemForm.value = { dictTypeId: currentTypeId.value, dictItemCode: '', dictItemName: '', status: 1 }; itemDialogVisible.value = true }

function handleSubmitItem() {
  createDictItem(itemForm.value).then(() => { ElMessage.success('操作成功'); itemDialogVisible.value = false; fetchItems(currentTypeId.value) })
    .catch(err => ElMessage.error(err.message))
}

function handleDeleteItem(id: number) {
  deleteDictItem(id).then(() => { ElMessage.success('删除成功'); fetchItems(currentTypeId.value) })
    .catch(err => ElMessage.error(err.message))
}

onMounted(() => fetchTypes())
</script>

<template>
  <div class="page-container">
    <ElInput v-model="searchForm.dictTypeName" placeholder="字典名称" style="width: 200px;" />
    <ElButton type="primary" @click="fetchTypes">搜索</ElButton>
    <ElButton type="success" @click="handleAddType">新增类型</ElButton>
    <ElTable :data="typeData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="dictTypeCode" label="编码" />
      <ElTableColumn prop="dictTypeName" label="名称" />
      <ElTableColumn label="操作">
        <template #default="scope">
          <ElButton size="small" @click="fetchItems(scope.row.id!)">查看项</ElButton>
          <ElButton size="small" @click="typeForm = { ...scope.row }; typeDialogVisible = true">编辑</ElButton>
          <ElButton size="small" type="danger" @click="handleDeleteType(scope.row.id!)">删除</ElButton>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchTypes()" @current-page-change="page = $event; fetchTypes()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <div v-if="itemData.length > 0" style="margin-top: 20px;">
      <ElButton type="success" @click="handleAddItem">新增字典项</ElButton>
      <ElTable :data="itemData" border style="width: 100%; margin-top: 10px;">
        <ElTableColumn prop="dictItemCode" label="编码" />
        <ElTableColumn prop="dictItemName" label="名称" />
        <ElTableColumn label="操作">
          <template #default="scope">
            <ElButton size="small" type="danger" @click="handleDeleteItem(scope.row.id!)">删除</ElButton>
          </template>
        </ElTableColumn>
      </ElTable>
    </div>
    <ElDialog v-model="typeDialogVisible" title="字典类型设置">
      <ElForm :model="typeForm" label-width="100px">
        <ElFormItem label="编码" required><ElInput v-model="typeForm.dictTypeCode" /></ElFormItem>
        <ElFormItem label="名称" required><ElInput v-model="typeForm.dictTypeName" /></ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="typeDialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmitType">确定</ElButton>
      </template>
    </ElDialog>
    <ElDialog v-model="itemDialogVisible" title="字典项设置">
      <ElForm :model="itemForm" label-width="100px">
        <ElFormItem label="编码" required><ElInput v-model="itemForm.dictItemCode" /></ElFormItem>
        <ElFormItem label="名称" required><ElInput v-model="itemForm.dictItemName" /></ElFormItem>
      </ElForm>
      <template #footer>
        <ElButton @click="itemDialogVisible = false">取消</ElButton>
        <ElButton type="primary" @click="handleSubmitItem">确定</ElButton>
      </template>
    </ElDialog>
  </div>
</template>

<style scoped>
.page-container { background: #fff; padding: 20px; border-radius: 8px; }
</style>
