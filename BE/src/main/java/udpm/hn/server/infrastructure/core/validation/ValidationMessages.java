package udpm.hn.server.infrastructure.core.validation;

public final class ValidationMessages {
    private ValidationMessages() {
        // Private constructor to prevent instantiation
    }

    // Common messages
    public static final String NOT_BLANK = "{not.blank}";
    public static final String NOT_NULL = "{not.null}";
    public static final String INVALID_EMAIL = "{invalid.email}";
    public static final String INVALID_SIZE = "{invalid.size}";
    public static final String INVALID_FORMAT = "{invalid.format}";
    
    // Custom messages
    public static final String INVALID_SLUG = "{invalid.slug}";
    public static final String DUPLICATE_SLUG = "{duplicate.slug}";
    public static final String INVALID_FILE_SIZE = "{invalid.file.size}";
    public static final String INVALID_FILE_TYPE = "{invalid.file.type}";
    
    // Field names (for error messages)
    public static final String FIELD_NAME = "name";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_SLUG = "slug";
    public static final String FIELD_FILE = "file";
}
