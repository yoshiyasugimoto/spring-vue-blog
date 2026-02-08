# フロントエンド改善ドキュメント

## 概要

フロントエンド (Vue 3 + TypeScript) に対して以下の改善を実施しました。

---

## 1. formatDate ユーティリティの共通化

### 問題点

`formatDate` 関数が **4つのコンポーネント** に重複して定義されていました。

| ファイル | フォーマット |
|---|---|
| `PostDetailView.vue` | `{ year: 'numeric', month: 'long', day: 'numeric' }` (例: 2025年1月15日) |
| `PostCard.vue` | 同上 |
| `PostListView.vue` | オプションなし (例: 2025/1/15) |
| `DashboardView.vue` | 同上 |

### 変更内容

**新規作成: `src/utils/date.ts`**

```typescript
export function formatDate(dateStr: string, full = false): string {
  if (!dateStr) return ''
  return new Date(dateStr).toLocaleDateString(
    'ja-JP',
    full ? { year: 'numeric', month: 'long', day: 'numeric' } : undefined,
  )
}
```

- `full = false`: 短いフォーマット (2025/1/15) — 管理画面のテーブル向け
- `full = true`: 長いフォーマット (2025年1月15日) — 記事詳細・カード向け

**変更されたファイル:**

- `views/public/PostDetailView.vue`: ローカル `formatDate` を削除 → `import { formatDate } from '@/utils/date'`
- `components/common/PostCard.vue`: 同上
- `views/admin/PostListView.vue`: 同上
- `views/admin/DashboardView.vue`: 同上

---

## 2. Marked インスタンスの共通化

### 問題点

`Marked` + `highlight.js` の同一設定が **2つのコンポーネント** で重複していました。

- `PostDetailView.vue` (記事表示)
- `MarkdownEditor.vue` (エディタプレビュー)

### 変更内容

**新規作成: `src/utils/markdown.ts`**

```typescript
import { Marked } from 'marked'
import { markedHighlight } from 'marked-highlight'
import hljs from 'highlight.js'

export const marked = new Marked(
  markedHighlight({
    langPrefix: 'hljs language-',
    highlight(code: string, lang: string) {
      if (lang && hljs.getLanguage(lang)) {
        return hljs.highlight(code, { language: lang }).value
      }
      return hljs.highlightAuto(code).value
    },
  }),
)
```

**変更されたファイル:**

- `views/public/PostDetailView.vue`: `Marked`, `markedHighlight`, `hljs` の個別インポートを削除 → `import { marked } from '@/utils/markdown'`
- `components/editor/MarkdownEditor.vue`: 同上

**効果:** Marked インスタンスがシングルトンとなり、設定変更時に1箇所のみの修正で済みます。

---

## 3. PostEditView の API 呼び出し改善

### 問題点

記事編集画面 (`PostEditView.vue`) で、1件の記事を取得するために **全件取得 API** を使用していました。

```typescript
// 変更前: 最大1000件を取得して find
const res = await postApi.getAll(0, 1000)
const post = res.data.content.find((p) => p.id === id)
```

記事数が増えると不要なデータ転送とフロント側の検索コストが発生します。

### 変更内容

**バックエンド: `PostController.java` にエンドポイント追加**

```java
@GetMapping("/api/admin/posts/{id}")
public ResponseEntity<PostResponse> getById(@PathVariable Long id) {
    Post post = postService.findById(id);
    return ResponseEntity.ok(PostResponse.from(post));
}
```

**フロントエンド: `api/post.ts` にメソッド追加**

```typescript
getById(id: number) {
  return apiClient.get<Post>(`/api/admin/posts/${id}`)
},
```

**フロントエンド: `PostEditView.vue` の修正**

```typescript
// 変更後: ID 指定で1件取得
const res = await postApi.getById(id)
const post = res.data
form.title = post.title
// ...
```

**効果:** 不要なデータ転送がなくなり、レスポンスが高速化されます。

---

## 4. API エラーハンドリングの統一化

### 問題点

各コンポーネントのエラーハンドリングが不統一でした。

| コンポーネント | 問題 |
|---|---|
| `stores/auth.ts` | catch で何も表示しない (サイレント失敗) |
| `DashboardView.vue` | try/finally のみ、catch なし |
| `PostEditView.vue` | 汎用メッセージ「保存に失敗しました」のみ |
| `PostListView.vue` | 削除成功時のみ表示、失敗時は無視 |

### 変更内容

**`api/client.ts` にレスポンスインターセプターを追加**

```typescript
apiClient.interceptors.response.use(
  (response) => response,
  (error) => {
    const { status, config } = error.response

    // 認証チェック・ログインは個別ハンドリングのため除外
    const silent = config.url === '/api/auth/me' || config.url === '/api/auth/login'

    if (!silent) {
      switch (status) {
        case 400: message.error('入力内容に誤りがあります'); break
        case 401: message.error('ログインが必要です'); break
        case 403: message.error('アクセス権限がありません'); break
        case 404: message.error('データが見つかりません'); break
        default:
          if (status >= 500) message.error('サーバーエラーが発生しました')
      }
    }

    return Promise.reject(error)
  },
)
```

**設計ポイント:**

- `/api/auth/me` (認証チェック) と `/api/auth/login` はサイレント (個別の UI でハンドリング)
- その他の API 呼び出しは自動的にトースト表示
- バックエンドがレスポンスボディに `message` フィールドを含む場合はそれを表示 (400 エラー時)
- ネットワーク未接続時は「ネットワークエラーが発生しました」を表示

---

## 確認済み (対応不要) のタスク

| タスク | 状況 |
|---|---|
| **XSS / sanitize** | `marked` ライブラリが HTML エスケープを実行。`v-html` の入力はバックエンドで管理されており安全 |
| **PostListView の完全性** | テーブル、ページネーション、削除確認、ステータスバッジなど全機能実装済み |
| **API ドメイン分割** | `post.ts` (Post/Category/Tag) と `auth.ts` は論理的にグループ化済み。現在の規模では過剰分割は不要 |
| **ルート遅延ロード** | 全ルートが `() => import()` で遅延ロード済み |
| **セッションストレージ** | HTTP-only Cookie + CSRF トークンによるセキュアな設計。localStorage 不使用は意図的 |
| **Storybook** | 15ストーリーファイル、モック、デコレーターが完備 |
