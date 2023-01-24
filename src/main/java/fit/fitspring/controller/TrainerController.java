package fit.fitspring.controller;

import fit.fitspring.controller.dto.communal.TrainerInformationDto;
import fit.fitspring.controller.dto.customer.UpdateTrainerInfoReq;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.response.BaseResponse;
import fit.fitspring.service.CustomerService;
import fit.fitspring.service.TrainerService;
import io.jsonwebtoken.io.IOException;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
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
    //임의의 트레이너 id값
    Long trainerIdx = 2L;

    @Operation(summary = "트레이너 정보수정", description = "트레이너 정보수정(Request)")
    @PutMapping("/information")
    public ResponseEntity modifyTrainerInformation(@RequestBody UpdateTrainerInfoReq req){
        if(!customerService.isTrainer(trainerIdx)){
            return ResponseEntity.ok(new TrainerException());
        }
        trainerService.updateTrainerInfo(trainerIdx, req);
        return ResponseEntity.ok().build();
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
}
