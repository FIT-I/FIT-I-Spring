package fit.fitspring.controller;

import fit.fitspring.controller.dto.trainer.UpdateTrainerInfoReq;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CommunalService;
import fit.fitspring.service.CustomerService;
import fit.fitspring.service.TrainerService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
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
    public BaseResponse<String> modifyTrainerInformation(@RequestBody UpdateTrainerInfoReq req, @AuthenticationPrincipal User user){
        Long trainerIdx = communalService.getUserIdxByUser(user);
        if(!customerService.isTrainer(trainerIdx)){
            return new BaseResponse<>(new TrainerException().getErrorCode());
        }
        trainerService.updateTrainerInfo(trainerIdx, req);
        return new BaseResponse<>("트레이너 정보를 수정하였습니다.");
    }

    @Operation(summary = "프로필수정", description = "프로필수정(Request)")
    @PatchMapping(value = "/profile", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> modifyTrainerProfileImage(Principal principal, @RequestPart(value = "profileImage") MultipartFile image){
        try {
            trainerService.modifyProfile(principal, image);
            return new BaseResponse<>("프로필 이미지를 변경하였습니다.");
        } catch (BusinessException e) {
            return new BaseResponse<>(e.getErrorCode());
        } catch (IOException | java.io.IOException e){
            return new BaseResponse<>("IO Exception Error");
        }
    }

    @Operation(summary = "트레이너 배경화면수정", description = "배경화면(Request)")
    @PatchMapping(value = "/bgimg", consumes = MediaType.MULTIPART_FORM_DATA_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public BaseResponse<String> modifyTrainerBgImage(@RequestPart(value = "backgroundImage") MultipartFile image, @AuthenticationPrincipal User user){
        Long trainerIdx = communalService.getUserIdxByUser(user);
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
    public BaseResponse<String> modifyTrainerBgImage(@RequestPart(value = "ectImage") List<MultipartFile> imageList, @AuthenticationPrincipal User user){
        Long trainerIdx = communalService.getUserIdxByUser(user);
        try {
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
}
