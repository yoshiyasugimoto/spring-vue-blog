export interface User {
  id: number
  username: string
  displayName: string
  role: string
}

export interface Category {
  id: number
  name: string
  slug: string
  description: string
}

export interface Tag {
  id: number
  name: string
  slug: string
}

export interface Post {
  id: number
  title: string
  slug: string
  content: string
  excerpt: string
  coverImage: string
  status: string
  category: Category | null
  author: User | null
  tags: Tag[]
  publishedAt: string
  createdAt: string
  updatedAt: string
}

export interface PostRequest {
  title: string
  slug?: string
  content: string
  excerpt?: string
  coverImage?: string
  status: string
  categoryId?: number | null
  tagIds?: number[]
}

export interface CategoryRequest {
  name: string
  slug?: string
  description?: string
}

export interface TagRequest {
  name: string
  slug?: string
}

export interface PageResponse<T> {
  content: T[]
  totalElements: number
  totalPages: number
  number: number
  size: number
}
