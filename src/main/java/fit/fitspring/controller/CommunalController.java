package fit.fitspring.controller;

import fit.fitspring.controller.dto.communal.AnnouncementDto;
import fit.fitspring.controller.dto.communal.ReviewDto;
import fit.fitspring.controller.dto.communal.TermDto;
import fit.fitspring.controller.dto.communal.TrainerInformationDto;
import fit.fitspring.controller.dto.customer.WishDto;
import fit.fitspring.domain.review.Review;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CommunalService;
import fit.fitspring.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "공용 API")
@RequestMapping("/api/communal")
@RequiredArgsConstructor
public class CommunalController {

    private final CommunalService communalService;
    private final CustomerService customerService;

    @Operation(summary = "트레이너 정보조회(미완)", description = "트레이너 정보조회(Request/Response)")
    @GetMapping("/trainer/{userIdx}")
    public ResponseEntity getTrainerInformation(@Parameter(description = "유저식별자")@PathVariable String userIdx){
        TrainerInformationDto trainerInformationDto = new TrainerInformationDto(); // 리턴객체
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 리뷰목록조회", description = "트레이너 리뷰목록조회(Request/Response)")
    @GetMapping("/review/{trainerIdx}")
    public BaseResponse<List<ReviewDto>> getTrainerReviewList(@Parameter(description = "트레이너 식별자")@PathVariable Long trainerIdx){
        try{
            return new BaseResponse<>(communalService.getTrainerReviewList(trainerIdx));
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "마이페이지조회(미완)", description = "마이페이지조회(Request/Response)")
    @GetMapping("/mypage")
    public ResponseEntity getMyPageInformation(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "공지사항목록조회", description = "공지사항목록조회(Response)")
    @GetMapping("/announcement")
    public BaseResponse<List<AnnouncementDto>> getAnnouncementList(){
        return new BaseResponse<>(communalService.getAnnouncementList());
    }

    @Operation(summary = "이용약관목록조회", description = "이용약관목록조회(Response)")
    @GetMapping("/terms")
    public BaseResponse<List<TermDto>> getTermList(){
        List<TermDto> termDtoList = communalService.getTermList();
        return new BaseResponse<>(termDtoList);
    }

}
