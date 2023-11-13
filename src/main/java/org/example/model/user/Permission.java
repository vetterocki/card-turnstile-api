package org.example.model.user;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.Getter;

public enum Permission {
  ADMIN_READ("admin:read"),
  ADMIN_UPDATE("admin:update"),
  ADMIN_CREATE("admin:create"),
  ADMIN_DELETE("admin:delete");

  @Getter
  private final String description;

  public static Set<Permission> getAllPermissions() {
    return Arrays.stream(Permission.values()).collect(Collectors.toSet());
  }

  Permission(String description) {
    this.description = description;
  }
}
