package fit.fitspring.controller;

import fit.fitspring.controller.dto.communal.TrainerInformationDto;
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
@Tag(name = "트레이너 API")
@RequestMapping("/api/trainer")
@RequiredArgsConstructor
public class TrainerController {

    @Operation(summary = "트레이너 정보수정(미완)", description = "트레이너 정보수정(Request)")
    @PutMapping("/information")
    public ResponseEntity modifyTrainerInformation(@RequestBody TrainerInformationDto trainerInformationDto){
        return ResponseEntity.ok().build();
    }
}
