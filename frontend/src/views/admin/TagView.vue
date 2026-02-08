<template>
  <div>
    <div class="flex justify-between items-center mb-6">
      <h2 class="text-xl font-semibold m-0">タグ管理</h2>
      <a-button type="primary" @click="showModal()">新規作成</a-button>
    </div>
    <a-card :bordered="false">
      <a-table
        :data-source="tags"
        :loading="loading"
        :columns="columns"
        row-key="id"
        :pagination="false"
      >
        <template #bodyCell="{ column, record }">
          <template v-if="column.key === 'actions'">
            <a-popconfirm title="削除しますか？" @confirm="handleDelete(record.id)">
              <a-button type="link" danger size="small">削除</a-button>
            </a-popconfirm>
          </template>
        </template>
      </a-table>
    </a-card>

    <a-modal
      v-model:open="modalVisible"
      title="タグを作成"
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
      </a-form>
    </a-modal>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, onMounted } from 'vue'
import { tagApi } from '@/api/post'
import { message } from 'ant-design-vue'
import type { Tag } from '@/types'

const tags = ref<Tag[]>([])
const loading = ref(false)
const modalVisible = ref(false)
const saving = ref(false)

const columns = [
  { title: '名前', dataIndex: 'name', key: 'name' },
  { title: 'スラッグ', dataIndex: 'slug', key: 'slug' },
  { title: '操作', key: 'actions', width: 100 },
]

const form = reactive({ name: '', slug: '' })

function showModal() {
  form.name = ''
  form.slug = ''
  modalVisible.value = true
}

async function fetchTags() {
  loading.value = true
  try {
    const res = await tagApi.getAll()
    tags.value = res.data
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
    await tagApi.create(form)
    message.success('タグを作成しました')
    modalVisible.value = false
    fetchTags()
  } finally {
    saving.value = false
  }
}

async function handleDelete(id: number) {
  await tagApi.delete(id)
  message.success('タグを削除しました')
  fetchTags()
}

onMounted(fetchTags)
</script>
