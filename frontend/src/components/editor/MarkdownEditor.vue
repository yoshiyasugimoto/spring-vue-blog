<template>
  <div class="markdown-editor">
    <div class="editor-pane">
      <a-textarea
        :value="modelValue"
        @update:value="$emit('update:modelValue', $event)"
        placeholder="Markdownで記事を書く..."
        :auto-size="{ minRows: 20 }"
        class="editor-textarea"
      />
    </div>
    <div class="preview-pane">
      <div class="preview-label">プレビュー</div>
      <div class="markdown-body" v-html="renderedHtml"></div>
    </div>
  </div>
</template>

<script setup lang="ts">
import { computed } from 'vue'
import { marked } from '@/utils/markdown'
import 'highlight.js/styles/github.css'

const props = defineProps<{
  modelValue: string
}>()

defineEmits<{
  'update:modelValue': [value: string]
}>()

const renderedHtml = computed(() => {
  if (!props.modelValue) return '<p style="color: #999;">プレビューがここに表示されます</p>'
  return marked.parse(props.modelValue) as string
})
</script>

<style scoped>
.markdown-editor {
  display: flex;
  gap: 16px;
  min-height: 500px;
}

.editor-pane {
  flex: 1;
}

.editor-textarea {
  font-family: 'SF Mono', 'Monaco', 'Menlo', 'Consolas', monospace !important;
  font-size: 14px !important;
  line-height: 1.7 !important;
  border-radius: 6px !important;
}

.preview-pane {
  flex: 1;
  border: 1px solid #d9d9d9;
  border-radius: 6px;
  padding: 16px;
  overflow-y: auto;
  background: #fff;
}

.preview-label {
  font-size: 12px;
  color: #999;
  margin-bottom: 12px;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.markdown-body {
  font-size: 15px;
  line-height: 1.8;
  color: #1a1a1a;
}

.markdown-body :deep(h1) {
  font-size: 28px;
  font-weight: 700;
  margin: 24px 0 16px;
  padding-bottom: 8px;
  border-bottom: 1px solid #eee;
}

.markdown-body :deep(h2) {
  font-size: 22px;
  font-weight: 600;
  margin: 20px 0 12px;
}

.markdown-body :deep(h3) {
  font-size: 18px;
  font-weight: 600;
  margin: 16px 0 8px;
}

.markdown-body :deep(p) {
  margin: 0 0 12px;
}

.markdown-body :deep(pre) {
  background: #f6f8fa;
  border-radius: 6px;
  padding: 16px;
  overflow-x: auto;
  margin: 16px 0;
}

.markdown-body :deep(code) {
  font-family: 'SF Mono', 'Monaco', 'Menlo', monospace;
  font-size: 13px;
}

.markdown-body :deep(p code) {
  background: #f0f0f0;
  padding: 2px 6px;
  border-radius: 3px;
  font-size: 13px;
}

.markdown-body :deep(blockquote) {
  border-left: 3px solid #ddd;
  padding: 4px 16px;
  color: #666;
  margin: 16px 0;
}

.markdown-body :deep(ul),
.markdown-body :deep(ol) {
  padding-left: 24px;
  margin: 8px 0;
}

.markdown-body :deep(img) {
  max-width: 100%;
  border-radius: 4px;
}

.markdown-body :deep(hr) {
  border: none;
  border-top: 1px solid #eee;
  margin: 24px 0;
}

.markdown-body :deep(table) {
  width: 100%;
  border-collapse: collapse;
  margin: 16px 0;
}

.markdown-body :deep(th),
.markdown-body :deep(td) {
  border: 1px solid #ddd;
  padding: 8px 12px;
  text-align: left;
}

.markdown-body :deep(th) {
  background: #f6f8fa;
  font-weight: 600;
}
</style>
