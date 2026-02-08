import type { Preview } from '@storybook/vue3';
import { setup } from '@storybook/vue3';
import Antd from 'ant-design-vue';
import '../src/app.css';
import { createPinia } from 'pinia';
import { createRouter, createMemoryHistory } from 'vue-router';

setup((app) => {
  app.use(Antd);
  app.use(createPinia());

  const router = createRouter({
    history: createMemoryHistory(),
    routes: [
      { path: '/', component: { template: '<div />' } },
      { path: '/login', component: { template: '<div />' } },
      { path: '/admin', redirect: '/admin/dashboard' },
      { path: '/admin/dashboard', component: { template: '<div />' } },
      { path: '/admin/posts', component: { template: '<div />' } },
      { path: '/admin/posts/new', component: { template: '<div />' } },
      { path: '/admin/posts/:id', component: { template: '<div />' } },
      { path: '/admin/categories', component: { template: '<div />' } },
      { path: '/admin/tags', component: { template: '<div />' } },
      { path: '/post/:slug', component: { template: '<div />' } },
    ],
  });
  app.use(router);
});

const preview: Preview = {
  parameters: {
    chromatic: {
      pauseAnimationAtEnd: true,
      diffThreshold: 0.063,
    },
    controls: {
      matchers: {
        color: /(background|color)$/i,
        date: /Date$/i,
      },
    },
  },
};

export default preview;
