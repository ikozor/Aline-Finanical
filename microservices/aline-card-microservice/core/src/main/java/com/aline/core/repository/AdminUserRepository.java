package com.aline.core.repository;

import com.aline.core.model.user.AdminUser;

public interface AdminUserRepository extends IUserRepository<AdminUser> {
    boolean existsByEmail(String email);
}
