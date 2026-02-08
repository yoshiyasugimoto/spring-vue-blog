import type { DecoratorFunction } from '@storybook/types'
import { useAuthStore } from '@/stores/auth'
import { usePostStore } from '@/stores/post'
import { postApi, categoryApi, tagApi } from '@/api/post'
import { authApi } from '@/api/auth'
import { mockUser, mockPosts, mockCategories, mockTags, mockPageResponse } from './data'
import type { Post } from '@/types'

export function withAuthenticatedUser(): DecoratorFunction {
  return (story) => {
    const authStore = useAuthStore()
    authStore.user = mockUser
    authStore.isAuthenticated = true
    authStore.loading = false
    authStore.fetchUser = async () => {}
    authStore.logout = async () => {
      authStore.user = null
      authStore.isAuthenticated = false
    }
    return story()
  }
}

export function withUnauthenticatedUser(): DecoratorFunction {
  return (story) => {
    const authStore = useAuthStore()
    authStore.user = null
    authStore.isAuthenticated = false
    authStore.loading = false
    authStore.fetchUser = async () => {}
    return story()
  }
}

export function withPopulatedPostStore(posts: Post[] = mockPosts): DecoratorFunction {
  return (story) => {
    const postStore = usePostStore()
    postStore.posts = posts
    postStore.currentPost = posts[0] || null
    postStore.totalElements = posts.length
    postStore.totalPages = Math.ceil(posts.length / 10)
    postStore.currentPage = 0
    postStore.loading = false
    postStore.fetchPublished = async () => {}
    postStore.fetchAll = async () => {}
    postStore.fetchBySlug = async () => {}
    postStore.deletePost = async () => {}
    return story()
  }
}

export function withLoadingPostStore(): DecoratorFunction {
  return (story) => {
    const postStore = usePostStore()
    postStore.posts = []
    postStore.currentPost = null
    postStore.totalElements = 0
    postStore.totalPages = 0
    postStore.currentPage = 0
    postStore.loading = true
    postStore.fetchPublished = async () => {}
    postStore.fetchAll = async () => {}
    postStore.fetchBySlug = async () => {}
    return story()
  }
}

export function withMockedApis(): DecoratorFunction {
  return (story) => {
    // Post API
    postApi.getPublished = async (page = 0, size = 10) =>
      ({ data: mockPageResponse(mockPosts.filter(p => p.status === 'PUBLISHED'), page, size) }) as any
    postApi.getAll = async (page = 0, size = 10) =>
      ({ data: mockPageResponse(mockPosts, page, size) }) as any
    postApi.getBySlug = async (slug: string) =>
      ({ data: mockPosts.find(p => p.slug === slug) || mockPosts[0] }) as any
    postApi.create = async (data) =>
      ({ data: { ...mockPosts[0], ...data, id: 999 } }) as any
    postApi.update = async (_id, data) =>
      ({ data: { ...mockPosts[0], ...data } }) as any
    postApi.delete = async () => ({}) as any

    // Category API
    categoryApi.getAll = async () => ({ data: mockCategories }) as any
    categoryApi.create = async (data) =>
      ({ data: { id: 999, ...data } }) as any
    categoryApi.update = async (id, data) =>
      ({ data: { id, ...data } }) as any
    categoryApi.delete = async () => ({}) as any

    // Tag API
    tagApi.getAll = async () => ({ data: mockTags }) as any
    tagApi.create = async (data) =>
      ({ data: { id: 999, ...data } }) as any
    tagApi.delete = async () => ({}) as any

    // Auth API
    authApi.me = async () => ({ data: mockUser }) as any
    authApi.login = async () => ({ data: mockUser }) as any
    authApi.logout = async () => ({}) as any

    return story()
  }
}

export function withMockedEmptyApis(): DecoratorFunction {
  return (story) => {
    postApi.getPublished = async (page = 0, size = 10) =>
      ({ data: mockPageResponse([], page, size) }) as any
    postApi.getAll = async (page = 0, size = 10) =>
      ({ data: mockPageResponse([], page, size) }) as any
    postApi.getBySlug = async () => ({ data: mockPosts[0] }) as any
    postApi.create = async (data) => ({ data: { ...mockPosts[0], ...data, id: 999 } }) as any
    postApi.update = async (_id, data) => ({ data: { ...mockPosts[0], ...data } }) as any
    postApi.delete = async () => ({}) as any

    categoryApi.getAll = async () => ({ data: [] }) as any
    categoryApi.create = async (data) => ({ data: { id: 999, ...data } }) as any
    categoryApi.update = async (id, data) => ({ data: { id, ...data } }) as any
    categoryApi.delete = async () => ({}) as any

    tagApi.getAll = async () => ({ data: [] }) as any
    tagApi.create = async (data) => ({ data: { id: 999, ...data } }) as any
    tagApi.delete = async () => ({}) as any

    authApi.me = async () => ({ data: mockUser }) as any
    authApi.login = async () => ({ data: mockUser }) as any
    authApi.logout = async () => ({}) as any

    return story()
  }
}
