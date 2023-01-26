package fit.fitspring.controller;

import fit.fitspring.controller.dto.communal.SliceResDto;
import fit.fitspring.controller.dto.customer.*;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CommunalService;
import fit.fitspring.service.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "고객 API")
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final CommunalService communalService;


    @Operation(summary = "트레이너 목록조회", description = "트레이너 목록조회(Response)")
    @GetMapping("/trainer-list")
    public BaseResponse<SliceResDto<TrainerDto>> getTrainerList(@RequestParam String category, @RequestParam(required = false) Long lastTrainerId, @PageableDefault(size=20, sort="recent",direction = Sort.Direction.DESC) Pageable pageable){
        try {
            SliceResDto<TrainerDto> trainerDtoList;
            trainerDtoList = customerService.getTrainerList(category, lastTrainerId, pageable);
            return new BaseResponse<>(trainerDtoList);
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "트레이너 찜하기", description = "트레이너 찜하기(Request)")
    @PostMapping("/{trainerIdx}")
    public BaseResponse<String> likeTrainer(@Parameter(description = "유저식별자")@PathVariable Long trainerIdx, @AuthenticationPrincipal User user){
        try {
            Long custIdx = communalService.getUserIdxByUser(user);
            if(!customerService.isTrainer(trainerIdx))
                return new BaseResponse<>(new TrainerException().getErrorCode());
            customerService.saveLikeTrainer(custIdx, trainerIdx);
            return new BaseResponse<>("트레이너를 찜했습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }
    @Operation(summary = "트레이너 찜하기 취소", description = "트레이너 찜하기 취소(Request)")
    @DeleteMapping("/{trainerIdx}")
    public BaseResponse<String> undoLikeTrainer(@Parameter(description = "유저식별자")@PathVariable Long trainerIdx, @AuthenticationPrincipal User user){
        try {
            Long custIdx = communalService.getUserIdxByUser(user);
            if(!customerService.isTrainer(trainerIdx))
                return new BaseResponse<>(new TrainerException().getErrorCode());
            customerService.undoLikeTrainer(custIdx, trainerIdx);
            return new BaseResponse<>("트레이너 찜하기 취소.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "트레이너 매칭요청", description = "트레이너 매칭요청(Request)")
    @PostMapping("/matching/{trainerIdx}")
    public BaseResponse<String> requestTrainerMatching(@RequestBody MatchingRequestDto matchingRequest ,@PathVariable Long trainerIdx, @AuthenticationPrincipal User user){
        try {
            Long custIdx = communalService.getUserIdxByUser(user);
            if (!customerService.isTrainer(trainerIdx))
                return new BaseResponse<>(new TrainerException().getErrorCode());
            try {
                customerService.saveMatchingOrder(custIdx, trainerIdx, matchingRequest);
            } catch (BusinessException e) {
                return new BaseResponse<>(e.getErrorCode());
            }
            return new BaseResponse<>("매칭요청 완료.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }
    @Operation(summary = "알림 on", description = "알림 on으로 설정")
    @PatchMapping("/notification/on")
    public BaseResponse<String> notificationOn(@AuthenticationPrincipal User user){
        try {
            Long userIdx = communalService.getUserIdxByUser(user);
            customerService.notificationOn(userIdx);
            return new BaseResponse<>("알림 on.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }
    @Operation(summary = "알림 off", description = "알림 off로 설정")
    @PatchMapping("/notification/off")
    public BaseResponse<String> notificationOff(@AuthenticationPrincipal User user){
        try {
            Long userIdx = communalService.getUserIdxByUser(user);
            customerService.notificationOff(userIdx);
            return new BaseResponse<>("알림 off.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "찜목록조회", description = "찜목록조회(Response)")
    @GetMapping("/wish")
    public BaseResponse<List<WishDto>> getWishList(Principal principal){
        try{
            //로그인 구현 후 수정
            List<WishDto> wishDtoList = customerService.getWishList(principal);
            return new BaseResponse<>(wishDtoList);
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "리뷰작성", description = "리뷰작성(Request)")
    @PostMapping("/review")
    public BaseResponse<String> reviewTrainer(Principal principal, @RequestBody RegisterReviewDto review){
        try{
            //로그인 구현 후 수정
            customerService.registerReview(principal, review);
            return new BaseResponse<>("리뷰를 작성하였습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "매칭위치설정", description = "매칭위치설정(Request)")
    @PatchMapping("/location/{location}")
    public BaseResponse<String> modifyMatchingLocation(Principal principal, @Parameter(description = "위치")@PathVariable String location){
        try{
            //로그인 구현 후 수정
            customerService.modifyUserLocation(principal, location);
            return new BaseResponse<>("매칭 위치를 변경하였습니다.");
        }catch(BusinessException e){
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "고객 프로필수정", description = "프로필수정(Request)")
    @PatchMapping("/profile/{profile}")
    public BaseResponse<String> modifyCustomerProfileImage(Principal principal, @Parameter(description = "프로필 문자열")@PathVariable String profile){
        try {
            customerService.modifyCustomerProfile(principal, profile);
            return new BaseResponse<>("고객 프로필 이미지를 변경하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }
}
