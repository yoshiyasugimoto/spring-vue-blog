const i={id:1,username:"admin",displayName:"管理者",role:"ADMIN"},r=[{id:1,name:"フロントエンド",slug:"frontend",description:"フロントエンド開発に関する記事"},{id:2,name:"バックエンド",slug:"backend",description:"バックエンド開発に関する記事"},{id:3,name:"DevOps",slug:"devops",description:"DevOpsに関する記事"}],a=[{id:1,name:"Vue.js",slug:"vuejs"},{id:2,name:"TypeScript",slug:"typescript"},{id:3,name:"Spring Boot",slug:"spring-boot"},{id:4,name:"Docker",slug:"docker"}],p=[{id:1,title:"Vue 3とTypeScriptで始めるモダンフロントエンド開発",slug:"vue3-typescript-modern-frontend",content:`# はじめに

Vue 3とTypeScriptを使ったモダンな開発手法を解説します。

## セットアップ

\`\`\`bash
npm create vue@latest
\`\`\`

これでプロジェクトが作成されます。`,excerpt:"この記事では、Vue 3とTypeScriptを使用した最新のフロントエンド開発手法について解説します。",coverImage:"https://picsum.photos/seed/post1/800/400",status:"PUBLISHED",category:{id:1,name:"フロントエンド",slug:"frontend",description:"フロントエンド開発に関する記事"},author:{id:1,username:"admin",displayName:"管理者",role:"ADMIN"},tags:[{id:1,name:"Vue.js",slug:"vuejs"},{id:2,name:"TypeScript",slug:"typescript"}],publishedAt:"2024-01-15T10:00:00",createdAt:"2024-01-15T10:00:00",updatedAt:"2024-01-15T10:00:00"},{id:2,title:"Spring Bootで作るRESTful API入門",slug:"spring-boot-restful-api",content:`# Spring Boot REST API

Spring Bootを使ったREST APIの構築方法を説明します。`,excerpt:"Spring Bootを使用してRESTful APIを効率的に構築する方法を学びます。",coverImage:"https://picsum.photos/seed/post2/800/400",status:"PUBLISHED",category:{id:2,name:"バックエンド",slug:"backend",description:"バックエンド開発に関する記事"},author:{id:1,username:"admin",displayName:"管理者",role:"ADMIN"},tags:[{id:3,name:"Spring Boot",slug:"spring-boot"}],publishedAt:"2024-01-20T14:00:00",createdAt:"2024-01-20T14:00:00",updatedAt:"2024-01-20T14:00:00"},{id:3,title:"Docker Composeで開発環境を構築する",slug:"docker-compose-dev-environment",content:`# Docker Compose入門

Docker Composeを使った開発環境の構築手順です。`,excerpt:"Docker Composeを活用して、再現可能な開発環境を素早く構築する方法を紹介します。",coverImage:"",status:"DRAFT",category:{id:3,name:"DevOps",slug:"devops",description:"DevOpsに関する記事"},author:{id:1,username:"admin",displayName:"管理者",role:"ADMIN"},tags:[{id:4,name:"Docker",slug:"docker"}],publishedAt:"",createdAt:"2024-02-01T09:00:00",updatedAt:"2024-02-01T09:00:00"}];function d(t,s=0,e=10){const n=s*e;return{content:t.slice(n,n+e),totalElements:t.length,totalPages:Math.ceil(t.length/e),number:s,size:e}}const c=`# サンプル記事タイトル

## はじめに

これは **Markdown** のサンプルです。\`inline code\` も使えます。

### コードブロック

\`\`\`typescript
interface User {
  id: number
  name: string
  email: string
}

function greet(user: User): string {
  return \`Hello, \${user.name}!\`
}
\`\`\`

\`\`\`python
def fibonacci(n: int) -> list[int]:
    fib = [0, 1]
    for i in range(2, n):
        fib.append(fib[i-1] + fib[i-2])
    return fib
\`\`\`

> 引用テキストです。
> 複数行にまたがることもできます。

### リスト

- リスト項目1
- リスト項目2
  - ネストされた項目
- リスト項目3

1. 番号付きリスト1
2. 番号付きリスト2
3. 番号付きリスト3

### テーブル

| 技術 | カテゴリ | 用途 |
|------|---------|------|
| Vue.js | フロントエンド | UIフレームワーク |
| Spring Boot | バックエンド | APIサーバー |
| PostgreSQL | データベース | データ永続化 |

---

以上がMarkdownのサンプルです。
`;export{p as a,d as b,r as c,a as d,i as e,c as m};
