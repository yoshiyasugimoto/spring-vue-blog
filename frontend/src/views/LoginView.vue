<template>
  <div class="login-container">
    <a-card class="login-card" :bordered="false">
      <h2 class="login-title">ログイン</h2>
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

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #f5f5f5;
}

.login-card {
  width: 400px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.login-title {
  text-align: center;
  margin-bottom: 32px;
  font-weight: 600;
  font-size: 24px;
  color: #1a1a1a;
}
</style>
