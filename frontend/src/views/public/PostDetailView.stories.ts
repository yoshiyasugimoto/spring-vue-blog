import type { Meta, StoryObj } from '@storybook/vue3';
import PostDetailView from './PostDetailView.vue';
import { withMockedApis } from '@/stories/__mocks__/decorators';
import { usePostStore } from '@/stores/post';
import { useRouter } from 'vue-router';
import { mockPosts, markdownSample } from '@/stories/__mocks__/data';
import type { DecoratorFunction } from '@storybook/types';
import type { Post } from '@/types';

function withPostDetail(post: Post): DecoratorFunction {
  return (story) => {
    const router = useRouter();
    router.push(`/post/${post.slug}`);
    const postStore = usePostStore();
    postStore.currentPost = post;
    postStore.loading = false;
    postStore.fetchBySlug = async () => {
      postStore.currentPost = post;
    };
    return story();
  };
}

const meta = {
  title: 'Views/Public/PostDetail',
  component: PostDetailView,
  tags: ['autodocs'],
  decorators: [withMockedApis()],
} satisfies Meta<typeof PostDetailView>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  decorators: [withPostDetail(mockPosts[0])],
};

export const WithoutCoverImage: Story = {
  decorators: [withPostDetail({ ...mockPosts[0], coverImage: '' })],
};

export const WithRichMarkdown: Story = {
  decorators: [withPostDetail({ ...mockPosts[0], content: markdownSample })],
};
