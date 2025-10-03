package com.mindmapper.security.utils;

public enum UserRole {
    ADMIN(1),
    STUDENT(2),
    INSTRUCTOR(3);

    private final int value;

    UserRole(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
