import type { Meta, StoryObj } from '@storybook/vue3';
import DashboardView from './DashboardView.vue';
import { withMockedApis, withMockedEmptyApis } from '@/stories/__mocks__/decorators';

const meta = {
  title: 'Views/Admin/Dashboard',
  component: DashboardView,
  tags: ['autodocs'],
} satisfies Meta<typeof DashboardView>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  decorators: [withMockedApis()],
};

export const Empty: Story = {
  decorators: [withMockedEmptyApis()],
};
