package fit.fitspring.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "트레이너 API")
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {

    @Operation(summary = "트레이너 배경이미지변경", description = "트레이너 배경이미지변경(Request)")
    @PatchMapping("/background/{image}")
    public ResponseEntity modifyTrainerBackground(@Parameter(description = "배경이미지")@PathVariable MultipartFile image){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 관리비용변경", description = "트레이너 관리비용변경(Request)")
    @PatchMapping("/cost/{cost1}/{cost2}")
    public ResponseEntity modifyTrainerCost(@Parameter(description = "비용1")@PathVariable Long cost1,
                                            @Parameter(description = "비용2")@PathVariable Long cost2){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 소개글변경", description = "트레이너 소개글변경(Request)")
    @PatchMapping("/introduction/{contents}")
    public ResponseEntity modifyTrainerIntroduction(@Parameter(description = "소개글")@PathVariable String contents){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 서비스설명글변경", description = "트레이너 서비스설명글변경(Request)")
    @PatchMapping("/service/{contents}")
    public ResponseEntity modifyTrainerServiceIntroduction(@Parameter(description = "서비스설명글")@PathVariable String contents){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 사진및자격증변경", description = "트레이너 사진및자격증변경(Request)")
    @PatchMapping("/image/{imageList}")
    public ResponseEntity modifyTrainerImageList(@Parameter(description = "사진및자격증")@PathVariable List<MultipartFile> imageList){
        return ResponseEntity.ok().build();
    }

}
