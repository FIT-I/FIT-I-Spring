package fit.fitspring.jwt;

import fit.fitspring.controller.dto.account.TokenDto;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
//import fit.fitspring.jwt.RedisUtil;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.stream.Collectors;

@Component
public class TokenProvider implements InitializingBean {

    private final Logger logger = LoggerFactory.getLogger(TokenProvider.class);
    private static final String AUTHORITIES_KEY = "auth";
    private final String secret;
    private final long accessTokenValidityInMilliseconds;
    private final long refreshTokenValidityInMilliseconds;
    private final RedisUtil redisUtil;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds,
            RedisUtil redisUtil) {

        this.secret = secret;
        this.accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 48 * 30; // 1달 - test를 위해 길게 잡음
        this.refreshTokenValidityInMilliseconds = refreshTokenValidityInMilliseconds * 48 * 30; // 1주 * 48 (약 1년)
        this.redisUtil = redisUtil;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public TokenDto createToken(Authentication authentication) {
        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenValidity = new Date(now + this.accessTokenValidityInMilliseconds);
        Date refreshTokenValidity = new Date(now + this.refreshTokenValidityInMilliseconds);


        String accessToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessTokenValidity)
                .compact();

        String refreshToken = Jwts.builder()
                .setSubject(authentication.getName())
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(refreshTokenValidity)
                .compact();

        return new TokenDto(accessToken, refreshToken);
    }

    //인증 객체 반환
    public Authentication getAuthentication(String token) {
        Claims claims = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();

        Collection<? extends GrantedAuthority> authorities =
                Arrays.stream(claims.get(AUTHORITIES_KEY).toString().split(","))
                        .map(SimpleGrantedAuthority::new)
                        .collect(Collectors.toList());

        User principal = new User(claims.getSubject(), "", authorities);

        return new UsernamePasswordAuthenticationToken(principal, token, authorities);
    }

    // 토큰 유효성 검증
    public boolean validateToken(String token) {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token);
            if (redisUtil.hasKeyBlackList(token)) {
                throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
            }
            return true;
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            //throw new BusinessException(ErrorCode.WRONG_JWT);
            logger.info("잘못된 JWT 서명입니다.");
        } catch (ExpiredJwtException e) {
            //throw new BusinessException(ErrorCode.EXPIRED_JWT);
            logger.info("만료된 JWT 토큰입니다.");
        } catch (UnsupportedJwtException e) {
            //throw new BusinessException(ErrorCode.UNSUPPORTED_JWT);
            logger.info("지원되지 않는 JWT 토큰입니다.");
        } catch (IllegalArgumentException e) {
            //throw new BusinessException(ErrorCode.ILLEGAL_JWT);
            logger.info("JWT 토큰이 잘못되었습니다.");
        }
        return false;
    }
}
