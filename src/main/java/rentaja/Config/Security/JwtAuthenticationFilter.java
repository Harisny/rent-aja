package rentaja.Config.Security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;
    private final EmailDetailsService emailDetailsService;

    public JwtAuthenticationFilter(JwtService jwtService, EmailDetailsService emailDetailsService) {
        this.jwtService = jwtService;
        this.emailDetailsService = emailDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return request.getServletPath().startsWith("/api/auth/**");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            String email = jwtService.extractUsername(token);

            if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails user = emailDetailsService.loadUserByUsername(email);

                if (jwtService.isTokenValid(token, user)) {
                    UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(user, null,
                            user.getAuthorities());

                    SecurityContextHolder.getContext().setAuthentication(auth);
                }
            }
        }
        filterChain.doFilter(request, response);
    }

}
