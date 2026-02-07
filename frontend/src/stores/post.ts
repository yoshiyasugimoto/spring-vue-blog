import { defineStore } from 'pinia'
import { ref } from 'vue'
import { postApi } from '@/api/post'
import type { Post, PostRequest } from '@/types'

export const usePostStore = defineStore('post', () => {
  const posts = ref<Post[]>([])
  const currentPost = ref<Post | null>(null)
  const totalElements = ref(0)
  const totalPages = ref(0)
  const currentPage = ref(0)
  const loading = ref(false)

  async function fetchPublished(page = 0, size = 10) {
    loading.value = true
    try {
      const response = await postApi.getPublished(page, size)
      posts.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = response.data.number
    } finally {
      loading.value = false
    }
  }

  async function fetchAll(page = 0, size = 10) {
    loading.value = true
    try {
      const response = await postApi.getAll(page, size)
      posts.value = response.data.content
      totalElements.value = response.data.totalElements
      totalPages.value = response.data.totalPages
      currentPage.value = response.data.number
    } finally {
      loading.value = false
    }
  }

  async function fetchBySlug(slug: string) {
    loading.value = true
    try {
      const response = await postApi.getBySlug(slug)
      currentPost.value = response.data
    } finally {
      loading.value = false
    }
  }

  async function createPost(data: PostRequest) {
    const response = await postApi.create(data)
    return response.data
  }

  async function updatePost(id: number, data: PostRequest) {
    const response = await postApi.update(id, data)
    return response.data
  }

  async function deletePost(id: number) {
    await postApi.delete(id)
  }

  return {
    posts,
    currentPost,
    totalElements,
    totalPages,
    currentPage,
    loading,
    fetchPublished,
    fetchAll,
    fetchBySlug,
    createPost,
    updatePost,
    deletePost,
  }
})
