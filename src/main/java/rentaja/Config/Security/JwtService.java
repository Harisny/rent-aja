package rentaja.Config.Security;

import java.security.Key;
import java.util.Date;

import org.springframework.core.env.Environment;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;

@Service
public class JwtService {
    private final Environment env;

    public JwtService(Environment env) {
        this.env = env;
    }

    private Key getKey() {
        String secret = env.getProperty("jwt.secret");
        if (secret == null) {
            throw new IllegalStateException("JWT secret is not configured");
        }
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    public String generateToken(UserDetails user) {
        String exp = env.getProperty("jwt.expiration");
        if (exp == null) {
            throw new IllegalStateException("JWT expiration is not configured");
        }
        long expiration = Long.parseLong(exp);
        return Jwts.builder()
                .setSubject(user.getUsername())
                .claim("role", user.getAuthorities().iterator().next().getAuthority())
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public String extractUsername(String token) {
        return extractClaims(token).getSubject();
    }

    public boolean isTokenValid(String token, UserDetails user) {
        return extractUsername(token).equals(user.getUsername())
                && extractClaims(token).getExpiration().after(new Date());
    }
}
