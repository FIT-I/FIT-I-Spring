package fit.fitspring.utils;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import fit.fitspring.controller.dto.account.AccountSessionDto;
import fit.fitspring.domain.account.AccountRepository;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;


public class SecurityUtil {

    public static AccountSessionDto getLoginUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if ("anonymousUser".equals(principal)) {
            return null;
        }
        return toDto(principal);
    }

    public static Long getLoginUserId() {
        AccountSessionDto loginUser = getLoginUser();
        return loginUser != null ? loginUser.getId() : null;
    }

    /**
     * 현재 로그인한 사용자가 userId가 아니면 AuthorizationServiceException 을 던진다.
     */
    public static void checkUser(Long userId) throws AuthorizationServiceException {
        if (!userId.equals(getLoginUserId())) {
            throw new AuthorizationServiceException(userId +" 는 해당 영역에 접근할 수 없습니다.");
        }
    }
    private static AccountSessionDto toDto(Object principal) {
        ObjectMapper mapper = new ObjectMapper();
        User user = (User) principal;

        try {
            return mapper.readValue(user.getUsername(), AccountSessionDto.class);
        } catch (JsonProcessingException e){
            return null;
        }
    }
}
