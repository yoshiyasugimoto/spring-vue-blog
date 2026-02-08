package com.blog.dto;

import com.blog.entity.User;
import com.blog.testutil.TestDataFactory;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class UserResponseTest {

    @Test
    void from_MapsAllFieldsCorrectly() {
        User user = TestDataFactory.createUser(1L, "admin", "Admin User", "ADMIN");

        UserResponse response = UserResponse.from(user);

        assertThat(response.id()).isEqualTo(1L);
        assertThat(response.username()).isEqualTo("admin");
        assertThat(response.displayName()).isEqualTo("Admin User");
        assertThat(response.role()).isEqualTo("ADMIN");
    }

    @Test
    void from_HandlesNullDisplayName() {
        User user = TestDataFactory.createUser(1L, "admin", null, "ADMIN");

        UserResponse response = UserResponse.from(user);

        assertThat(response.displayName()).isNull();
    }
}
