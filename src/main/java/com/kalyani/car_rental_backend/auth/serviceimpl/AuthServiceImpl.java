package com.kalyani.car_rental_backend.auth.serviceimpl;

import com.kalyani.car_rental_backend.auth.dto.*;
import com.kalyani.car_rental_backend.auth.service.AuthService;
import com.kalyani.car_rental_backend.config.JwtUtil;
import com.kalyani.car_rental_backend.driver.entity.DriverProfile;
import com.kalyani.car_rental_backend.driver.repository.DriverProfileRepository;
import com.kalyani.car_rental_backend.user.dto.UserResponse;
import com.kalyani.car_rental_backend.user.entity.*;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class AuthServiceImpl implements AuthService {
    private final UserRepository users; private final PasswordEncoder encoder; private final JwtUtil jwt; private final DriverProfileRepository drivers;
    public AuthServiceImpl(UserRepository users,PasswordEncoder encoder,JwtUtil jwt,DriverProfileRepository drivers){this.users=users;this.encoder=encoder;this.jwt=jwt;this.drivers=drivers;}
    @Transactional public AuthResponse register(RegisterRequest request){return createUser(request,Role.USER);}
    @Transactional public AuthResponse registerDriver(DriverRegisterRequest request){
        AuthResponse response=createUser(request,Role.DRIVER);
        User user=users.findByEmailIgnoreCase(request.getEmail().trim()).orElseThrow();
        DriverProfile p=new DriverProfile();p.setUser(user);p.setLicenseNumber(request.getLicenseNumber());p.setVehicleNumber(request.getVehicleNumber());p.setVehicleModel(request.getVehicleModel());p.setAadhaarLast4(request.getAadhaarLast4());drivers.save(p);
        return response;
    }
    private AuthResponse createUser(RegisterRequest request,Role role){String email=request.getEmail().trim().toLowerCase();if(users.existsByEmailIgnoreCase(email))throw new IllegalArgumentException("Email is already registered");User u=new User();u.setFullName(request.getFullName().trim());u.setEmail(email);u.setPhone(request.getPhone());u.setPassword(encoder.encode(request.getPassword()));u.setRole(role);u.setProvider("LOCAL");u.setStatus("ACTIVE");u.setEmailVerified(false);return response(users.save(u));}
    public AuthResponse login(LoginRequest request){User u=users.findByEmailIgnoreCase(request.getEmail().trim()).orElseThrow(()->new IllegalArgumentException("Invalid email or password"));if(!"ACTIVE".equalsIgnoreCase(u.getStatus()))throw new IllegalArgumentException("Account is not active");if(!encoder.matches(request.getPassword(),u.getPassword()))throw new IllegalArgumentException("Invalid email or password");return response(u);}
    private AuthResponse response(User u){UserResponse user=new UserResponse(u.getId(),u.getFullName(),u.getEmail(),u.getPhone(),u.getRole().name(),u.getProvider(),u.getStatus());return new AuthResponse(jwt.generateToken(u),user);}
}
