package org.stockmaster3000;

import org.stockmaster3000.stockmaster3000.model.User;

// Utility class for creating test user objects
public final class TestDataUtil {
    private TestDataUtil() {
    }

    // Creates and returns a test user with predefined values
    public static User createTestUserA() {
        User user = new User();
        user.setId(1L); // Assign a unique ID to user A
        user.setUsername("test1"); // Set username
        user.setPassword("12345678"); // Set password
        return user;
    }

    // Creates and returns another test user with different values
    public static User createTestUserB() {
        User user = new User();
        user.setId(2L); // Assign a unique ID to user B
        user.setUsername("test2");
        user.setPassword("12345678");
        return user;
    }

    // Creates and returns a third test user with different values
    public static User createTestUserC() {
        User user = new User();
        user.setId(3L); // Assign a unique ID to user C
        user.setUsername("test3");
        user.setPassword("12345678");
        return user;
    }
}
