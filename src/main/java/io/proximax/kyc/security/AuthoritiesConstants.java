package io.proximax.kyc.security;

/**
 * Constants for Spring Security authorities.
 */
public final class AuthoritiesConstants {

    public static final String ADMIN = "ROLE_ADMIN";

    public static final String USER = "ROLE_USER";

    public static final String ANONYMOUS = "ROLE_ANONYMOUS";

    public static final String OBTEAM  = "ROLE_OBTEAM";

    public static final String ORG_ADMIN  = "ROLE_ORGANIZATION_ADMIN";

    public static final String NOTARY  = "ROLE_NOTARY";

    private AuthoritiesConstants() {
    }
}
