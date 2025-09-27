package udpm.hn.server.infrastructure.core.constant;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public enum Role {

    ADMIN("ADMIN"),

    MANAGE("Customer");


    private final String nameInVietnamese;

    Role(String nameInVietnamese) {
        this.nameInVietnamese = nameInVietnamese;
    }

    public static List<String> getAllRoles() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.toList());
    }

    public static String getAllRolesString() {
        return Arrays.stream(Role.values())
                .map(Enum::name)
                .collect(Collectors.joining(", "));
    }

    // lấy tên tiếng việt
    public static List<String> getAllRolesInVietnamese() {
        return Arrays.stream(Role.values())
                .map(Role::getNameInVietnamese)
                .collect(Collectors.toList());
    }

    // lấy tên tiếng việt từng vai trò
    public static String getVietnameseNameByRole(String roleName) {
        return Arrays.stream(Role.values())
                .filter(role -> role.name().equalsIgnoreCase(roleName))
                .map(Role::getNameInVietnamese)
                .findFirst()
                .orElse(null);
    }

}
