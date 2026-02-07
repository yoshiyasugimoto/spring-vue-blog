<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">カテゴリ管理</h2>
      <a-button type="primary" @click="showModal()">新規作成</a-button>
    </div>
    <a-card :bordered="false">
      <a-table
        :data-source="categories"
        :loading="loading"
        :columns="columns"
        row-key="id"
        :pagination="false"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'actions'">
            <a-space>
              <a-button type="link" size="small" @click="showModal(record)">編集</a-button>
              <a-popconfirm title="削除しますか？" @confirm="handleDelete(record.id)">
                <a-button type="link" danger size="small">削除</a-button>
              </a-popconfirm>
            </a-space>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal
      v-model:open="modalVisible"
      :title="editingId ? 'カテゴリを編集' : 'カテゴリを作成'"
      @ok="handleSave"
      :confirm-loading="saving"
    >
      <a-form layout="vertical">
        <a-form-item label="名前" required>
          <a-input v-model:value="form.name" />
        </a-form-item>
        <a-form-item label="スラッグ">
          <a-input v-model:value="form.slug" placeholder="auto-generated" />
        </a-form-item>
        <a-form-item label="説明">
          <a-textarea v-model:value="form.description" :rows="3" />
        </a-form-item>
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { categoryApi } from '@/api/post'
import { message } from 'ant-design-vue'
import type { Category } from '@/types'

const categories = ref<Category[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const saving = ref(false)
const editingId = ref<number | null>(null)

const columns = [
  { title: '名前', dataIndex: 'name', key: 'name' },
  { title: 'スラッグ', dataIndex: 'slug', key: 'slug' },
  { title: '説明', dataIndex: 'description', key: 'description' },
  { title: '操作', key: 'actions', width: 150 },
]

const form = reactive({ name: '', slug: '', description: '' })

function showModal(category?: Category) {
  if (category) {
    editingId.value = category.id
    form.name = category.name
    form.slug = category.slug
    form.description = category.description || ''
  } else {
    editingId.value = null
    form.name = ''
    form.slug = ''
    form.description = ''
  }
  modalVisible.value = true
}

async function fetchCategories() {
  loading.value = true
  try {
    const res = await categoryApi.getAll()
    categories.value = res.data
  } finally {
    loading.value = false
  }
}

async function handleSave() {
  if (!form.name.trim()) {
    message.warning('名前を入力してください')
    return
  }
  saving.value = true
  try {
    if (editingId.value) {
      await categoryApi.update(editingId.value, form)
      message.success('カテゴリを更新しました')
    } else {
      await categoryApi.create(form)
      message.success('カテゴリを作成しました')
    }
    modalVisible.value = false
    fetchCategories()
  } finally {
    saving.value = false
  }
}

async function handleDelete(id: number) {
  await categoryApi.delete(id)
  message.success('カテゴリを削除しました')
  fetchCategories()
}

onMounted(fetchCategories)
</script>

<style scoped>
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-title {
  font-size: 20px;
  font-weight: 600;
  margin: 0;
}
</style>
