import type { Meta, StoryObj } from '@storybook/vue3';
import HomeView from './HomeView.vue';
import { withPopulatedPostStore, withLoadingPostStore, withMockedApis } from '@/stories/__mocks__/decorators';

const meta = {
  title: 'Views/Public/Home',
  component: HomeView,
  tags: ['autodocs'],
  decorators: [withMockedApis()],
} satisfies Meta<typeof HomeView>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  decorators: [withPopulatedPostStore()],
};

export const Empty: Story = {
  decorators: [withPopulatedPostStore([])],
};

export const Loading: Story = {
  decorators: [withLoadingPostStore()],
};
