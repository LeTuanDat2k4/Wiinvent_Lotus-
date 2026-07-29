package com.Wiinvent.Lotus.core.security;

import com.Wiinvent.Lotus.core.config.LotusProperties;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InternalApiKeyFilter extends OncePerRequestFilter {

    public static final String INTERNAL_API_KEY_HEADER = "X-Internal-Api-Key";
    public static final String INTERNAL_SERVICE_AUTH = "INTERNAL_SERVICE";

    private final LotusProperties lotusProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String apiKey = request.getHeader(INTERNAL_API_KEY_HEADER);
        if (apiKey != null
                && apiKey.equals(lotusProperties.getInternalApiKey())
                && SecurityContextHolder.getContext().getAuthentication() == null) {
            var authentication = new UsernamePasswordAuthenticationToken(
                    INTERNAL_SERVICE_AUTH,
                    null,
                    List.of(new SimpleGrantedAuthority("ROLE_SERVICE")));
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }
        filterChain.doFilter(request, response);
    }
}
