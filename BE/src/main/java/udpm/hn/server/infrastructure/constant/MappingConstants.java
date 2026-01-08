package udpm.hn.server.infrastructure.constant;

public final class MappingConstants {
    public static final String API_AUTH_PREFIX = "/auth";

    // Các tiền tố GỐC của ứng dụng
    public static final String ADMIN = "/admin";
    public static final String CUSTOMER = "/customer";

    public static final String API_ADMIN_PREFIX = ADMIN;
    public static final String API_CUSTOMER_PREFIX = CUSTOMER;
    public static final String API_COMMON = "/common";

    // --- ADMIN MODULES ---
    public static final String API_ADMIN_DOMAIN = API_ADMIN_PREFIX + "/domains";
    public static final String API_ADMIN_APP = API_ADMIN_PREFIX + "/apps";
    public static final String API_ADMIN_FEATURE = API_ADMIN_PREFIX + "/features";
    public static final String API_ADMIN_TECHNOLOGY = API_ADMIN_PREFIX + "/technologies";
    public static final String API_ADMIN_DASHBOARD = API_ADMIN_PREFIX + "/dashboard";

    // customer
    public static final String API_CUSTOMER_DOMAIN = API_CUSTOMER_PREFIX + "/domains";
    public static final String API_CUSTOMER_APP = API_CUSTOMER_PREFIX + "/apps";
    public static final String API_CUSTOMER_TECHNOLOGY = API_CUSTOMER_PREFIX + "/technologies";
    public static final String API_CUSTOMER_DASHBOARD = API_CUSTOMER_PREFIX + "/dashboard";
}