package fit.fitspring.jwt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fit.fitspring.controller.dto.account.AccountSessionDto;
import fit.fitspring.controller.dto.account.TokenDto;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
//import fit.fitspring.jwt.RedisUtil;
import fit.fitspring.utils.AccountAdapter;
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;
import org.json.JSONObject;

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
    @Autowired
    private final RedisTemplate redisTemplate;
    private Key key;

    public TokenProvider(
            @Value("${jwt.secret}") String secret,
            @Value("${jwt.token-validity-in-seconds}") long accessTokenValidityInSeconds,
            @Value("${jwt.refresh-token-validity-in-seconds}") long refreshTokenValidityInMilliseconds,
            RedisTemplate redisTemplate) {

        this.secret = secret;
        this.accessTokenValidityInMilliseconds = (accessTokenValidityInSeconds * 1000) * 1000000; // (default) 30분
        this.refreshTokenValidityInMilliseconds = (refreshTokenValidityInMilliseconds * 2000) * 1000000; // (default)2주
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void afterPropertiesSet() {
        byte[] keyBytes = Decoders.BASE64.decode(secret);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    // 토큰 생성
    public TokenDto createToken(Authentication authentication) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();

        String authorities = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(","));

        long now = (new Date()).getTime();
        Date accessTokenValidity = new Date(now + this.accessTokenValidityInMilliseconds);
        Date refreshTokenValidity = new Date(now + this.refreshTokenValidityInMilliseconds);



        AccountAdapter adapter= (AccountAdapter) authentication.getPrincipal();
        String subject = objectMapper.writeValueAsString(adapter.getUser());
        String accessToken = Jwts.builder()
        //        .setSubject(authentication.getName())
                .setSubject(subject)
                .claim(AUTHORITIES_KEY, authorities)
                .signWith(key, SignatureAlgorithm.HS512)
                .setExpiration(accessTokenValidity)
                .compact();

        String refreshToken = Jwts.builder()
                //.setSubject(authentication.getName())
                .setSubject(subject)
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
//            if (redisUtil.hasKeyBlackList(token)) {
//                throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
//            }
            if (redisTemplate.hasKey(token)) {
                throw new BusinessException(ErrorCode.ACCOUNT_NOT_FOUND);
            }
//            if(getExpirationRefreshToken(token)){
//                return true;
//            }
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

    // 토큰 만료 시간 확인
    public Long getExpiration(String accessToken) {
        Date expiration = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(accessToken)
                .getBody()
                .getExpiration();

        Long now = new Date().getTime();
        return (expiration.getTime() - now);
    }

    public Boolean getExpirationRefreshToken(String refreshToken) {
        Date expiration = Jwts
                .parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(refreshToken)
                .getBody()
                .getExpiration();

        Long now = new Date().getTime();
        System.out.println("expiration: " +expiration.getTime());
        System.out.println("now: " + now);

        if(expiration.getTime() - now > 0){
            return true;
        }
        return false;
    }

    // 토큰에서 회원 정보 추출하기
    public String getEmail(String accessToken){
        String subject =  Jwts
                .parser()
                .setSigningKey(key)
                .parseClaimsJws(accessToken)
                .getBody()
                .getSubject();

        JSONObject parser = new JSONObject(subject);
        String email = parser.getString("email");

        return email;
    }
}
