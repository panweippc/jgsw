import request from './request'

export interface SysDictType {
  id?: number
  dictTypeCode: string
  dictTypeName: string
  description?: string
  sortOrder?: number
  status?: number
}

export interface SysDictItem {
  id?: number
  dictTypeId: number
  dictItemCode: string
  dictItemName: string
  sortOrder?: number
  status?: number
}

export function getDictTypeList(page: number, size: number, dictTypeName?: string) {
  return request.get<{ records: SysDictType[]; total: number; size: number; current: number }>(
    '/system/dict/type',
    { params: { page, size, dictTypeName } }
  )
}

export function getDictTypeById(id: number) {
  return request.get<SysDictType>(`/system/dict/type/${id}`)
}

export function createDictType(data: SysDictType) {
  return request.post<SysDictType>('/system/dict/type', data)
}

export function updateDictType(id: number, data: SysDictType) {
  return request.put<SysDictType>(`/system/dict/type/${id}`, data)
}

export function deleteDictType(id: number) {
  return request.delete(`/system/dict/type/${id}`)
}

export function getDictItems(typeId: number) {
  return request.get<SysDictItem[]>(`/system/dict/items/${typeId}`)
}

export function createDictItem(data: SysDictItem) {
  return request.post<SysDictItem>('/system/dict/item', data)
}

export function updateDictItem(id: number, data: SysDictItem) {
  return request.put<SysDictItem>(`/system/dict/item/${id}`, data)
}

export function deleteDictItem(id: number) {
  return request.delete(`/system/dict/item/${id}`)
}
