<template>
  <div>
    <a-spin :spinning="postStore.loading">
      <div v-if="postStore.currentPost" class="post-detail">
        <article>
          <div class="post-header">
            <div class="post-meta">
              <a-tag v-if="postStore.currentPost.category" color="blue" :bordered="false">
                {{ postStore.currentPost.category.name }}
              </a-tag>
              <span class="post-date">{{ formatDateFull(postStore.currentPost.publishedAt) }}</span>
            </div>
            <h1 class="post-title">{{ postStore.currentPost.title }}</h1>
            <div class="post-author" v-if="postStore.currentPost.author">
              {{ postStore.currentPost.author.displayName || postStore.currentPost.author.username }}
            </div>
          </div>
          <div
            v-if="postStore.currentPost.coverImage"
            class="post-cover"
          >
            <img :src="postStore.currentPost.coverImage" :alt="postStore.currentPost.title" />
          </div>
          <div class="markdown-body" v-html="renderedContent"></div>
          <div v-if="postStore.currentPost.tags?.length" class="post-tags">
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

<style scoped>
.post-detail {
  max-width: 100%;
}

.post-header {
  margin-bottom: 32px;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 16px;
}

.post-date {
  font-size: 14px;
  color: #999;
}

.post-title {
  font-size: 36px;
  font-weight: 700;
  line-height: 1.3;
  color: #1a1a1a;
  margin: 0 0 12px;
}

.post-author {
  font-size: 14px;
  color: #666;
}

.post-cover {
  margin-bottom: 32px;
  border-radius: 8px;
  overflow: hidden;
}

.post-cover img {
  width: 100%;
  max-height: 400px;
  object-fit: cover;
}

.markdown-body {
  font-size: 16px;
  line-height: 1.8;
  color: #333;
}

.markdown-body :deep(h1) {
  font-size: 28px;
  font-weight: 700;
  margin: 32px 0 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.markdown-body :deep(h2) {
  font-size: 24px;
  font-weight: 600;
  margin: 28px 0 12px;
}

.markdown-body :deep(h3) {
  font-size: 20px;
  font-weight: 600;
  margin: 24px 0 8px;
}

.markdown-body :deep(p) {
  margin: 0 0 16px;
}

.markdown-body :deep(pre) {
  background: #f6f8fa;
  border-radius: 6px;
  padding: 16px;
  overflow-x: auto;
  margin: 20px 0;
}

.markdown-body :deep(code) {
  font-family: 'SF Mono', 'Monaco', 'Menlo', monospace;
  font-size: 14px;
}

.markdown-body :deep(p code) {
  background: #f0f0f0;
  padding: 2px 6px;
  border-radius: 3px;
}

.markdown-body :deep(blockquote) {
  border-left: 3px solid #ddd;
  padding: 4px 16px;
  color: #666;
  margin: 20px 0;
}

.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  padding-left: 24px;
  margin: 12px 0;
}

.markdown-body :deep(img) {
  max-width: 100%;
  border-radius: 4px;
}

.markdown-body :deep(hr) {
  border: none;
  border-top: 1px solid #eee;
  margin: 32px 0;
}

.markdown-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 20px 0;
}

.markdown-body :deep(th),
.markdown-body :deep(td) {
  border: 1px solid #ddd;
  padding: 8px 12px;
}

.markdown-body :deep(th) {
  background: #f6f8fa;
  font-weight: 600;
}

.post-tags {
  margin-top: 32px;
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}
</style>
