package fit.fitspring.controller;

import fit.fitspring.controller.dto.communal.ReviewDto;
import fit.fitspring.controller.dto.communal.TrainerInformationDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@Tag(name = "공용 API")
@RequestMapping("/api/communal")
@RequiredArgsConstructor
public class CommunalController {

    @Operation(summary = "트레이너 정보조회", description = "트레이너 정보조회(Request/Response)")
    @GetMapping("/trainer/{userIdx}")
    public ResponseEntity getTrainerInformation(@Parameter(description = "유저식별자")@PathVariable String userIdx){
        TrainerInformationDto trainerInformationDto = new TrainerInformationDto(); // 리턴객체
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 리뷰목록조회", description = "트레이너 리뷰목록조회(Request)")
    @GetMapping("/review/{userIdx}")
    public ResponseEntity getTrainerReviewList(@Parameter(description = "유저식별자")@PathVariable String userIdx){
        List<ReviewDto> reviewDtoList; // 리턴객체
        return ResponseEntity.ok().build();
    }
}
