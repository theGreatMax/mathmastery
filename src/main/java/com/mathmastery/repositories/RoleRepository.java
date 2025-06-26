package com.mathmastery.repositories;

import com.mathmastery.models.Role;

import java.util.Optional;

public interface RoleRepository {
    Optional<Role> findByName(String name);
}
