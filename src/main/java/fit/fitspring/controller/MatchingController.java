package fit.fitspring.controller;

import fit.fitspring.controller.dto.customer.SearchTrainerDto;
import fit.fitspring.controller.dto.customer.TrainerDto;
import fit.fitspring.controller.dto.customer.WishDto;
import fit.fitspring.controller.dto.matching.MatchingInfo;
import fit.fitspring.controller.dto.matching.MatchingListForCust;
import fit.fitspring.controller.dto.matching.MatchingListForTrainer;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CommunalService;
import fit.fitspring.service.MatchingService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "매칭 API")
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {

    private final CommunalService communalService;
    private final MatchingService matchingService;

    @Operation(summary = "고객의 매칭목록조회", description = "고객의 매칭목록조회(Response)")
    @GetMapping("/customer")
    public BaseResponse<List<MatchingListForCust>> getCustomerMatchingList(@AuthenticationPrincipal User user){
        Long custIdx = communalService.getUserIdxByUser(user);
        try{
            List<MatchingListForCust> matchingList = matchingService.getMatchingListForCust(custIdx);
            return new BaseResponse<>(matchingList);
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "트레이너의 매칭목록조회", description = "트레이너의 매칭목록조회(Response)")
    @GetMapping("/trainer")
    public BaseResponse<List<MatchingListForTrainer>> getTrainerMatchingList(@AuthenticationPrincipal User user){
        Long trainerIdx = communalService.getUserIdxByUser(user);
        try{
            List<MatchingListForTrainer> matchingList = matchingService.getMatchingListForTrainer(trainerIdx);
            return new BaseResponse<>(matchingList);
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "매칭정보조회", description = "매칭정보조회(Response)")
    @GetMapping("/{matchingIdx}")
    public BaseResponse<MatchingInfo> getMatchingInformation(@Parameter(description = "매칭신청식별자", in = ParameterIn.PATH)@PathVariable Long matchingIdx){
        try{
            return new BaseResponse<>(matchingService.getMatchingInfo(matchingIdx));
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "매칭 수락", description = "매칭 수락하기")
    @PatchMapping("/{matchingIdx}/accept")
    public BaseResponse<String> MatchingAccept(@Parameter(description = "매칭식별자", in = ParameterIn.PATH)@PathVariable Long matchingIdx, @AuthenticationPrincipal User user){
        Long trainerIdx = communalService.getUserIdxByUser(user);
        try{
            matchingService.matchingAccept(trainerIdx, matchingIdx);
            return new BaseResponse<>("매칭을 수락하였습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }
    @Operation(summary = "매칭 거절", description = "매칭 거절하기")
    @PatchMapping("/{matchingIdx}/reject")
    public BaseResponse<String> MatchingReject(@Parameter(description = "매칭식별자")@PathVariable Long matchingIdx, @AuthenticationPrincipal User user){
        Long trainerIdx = communalService.getUserIdxByUser(user);
        try{
            matchingService.matchingReject(trainerIdx, matchingIdx);
            return new BaseResponse<>("매칭을 거절하였습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }
}
