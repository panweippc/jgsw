import request from './request'

export interface Material {
  id?: number
  materialCode: string
  materialName: string
  categoryId?: number
  specification?: string
  unit?: string
  price?: number
  description?: string
  status?: number
}

export interface MaterialCategory {
  id?: number
  parentId: number
  categoryCode: string
  categoryName: string
  sortOrder?: number
  status?: number
  children?: MaterialCategory[]
}

export interface MaterialWarehouse {
  id?: number
  warehouseCode: string
  warehouseName: string
  location?: string
  sortOrder?: number
  status?: number
}

export interface MaterialInbound {
  id?: number
  inboundNo?: string
  materialId: number
  warehouseId: number
  quantity: number
  supplier?: string
  operator?: string
  remark?: string
  status?: number
}

export interface MaterialOutbound {
  id?: number
  outboundNo?: string
  materialId: number
  warehouseId: number
  quantity: number
  receiver?: string
  operator?: string
  remark?: string
  status?: number
}

export interface MaterialApply {
  id?: number
  applyNo?: string
  materialId: number
  warehouseId: number
  quantity: number
  applicant?: string
  deptId?: number
  remark?: string
  status?: number
}

export interface MaterialStock {
  id?: number
  materialId: number
  warehouseId: number
  quantity: number
  availableQuantity: number
  lockedQuantity: number
}

export function getMaterialList(page: number, size: number, materialName?: string, categoryId?: number) {
  return request.get<{ records: Material[]; total: number; size: number; current: number }>(
    '/material/info',
    { params: { page, size, materialName, categoryId } }
  )
}

export function getMaterialById(id: number) {
  return request.get<Material>(`/material/info/${id}`)
}

export function createMaterial(data: Material) {
  return request.post<Material>('/material/info', data)
}

export function updateMaterial(id: number, data: Material) {
  return request.put<Material>(`/material/info/${id}`, data)
}

export function deleteMaterial(id: number) {
  return request.delete(`/material/info/${id}`)
}

export function getCategoryTree() {
  return request.get<MaterialCategory[]>('/material/category')
}

export function createCategory(data: MaterialCategory) {
  return request.post<MaterialCategory>('/material/category', data)
}

export function updateCategory(id: number, data: MaterialCategory) {
  return request.put<MaterialCategory>(`/material/category/${id}`, data)
}

export function deleteCategory(id: number) {
  return request.delete(`/material/category/${id}`)
}

export function getWarehouseList(page: number, size: number, warehouseName?: string) {
  return request.get<{ records: MaterialWarehouse[]; total: number; size: number; current: number }>(
    '/material/warehouse',
    { params: { page, size, warehouseName } }
  )
}

export function getAllWarehouses() {
  return request.get<MaterialWarehouse[]>('/material/warehouse/all')
}

export function createWarehouse(data: MaterialWarehouse) {
  return request.post<MaterialWarehouse>('/material/warehouse', data)
}

export function updateWarehouse(id: number, data: MaterialWarehouse) {
  return request.put<MaterialWarehouse>(`/material/warehouse/${id}`, data)
}

export function deleteWarehouse(id: number) {
  return request.delete(`/material/warehouse/${id}`)
}

export function getInboundList(page: number, size: number, inboundNo?: string, materialId?: number) {
  return request.get<{ records: MaterialInbound[]; total: number; size: number; current: number }>(
    '/material/inbound',
    { params: { page, size, inboundNo, materialId } }
  )
}

export function createInbound(data: MaterialInbound) {
  return request.post<MaterialInbound>('/material/inbound', data)
}

export function updateInbound(id: number, data: MaterialInbound) {
  return request.put<MaterialInbound>(`/material/inbound/${id}`, data)
}

export function deleteInbound(id: number) {
  return request.delete(`/material/inbound/${id}`)
}

export function getOutboundList(page: number, size: number, outboundNo?: string, materialId?: number) {
  return request.get<{ records: MaterialOutbound[]; total: number; size: number; current: number }>(
    '/material/outbound',
    { params: { page, size, outboundNo, materialId } }
  )
}

export function createOutbound(data: MaterialOutbound) {
  return request.post<MaterialOutbound>('/material/outbound', data)
}

export function updateOutbound(id: number, data: MaterialOutbound) {
  return request.put<MaterialOutbound>(`/material/outbound/${id}`, data)
}

export function deleteOutbound(id: number) {
  return request.delete(`/material/outbound/${id}`)
}

export function getApplyList(page: number, size: number, applyNo?: string, status?: number) {
  return request.get<{ records: MaterialApply[]; total: number; size: number; current: number }>(
    '/material/apply',
    { params: { page, size, applyNo, status } }
  )
}

export function createApply(data: MaterialApply) {
  return request.post<MaterialApply>('/material/apply', data)
}

export function approveApply(id: number, status: number) {
  return request.put<MaterialApply>(`/material/apply/${id}/approve`, { params: { status } })
}

export function deleteApply(id: number) {
  return request.delete(`/material/apply/${id}`)
}

export function getStockList(page: number, size: number, materialId?: number, warehouseId?: number) {
  return request.get<{ records: MaterialStock[]; total: number; size: number; current: number }>(
    '/material/stock',
    { params: { page, size, materialId, warehouseId } }
  )
}
