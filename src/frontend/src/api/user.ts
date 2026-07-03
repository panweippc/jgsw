import request from './request'

export interface SysUser {
  id?: number
  username: string
  password?: string
  realName: string
  phone?: string
  email?: string
  deptId?: number
  postId?: number
  isAdmin?: boolean
  status?: number
  roleIds?: number[]
}

export interface PageResponse<T> {
  records: T[]
  total: number
  size: number
  current: number
}

export function getUserList(page: number, size: number, username?: string, realName?: string) {
  return request.get<PageResponse<SysUser>>('/system/user', {
    params: { page, size, username, realName }
  })
}

export function getUserById(id: number) {
  return request.get<SysUser>(`/system/user/${id}`)
}

export function createUser(data: SysUser) {
  return request.post<SysUser>('/system/user', data)
}

export function updateUser(id: number, data: SysUser) {
  return request.put<SysUser>(`/system/user/${id}`, data)
}

export function deleteUser(id: number) {
  return request.delete(`/system/user/${id}`)
}

export function resetPassword(id: number) {
  return request.put(`/system/user/${id}/reset-password`)
}

export function getAllUsers() {
  return request.get<SysUser[]>('/system/user/all')
}
