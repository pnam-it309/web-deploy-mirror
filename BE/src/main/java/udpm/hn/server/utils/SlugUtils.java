package udpm.hn.server.utils;

import java.text.Normalizer;
import java.util.Locale;
import java.util.regex.Pattern;

public class SlugUtils {
    private static final Pattern NON_LATIN = Pattern.compile("[^\\w-]");
    private static final Pattern WHITESPACE = Pattern.compile("[\\s]");
    private static final Pattern EDGES_DASHES = Pattern.compile("(^-|-$)");

    private SlugUtils() {
        // Private constructor to prevent instantiation
    }

    public static String toSlug(String input) {
        if (input == null || input.trim().isEmpty()) {
            return "";
        }

        // Convert to ASCII
        String noWhitespace = WHITESPACE.matcher(input).replaceAll("-");
        String normalized = Normalizer.normalize(noWhitespace, Normalizer.Form.NFD);
        String slug = NON_LATIN.matcher(normalized).replaceAll("");

        // Convert to lowercase and trim dashes
        slug = slug.toLowerCase(Locale.ENGLISH);
        slug = EDGES_DASHES.matcher(slug).replaceAll("");

        // Remove consecutive dashes
        slug = slug.replaceAll("-{2,}", "-");

        return slug;
    }
}
