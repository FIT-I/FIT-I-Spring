package fit.fitspring.controller.dto.account;

import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountType;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@Builder
@ToString
@Schema(title = "회원정보")
public class RegisterTrainerDto {
    @Schema(description = "이름", example = "홍길동")
    private String name;
    @Schema(description = "이메일", example = "fiti@soongsil.ac.kr")
    private String email;
    @Schema(description = "비밀번호", example = "fiti123!")
    private String password;
    @Schema(description = "전공", example = "체육학 전공")
    private String major;

    public RegisterTrainerDto(String name, String email, String password, String major) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.major = major;
    }

    public Account toEntity(){
        return Account.builder()
                .name(name)
                .email(email)
                .password(password)
                .accountType(AccountType.TRAINER)
                .build();
    }
}