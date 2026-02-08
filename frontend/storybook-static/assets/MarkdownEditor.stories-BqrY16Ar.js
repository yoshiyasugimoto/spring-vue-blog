import{M as f}from"./MarkdownEditor-DfLFTWu8.js";import{m as E}from"./data-BUIPDpdg.js";import"./iframe-DK6zhBXe.js";import"./preload-helper-Dp1pzeXC.js";import"./github-Cvf2Y9Do.js";import"./_plugin-vue_export-helper-DlAUqK2U.js";const k={title:"Components/MarkdownEditor",component:f,tags:["autodocs"],argTypes:{modelValue:{control:"text",description:"Markdown文字列（v-model）"}}},s={args:{modelValue:""}},t={args:{modelValue:E}},n={args:{modelValue:`# コードブロックのサンプル

## TypeScript

\`\`\`typescript
interface Post {
  id: number
  title: string
  content: string
  tags: string[]
}

async function fetchPosts(): Promise<Post[]> {
  const response = await fetch('/api/posts')
  return response.json()
}
\`\`\`

## Python

\`\`\`python
from dataclasses import dataclass
from typing import List

@dataclass
class Post:
    id: int
    title: str
    content: str
    tags: List[str]

async def fetch_posts() -> List[Post]:
    async with aiohttp.ClientSession() as session:
        async with session.get('/api/posts') as response:
            return await response.json()
\`\`\`

## SQL

\`\`\`sql
SELECT p.title, p.content, c.name AS category
FROM posts p
LEFT JOIN categories c ON p.category_id = c.id
WHERE p.status = 'PUBLISHED'
ORDER BY p.published_at DESC
LIMIT 10;
\`\`\`
`}},e={args:{modelValue:Array.from({length:20},(S,o)=>`## セクション ${o+1}

これはセクション${o+1}の本文です。長いコンテンツのスクロール挙動を確認するために、複数のセクションを含んでいます。

\`\`\`javascript
console.log('セクション ${o+1}')
\`\`\`
`).join(`
`)}};var a,r,i;s.parameters={...s.parameters,docs:{...(a=s.parameters)==null?void 0:a.docs,source:{originalSource:`{
  args: {
    modelValue: ''
  }
}`,...(i=(r=s.parameters)==null?void 0:r.docs)==null?void 0:i.source}}};var c,p,m;t.parameters={...t.parameters,docs:{...(c=t.parameters)==null?void 0:c.docs,source:{originalSource:`{
  args: {
    modelValue: markdownSample
  }
}`,...(m=(p=t.parameters)==null?void 0:p.docs)==null?void 0:m.source}}};var d,l,g;n.parameters={...n.parameters,docs:{...(d=n.parameters)==null?void 0:d.docs,source:{originalSource:`{
  args: {
    modelValue: \`# コードブロックのサンプル

## TypeScript

\\\`\\\`\\\`typescript
interface Post {
  id: number
  title: string
  content: string
  tags: string[]
}

async function fetchPosts(): Promise<Post[]> {
  const response = await fetch('/api/posts')
  return response.json()
}
\\\`\\\`\\\`

## Python

\\\`\\\`\\\`python
from dataclasses import dataclass
from typing import List

@dataclass
class Post:
    id: int
    title: str
    content: str
    tags: List[str]

async def fetch_posts() -> List[Post]:
    async with aiohttp.ClientSession() as session:
        async with session.get('/api/posts') as response:
            return await response.json()
\\\`\\\`\\\`

## SQL

\\\`\\\`\\\`sql
SELECT p.title, p.content, c.name AS category
FROM posts p
LEFT JOIN categories c ON p.category_id = c.id
WHERE p.status = 'PUBLISHED'
ORDER BY p.published_at DESC
LIMIT 10;
\\\`\\\`\\\`
\`
  }
}`,...(g=(l=n.parameters)==null?void 0:l.docs)==null?void 0:g.source}}};var u,y,h;e.parameters={...e.parameters,docs:{...(u=e.parameters)==null?void 0:u.docs,source:{originalSource:"{\n  args: {\n    modelValue: Array.from({\n      length: 20\n    }, (_, i) => `## セクション ${i + 1}\\n\\nこれはセクション${i + 1}の本文です。長いコンテンツのスクロール挙動を確認するために、複数のセクションを含んでいます。\\n\\n\\`\\`\\`javascript\\nconsole.log('セクション ${i + 1}')\\n\\`\\`\\`\\n`).join('\\n')\n  }\n}",...(h=(y=e.parameters)==null?void 0:y.docs)==null?void 0:h.source}}};const O=["Empty","WithMarkdown","WithCodeBlock","LongContent"];export{s as Empty,e as LongContent,n as WithCodeBlock,t as WithMarkdown,O as __namedExportsOrder,k as default};
