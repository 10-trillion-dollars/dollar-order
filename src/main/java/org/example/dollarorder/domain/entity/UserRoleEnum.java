package org.example.dollarorder.domain.entity;

public enum UserRoleEnum {
    USER(Authority.USER),  // 사용자 권한
    SELLER(Authority.SELLER);  // 관리자 권한

    private final String authority;

    UserRoleEnum(String authority) {
        this.authority = authority;
    }

    public String getAuthority() {
        return this.authority;
    }

    public static class Authority {

        public static final String USER = "ROLE_USER";
        public static final String SELLER = "ROLE_SELLER";
    }


}
