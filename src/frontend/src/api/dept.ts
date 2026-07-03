import request from './request'

export interface SysDept {
  id?: number
  parentId: number
  deptCode: string
  deptName: string
  leader?: string
  phone?: string
  sortOrder?: number
  status?: number
  children?: SysDept[]
}

export function getDeptTree() {
  return request.get<SysDept[]>('/system/dept')
}

export function getDeptById(id: number) {
  return request.get<SysDept>(`/system/dept/${id}`)
}

export function createDept(data: SysDept) {
  return request.post<SysDept>('/system/dept', data)
}

export function updateDept(id: number, data: SysDept) {
  return request.put<SysDept>(`/system/dept/${id}`, data)
}

export function deleteDept(id: number) {
  return request.delete(`/system/dept/${id}`)
}
