package fit.fitspring.exception.account;

import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;

public class DuplicatedAccountException extends BusinessException {
    public DuplicatedAccountException() {
        super(ErrorCode.DUPLICATE_ACCOUNT);
    }
}
