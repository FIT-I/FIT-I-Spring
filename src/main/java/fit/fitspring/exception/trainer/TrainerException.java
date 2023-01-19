package fit.fitspring.exception.trainer;

import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;

public class TrainerException extends BusinessException {
    public TrainerException(){super(ErrorCode.IS_NOT_TRAINER);}
}
