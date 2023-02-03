package fit.fitspring.controller;

import fit.fitspring.controller.dto.communal.TrainerInformationDto;
import fit.fitspring.controller.dto.redBell.ReasonListRes;
import fit.fitspring.controller.dto.redBell.RedBellReq;
import fit.fitspring.controller.dto.trainer.UpdateTrainerInfoReq;
import fit.fitspring.domain.redBell.ReasonForRedBell;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.RedBellService;
import fit.fitspring.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "신고 API")
@RequestMapping("/api/redbell")
@RequiredArgsConstructor
public class RedBellController {

    private final RedBellService redBellService;

    @Operation(summary = "신고 사유 리스트 받아오기", description = "신고 사유 리스트 받아오기(response)")
    @GetMapping("/reason")
    public BaseResponse<List<ReasonListRes>> getReasonList(){
        return new BaseResponse<>(redBellService.getReasonList());
    }

    @Operation(summary = "신고 하기", description = "신고 하기(request,response)")
    @PostMapping("")
    public BaseResponse<String> getReasonList(@RequestBody RedBellReq redBellReq){
        Long custId = SecurityUtil.getLoginUserId();
        try {
            redBellService.saveRedBell(custId, redBellReq);
        }catch (BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
        return new BaseResponse<>("신고가 접수되었습니다.");
    }
}
