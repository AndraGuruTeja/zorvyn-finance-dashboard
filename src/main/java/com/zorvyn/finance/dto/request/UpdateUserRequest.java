package com.zorvyn.finance.dto.request;

import com.zorvyn.finance.entity.Role;
import com.zorvyn.finance.entity.UserStatus;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateUserRequest {

    @Size(min = 2, max = 100, message = "Name must be 2–100 characters")
    private String name;

    @Email(message = "Must be a valid email address")
    private String email;

    @Size(min = 6, message = "Password must be at least 6 characters")
    private String password;

    private Role role;

    private UserStatus status;
}
