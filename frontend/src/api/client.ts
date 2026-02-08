import axios from 'axios'
import { message } from 'ant-design-vue'

const apiClient = axios.create({
  baseURL: '',
  withCredentials: true,
  headers: {
    'Content-Type': 'application/json',
  },
})

// CSRF token handling
apiClient.interceptors.request.use((config) => {
  const csrfToken = document.cookie
    .split('; ')
    .find((row) => row.startsWith('XSRF-TOKEN='))
    ?.split('=')[1]

  if (csrfToken) {
    config.headers['X-XSRF-TOKEN'] = decodeURIComponent(csrfToken)
  }
  return config
})

// Error response handling
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    if (!axios.isAxiosError(error) || !error.response) {
      message.error('ネットワークエラーが発生しました')
      return Promise.reject(error)
    }

    const { status, config } = error.response

    // Skip toast for auth check endpoint (expected 401 for unauthenticated users)
    const silent = config.url === '/api/auth/me' || config.url === '/api/auth/login'

    if (!silent) {
      switch (status) {
        case 400:
          message.error(error.response.data?.message || '入力内容に誤りがあります')
          break
        case 401:
          message.error('ログインが必要です')
          break
        case 403:
          message.error('アクセス権限がありません')
          break
        case 404:
          message.error('データが見つかりません')
          break
        default:
          if (status >= 500) {
            message.error('サーバーエラーが発生しました')
          }
      }
    }

    return Promise.reject(error)
  },
)

export default apiClient
