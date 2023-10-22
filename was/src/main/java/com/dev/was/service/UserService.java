package com.dev.was.service;

import com.dev.was.controller.ApiException;
import com.dev.was.controller.ExceptionCodeEnum;
import com.dev.was.dto.UserDto;
import com.dev.was.entity.UserEntity;
import com.dev.was.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.SQLException;


@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    public UserDto joinUserInfo(String email, String password, String name, String phone) {
        try {
            if (userRepository.findByEmail(email) != null)
                throw new ApiException(ExceptionCodeEnum.DUPLICATE_EMAIL);
            if (userRepository.findByPhone(phone) != null)
                throw new ApiException(ExceptionCodeEnum.DUPLICATE_PHONE);

            UserEntity userEntity = userRepository.save(
                    UserEntity.builder()
                            .email(email)
                            .password(passwordEncoder.encode(password))
                            .name(name)
                            .phone(phone)
                            .role("ROLE_USER")
                            .profileImg("https://ssl.pstatic.net/static/pwe/address/img_profile.png")
                            .build()
            );
            if (userEntity == null)
                throw new ApiException(ExceptionCodeEnum.INVALID_ARGUMENT);
            return new UserDto(userEntity);
        }
        catch (ApiException e) {
            throw e;
        }
        catch(RuntimeException e) {
            throw new ApiException(ExceptionCodeEnum.DB_ERROR);
        }
    }

    public UserDto getUserInfo(String userId) {
        UserEntity userEntity = userRepository.findByUserId(userId);

        if (userEntity == null)
            throw new ApiException(ExceptionCodeEnum.INVALID_ARGUMENT);
        return new UserDto(userEntity);
    }

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);
}
