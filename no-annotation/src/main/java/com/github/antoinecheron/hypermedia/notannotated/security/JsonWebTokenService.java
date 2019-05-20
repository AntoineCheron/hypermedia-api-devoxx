package com.github.antoinecheron.hypermedia.notannotated.security;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import com.github.antoinecheron.hypermedia.notannotated.Config;
import com.github.antoinecheron.hypermedia.notannotated.user.User;
import com.github.antoinecheron.hypermedia.notannotated.user.UserRole;

public class JsonWebTokenService {

  public static String createToken(User user) {
    return Jwts.builder()
      .setHeaderParam("alg", Config.configuration.getString("jwt.alg"))
      .setHeaderParam("typ", "JWT")
      .setIssuer("com.github.antoinecheron.hypermedia")
      .setSubject(user.getId())
      .setExpiration(Date.from(Instant.now().plus(1, ChronoUnit.DAYS)))
      .setIssuedAt(Date.from(Instant.now()))
      .signWith(null)
      .setClaims(getClaimsFromUser(user))
      .compact();
  }

  public static Optional<User> verifyToken(String token) {
    try {
      final var jwt = Jwts.parser()
        .setSigningKey(getSecretKey())
        .parseClaimsJws(token);
      return Optional.of(getUserFromClaims(jwt.getBody()));
    } catch (JwtException e) {
      return Optional.empty();
    }
  }

  private static SecretKey getSecretKey() {
    return Keys.hmacShaKeyFor(Config.getJwtSigningKeyFromApplicationConfiguration());
  }

  private static Map<String, Object> getClaimsFromUser(User user) {
    return Map.of(
      "lastName", user.getLastName(),
      "firstName", user.getFirstName(),
      "email", user.getEmail(),
      "role", user.getRole().name()
    );
  }

  private static User getUserFromClaims(Claims claims) {
    return new User(
      claims.getSubject(),
      claims.get("lastName", String.class),
      claims.get("firstName", String.class),
      claims.get("email", String.class),
      UserRole.valueOf(claims.get("role", String.class))
    );
  }

}
