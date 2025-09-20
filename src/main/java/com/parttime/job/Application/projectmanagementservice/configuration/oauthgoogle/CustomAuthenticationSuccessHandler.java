package com.parttime.job.Application.projectmanagementservice.configuration.oauthgoogle;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.parttime.job.Application.common.constant.MessageCodeConstant;
import com.parttime.job.Application.common.dto.GenericResponse;
import com.parttime.job.Application.common.dto.MessageDTO;
import com.parttime.job.Application.common.exception.AppException;
import com.parttime.job.Application.projectmanagementservice.configuration.jwt.JwtUtil;
import com.parttime.job.Application.projectmanagementservice.point.entity.Point;
import com.parttime.job.Application.projectmanagementservice.point.repository.PointRepository;
import com.parttime.job.Application.projectmanagementservice.profile.entity.Profile;
import com.parttime.job.Application.projectmanagementservice.profile.enumration.Gender;
import com.parttime.job.Application.projectmanagementservice.profile.repository.ProfileRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.constant.StartDefinedRole;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.Role;
import com.parttime.job.Application.projectmanagementservice.usermanagement.entity.User;
import com.parttime.job.Application.projectmanagementservice.usermanagement.enumration.UserStatus;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.RoleRepository;
import com.parttime.job.Application.projectmanagementservice.usermanagement.repository.UserRepository;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.security.SecureRandom;
import java.util.*;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    private static final String UPPERCASE = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
    private static final String LOWERCASE = "abcdefghijklmnopqrstuvwxyz";
    private static final String DIGITS = "0123456789";

    private static final SecureRandom random = new SecureRandom();

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final UserRepository userRepository;
    private final ProfileRepository profileRepository;
    private final PointRepository pointRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CustomAuthenticationSuccessHandler(JwtUtil jwtUtil,
                                              UserDetailsService userDetailsService,
                                              UserRepository userRepository,
                                              ProfileRepository profileRepository,
                                              PointRepository pointRepository,
                                              RoleRepository roleRepository,
                                              PasswordEncoder passwordEncoder) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
        this.userRepository = userRepository;
        this.profileRepository = profileRepository;
        this.pointRepository = pointRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
        OAuth2User oauthUser = (OAuth2User) authentication.getPrincipal();

        String email = oauthUser.getAttribute("email");
        String name = oauthUser.getAttribute("name");
        String picture = oauthUser.getAttribute("picture");

        User user = createOrGetUser(email, name, picture);

        User managedUser = userRepository.findById(user.getId())
                .orElseThrow(() -> new RuntimeException("User not found"));


        Point point = pointRepository.findByUserId(user.getId());
        if (point == null) {
            point = new Point();
            point.setUser(managedUser);
            point.setCurrentPoints(0);
            pointRepository.save(point);
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(email);

        String token = jwtUtil.generateToken(userDetails);

//        Map<String, Object> data = new HashMap<>();
//        data.put("accessToken", token);
//        data.put("userId", user.getId());
//        data.put("username", user.getUsername());
//        data.put("email", user.getEmail());

//        GenericResponse<Map<String, Object>> apiResponse = new GenericResponse<>(
//                true,
//                new MessageDTO(MessageCodeConstant.M001_SUCCESS, "Login successful"),
//                null,
//                data
//        );
//
//        response.setContentType("application/json");
//        response.setCharacterEncoding("UTF-8");
//
//        ObjectMapper mapper = new ObjectMapper();
//        response.getWriter().write(mapper.writeValueAsString(apiResponse));
        response.sendRedirect("https://www.detoxcare.site/login/success?token=" + token);

    }

    private User createOrGetUser(String email, String name, String picture) {
        if (email == null) {
            throw new AppException(MessageCodeConstant.M006_UNAUTHORIZED, "Email not found from OAuth2 provider");
        }
        Role role = roleRepository.findByName(StartDefinedRole.USER)
                .orElseThrow(() -> new AppException(MessageCodeConstant.M003_NOT_FOUND, "Role not found"));

        User existingUser = userRepository.findByEmail(email).orElse(null);

        if (existingUser != null) {
            if (existingUser.getStatus() != UserStatus.ACTIVE) {
                throw new AppException(MessageCodeConstant.M005_INVALID, "Account is not active");
            }
            return existingUser;
        }

        User user = new User();
        user.setEmail(email);
        user.setRole(role);
        user.setUsername(email);
        user.setPassword(passwordEncoder.encode(generatePassword(8)));
        user.setStatus(UserStatus.ACTIVE);
        user = userRepository.save(user);

        Profile profile = new Profile();
        profile.setGender(Gender.MALE);
        profile.setAvatar(picture);
        profile.setFullName(name);
        profile.setNickName(name);
        profile.setUser(user);

        profile = profileRepository.save(profile);

        user.setProfile(profile);
        user = userRepository.save(user);

        return user;
    }

    public static String generatePassword(int length) {
        if (length < 6) {
            throw new IllegalArgumentException("Password length must be at least 6 characters");
        }

        List<Character> passwordChars = new ArrayList<>();

        passwordChars.add(UPPERCASE.charAt(random.nextInt(UPPERCASE.length())));
        passwordChars.add(LOWERCASE.charAt(random.nextInt(LOWERCASE.length())));
        passwordChars.add(DIGITS.charAt(random.nextInt(DIGITS.length())));

        String allChars = UPPERCASE + LOWERCASE + DIGITS;
        for (int i = 3; i < length; i++) {
            passwordChars.add(allChars.charAt(random.nextInt(allChars.length())));
        }

        Collections.shuffle(passwordChars, random);

        StringBuilder password = new StringBuilder();
        for (char c : passwordChars) {
            password.append(c);
        }
        return password.toString();
    }
}
