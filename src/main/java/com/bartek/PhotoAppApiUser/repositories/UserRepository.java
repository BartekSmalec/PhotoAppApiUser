package com.bartek.PhotoAppApiUser.repositories;

import com.bartek.PhotoAppApiUser.data.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByEmail(String email);

    UserEntity findByUserId(String userId);
}
