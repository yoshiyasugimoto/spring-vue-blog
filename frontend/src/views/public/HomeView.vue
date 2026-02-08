<template>
  <div>
    <h1 class="text-[28px] font-bold m-0 mb-8 text-text-primary">記事一覧</h1>
    <a-spin :spinning="postStore.loading">
      <div v-if="postStore.posts.length === 0 && !postStore.loading" class="py-15">
        <a-empty description="記事がまだありません" />
      </div>
      <div v-else>
        <PostCard v-for="post in postStore.posts" :key="post.id" :post="post" />
      </div>
      <div v-if="postStore.totalPages > 1" class="flex justify-center py-8">
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
