package org.stockmaster3000;

import org.stockmaster3000.stockmaster3000.model.User;

public final class TestDataUtil {
    private TestDataUtil() {
    }

    public static User createTestUserA() {
        User user = new User();
        user.setId(1L);
        user.setUsername("test1");
        user.setPassword("12345678");
        return user;
    }

    public static User createTestUserB() {
        User user = new User();
        user.setId(2L); // Changed id to be unique
        user.setUsername("test2");
        user.setPassword("12345678");
        return user;
    }

    public static User createTestUserC() {
        User user = new User();
        user.setId(3L); // Changed id to be unique
        user.setUsername("test3");
        user.setPassword("12345678");
        return user;
    }
}
