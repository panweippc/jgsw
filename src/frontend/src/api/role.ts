import request from './request'

export interface SysRole {
  id?: number
  roleName: string
  roleCode: string
  description?: string
  status?: number
  permissionIds?: number[]
}

export function getRoleList(page: number, size: number, roleName?: string) {
  return request.get<{ records: SysRole[]; total: number; size: number; current: number }>(
    '/system/role',
    { params: { page, size, roleName } }
  )
}

export function getRoleById(id: number) {
  return request.get<SysRole>(`/system/role/${id}`)
}

export function createRole(data: SysRole) {
  return request.post<SysRole>('/system/role', data)
}

export function updateRole(id: number, data: SysRole) {
  return request.put<SysRole>(`/system/role/${id}`, data)
}

export function deleteRole(id: number) {
  return request.delete(`/system/role/${id}`)
}

export function getAllRoles() {
  return request.get<SysRole[]>('/system/role/all')
}
