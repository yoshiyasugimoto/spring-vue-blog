import type { Meta, StoryObj } from '@storybook/vue3';
import PostCard from './PostCard.vue';
import type { Post } from '@/types';

const meta = {
  title: 'Components/PostCard',
  component: PostCard,
  tags: ['autodocs'],
  argTypes: {
    post: {
      description: 'ブログ記事オブジェクト',
    },
  },
} satisfies Meta<typeof PostCard>;

export default meta;
type Story = StoryObj<typeof meta>;

const mockPost: Post = {
  id: 1,
  title: 'Vue 3とTypeScriptで始めるモダンフロントエンド開発',
  slug: 'vue3-typescript-modern-frontend',
  content: 'コンテンツ本文...',
  excerpt: 'この記事では、Vue 3とTypeScriptを使用した最新のフロントエンド開発手法について解説します。',
  coverImage: 'https://picsum.photos/800/400',
  status: 'PUBLISHED',
  category: {
    id: 1,
    name: 'フロントエンド',
    slug: 'frontend',
    description: 'フロントエンド開発に関する記事',
  },
  author: {
    id: 1,
    username: 'admin',
    displayName: '管理者',
    role: 'ADMIN',
  },
  tags: [
    { id: 1, name: 'Vue.js', slug: 'vuejs' },
    { id: 2, name: 'TypeScript', slug: 'typescript' },
  ],
  publishedAt: '2024-01-15T10:00:00',
  createdAt: '2024-01-15T10:00:00',
  updatedAt: '2024-01-15T10:00:00',
};

export const Default: Story = {
  args: {
    post: mockPost,
  },
};

export const WithoutCoverImage: Story = {
  args: {
    post: {
      ...mockPost,
      coverImage: '',
    },
  },
};

export const WithoutCategory: Story = {
  args: {
    post: {
      ...mockPost,
      category: null,
    },
  },
};

export const WithoutTags: Story = {
  args: {
    post: {
      ...mockPost,
      tags: [],
    },
  },
};

export const MinimalPost: Story = {
  args: {
    post: {
      ...mockPost,
      coverImage: '',
      excerpt: '',
      category: null,
      tags: [],
    },
  },
};
