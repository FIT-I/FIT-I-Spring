package fit.fitspring.controller.mdoel.account;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class PostLoginRes {

    //로그인 하면 user_idx와 jwt를 넘겨줌
    private int userIdx;
    private String jwt;
}
