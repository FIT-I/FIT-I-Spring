package fit.fitspring.jwt;


import fit.fitspring.config.RedisConfig;
import fit.fitspring.jwt.RedisUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.GenericFilterBean;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;
import java.io.IOException;

public class JwtFilter extends GenericFilterBean {

    private static final Logger logger = LoggerFactory.getLogger(JwtFilter.class);
    public static final String AUTHORIZATION_HEADER = "Authorization";
    private TokenProvider tokenProvider;

    @Autowired
    private final RedisTemplate redisTemplate;

    @Autowired
    public JwtFilter(TokenProvider tokenProvider, RedisTemplate redisTemplate) {
        this.tokenProvider = tokenProvider;
        this.redisTemplate = redisTemplate;
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        // 1. request header에서 JWT 토큰 추출
        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        String jwt = resolveToken(httpServletRequest);
        String requestURI = httpServletRequest.getRequestURI();

        // 2. validation으로 유효성 검사
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) { // 유효성 검증 코드에 돌려보기
            // 2-1. Redis에 해당 access Token 로그아웃 여부 확인
            String isLogout = (String) redisTemplate.opsForValue().get(jwt);

            // 2-2. 로그아웃 상태가 아니라면 토큰이 정상적으로 작동
            if (ObjectUtils.isEmpty(isLogout)){
                Authentication authentication = tokenProvider.getAuthentication(jwt); // 검증 통과하면 인증 객체 생성하기
                SecurityContextHolder.getContext().setAuthentication(authentication); // 인증 객체를 홀더에 저장
                logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.getName(), requestURI);
            }
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI);
        }

        filterChain.doFilter(servletRequest, servletResponse);
    }

    // request header에서 토큰 정보 추출
    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader(AUTHORIZATION_HEADER);

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer")) {
            return bearerToken.substring(7);
        }

        return null;
    }
}
