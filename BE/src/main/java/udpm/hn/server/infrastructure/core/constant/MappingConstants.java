package udpm.hn.server.infrastructure.core.constant;

public final class MappingConstants {
    public static final String API_AUTH_PREFIX = "/auth";

    // Các tiền tố GỐC của ứng dụng
    public static final String ADMIN = "/admin";
    public static final String CUSTOMER = "/customer";
    public static final String PUBLIC = "/public";

    public static final String API_ADMIN_PREFIX = ADMIN;
    // Đã sửa: Chỉ còn là /customer
    public static final String API_CUSTOMER_PREFIX = CUSTOMER;

    //ADMIN
    public static final String API_ADMIN_IMPORT = API_ADMIN_PREFIX + "/import_data";
    public static final String API_ADMIN_PRODUCT = API_ADMIN_PREFIX + "/products";
    public static final String API_ADMIN_ORDER = API_ADMIN_PREFIX + "/orders";
    public static final String API_ADMIN_WAREHOUSE = API_ADMIN_PREFIX + "/warehouses";
    public static final String API_ADMIN_CUSTOMER = API_ADMIN_PREFIX + "/customers";
    public static final String API_ADMIN_CATEGORY = API_ADMIN_PREFIX + "/categories";
    public static final String API_ADMIN_BRAND = API_ADMIN_PREFIX + "/brands";

    //CUSTOMER
    public static final String API_CUSTOMER_VIEW_PRODUCT = API_CUSTOMER_PREFIX + "/view_products";
    public static final String API_CUSTOMER_ORDER = API_CUSTOMER_PREFIX + "/order";
    public static final String API_CUSTOMER_DASHBOARD = API_CUSTOMER_PREFIX + "/dashboard";
    public static final String API_CUSTOMER_WHISLIST = API_CUSTOMER_PREFIX + "/wishlist";
    //PUBLIC
    public static final String API_PUBLIC_PREFIX = PUBLIC;
    public static final String API_PUBLIC_PRODUCTS = API_PUBLIC_PREFIX + "/view_products";
    public static final String API_PUBLIC_CATEGORIES = API_PUBLIC_PREFIX + "/categories";

}