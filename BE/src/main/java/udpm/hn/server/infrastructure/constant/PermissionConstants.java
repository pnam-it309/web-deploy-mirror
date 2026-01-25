package udpm.hn.server.infrastructure.constant;

/**
 * Permission codes used in the system
 */
public class PermissionConstants {

    // App Management
    public static final String VIEW_APPS = "VIEW_APPS";
    public static final String CREATE_APP = "CREATE_APP";
    public static final String EDIT_APP = "EDIT_APP";
    public static final String DELETE_APP = "DELETE_APP";
    public static final String APPROVE_APP = "APPROVE_APP";

    // Domain Management
    public static final String VIEW_DOMAINS = "VIEW_DOMAINS";
    public static final String CREATE_DOMAIN = "CREATE_DOMAIN";
    public static final String EDIT_DOMAIN = "EDIT_DOMAIN";
    public static final String DELETE_DOMAIN = "DELETE_DOMAIN";

    // Technology Management
    public static final String VIEW_TECHNOLOGIES = "VIEW_TECHNOLOGIES";
    public static final String CREATE_TECHNOLOGY = "CREATE_TECHNOLOGY";
    public static final String EDIT_TECHNOLOGY = "EDIT_TECHNOLOGY";
    public static final String DELETE_TECHNOLOGY = "DELETE_TECHNOLOGY";

    // Feature Management
    public static final String VIEW_FEATURES = "VIEW_FEATURES";
    public static final String CREATE_FEATURE = "CREATE_FEATURE";
    public static final String EDIT_FEATURE = "EDIT_FEATURE";
    public static final String DELETE_FEATURE = "DELETE_FEATURE";

    // User Management
    public static final String VIEW_USERS = "VIEW_USERS";
    public static final String CREATE_USER = "CREATE_USER";
    public static final String EDIT_USER = "EDIT_USER";
    public static final String DELETE_USER = "DELETE_USER";

    // Role Management
    public static final String VIEW_ROLES = "VIEW_ROLES";
    public static final String CREATE_ROLE = "CREATE_ROLE";
    public static final String EDIT_ROLE = "EDIT_ROLE";
    public static final String DELETE_ROLE = "DELETE_ROLE";
    public static final String ASSIGN_PERMISSIONS = "ASSIGN_PERMISSIONS";

    // System Management
    public static final String VIEW_DASHBOARD = "VIEW_DASHBOARD";
    public static final String VIEW_ANALYTICS = "VIEW_ANALYTICS";
    public static final String MANAGE_SETTINGS = "MANAGE_SETTINGS";
    public static final String VIEW_AUDIT_LOG = "VIEW_AUDIT_LOG";
    public static final String MANAGE_BACKUPS = "MANAGE_BACKUPS";

    // Content Moderation
    public static final String MODERATE_REVIEWS = "MODERATE_REVIEWS";
    public static final String MODERATE_COMMENTS = "MODERATE_COMMENTS";

    private PermissionConstants() {
        // Private constructor to prevent instantiation
    }
}
