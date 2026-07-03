import request from './request'

export interface SysPost {
  id?: number
  postCode: string
  postName: string
  sortOrder?: number
  status?: number
}

export function getPostList(page: number, size: number, postName?: string) {
  return request.get<{ records: SysPost[]; total: number; size: number; current: number }>(
    '/system/post',
    { params: { page, size, postName } }
  )
}

export function getPostById(id: number) {
  return request.get<SysPost>(`/system/post/${id}`)
}

export function createPost(data: SysPost) {
  return request.post<SysPost>('/system/post', data)
}

export function updatePost(id: number, data: SysPost) {
  return request.put<SysPost>(`/system/post/${id}`, data)
}

export function deletePost(id: number) {
  return request.delete(`/system/post/${id}`)
}

export function getAllPosts() {
  return request.get<SysPost[]>('/system/post/all')
}
