<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold m-0">記事管理</h2>
      <router-link to="/admin/posts/new">
        <a-button type="primary">新規作成</a-button>
      </router-link>
    </div>
    <a-card :bordered="false">
      <a-table
        :data-source="postStore.posts"
        :loading="postStore.loading"
        :pagination="pagination"
        :columns="columns"
        row-key="id"
        @change="handleTableChange"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'title'">
            <router-link :to="`/admin/posts/${record.id}`">
              {{ record.title }}
            </router-link>
          </template>
          <template v-if="column.key === 'status'">
            <a-tag :color="record.status === 'PUBLISHED' ? 'green' : 'orange'">
              {{ record.status === 'PUBLISHED' ? '公開' : '下書き' }}
            </a-tag>
          </template>
          <template v-if="column.key === 'category'">
            {{ record.category?.name || '-' }}
          </template>
          <template v-if="column.key === 'updatedAt'">
            {{ formatDate(record.updatedAt) }}
          </template>
          <template v-if="column.key === 'actions'">
            <a-space>
              <router-link :to="`/admin/posts/${record.id}`">
                <a-button type="link" size="small">編集</a-button>
              </router-link>
              <a-popconfirm
                title="この記事を削除しますか？"
                @confirm="handleDelete(record.id)"
              >
                <a-button type="link" danger size="small">削除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { computed, onMounted } from 'vue'
import { usePostStore } from '@/stores/post'
import { message } from 'ant-design-vue'
import { formatDate } from '@/utils/date'

const postStore = usePostStore()

const columns = [
  { title: 'タイトル', key: 'title', dataIndex: 'title' },
  { title: 'ステータス', key: 'status', dataIndex: 'status', width: 100 },
  { title: 'カテゴリ', key: 'category', width: 150 },
  { title: '更新日', key: 'updatedAt', width: 120 },
  { title: '操作', key: 'actions', width: 150 },
]

const pagination = computed(() => ({
  current: postStore.currentPage + 1,
  total: postStore.totalElements,
  pageSize: 10,
  showSizeChanger: false,
}))

function handleTableChange(pag: { current: number }) {
  postStore.fetchAll(pag.current - 1)
}

async function handleDelete(id: number) {
  await postStore.deletePost(id)
  message.success('記事を削除しました')
  postStore.fetchAll(postStore.currentPage)
}

onMounted(() => {
  postStore.fetchAll()
})
</script>
