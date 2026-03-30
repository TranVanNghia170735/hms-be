package com.hms.user.jwt;

import com.hms.user.dto.UserDTO;
import com.hms.user.exception.HmsException;
import com.hms.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MyUserDetailsService implements UserDetailsService {
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
            try{
                UserDTO dto = userService.getUser(email);
                return new CustomUserDetails(dto.getId(), dto.getEmail(),dto.getEmail(),
                        dto.getPassword(),dto.getRole(), dto.getName(), null);
        } catch (HmsException e) {
                e.printStackTrace();
            }
        return null;
    }
}
