package com.fpt.locketcoupleapi.config;

import com.fpt.locketcoupleapi.entity.User;
import com.fpt.locketcoupleapi.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class Util {
    @Autowired
    private final UserRepository userRepository;

    public User getUserFromAuthentication(){
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        return userRepository.findByUserName(authentication.getName())
                .orElseThrow(() -> new EntityNotFoundException(new Exception("UserName not found!!!")));
    }
}
