package udpm.hn.server.infrastructure.core.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Roles {

    ADMIN("Administrator"),
    CUSTOMER("Customer");

    private final String nameInVietnamese;

    Roles(String nameInVietnamese) {
        this.nameInVietnamese = nameInVietnamese;
    }

    public static List<String> getAllRoles() {
        return Arrays.stream(Roles.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static String getAllRolesString() {
        return Arrays.stream(Roles.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    // lấy tên tiếng việt
    public static List<String> getAllRolesInVietnamese() {
        return Arrays.stream(Roles.values())
                .map(Roles::getNameInVietnamese)
                .collect(Collectors.toList());
    }

    // lấy tên tiếng việt từng vai trò
    public static String getVietnameseNameByRole(String roleName) {
        return Arrays.stream(Roles.values())
                .filter(role -> role.name().equalsIgnoreCase(roleName))
                .map(Roles::getNameInVietnamese)
                .findFirst()
                .orElse(null);
    }
}
