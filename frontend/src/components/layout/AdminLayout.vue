<template>
  <a-layout class="admin-layout">
    <a-layout-sider
      v-model:collapsed="collapsed"
      :trigger="null"
      collapsible
      theme="light"
      :width="220"
      class="admin-sider"
    >
      <div class="sider-logo">
        <router-link to="/admin">Blog Admin</router-link>
      </div>
      <a-menu
        v-model:selectedKeys="selectedKeys"
        mode="inline"
        @click="handleMenuClick"
      >
        <a-menu-item key="dashboard">
          <DashboardOutlined />
          <span>ダッシュボード</span>
        </a-menu-item>
        <a-menu-item key="posts">
          <FileTextOutlined />
          <span>記事管理</span>
        </a-menu-item>
        <a-menu-item key="categories">
          <FolderOutlined />
          <span>カテゴリ</span>
        </a-menu-item>
        <a-menu-item key="tags">
          <TagOutlined />
          <span>タグ</span>
        </a-menu-item>
      </a-menu>
    </a-layout-sider>
    <a-layout>
      <a-layout-header class="admin-header">
        <div class="header-left">
          <MenuFoldOutlined
            v-if="!collapsed"
            class="trigger"
            @click="collapsed = true"
          />
          <MenuUnfoldOutlined
            v-else
            class="trigger"
            @click="collapsed = false"
          />
        </div>
        <div class="header-right">
          <a-dropdown>
            <span class="user-info">
              {{ authStore.user?.displayName || authStore.user?.username }}
            </span>
            <template #overlay>
              <a-menu>
                <a-menu-item @click="goToPublic">
                  <GlobalOutlined />
                  サイトを表示
                </a-menu-item>
                <a-menu-item @click="handleLogout">
                  <LogoutOutlined />
                  ログアウト
                </a-menu-item>
              </a-menu>
            </template>
          </a-dropdown>
        </div>
      </a-layout-header>
      <a-layout-content class="admin-content">
        <router-view />
      </a-layout-content>
    </a-layout>
  </a-layout>
</template>

<script setup lang="ts">
import { ref, watch } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useAuthStore } from '@/stores/auth'
import {
  DashboardOutlined,
  FileTextOutlined,
  FolderOutlined,
  TagOutlined,
  MenuFoldOutlined,
  MenuUnfoldOutlined,
  GlobalOutlined,
  LogoutOutlined,
} from '@ant-design/icons-vue'

const router = useRouter()
const route = useRoute()
const authStore = useAuthStore()
const collapsed = ref(false)
const selectedKeys = ref<string[]>(['dashboard'])

const menuRouteMap: Record<string, string> = {
  dashboard: '/admin/dashboard',
  posts: '/admin/posts',
  categories: '/admin/categories',
  tags: '/admin/tags',
}

watch(
  () => route.path,
  (path) => {
    if (path.includes('/posts')) selectedKeys.value = ['posts']
    else if (path.includes('/categories')) selectedKeys.value = ['categories']
    else if (path.includes('/tags')) selectedKeys.value = ['tags']
    else selectedKeys.value = ['dashboard']
  },
  { immediate: true }
)

function handleMenuClick({ key }: { key: string }) {
  const target = menuRouteMap[key]
  if (target) router.push(target)
}

function goToPublic() {
  router.push('/')
}

async function handleLogout() {
  await authStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  min-height: 100vh;
}

.admin-sider {
  border-right: 1px solid #f0f0f0;
}

.sider-logo {
  height: 56px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-bottom: 1px solid #f0f0f0;
}

.sider-logo a {
  font-size: 16px;
  font-weight: 700;
  color: #1a1a1a;
  text-decoration: none;
}

.admin-header {
  background: #fff;
  padding: 0 24px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  height: 56px;
  border-bottom: 1px solid #f0f0f0;
}

.trigger {
  font-size: 18px;
  cursor: pointer;
  transition: color 0.3s;
}

.trigger:hover {
  color: #1890ff;
}

.user-info {
  cursor: pointer;
  font-size: 14px;
}

.admin-content {
  padding: 24px;
  background: #f5f5f5;
}
</style>
