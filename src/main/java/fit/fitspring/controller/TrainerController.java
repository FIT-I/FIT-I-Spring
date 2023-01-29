package fit.fitspring.controller;

import fit.fitspring.controller.dto.trainer.CategoryReq;
import fit.fitspring.controller.dto.trainer.UpdateTrainerInfoReq;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CommunalService;
import fit.fitspring.service.CustomerService;
import fit.fitspring.service.TrainerService;
import fit.fitspring.utils.SecurityUtil;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.security.Principal;
import java.util.List;

@Slf4j
@RestController
@Tag(name = "트레이너 API")
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {

    private final TrainerService trainerService;
    private final CustomerService customerService;
    private final CommunalService communalService;

    //임의의 트레이너 id값
    Long trainerIdx = 2L;

    @Operation(summary = "트레이너 정보수정", description = "트레이너 정보수정(Request)")
    @PutMapping("/information")
    public BaseResponse<String> modifyTrainerInformation(@RequestBody UpdateTrainerInfoReq req){
        Long trainerIdx = SecurityUtil.getLoginUserId();
        if(!customerService.isTrainer(trainerIdx)){
            return new BaseResponse<>(new TrainerException().getErrorCode());
        }
        trainerService.updateTrainerInfo(trainerIdx, req);
        return new BaseResponse<>("트레이너 정보를 수정하였습니다.");
    }

    @Operation(summary = "트레이너 프로필수정", description = "트레이너 프로필수정(Request)")
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> modifyTrainerProfileImage(@RequestPart(value = "profileImage") MultipartFile image){
        try {
            trainerService.modifyTrainerProfile(image);
            return new BaseResponse<>("프로필 이미지를 변경하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (IOException | java.io.IOException e){
            return new BaseResponse<>("IO Exception Error");
        }
    }

    @Operation(summary = "트레이너 배경화면수정", description = "배경화면(Request)")
    @PatchMapping(value = "/bgimg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> modifyTrainerBgImage(@RequestPart(value = "backgroundImage") MultipartFile image){
        Long trainerIdx = SecurityUtil.getLoginUserId();
        try {
            trainerService.modifyTrainerBgImage(trainerIdx, image);
            return new BaseResponse<>("배경 이미지를 변경하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (IOException | java.io.IOException e){
            return new BaseResponse<>("IO Exception Error");
        }
    }

    @Operation(summary = "트레이너 사진 및 자격증 추가", description = "기타 사진 및 자격증 사진(Request)")
    @PostMapping(value = "/etcimg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> modifyTrainerEtcImage(@RequestPart(value = "ectImage") List<MultipartFile> imageList){
        try {
            Long trainerIdx =SecurityUtil.getLoginUserId();
            for(MultipartFile image : imageList){
                trainerService.addTrainerEctImage(trainerIdx, image);
            }
            return new BaseResponse<>("기타 사진을 추가하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (IOException | java.io.IOException e){
            return new BaseResponse<>("IO Exception Error");
        }
    }

    @Operation(summary = "트레이너 프로필 삭제", description = "트레이너 프로필 삭제(Request)")
    @DeleteMapping("/profile")
    public BaseResponse<String> deleteTrainerProfile(){
        try {
            trainerService.deleteTrainerProfile();
            return new BaseResponse<>("프로필 이미지를 삭제하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "트레이너 사진 및 자격증 삭제", description = "기타 사진 및 자격증 사진(Request)")
    @DeleteMapping(value = "/etcimg/{etcImgIdx}")
    public BaseResponse<String> deleteTrainerEtcImage(@Parameter(description = "기타 사진의 식별자", in = ParameterIn.PATH)@PathVariable Long etcImgIdx){
        Long trainerIdx = SecurityUtil.getLoginUserId();
        try {
            trainerService.deleteEtcImg(trainerIdx, etcImgIdx);
            return new BaseResponse<>("기타 사진을 삭제하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }

    @Operation(summary = "내 매칭 관리 on/off", description = "내 매칭관리가 on이었다면 off로, off 였다면 on으로 바뀜")
    @PatchMapping("/mymatching")
    public BaseResponse<String> myMatching(){
        try{
            // 계정 활성화가 되었습니다. 계정 비활성화가 되었습니다. 계정 탈퇴가 되었습니다.
            Long userIdx = SecurityUtil.getLoginUserId();
            String status = trainerService.modifyMatching(userIdx);
            return new BaseResponse<>("나의 매칭 관리가 " + status + " 되었습니다.");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }
    @Operation(summary = "트레이너 카테고리 수정", description = "트레이너 카테고리 수정(Request)")
    @PatchMapping("/category")
    public BaseResponse<String> modifyCategory(@RequestBody CategoryReq categoryReq){
        try{
            Long trainerIdx = SecurityUtil.getLoginUserId();
            trainerService.modifyCategory(trainerIdx, categoryReq.getCategory());
            return new BaseResponse<>("카테고리가 변경되었습니다.");
        } catch(BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        }
    }
}
