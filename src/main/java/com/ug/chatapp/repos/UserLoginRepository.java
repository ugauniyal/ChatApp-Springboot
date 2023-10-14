package com.ug.chatapp.repos;


import com.ug.chatapp.entity.UserLoginModel;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserLoginRepository extends JpaRepository<UserLoginModel, Long> {

    public UserLoginModel findByUserModelId(Long id);

    UserLoginModel findByToken(String token);
}
