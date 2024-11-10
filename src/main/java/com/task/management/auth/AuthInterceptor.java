package com.task.management.auth;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;

@Component
public class AuthInterceptor implements HandlerInterceptor {


    private final AuthJwtToken jwtTokenUtil = new AuthJwtToken();
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private Map<String, List<String>> roleUriMap;

    @PostConstruct
    public void loadRoleConfigurations() {
        try {
            // Load the roles-config.json file into roleUriMap
            ObjectMapper objectMapper = new ObjectMapper();
            roleUriMap = objectMapper.readValue(new File("roles-config.json"), Map.class);
        } catch (Exception e) {
            throw new RuntimeException("Failed to load roles configuration", e);
        }
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {

        if(handler instanceof HandlerMethod ) {
            HandlerMethod handlerMethod = (HandlerMethod) handler;
            Method method = handlerMethod.getMethod();
            AuthAnnotation annotation = method.getAnnotation(AuthAnnotation.class);
            if (annotation == null || annotation.optional()) {
                return true;
            }


            String authorizationHeader = request.getHeader("Authorization");

            if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            String token = authorizationHeader.substring(7);
            if (!jwtTokenUtil.validateToken(token)) {
                response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                return false;
            }

            // Extract roles and check permissions
            String role = jwtTokenUtil.extractRole(token);
            String requestedUri = request.getRequestURI();

            if (!hasAccess(role, requestedUri)) {
                response.setStatus(HttpServletResponse.SC_FORBIDDEN);
                return false;
            }
            UserDetails user = CustomUserDetails.builder().userId(jwtTokenUtil.extractId(token)).username(jwtTokenUtil.extractUsername(token)).build();
            SecurityContextHolder.getContext().setAuthentication(new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()));
        }
        return true;
    }

    private boolean hasAccess(String role, String uri) {
        List<String> allowedPatterns = roleUriMap.get(role);

        if (allowedPatterns == null) {
            return false;
        }

        for (String pattern : allowedPatterns) {
            if (pathMatcher.match(pattern, uri)) {
                return true;
            }
        }
        return false;
    }
}

