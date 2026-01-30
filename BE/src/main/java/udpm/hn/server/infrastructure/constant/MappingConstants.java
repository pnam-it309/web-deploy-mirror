package udpm.hn.server.infrastructure.constant;

public final class MappingConstants {
    public static final String API_AUTH_PREFIX = "/api/v1/auth";

    // Các tiền tố GỐC của ứng dụng
    public static final String ADMIN = "/api/v1/admin";
    public static final String CUSTOMER = "/api/v1/customer";

    public static final String API_ADMIN_PREFIX = ADMIN;
    public static final String API_CUSTOMER_PREFIX = CUSTOMER;
    public static final String API_COMMON = "/api/v1/common";

    // --- ADMIN MODULES ---
    public static final String API_ADMIN_DOMAIN = API_ADMIN_PREFIX + "/domains";
    public static final String API_ADMIN_APP = API_ADMIN_PREFIX + "/apps";
    public static final String API_ADMIN_FEATURE = API_ADMIN_PREFIX + "/features";
    public static final String API_ADMIN_TECHNOLOGY = API_ADMIN_PREFIX + "/technologies";
    public static final String API_ADMIN_DASHBOARD = API_ADMIN_PREFIX + "/dashboard";
    public static final String API_AUDIT_LOGS = API_ADMIN_PREFIX + "/audit-logs";
    public static final String API_ADMIN_CUSOTMER = API_ADMIN_PREFIX + "/customers";
    public static final String API_ADMIN_EXPORT = API_ADMIN_PREFIX + "/export";
    public static final String API_ADMIN_MEDIA = API_ADMIN_PREFIX + "/media-library";
    public static final String API_ADMIN_MODERATION = API_ADMIN_PREFIX + "/moderation";
    public static final String API_ADMIN_PREVIEW = API_ADMIN_PREFIX + "/preview";
    public static final String API_ADMIN_ROLE = API_ADMIN_PREFIX + "/roles";
    public static final String API_ADMIN_SECURITY = API_ADMIN_PREFIX + "/ip-whitelist";
    public static final String API_ADMIN_SUBCRIPTION = API_ADMIN_PREFIX + "/subscriptions";
    public static final String API_ADMIN_TRASH = API_ADMIN_PREFIX + "/trash";
    // customer
    public static final String API_CUSTOMER_DOMAIN = API_CUSTOMER_PREFIX + "/domains";
    public static final String API_CUSTOMER_APP = API_CUSTOMER_PREFIX + "/apps";
    public static final String API_CUSTOMER_TECHNOLOGY = API_CUSTOMER_PREFIX + "/technologies";
    public static final String API_CUSTOMER_DASHBOARD = API_CUSTOMER_PREFIX + "/dashboard";
    public static final String API_CUSTOMER_REVIEW = API_CUSTOMER_PREFIX + "/reviews";
    public static final String API_CUSTOMER_NOTIFICATION = API_CUSTOMER_PREFIX + "/push";
    public static final String API_CUSTOMER_LIKE = API_CUSTOMER_PREFIX + "/likes";
    public static final String API_CUSTOMER_PREVIEW = API_CUSTOMER_PREFIX + "/preview";
    public static final String API_CUSTOMER_SUBCRIPTION = API_CUSTOMER_PREFIX + "/subscribe";
}