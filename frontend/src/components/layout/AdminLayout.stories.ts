import type { Meta, StoryObj } from '@storybook/vue3';
import AdminLayout from './AdminLayout.vue';
import { withAuthenticatedUser, withMockedApis } from '@/stories/__mocks__/decorators';
import { useRouter } from 'vue-router';

const meta = {
  title: 'Layout/AdminLayout',
  component: AdminLayout,
  tags: ['autodocs'],
  decorators: [withMockedApis(), withAuthenticatedUser()],
} satisfies Meta<typeof AdminLayout>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};

export const Collapsed: Story = {
  play: async ({ canvasElement }) => {
    const trigger = canvasElement.querySelector('.trigger') as HTMLElement;
    if (trigger) trigger.click();
  },
};

export const PostsSelected: Story = {
  decorators: [
    (story) => {
      const router = useRouter();
      router.push('/admin/posts');
      return story();
    },
  ],
};
