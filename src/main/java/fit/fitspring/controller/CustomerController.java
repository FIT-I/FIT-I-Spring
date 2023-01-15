package fit.fitspring.controller;

import fit.fitspring.controller.dto.customer.MatchingRequestDto;
import fit.fitspring.controller.dto.customer.SearchTrainerDto;
import fit.fitspring.controller.dto.customer.TrainerDto;
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
@Tag(name = "고객 API")
@RequestMapping("/api/customer")
@RequiredArgsConstructor
public class CustomerController {

    @Operation(summary = "트레이너 목록조회(미완)", description = "트레이너 목록조회(Request/Response)")
    @GetMapping()
    public ResponseEntity getTrainerList(@RequestBody SearchTrainerDto category){
        List<TrainerDto> trainerDtoList; // 리턴객체
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 찜하기(미완)", description = "트레이너 찜하기(Request)")
    @PostMapping("/{userIdx}")
    public ResponseEntity likeTrainer(@Parameter(description = "유저식별자")@PathVariable String userIdx){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 매칭요청(미완)", description = "트레이너 매칭요청(Request)")
    @PostMapping("/matching/{userIdx}")
    public ResponseEntity requestTrainerMatching(@RequestBody MatchingRequestDto matchingRequest){
        return ResponseEntity.ok().build();
    }
}
