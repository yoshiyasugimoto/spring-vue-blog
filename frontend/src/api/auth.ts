import apiClient from './client'
import type { User } from '@/types'

export const authApi = {
  login(username: string, password: string) {
    return apiClient.post<User>('/api/auth/login', { username, password })
  },

  logout() {
    return apiClient.post('/api/auth/logout')
  },

  me() {
    return apiClient.get<User>('/api/auth/me')
  },
}
