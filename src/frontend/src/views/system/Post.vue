<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElMessage, ElPagination } from 'element-plus'
import { getPostList, createPost, updatePost, deletePost, type SysPost } from '@/api/post'

const tableData = ref<SysPost[]>([])
const total = ref(0)
const page = ref(1)
const size = ref(10)
const dialogVisible = ref(false)
const form = ref<SysPost>({ postCode: '', postName: '', status: 1 })
const searchForm = ref({ postName: '' })

function fetchList() {
  getPostList(page.value, size.value, searchForm.value.postName)
    .then(res => { tableData.value = res.data.records; total.value = res.data.total })
}

function handleAdd() { form.value = { postCode: '', postName: '', status: 1 }; dialogVisible.value = true }

function handleSubmit() {
  const action = form.value.id ? updatePost(form.value.id, form.value) : createPost(form.value)
  action.then(() => { ElMessage.success('操作成功'); dialogVisible.value = false; fetchList() })
    .catch(err => ElMessage.error(err.message))
}

function handleDelete(id: number) {
  deletePost(id).then(() => { ElMessage.success('删除成功'); fetchList() })
    .catch(err => ElMessage.error(err.message))
}

onMounted(() => fetchList())
</script>

<template>
  <div class="page-container">
    <ElInput v-model="searchForm.postName" placeholder="岗位名称" style="width: 200px;" />
    <ElButton type="primary" @click="fetchList">搜索</ElButton>
    <ElButton type="success" @click="handleAdd">新增</ElButton>
    <ElTable :data="tableData" border style="width: 100%; margin-top: 20px;">
      <ElTableColumn prop="id" label="ID" width="80" />
      <ElTableColumn prop="postCode" label="编码" />
      <ElTableColumn prop="postName" label="名称" />
      <ElTableColumn prop="status" label="状态">
        <template #default="scope">{{ scope.row.status === 1 ? '启用' : '禁用' }}</template>
      </ElTableColumn>
      <ElTableColumn label="操作" width="150" fixed="right">
        <template #default="scope">
          <div class="action-buttons">
            <ElButton size="small" @click="form = { ...(scope.row as SysPost) }; dialogVisible = true">编辑</ElButton>
            <ElButton size="small" type="danger" @click="handleDelete((scope.row as SysPost).id!)">删除</ElButton>
          </div>
        </template>
      </ElTableColumn>
    </ElTable>
    <ElPagination @size-change="size = $event; fetchList()" @current-page-change="page = $event; fetchList()"
      :current-page="page" :page-size="size" :total="total" layout="total, sizes, prev, pager, next, jumper" />
    <ElDialog v-model="dialogVisible" title="岗位设置">
      <ElForm :model="form" label-width="100px">
        <ElFormItem label="编码" required><ElInput v-model="form.postCode" /></ElFormItem>
        <ElFormItem label="名称" required><ElInput v-model="form.postName" /></ElFormItem>
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
