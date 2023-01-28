package fit.fitspring.controller.dto.account;

import fit.fitspring.domain.account.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccountSessionDto {
    Long id;
    String email;

    public static AccountSessionDto from(Account account){
        return AccountSessionDto.builder()
                .id(account.getId())
                .email(account.getEmail())
                .build();
    }
}
