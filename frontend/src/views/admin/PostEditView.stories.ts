import type { Meta, StoryObj } from '@storybook/vue3';
import PostEditView from './PostEditView.vue';
import { withMockedApis } from '@/stories/__mocks__/decorators';
import { useRouter } from 'vue-router';
import type { DecoratorFunction } from '@storybook/types';

function withRoute(path: string): DecoratorFunction {
  return (story) => {
    const router = useRouter();
    router.push(path);
    return story();
  };
}

const meta = {
  title: 'Views/Admin/PostEdit',
  component: PostEditView,
  tags: ['autodocs'],
  decorators: [withMockedApis()],
} satisfies Meta<typeof PostEditView>;

export default meta;
type Story = StoryObj<typeof meta>;

export const NewPost: Story = {
  decorators: [withRoute('/admin/posts/new')],
};

export const EditPost: Story = {
  decorators: [withRoute('/admin/posts/1')],
};
