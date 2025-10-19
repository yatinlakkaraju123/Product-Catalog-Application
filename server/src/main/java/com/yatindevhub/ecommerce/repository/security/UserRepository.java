package com.yatindevhub.ecommerce.repository.security;

import com.yatindevhub.ecommerce.entity.security.UserInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<UserInfo,String> {


    public UserInfo findByUsername(String username);


}
