package fit.fitspring.controller;

import fit.fitspring.controller.dto.customer.*;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CustomerService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "고객 API")
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;

    //로그인한 유저 idx(임의)
    Long custIdx = 1L;

    @Operation(summary = "트레이너 목록조회(미완)", description = "트레이너 목록조회(Request/Response)")
    @GetMapping()
    public ResponseEntity getTrainerList(@RequestBody SearchTrainerDto category){
        /*List<TrainerDto> trainerDtoList; // 리턴객체
        trainerDtoList = customerService.getTrainerList(category);*/
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 찜하기", description = "트레이너 찜하기(Request)")
    @PostMapping("/{trainerIdx}")
    public ResponseEntity likeTrainer(@Parameter(description = "유저식별자")@PathVariable Long trainerIdx){
        if(!customerService.isTrainer(trainerIdx))
            return ResponseEntity.ok(new TrainerException());
        customerService.saveLikeTrainer(custIdx, trainerIdx);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 매칭요청", description = "트레이너 매칭요청(Request)")
    @PostMapping("/matching/{trainerIdx}")
    public ResponseEntity requestTrainerMatching(@RequestBody MatchingRequestDto matchingRequest ,@PathVariable Long trainerIdx){
        if(!customerService.isTrainer(trainerIdx))
            return ResponseEntity.ok(new TrainerException());
        customerService.saveMatchingOrder(custIdx, trainerIdx, matchingRequest);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "알림설정(미완)", description = "알림설정(Request)")
    @PatchMapping("/notification/{notIdx}")
    public ResponseEntity setNotification(@Parameter(description = "알람설정('on' or 'off'")@PathVariable Integer notIdx){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "찜목록조회(거의완)", description = "찜목록조회(Response)")
    @GetMapping("/wish")
    public BaseResponse<List<WishDto>> getWishList(Long userIdx){
        try{
            //로그인 구현 후 수정
            List<WishDto> wishDtoList = customerService.getWishList(userIdx);
            return new BaseResponse<>(wishDtoList);
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "리뷰작성(거의완)", description = "리뷰작성(Request)")
    @PostMapping("/review")
    public BaseResponse<String> reviewTrainer(Long userIdx, @RequestBody RegisterReviewDto review){
        try{
            //로그인 구현 후 수정
            customerService.registerReview(userIdx, review);
            return new BaseResponse<>("리뷰를 작성하였습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "매칭위치설정(거의완)", description = "매칭위치설정(Request)")
    @PatchMapping("/location/{location}")
    public BaseResponse<String> modifyMatchingLocation(Long userIdx, @Parameter(description = "위치")@PathVariable String location){
        try{
            //로그인 구현 후 수정
            customerService.modifyUserLocation(userIdx, location);
            return new BaseResponse<>("매칭 위치를 변경하였습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "고객 프로필수정(거의완)", description = "프로필수정(Request)")
    @PatchMapping("/profile/{profile}")
    public BaseResponse<String> modifyCustomerProfileImage(@Parameter(description = "프로필 문자열")@PathVariable String profile){
        try {
            customerService.modifyCustomerProfile(custIdx, profile);
            return new BaseResponse<>("고객 프로필 이미지를 변경하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }
}
