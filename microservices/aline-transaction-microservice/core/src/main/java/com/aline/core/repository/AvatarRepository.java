package com.aline.core.repository;

import com.aline.core.model.user.User;
import com.aline.core.model.user.UserAvatar;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AvatarRepository extends JpaRepository<UserAvatar, Long> {

}
