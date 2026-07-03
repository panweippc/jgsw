import request from './request'

export interface LoginRequest {
  username: string
  password: string
}

export interface LoginResponse {
  token: string
  user: {
    id: number
    username: string
    realName: string
    isAdmin: boolean
  }
}

export function login(data: LoginRequest) {
  return request.post<LoginResponse>('/auth/login', data)
}

export function logout() {
  return request.post('/auth/logout')
}

export function getUserInfo() {
  return request.get('/auth/info')
}
