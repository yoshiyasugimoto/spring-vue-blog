import type { Meta, StoryObj } from "@storybook/vue3"
import PostListView from "./PostListView.vue"
import {
	withPopulatedPostStore,
	withMockedApis,
} from "@/stories/__mocks__/decorators"
import { mockPosts } from "@/stories/__mocks__/data"

const meta = {
	title: "Views/Admin/PostList",
	component: PostListView,
	tags: ["autodocs"],
	decorators: [withMockedApis()],
} satisfies Meta<typeof PostListView>

export default meta
type Story = StoryObj<typeof meta>

export const Default: Story = {
	decorators: [withPopulatedPostStore()],
}

export const Empty: Story = {
	decorators: [withPopulatedPostStore([])],
}

export const WithMixedStatuses: Story = {
	decorators: [
		withPopulatedPostStore([
			{ ...mockPosts[0], status: "PUBLISHED" },
			{ ...mockPosts[1], status: "PUBLISHED" },
			{ ...mockPosts[2], status: "DRAFT" },
		]),
	],
}
