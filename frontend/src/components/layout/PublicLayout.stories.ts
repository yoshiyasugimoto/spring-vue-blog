import type { Meta, StoryObj } from '@storybook/vue3';
import PublicLayout from './PublicLayout.vue';
import { withAuthenticatedUser, withUnauthenticatedUser, withMockedApis } from '@/stories/__mocks__/decorators';

const meta = {
  title: 'Layout/PublicLayout',
  component: PublicLayout,
  tags: ['autodocs'],
  decorators: [withMockedApis()],
} satisfies Meta<typeof PublicLayout>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Unauthenticated: Story = {
  decorators: [withUnauthenticatedUser()],
};

export const Authenticated: Story = {
  decorators: [withAuthenticatedUser()],
};
