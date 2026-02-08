<template>
  <div>
    <a-spin :spinning="postStore.loading">
      <div v-if="postStore.currentPost" class="max-w-full">
        <article>
          <div class="mb-8">
            <div class="flex items-center gap-2 mb-4">
              <a-tag v-if="postStore.currentPost.category" color="blue" :bordered="false">
                {{ postStore.currentPost.category.name }}
              </a-tag>
              <span class="text-sm text-text-muted">{{ formatDateFull(postStore.currentPost.publishedAt) }}</span>
            </div>
            <h1 class="text-4xl font-bold leading-tight text-text-primary m-0 mb-3">{{ postStore.currentPost.title }}</h1>
            <div class="text-sm text-text-secondary" v-if="postStore.currentPost.author">
              {{ postStore.currentPost.author.displayName || postStore.currentPost.author.username }}
            </div>
          </div>
          <div
            v-if="postStore.currentPost.coverImage"
            class="mb-8 rounded-lg overflow-hidden"
          >
            <img :src="postStore.currentPost.coverImage" :alt="postStore.currentPost.title" class="w-full max-h-[400px] object-cover" />
          </div>
          <div class="markdown-body markdown-body--detail" v-html="renderedContent"></div>
          <div v-if="postStore.currentPost.tags?.length" class="mt-8 flex gap-1 flex-wrap">
            <a-tag v-for="tag in postStore.currentPost.tags" :key="tag.id" :bordered="false">
              {{ tag.name }}
            </a-tag>
          </div>
        </article>
        <a-divider />
        <router-link to="/">
          <a-button type="text">&larr; 記事一覧に戻る</a-button>
        </router-link>
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { usePostStore } from '@/stores/post'
import { marked } from '@/utils/markdown'
import { formatDate } from '@/utils/date'
import 'highlight.js/styles/github.css'

const route = useRoute()
const postStore = usePostStore()

const renderedContent = computed(() => {
  if (!postStore.currentPost?.content) return ''
  return marked.parse(postStore.currentPost.content) as string
})

const formatDateFull = (dateStr: string) => formatDate(dateStr, true)

onMounted(() => {
  const slug = route.params.slug as string
  postStore.fetchBySlug(slug)
})
</script>
