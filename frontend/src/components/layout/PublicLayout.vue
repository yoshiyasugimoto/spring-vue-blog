<template>
  <a-layout class="public-layout">
    <a-layout-header class="public-header">
      <div class="header-content">
        <router-link to="/" class="logo">Blog</router-link>
        <div class="header-nav">
          <router-link v-if="authStore.isAuthenticated" to="/admin">
            <a-button type="text">管理画面</a-button>
          </router-link>
          <router-link v-else to="/login">
            <a-button type="text">ログイン</a-button>
          </router-link>
        </div>
      </div>
    </a-layout-header>
    <a-layout-content class="public-content">
      <div class="content-wrapper">
        <router-view />
      </div>
    </a-layout-content>
    <a-layout-footer class="public-footer">
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

<style scoped>
.public-layout {
  min-height: 100vh;
  background: #fff;
}

.public-header {
  background: #fff;
  border-bottom: 1px solid #f0f0f0;
  padding: 0 24px;
  height: 56px;
  line-height: 56px;
}

.header-content {
  max-width: 800px;
  margin: 0 auto;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.logo {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a1a;
  text-decoration: none;
  letter-spacing: -0.5px;
}

.public-content {
  background: #fff;
}

.content-wrapper {
  max-width: 800px;
  margin: 0 auto;
  padding: 40px 24px;
}

.public-footer {
  text-align: center;
  background: #fafafa;
  color: #999;
  font-size: 13px;
}
</style>
