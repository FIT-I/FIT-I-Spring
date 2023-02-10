package fit.fitspring.controller;

import fit.fitspring.controller.dto.communal.*;
import fit.fitspring.controller.dto.trainer.TrainerInformationDto;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CommunalService;
import fit.fitspring.service.CustomerService;
import fit.fitspring.utils.SecurityUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "공용 API")
@RequestMapping("/api/communal")
@RequiredArgsConstructor
public class CommunalController {

    private final CommunalService communalService;
    private final CustomerService customerService;

    @Operation(summary = "트레이너 정보조회", description = "트레이너 정보조회(Request/Response)")
    @GetMapping("/trainer/{trainerIdx}")
    public BaseResponse<TrainerInformationForUserDto> getTrainerInformation(@Parameter(description = "트레이너 식별자")@PathVariable Long trainerIdx){
        try{
            return new BaseResponse<>(communalService.getTrainerInformation(trainerIdx));
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
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

    @Operation(summary = "마이페이지조회", description = "마이페이지조회(Request/Response)")
    @GetMapping("/mypage")
    public BaseResponse<MyPageDto> getMyPage(){
        try{
            return new BaseResponse<>(communalService.getMyPageBriefInformation());
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "공지사항목록조회", description = "공지사항목록조회(Response)")
    @GetMapping("/announcement")
    public BaseResponse<List<AnnouncementDto>> getAnnouncementList(){
        return new BaseResponse<>(communalService.getAnnouncementList());
    }

    @Operation(summary = "이용약관목록조회", description = "이용약관목록조회 - 회원가입 직후용(Response)")
    @GetMapping("/terms")
    public BaseResponse<List<TermDto>> getTermList(){
        List<TermDto> termDtoList = communalService.getTermList();
        return new BaseResponse<>(termDtoList);
    }

    @Operation(summary = "이용약관조회", description = "이용약관조회 - 모든 약관을 하나의 문자열로 리턴, 마이페이지용(Response)")
    @GetMapping("/terms/all")
    public BaseResponse<String> getAllTermContents(){
        return new BaseResponse<>(communalService.getAllTermContents());
    }

    @Operation(summary = "매칭위치설정", description = "매칭위치설정(Request)")
    @PatchMapping("/location/{location}")
    public BaseResponse<String> modifyMatchingLocation(@Parameter(description = "위치")@PathVariable String location){
        try{
            communalService.modifyUserLocation(location);
            return new BaseResponse<>("매칭 위치를 변경하였습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "유저의 state 확인", description = "유저의 state 확인(Response)")
    @PatchMapping("/state")
    public BaseResponse<StateDto> getUserState(){
        try{
            Long userIdx = SecurityUtil.getLoginUserId();
            String state = communalService.getUserState(userIdx);
            return new BaseResponse<>(new StateDto(state));
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "카카오 우편 번호 페이지 로딩 (거의 완성)", description = "카카오 우편 번호 서비스 로딩")
    @GetMapping("/zipcode/page")
    public ModelAndView zipcode(){
        ModelAndView modelAndView = new ModelAndView();
        modelAndView.setViewName("zipcode");
        modelAndView.addObject("address", new ZipCodeDto());

        return modelAndView;
    }

    @Operation(summary = "카카오 우편 번호 입력값 반환 (거의 완성)", description = "카카오 우편 번호 입력값 클라에게 반환해주는 api")
    @GetMapping("/zipcode/address")
    public BaseResponse<ZipCodeDto> zipcodeResponse(ZipCodeDto zipCodeDto){
        try{
            // 생성한 값 반환
            return new BaseResponse<>(zipCodeDto);
        } catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

}
