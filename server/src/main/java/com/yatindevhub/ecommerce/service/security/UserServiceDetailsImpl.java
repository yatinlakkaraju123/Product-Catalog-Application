package com.yatindevhub.ecommerce.service.security;

import com.yatindevhub.ecommerce.dto.security.UserInfoDto;
import com.yatindevhub.ecommerce.entity.security.Role;
import com.yatindevhub.ecommerce.entity.security.UserInfo;
import com.yatindevhub.ecommerce.entity.security.UserRole;
import com.yatindevhub.ecommerce.repository.security.UserRepository;
import com.yatindevhub.ecommerce.repository.security.UserRoleRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;

@Service
@Data
@RequiredArgsConstructor
@Slf4j
public class UserServiceDetailsImpl implements UserDetailsService {

    private  final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserRoleRepository userRoleRepository;
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserInfo user = userRepository.findByUsername(username);
        if(user==null){
            throw new UsernameNotFoundException("could not find user");
        }

        return new CustomUserDetails(user);
    }
    public UserInfo checkIfUserAlreadyExists(UserInfoDto userInfoDto){
        return userRepository.findByUsername(userInfoDto.getUsername());
    }

    public Boolean signupUser(UserInfoDto userInfoDto){
        // check if email and password are valid create a ValidationUtil in that define these methods
        userInfoDto.setPassword(passwordEncoder.encode(userInfoDto.getPassword()));
        if(Objects.nonNull(checkIfUserAlreadyExists(userInfoDto))){
            return false;
        }

        String userId = UUID.randomUUID().toString();

//        UserRole userRole = new UserRole();
//        userRole.setName(Role.ROLE_USER);
        UserRole role = userRoleRepository.findByName(Role.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Role not found"));
        userRepository.save(new UserInfo(userId,
                userInfoDto.getUsername(),
                userInfoDto.getPassword(),
                new HashSet<>(Set.of(role))));
        return true;

    }
}
