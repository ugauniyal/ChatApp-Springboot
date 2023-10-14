package com.ug.chatapp.repos;

import com.ug.chatapp.entity.UserModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Repository
public interface UserRepository extends JpaRepository<UserModel,Long> {
//    @Cacheable(value = "user")
    public UserModel findByUsername(String username);


    public UserModel findByEmail(String email);

    public UserModel findByVerificationCode(String code);
}
