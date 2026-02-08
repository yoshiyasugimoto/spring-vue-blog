import type { Meta, StoryObj } from '@storybook/vue3';
import LoginView from './LoginView.vue';
import { withUnauthenticatedUser, withMockedApis } from '@/stories/__mocks__/decorators';
import { authApi } from '@/api/auth';

const meta = {
  title: 'Views/Auth/Login',
  component: LoginView,
  tags: ['autodocs'],
  decorators: [withMockedApis(), withUnauthenticatedUser()],
} satisfies Meta<typeof LoginView>;

export default meta;
type Story = StoryObj<typeof meta>;

export const Default: Story = {};

export const WithError: Story = {
  decorators: [
    (story) => {
      authApi.login = async () => {
        throw new Error('Invalid credentials');
      };
      return story();
    },
  ],
  play: async ({ canvasElement }) => {
    const usernameInput = canvasElement.querySelector('#login_username input, input[type="text"]') as HTMLInputElement;
    const passwordInput = canvasElement.querySelector('#login_password input, input[type="password"]') as HTMLInputElement;

    if (usernameInput && passwordInput) {
      // Simulate input
      const nativeInputValueSetter = Object.getOwnPropertyDescriptor(window.HTMLInputElement.prototype, 'value')!.set!;
      nativeInputValueSetter.call(usernameInput, 'testuser');
      usernameInput.dispatchEvent(new Event('input', { bubbles: true }));
      nativeInputValueSetter.call(passwordInput, 'wrongpassword');
      passwordInput.dispatchEvent(new Event('input', { bubbles: true }));

      // Click submit
      const submitBtn = canvasElement.querySelector('button[type="submit"]') as HTMLButtonElement;
      if (submitBtn) submitBtn.click();
    }
  },
};
