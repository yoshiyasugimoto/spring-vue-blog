import type { Meta, StoryObj } from '@storybook/vue3';
import CategoryView from './CategoryView.vue';
import { withMockedApis, withMockedEmptyApis } from '@/stories/__mocks__/decorators';

const meta = {
  title: 'Views/Admin/Category',
  component: CategoryView,
  tags: ['autodocs'],
} satisfies Meta<typeof CategoryView>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {
  decorators: [withMockedApis()],
};

export const Empty: Story = {
  decorators: [withMockedEmptyApis()],
};

export const WithModalOpen: Story = {
  decorators: [withMockedApis()],
  play: async ({ canvasElement }) => {
    await new Promise((resolve) => setTimeout(resolve, 500));
    const btn = Array.from(canvasElement.querySelectorAll('button')).find(
      (el) => el.textContent?.includes('新規作成')
    ) as HTMLButtonElement;
    if (btn) btn.click();
  },
};
