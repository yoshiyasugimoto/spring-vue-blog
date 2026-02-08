<template>
  <div class="flex gap-4 min-h-[500px]">
    <div class="flex-1">
      <a-textarea
        :value="modelValue"
        @update:value="$emit('update:modelValue', $event)"
        placeholder="Markdownで記事を書く..."
        :auto-size="{ minRows: 20 }"
        class="editor-textarea"
      />
    </div>
    <div class="flex-1 border border-border-medium rounded-md p-4 overflow-y-auto bg-white">
      <div class="text-xs text-text-muted mb-3 uppercase tracking-wide">プレビュー</div>
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
.editor-textarea {
  font-family: var(--font-mono) !important;
  font-size: 14px !important;
  line-height: 1.7 !important;
  border-radius: 6px !important;
}
</style>
