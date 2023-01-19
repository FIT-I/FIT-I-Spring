package fit.fitspring.exception.account;

import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;

public class AccountNotFoundException extends BusinessException {
    public AccountNotFoundException() {
        super(ErrorCode.ACCOUNT_NOT_FOUND);
    }
}
