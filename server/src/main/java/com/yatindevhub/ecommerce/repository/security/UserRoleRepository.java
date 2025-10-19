package com.yatindevhub.ecommerce.repository.security;

import com.yatindevhub.ecommerce.entity.security.Role;
import com.yatindevhub.ecommerce.entity.security.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRoleRepository extends JpaRepository<UserRole,Long> {

    Optional<UserRole> findByName(Role role);
}
