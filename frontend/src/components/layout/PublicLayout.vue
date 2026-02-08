<template>
  <a-layout class="min-h-screen bg-white">
    <a-layout-header class="!bg-white border-b border-border-light !px-6 !h-14 !leading-[56px]">
      <div class="max-w-[800px] mx-auto flex justify-between items-center">
        <router-link to="/" class="text-xl font-bold text-text-primary no-underline tracking-[-0.5px]">Blog</router-link>
        <div>
          <router-link v-if="authStore.isAuthenticated" to="/admin">
            <a-button type="text">管理画面</a-button>
          </router-link>
          <router-link v-else to="/login">
            <a-button type="text">ログイン</a-button>
          </router-link>
        </div>
      </div>
    </a-layout-header>
    <a-layout-content class="!bg-white">
      <div class="max-w-[800px] mx-auto py-10 px-6">
        <router-view />
      </div>
    </a-layout-content>
    <a-layout-footer class="!text-center !bg-bg-subtle text-text-muted text-[13px]">
      Blog &copy; {{ new Date().getFullYear() }}
    </a-layout-footer>
  </a-layout>
</template>

<script setup lang="ts">
import { onMounted } from 'vue'
import { useAuthStore } from '@/stores/auth'

const authStore = useAuthStore()

onMounted(async () => {
  if (!authStore.isAuthenticated) {
    await authStore.fetchUser()
  }
})
</script>
