package udpm.hn.server.infrastructure.core.constant;

public final class MappingConstants {
    /* API VERSION PREFIX */
    public static final String API_VERSION_PREFIX = "/api/v1";
    /* API COMMON */
    public static final String API_COMMON = API_VERSION_PREFIX + "/common";

    /* AUTHENTICATION */
    public static final String API_AUTH_PREFIX = API_VERSION_PREFIX + "/auth";

    public static final String ADMIN = "/admin";

    public static final String CUSTOMER = "/customer";

    public static final String API_ADMIN_PREFIX = API_VERSION_PREFIX + ADMIN;
    public static final String API_CUSTOMER_PREFIX = API_VERSION_PREFIX + CUSTOMER;
    //ADMIN
    public static final String API_ADMIN_IMPORT = API_ADMIN_PREFIX + "/import_data";
    public static final String API_ADMIN_PRODUCT = API_ADMIN_PREFIX + "/products";
    public static final String API_ADMIN_ORDER = API_ADMIN_PREFIX + "/orders";
    public static final String API_ADMIN_WAREHOUSE = API_ADMIN_PREFIX + "/warehouses";
    public static final String API_ADMIN_CUSTOMER = API_ADMIN_PREFIX + "/customers";
    //CUSTOMER
    public static final String API_CUSTOMER_VIEW_PRODUCT = API_CUSTOMER_PREFIX + "/view_products";
    public static final String API_CUSTOMER_REGISTER = API_CUSTOMER_PREFIX + "/register";

}

