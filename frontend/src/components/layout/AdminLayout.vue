<template>
  <a-layout class="min-h-screen">
    <a-layout-sider
      v-model:collapsed="collapsed"
      :trigger="null"
      collapsible
      theme="light"
      :width="220"
      class="!border-r !border-border-light"
    >
      <div class="h-14 flex items-center justify-center border-b border-border-light">
        <router-link to="/admin" class="text-base font-bold text-text-primary no-underline">Blog Admin</router-link>
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
      <a-layout-header class="!bg-white !px-6 flex justify-between items-center !h-14 border-b border-border-light">
        <div>
          <MenuFoldOutlined
            v-if="!collapsed"
            class="text-lg cursor-pointer transition-colors duration-300 hover:text-antd-blue"
            @click="collapsed = true"
          />
          <MenuUnfoldOutlined
            v-else
            class="text-lg cursor-pointer transition-colors duration-300 hover:text-antd-blue"
            @click="collapsed = false"
          />
        </div>
        <div>
          <a-dropdown>
            <span class="cursor-pointer text-sm">
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
      <a-layout-content class="!p-6 !bg-bg-gray">
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
