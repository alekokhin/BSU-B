package com.dev.mainbackend.controllers;

import com.dev.mainbackend.models.Users;
import com.dev.mainbackend.repository.UserRepository;
import com.dev.mainbackend.request.LoginRequest;
import com.dev.mainbackend.request.RefreshTokenRequest;
import com.dev.mainbackend.request.SignupRequest;
import com.dev.mainbackend.response.JwtResponse;
import com.dev.mainbackend.response.MessageResponse;
import com.dev.mainbackend.security.jwt.JwtUtils;
import com.dev.mainbackend.security.services.UserDetailsImpl;
import io.jsonwebtoken.Jwts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    private final Logger logger = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @GetMapping
    public List<Users> getAllUsers() {
        return userRepository.findAll();
    }

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateuser(@RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtils.generateJwtAccessToken(loginRequest.getUsername());
        String refreshToken = jwtUtils.generateJwtRefreshToken(loginRequest.getUsername());


        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();
        logger.info("auth user -> " + loginRequest.getUsername());
        return ResponseEntity
                .ok(new JwtResponse(accessToken, refreshToken, userDetails.get_id(), userDetails.getUsername()));
    }

    @PostMapping("/refreshtoken")
    public ResponseEntity<?> refreshToken(@RequestBody RefreshTokenRequest refreshTokenRequest) {

        // Get the refresh token from the request body.
        String refreshToken = refreshTokenRequest.getRefreshToken();
        if (refreshToken != null) {

            // Verify the refresh token.
            if (jwtUtils.isRefreshTokenValid(refreshToken)) {
                String username = "";
                try {
                    username = Jwts.parser().setSigningKey(jwtUtils.getJwtSecret()).parseClaimsJws(refreshToken).getBody().getSubject();
                    String newAccessToken = jwtUtils.generateJwtAccessToken(username);
                    String newRefreshToken = jwtUtils.generateJwtRefreshToken(username);
                    Users users = userRepository.findByUsername(username).get();
                    return ResponseEntity.ok(new JwtResponse(newAccessToken, newRefreshToken, users.get_id(), users.getUsername()));
                } catch (Exception e) {
                    logger.error("JWT token is expired: " + e.getMessage());
                }
            }
        }
        logger.info("invalid or empty refresh token");
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid refresh token.");
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@RequestBody SignupRequest signUpRequest) {
        if (mongoTemplate.findOne(findBy("username", signUpRequest.getUsername()), Users.class) != null) {
            logger.error("username is already taken! ");
            return ResponseEntity.badRequest().body(new MessageResponse("Error: username is already taken!"));
        }


        // Create new user account
        Users users = new Users(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        mongoTemplate.save(users);
        logger.info("user -> " + signUpRequest.getUsername() + "registered successfully!");
        return ResponseEntity.ok(new MessageResponse("user registered successfully!"));
    }

    private Query findBy(String key, String value) {
        return new Query().addCriteria(Criteria.where(key).is(value));
    }
}
