package com.hms.user.api;


import com.hms.user.dto.LoginDTO;
import com.hms.user.dto.ResponseDTO;
import com.hms.user.dto.UserDTO;
import com.hms.user.exception.HmsException;
import com.hms.user.jwt.JwtUtil;
import com.hms.user.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@Validated
@CrossOrigin
@RequiredArgsConstructor
public class UserAPI {

    private final UserService userService;
    private final UserDetailsService userDetailsService;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<ResponseDTO> registerUser(@RequestBody @Valid UserDTO userDTO) throws HmsException {
        userService.registerUser(userDTO);
        return new ResponseEntity<>(new ResponseDTO("Account created"), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<String> postMethodName (@RequestBody LoginDTO loginDTO) throws HmsException {
        try{
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginDTO.getEmail(), loginDTO.getPassword()));
        } catch (AuthenticationException e) {
            throw new HmsException("INVALID_CREDENTIALS");
        }
        final UserDetails userDetails = userDetailsService.loadUserByUsername(loginDTO.getEmail());
        final String jwt = jwtUtil.generateToken(userDetails);
        return new ResponseEntity<>(jwt, HttpStatus.OK);
    }

    @GetMapping("/test")
    public ResponseEntity<String> test(){
        return new ResponseEntity<>("Test2", HttpStatus.OK);
    }
}
