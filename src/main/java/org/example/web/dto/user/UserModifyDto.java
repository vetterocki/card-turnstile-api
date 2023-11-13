package org.example.web.dto.user;

import jakarta.validation.constraints.Email;
import lombok.Data;
import org.example.model.user.Role;

@Data
public class UserModifyDto {
  @Email(message = "Incorrect email syntax")
  private String email;

  private String password;

  private final Role role = Role.USER;


}
