package org.example.model.user;

import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;
import lombok.Getter;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


public enum Role {
  USER(Collections.emptySet()),
  ADMIN(Permission.getAllPermissions());

  Role(Set<Permission> permissions) {
    this.permissions = permissions;
  }

  @Getter
  private final Set<Permission> permissions;

  public List<SimpleGrantedAuthority> getAuthorities() {
    return Stream.concat(
        Stream.of(new SimpleGrantedAuthority("ROLE_" + this.name())),
        this.permissions.stream()
            .map(Permission::name)
            .map(SimpleGrantedAuthority::new)
    ).toList();
  }
}