<template>
  <div>
    <div class="page-header">
      <h2 class="page-title">{{ isEdit ? '記事を編集' : '新規記事' }}</h2>
      <a-space>
        <a-button @click="router.back()">戻る</a-button>
        <a-button @click="handleSave('DRAFT')" :loading="saving">下書き保存</a-button>
        <a-button type="primary" @click="handleSave('PUBLISHED')" :loading="saving">公開</a-button>
      </a-space>
    </div>

    <a-row :gutter="24">
      <a-col :span="18">
        <a-card :bordered="false" style="margin-bottom: 24px">
          <a-input
            v-model:value="form.title"
            placeholder="タイトルを入力..."
            size="large"
            :bordered="false"
            class="title-input"
          />
        </a-card>
        <a-card :bordered="false">
          <MarkdownEditor v-model="form.content" />
        </a-card>
      </a-col>
      <a-col :span="6">
        <a-card title="設定" :bordered="false" style="margin-bottom: 16px">
          <a-form layout="vertical">
            <a-form-item label="スラッグ">
              <a-input v-model:value="form.slug" placeholder="url-slug" />
            </a-form-item>
            <a-form-item label="抜粋">
              <a-textarea v-model:value="form.excerpt" :rows="3" placeholder="記事の要約..." />
            </a-form-item>
            <a-form-item label="カバー画像URL">
              <a-input v-model:value="form.coverImage" placeholder="https://..." />
            </a-form-item>
          </a-form>
        </a-card>
        <a-card title="カテゴリ" :bordered="false" style="margin-bottom: 16px">
          <a-select
            v-model:value="form.categoryId"
            placeholder="カテゴリを選択"
            allow-clear
            style="width: 100%"
          >
            <a-select-option v-for="cat in categories" :key="cat.id" :value="cat.id">
              {{ cat.name }}
            </a-select-option>
          </a-select>
        </a-card>
        <a-card title="タグ" :bordered="false">
          <a-select
            v-model:value="form.tagIds"
            mode="multiple"
            placeholder="タグを選択"
            style="width: 100%"
          >
            <a-select-option v-for="tag in tags" :key="tag.id" :value="tag.id">
              {{ tag.name }}
            </a-select-option>
          </a-select>
        </a-card>
      </a-col>
    </a-row>
  </div>
</template>

<script setup lang="ts">
import { ref, reactive, computed, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { usePostStore } from '@/stores/post'
import { categoryApi, tagApi, postApi } from '@/api/post'
import { message } from 'ant-design-vue'
import MarkdownEditor from '@/components/editor/MarkdownEditor.vue'
import type { Category, Tag } from '@/types'

const router = useRouter()
const route = useRoute()
const postStore = usePostStore()
const saving = ref(false)
const categories = ref<Category[]>([])
const tags = ref<Tag[]>([])

const isEdit = computed(() => !!route.params.id)

const form = reactive({
  title: '',
  slug: '',
  content: '',
  excerpt: '',
  coverImage: '',
  status: 'DRAFT',
  categoryId: null as number | null,
  tagIds: [] as number[],
})

onMounted(async () => {
  const [catRes, tagRes] = await Promise.all([categoryApi.getAll(), tagApi.getAll()])
  categories.value = catRes.data
  tags.value = tagRes.data

  if (isEdit.value) {
    const id = Number(route.params.id)
    const res = await postApi.getAll(0, 1000)
    const post = res.data.content.find((p) => p.id === id)
    if (post) {
      form.title = post.title
      form.slug = post.slug
      form.content = post.content || ''
      form.excerpt = post.excerpt || ''
      form.coverImage = post.coverImage || ''
      form.status = post.status
      form.categoryId = post.category?.id || null
      form.tagIds = post.tags?.map((t) => t.id) || []
    }
  }
})

async function handleSave(status: string) {
  if (!form.title.trim()) {
    message.warning('タイトルを入力してください')
    return
  }

  saving.value = true
  try {
    const data = {
      title: form.title,
      slug: form.slug || undefined,
      content: form.content,
      excerpt: form.excerpt || undefined,
      coverImage: form.coverImage || undefined,
      status,
      categoryId: form.categoryId,
      tagIds: form.tagIds,
    }

    if (isEdit.value) {
      await postStore.updatePost(Number(route.params.id), data)
      message.success('記事を更新しました')
    } else {
      await postStore.createPost(data)
      message.success('記事を作成しました')
      router.push('/admin/posts')
    }
  } catch {
    message.error('保存に失敗しました')
  } finally {
    saving.value = false
  }
}
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

.title-input {
  font-size: 24px !important;
  font-weight: 600 !important;
  padding: 0 !important;
}

.title-input :deep(input) {
  font-size: 24px !important;
  font-weight: 600 !important;
}
</style>
