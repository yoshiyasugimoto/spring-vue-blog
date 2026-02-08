<template>
  <router-link :to="`/post/${post.slug}`" class="post-card-link">
    <div class="post-card">
      <div v-if="post.coverImage" class="post-cover">
        <img :src="post.coverImage" :alt="post.title" />
      </div>
      <div class="post-info">
        <div class="post-meta">
          <a-tag v-if="post.category" color="blue" :bordered="false">{{ post.category.name }}</a-tag>
          <span class="post-date">{{ formatDate(post.publishedAt) }}</span>
        </div>
        <h3 class="post-title">{{ post.title }}</h3>
        <p v-if="post.excerpt" class="post-excerpt">{{ post.excerpt }}</p>
        <div v-if="post.tags?.length" class="post-tags">
          <a-tag v-for="tag in post.tags" :key="tag.id" :bordered="false">{{ tag.name }}</a-tag>
        </div>
      </div>
    </div>
  </router-link>
</template>

<script setup lang="ts">
import type { Post } from '@/types'

defineProps<{
  post: Post
}>()

function formatDate(dateStr: string) {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString('ja-JP', {
    year: 'numeric',
    month: 'long',
    day: 'numeric',
  })
}
</script>

<style scoped>
.post-card-link {
  text-decoration: none;
  color: inherit;
  display: block;
}

.post-card {
  padding: 24px;
  border-bottom: 2px solid #e6e6e6;
  border-radius: 12px;
  margin-bottom: 16px;
  background: #fafafa;
  transition: box-shadow 0.2s, transform 0.2s;
}

.post-card:hover {
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.1);
  transform: translateY(-2px);
}

.post-cover {
  margin-bottom: 16px;
  border-radius: 8px;
  overflow: hidden;
}

.post-cover img {
  width: 100%;
  height: 200px;
  object-fit: cover;
}

.post-meta {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 8px;
}

.post-date {
  font-size: 13px;
  color: #999;
}

.post-title {
  font-size: 22px;
  font-weight: 700;
  color: #722ed1;
  margin: 0 0 8px;
  line-height: 1.4;
}

.post-excerpt {
  color: #666;
  font-size: 14px;
  line-height: 1.6;
  margin: 0 0 12px;
}

.post-tags {
  display: flex;
  gap: 4px;
  flex-wrap: wrap;
}
</style>
