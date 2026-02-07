<template>
  <div>
    <h1 class="page-heading">記事一覧</h1>
    <a-spin :spinning="postStore.loading">
      <div v-if="postStore.posts.length === 0 && !postStore.loading" class="empty-state">
        <a-empty description="記事がまだありません" />
      </div>
      <div v-else>
        <PostCard v-for="post in postStore.posts" :key="post.id" :post="post" />
      </div>
      <div v-if="postStore.totalPages > 1" class="pagination-wrapper">
        <a-pagination
          :current="postStore.currentPage + 1"
          :total="postStore.totalElements"
          :page-size="10"
          @change="handlePageChange"
          show-less-items
        />
      </div>
    </a-spin>
  </div>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { usePostStore } from '@/stores/post'
import PostCard from '@/components/common/PostCard.vue'

const postStore = usePostStore()

function handlePageChange(page: number) {
  postStore.fetchPublished(page - 1)
}

onMounted(() => {
  postStore.fetchPublished()
})
</script>

<style scoped>
.page-heading {
  font-size: 28px;
  font-weight: 700;
  margin: 0 0 32px;
  color: #1a1a1a;
}

.empty-state {
  padding: 60px 0;
}

.pagination-wrapper {
  display: flex;
  justify-content: center;
  padding: 32px 0;
}
</style>
