import request from './request'

export interface Vehicle {
  id?: number
  plateNumber: string
  vehicleType?: string
  brand?: string
  model?: string
  color?: string
  seatCount?: number
  purchaseDate?: string | Date
  mileage?: number
  fuelType?: string
  engineNo?: string
  vin?: string
  insuranceExpire?: string | Date
  inspectionExpire?: string | Date
  status?: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface Driver {
  id?: number
  driverName: string
  phone?: string
  driverLicense: string
  licenseType?: string
  licenseExpire?: string | Date
  hireDate?: string | Date
  department?: string
  status?: number
  remark?: string
  createTime?: string
  updateTime?: string
}

export interface DispatchOrder {
  id?: number
  orderNo?: string
  vehicleId: number
  driverId: number
  applicantId?: number
  applicantName?: string
  applyDept?: string
  startPlace: string
  endPlace: string
  useDate: string | Date
  startTime?: string
  endTime?: string
  purpose?: string
  passengerCount?: number
  status?: number
  approveId?: number
  approveName?: string
  approveTime?: string
  approveRemark?: string
  actualMileage?: number
  remark?: string
  createTime?: string
  updateTime?: string
  plateNumber?: string
  driverName?: string
}

export function getVehicleList(page: number, size: number, plateNumber?: string, vehicleType?: string) {
  return request.get<{ records: Vehicle[]; total: number; size: number; current: number }>(
    '/dispatch/vehicle',
    { params: { page, size, plateNumber, vehicleType } }
  )
}

export function getVehicleById(id: number) {
  return request.get<Vehicle>(`/dispatch/vehicle/${id}`)
}

export function createVehicle(data: Vehicle) {
  return request.post<Vehicle>('/dispatch/vehicle', data)
}

export function updateVehicle(id: number, data: Vehicle) {
  return request.put<Vehicle>(`/dispatch/vehicle/${id}`, data)
}

export function deleteVehicle(id: number) {
  return request.delete(`/dispatch/vehicle/${id}`)
}

export function getAvailableVehicles() {
  return request.get<Vehicle[]>('/dispatch/vehicle/available')
}

export function getAllVehicles() {
  return request.get<Vehicle[]>('/dispatch/vehicle/all')
}

export function getDriverList(page: number, size: number, driverName?: string, department?: string) {
  return request.get<{ records: Driver[]; total: number; size: number; current: number }>(
    '/dispatch/driver',
    { params: { page, size, driverName, department } }
  )
}

export function getDriverById(id: number) {
  return request.get<Driver>(`/dispatch/driver/${id}`)
}

export function createDriver(data: Driver) {
  return request.post<Driver>('/dispatch/driver', data)
}

export function updateDriver(id: number, data: Driver) {
  return request.put<Driver>(`/dispatch/driver/${id}`, data)
}

export function deleteDriver(id: number) {
  return request.delete(`/dispatch/driver/${id}`)
}

export function getAvailableDrivers() {
  return request.get<Driver[]>('/dispatch/driver/available')
}

export function getAllDrivers() {
  return request.get<Driver[]>('/dispatch/driver/all')
}

export function getOrderList(page: number, size: number, orderNo?: string, status?: number, useDate?: string) {
  return request.get<{ records: DispatchOrder[]; total: number; size: number; current: number }>(
    '/dispatch/order',
    { params: { page, size, orderNo, status, useDate } }
  )
}

export function getOrderById(id: number) {
  return request.get<DispatchOrder>(`/dispatch/order/${id}`)
}

export function createOrder(data: DispatchOrder) {
  return request.post<DispatchOrder>('/dispatch/order', data)
}

export function updateOrder(id: number, data: DispatchOrder) {
  return request.put<DispatchOrder>(`/dispatch/order/${id}`, data)
}

export function approveOrder(id: number, status: number, approveRemark?: string) {
  return request.put<DispatchOrder>(`/dispatch/order/${id}/approve`, { params: { status, approveRemark } })
}

export function completeOrder(id: number, actualMileage?: number) {
  return request.put<DispatchOrder>(`/dispatch/order/${id}/complete`, { params: { actualMileage } })
}

export function cancelOrder(id: number) {
  return request.put<DispatchOrder>(`/dispatch/order/${id}/cancel`)
}

export function deleteOrder(id: number) {
  return request.delete(`/dispatch/order/${id}`)
}

export function exportOrders(status?: number) {
  return request.get('/dispatch/order/export', { params: { status }, responseType: 'blob' })
}

export function exportVehicles(plateNumber?: string, vehicleType?: string) {
  return request.get('/dispatch/vehicle/export', { params: { plateNumber, vehicleType }, responseType: 'blob' })
}

export function exportDrivers(driverName?: string, department?: string) {
  return request.get('/dispatch/driver/export', { params: { driverName, department }, responseType: 'blob' })
}