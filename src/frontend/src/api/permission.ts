import request from './request'

export interface SysPermission {
  id?: number
  parentId: number
  permissionName: string
  permissionCode: string
  icon?: string
  type: number
  sortOrder?: number
  path?: string
  component?: string
  status?: number
  children?: SysPermission[]
}

export function getPermissionTree() {
  return request.get<SysPermission[]>('/system/permission/tree')
}

export function getPermissionById(id: number) {
  return request.get<SysPermission>(`/system/permission/${id}`)
}

export function createPermission(data: SysPermission) {
  return request.post<SysPermission>('/system/permission', data)
}

export function updatePermission(id: number, data: SysPermission) {
  return request.put<SysPermission>(`/system/permission/${id}`, data)
}

export function deletePermission(id: number) {
  return request.delete(`/system/permission/${id}`)
}

export function getAllPermissions() {
  return request.get<SysPermission[]>('/system/permission/all')
}
