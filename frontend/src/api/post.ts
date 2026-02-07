import apiClient from './client'
import type { Post, PostRequest, PageResponse, Category, CategoryRequest, Tag, TagRequest } from '@/types'

export const postApi = {
  getPublished(page = 0, size = 10) {
    return apiClient.get<PageResponse<Post>>('/api/posts', { params: { page, size } })
  },

  getBySlug(slug: string) {
    return apiClient.get<Post>(`/api/posts/${slug}`)
  },

  getAll(page = 0, size = 10) {
    return apiClient.get<PageResponse<Post>>('/api/admin/posts', { params: { page, size } })
  },

  create(data: PostRequest) {
    return apiClient.post<Post>('/api/admin/posts', data)
  },

  update(id: number, data: PostRequest) {
    return apiClient.put<Post>(`/api/admin/posts/${id}`, data)
  },

  delete(id: number) {
    return apiClient.delete(`/api/admin/posts/${id}`)
  },
}

export const categoryApi = {
  getAll() {
    return apiClient.get<Category[]>('/api/categories')
  },

  create(data: CategoryRequest) {
    return apiClient.post<Category>('/api/admin/categories', data)
  },

  update(id: number, data: CategoryRequest) {
    return apiClient.put<Category>(`/api/admin/categories/${id}`, data)
  },

  delete(id: number) {
    return apiClient.delete(`/api/admin/categories/${id}`)
  },
}

export const tagApi = {
  getAll() {
    return apiClient.get<Tag[]>('/api/tags')
  },

  create(data: TagRequest) {
    return apiClient.post<Tag>('/api/admin/tags', data)
  },

  delete(id: number) {
    return apiClient.delete(`/api/admin/tags/${id}`)
  },
}
