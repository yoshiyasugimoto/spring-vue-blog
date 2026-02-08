<template>
  <div>
    <h2 class="page-title">ダッシュボード</h2>
    <a-row :gutter="16">
      <a-col :span="8">
        <a-card>
          <a-statistic title="記事数" :value="stats.posts" />
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-card>
          <a-statistic title="カテゴリ数" :value="stats.categories" />
        </a-card>
      </a-col>
      <a-col :span="8">
        <a-card>
          <a-statistic title="タグ数" :value="stats.tags" />
        </a-card>
      </a-col>
    </a-row>
    <a-card style="margin-top: 24px">
      <template #title>最近の記事</template>
      <template #extra>
        <router-link to="/admin/posts">
          <a-button type="link">すべて表示</a-button>
        </router-link>
      </template>
      <a-list :data-source="recentPosts" :loading="loading">
        <template #renderItem="{ item }">
          <a-list-item>
            <a-list-item-meta :title="item.title">
              <template #description>
                <a-space>
                  <a-tag :color="item.status === 'PUBLISHED' ? 'green' : 'orange'">
                    {{ item.status === 'PUBLISHED' ? '公開' : '下書き' }}
                  </a-tag>
                  <span>{{ formatDate(item.updatedAt) }}</span>
                </a-space>
              </template>
            </a-list-item-meta>
            <template #actions>
              <router-link :to="`/admin/posts/${item.id}`">編集</router-link>
            </template>
          </a-list-item>
        </template>
      </a-list>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { ref, onMounted } from 'vue'
import { postApi, categoryApi, tagApi } from '@/api/post'
import { formatDate } from '@/utils/date'
import type { Post } from '@/types'

const stats = ref({ posts: 0, categories: 0, tags: 0 })
const recentPosts = ref<Post[]>([])
const loading = ref(false)

onMounted(async () => {
  loading.value = true
  try {
    const [postsRes, categoriesRes, tagsRes] = await Promise.all([
      postApi.getAll(0, 5),
      categoryApi.getAll(),
      tagApi.getAll(),
    ])
    recentPosts.value = postsRes.data.content
    stats.value = {
      posts: postsRes.data.totalElements,
      categories: categoriesRes.data.length,
      tags: tagsRes.data.length,
    }
  } catch {
    // Error toast is shown by the API client interceptor
  } finally {
    loading.value = false
  }
})
</script>

<style scoped>
.page-title {
  margin-bottom: 24px;
  font-size: 20px;
  font-weight: 600;
}
</style>
