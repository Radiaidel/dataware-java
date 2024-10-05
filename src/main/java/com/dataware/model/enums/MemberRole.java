package com.dataware.model.enums;


public enum MemberRole {
    PROJECT_MANAGER("Project manager"),
    DEVELOPER("Developer"),
    DESIGNER("Designer");

    private final String role;

    MemberRole(String role) {
        this.role = role;
    }

    @Override
    public String toString() {
        return role;
    }
    
    public static MemberRole fromString(String role) {
        for (MemberRole memberRole : MemberRole.values()) {
            // Compare the display role (case insensitive)
            if (memberRole.role.equalsIgnoreCase(role)) {
                return memberRole;
            }
        }
        throw new IllegalArgumentException("No enum constant for role: " + role);
    }
}
