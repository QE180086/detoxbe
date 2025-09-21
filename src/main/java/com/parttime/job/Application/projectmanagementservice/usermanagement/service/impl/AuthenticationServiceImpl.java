package com.parttime.job.Application.projectmanagementservice.usermanagement.service.impl;

import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.cart.entity.Cart;
import com.parttime.job.Application.projectmanagementservice.cart.repository.CartRepository;
import com.parttime.job.Application.projectmanagementservice.configuration.jwt.JwtUtil;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.profile.enumration.Gender;
import com.parttime.job.Application.projectmanagementservice.profile.repository.ProfileRepository;
import com.parttime.job.Application.projectmanagementservice.profile.request.CreateProfileDefaultRequest;
import com.parttime.job.Application.projectmanagementservice.profile.service.ProfileService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.OTPConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.StartDefinedRole;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.UserConstant;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.mapper.UserMapper;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UserRequestLogin;
import com.parttime.job.Application.projectmanagementservice.usermanagement.request.UserRequestRegister;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.AuthResponseLogin;
import com.parttime.job.Application.projectmanagementservice.usermanagement.response.UserResponse;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.AuthenticationService;
import com.parttime.job.Application.projectmanagementservice.usermanagement.service.EmailService;
import jakarta.mail.MessagingException;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AuthenticationServiceImpl implements AuthenticationService {
    private final UserDetailsService userDetailsService;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EmailService emailService;
    private final UserMapper userMapper;
    private final ProfileRepository profileRepository;
    private final ProfileService profileService;
    private final PointRepository pointRepository;
    private final CartRepository cartRepository;

    @Bean
    public AuthenticationManager authenticationManager() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return new ProviderManager(List.of(authProvider));
    }

    @Override
    public AuthResponseLogin login(UserRequestLogin request) {
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new AppException(MessageCodeConstant.M006_UNAUTHORIZED, "Account not found"));

        if (!user.getStatus().equals(UserStatus.ACTIVE)) {
            throw new AppException(MessageCodeConstant.M005_INVALID, "Account is not active");
        }

        Authentication authentication = authenticationManager().authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        AuthResponseLogin authResponseLogin = new AuthResponseLogin();
        authResponseLogin.setAccessToken(jwtUtil.generateToken(userDetails));
        authResponseLogin.setUserId(user.getId());
        authResponseLogin.setEmail(user.getEmail());
        authResponseLogin.setUsername(user.getUsername());
        return authResponseLogin;
    }

    @Override
    public UserResponse register(UserRequestRegister register) throws MessagingException {
        Optional<User> userG = userRepository.findByEmail(register.getEmail());
        Optional<User> userU = userRepository.findByUsername(register.getUsername());
        if (userU.isPresent()) {
            throw new AppException(MessageCodeConstant.M005_INVALID, UserConstant.USERNAME_IS_EXIST);
        }
        if (userG.isPresent()) {
            throw new AppException(MessageCodeConstant.M005_INVALID, UserConstant.GMAIL_IS_EXIST);
        }
        User u = new User();
        u.setStatus(UserStatus.INACTIVE);
        u.setRole(roleRepository.findByName(StartDefinedRole.USER).orElseThrow(
                () -> new AppException(MessageCodeConstant.M005_INVALID, UserConstant.ROLE_NOT_FOUND)));
        u.setDeleted(false);
        u.setUsername(register.getUsername());
        u.setEmail(register.getEmail());
        u.setPassword(passwordEncoder.encode(register.getPassword()));
        try {
            emailService.sendOTP(register.getEmail());
        } catch (MessagingException e) {
            throw new AppException(MessageCodeConstant.M005_INVALID, OTPConstant.SEND_OTP_FAIL);
        }
        userRepository.save(u);

        // Create default profile
        CreateProfileDefaultRequest request = new CreateProfileDefaultRequest();
        request.setUser(u);
        request.setGender(Gender.MALE);
        request.setFullName(u.getUsername());

        profileService.createDefaultProfile(request);

        // Create Point Default
        Point point = new Point();
        point.setCurrentPoints(0);
        point.setUser(u);
        pointRepository.save(point);

        // Create Cart
        Cart cart = new Cart();
        cart.setUser(u);
        cart.setActive(true);
        cartRepository.save(cart);

        return userMapper.toUserDTO(u);
    }
}
