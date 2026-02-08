import type { Meta, StoryObj } from '@storybook/vue3';
import TagView from './TagView.vue';
import { withMockedApis, withMockedEmptyApis } from '@/stories/__mocks__/decorators';

const meta = {
  title: 'Views/Admin/Tag',
  component: TagView,
  tags: ['autodocs'],
} satisfies Meta<typeof TagView>;

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
