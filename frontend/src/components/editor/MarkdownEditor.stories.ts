import type { Meta, StoryObj } from "@storybook/vue3"
import MarkdownEditor from "./MarkdownEditor.vue"
import { markdownSample } from "@/stories/__mocks__/data"

const meta = {
	title: "Components/MarkdownEditor",
	component: MarkdownEditor,
	tags: ["autodocs"],
	parameters: {
		docs: {
			description: {
				component:
					"Markdown形式でコンテンツを編集するためのエディタコンポーネント。",
			},
		},
	},
	argTypes: {
		modelValue: {
			control: "text",
			description: "Markdown文字列（v-model）",
		},
	},
} satisfies Meta<typeof MarkdownEditor>

export default meta
type Story = StoryObj<typeof meta>

export const Empty: Story = {
	args: {
		modelValue: "",
	},
}

export const WithMarkdown: Story = {
	args: {
		modelValue: markdownSample,
	},
}

export const WithCodeBlock: Story = {
	args: {
		modelValue: `# コードブロックのサンプル

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
`,
	},
}

export const LongContent: Story = {
	args: {
		modelValue: Array.from(
			{ length: 20 },
			(_, i) =>
				`## セクション ${i + 1}\n\nこれはセクション${i + 1}の本文です。長いコンテンツのスクロール挙動を確認するために、複数のセクションを含んでいます。\n\n\`\`\`javascript\nconsole.log('セクション ${i + 1}')\n\`\`\`\n`,
		).join("\n"),
	},
}
