package com.kalyani.car_rental_backend.config;

import com.kalyani.car_rental_backend.user.entity.User;
import com.kalyani.car_rental_backend.user.repository.UserRepository;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import java.io.IOException;
import java.util.List;

@Component
public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil; private final UserRepository users;
    public JwtFilter(JwtUtil jwtUtil,UserRepository users){this.jwtUtil=jwtUtil;this.users=users;}

    protected void doFilterInternal(HttpServletRequest request,HttpServletResponse response,FilterChain chain)
            throws ServletException, IOException {
        String header=request.getHeader("Authorization");
        if(header==null || !header.startsWith("Bearer ")){chain.doFilter(request,response);return;}
        String token=header.substring(7);
        try{
            String email=jwtUtil.extractUsername(token);
            if(SecurityContextHolder.getContext().getAuthentication()==null){
                User u=users.findByEmailIgnoreCase(email).orElse(null);
                if(u!=null && jwtUtil.isTokenValid(token,u.getEmail())){
                    var auth=new UsernamePasswordAuthenticationToken(
                            u.getEmail(),null,List.of(new SimpleGrantedAuthority("ROLE_"+u.getRole().name())));
                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }catch(Exception ignored){SecurityContextHolder.clearContext();}
        chain.doFilter(request,response);
    }
}
