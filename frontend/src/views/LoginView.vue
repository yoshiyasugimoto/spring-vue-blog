<template>
  <div class="min-h-screen flex items-center justify-center bg-bg-gray">
    <a-card class="w-100 shadow-[0_2px_8px_rgba(0,0,0,0.06)]" :bordered="false">
      <h2 class="text-center mb-8 font-semibold text-2xl text-text-primary">ログイン</h2>
      <a-form
        :model="form"
        @finish="handleLogin"
        layout="vertical"
      >
        <a-form-item
          label="ユーザー名"
          name="username"
          :rules="[{ required: true, message: 'ユーザー名を入力してください' }]"
        >
          <a-input v-model:value="form.username" size="large" />
        </a-form-item>
        <a-form-item
          label="パスワード"
          name="password"
          :rules="[{ required: true, message: 'パスワードを入力してください' }]"
        >
          <a-input-password v-model:value="form.password" size="large" />
        </a-form-item>
        <a-form-item>
          <a-button
            type="primary"
            html-type="submit"
            :loading="authStore.loading"
            block
            size="large"
          >
            ログイン
          </a-button>
        </a-form-item>
      </a-form>
      <a-alert
        v-if="errorMessage"
        :message="errorMessage"
        type="error"
        show-icon
        closable
        @close="errorMessage = ''"
      />
    </a-card>
  </div>
</template>

<script setup lang="ts">
import { reactive, ref } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const errorMessage = ref('')

const form = reactive({
  username: '',
  password: '',
})

async function handleLogin() {
  try {
    errorMessage.value = ''
    await authStore.login(form.username, form.password)
    const redirect = (route.query.redirect as string) || '/admin'
    router.push(redirect)
  } catch {
    errorMessage.value = 'ユーザー名またはパスワードが正しくありません'
  }
}
</script>
