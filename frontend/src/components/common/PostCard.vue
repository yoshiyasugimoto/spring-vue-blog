<template>
  <router-link :to="`/post/${post.slug}`" class="no-underline text-inherit block">
    <div class="py-6 border-b border-border-light transition-opacity duration-200 hover:opacity-80">
      <div v-if="post.coverImage" class="mb-4 rounded-lg overflow-hidden">
        <img :src="post.coverImage" :alt="post.title" class="w-full h-50 object-cover" />
      </div>
      <div class="post-info">
        <div class="flex items-center gap-2 mb-2">
          <a-tag v-if="post.category" color="blue" :bordered="false">{{ post.category.name }}</a-tag>
          <span class="text-[13px] text-text-muted">{{ formatDateFull(post.publishedAt) }}</span>
        </div>
        <h3 class="text-xl font-semibold text-text-primary m-0 mb-2 leading-snug">{{ post.title }}</h3>
        <p v-if="post.excerpt" class="text-text-secondary text-sm leading-relaxed m-0 mb-3">{{ post.excerpt }}</p>
        <div v-if="post.tags?.length" class="flex gap-1 flex-wrap">
          <a-tag v-for="tag in post.tags" :key="tag.id" :bordered="false">{{ tag.name }}</a-tag>
        </div>
      </div>
    </div>
  </router-link>
</template>

<script setup lang="ts">
import type { Post } from '@/types'
import { formatDate } from '@/utils/date'

defineProps<{
  post: Post
}>()

const formatDateFull = (dateStr: string) => formatDate(dateStr, true)
</script>
