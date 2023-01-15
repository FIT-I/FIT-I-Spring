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

    @Operation(summary = "알림설정(미완)", description = "알림설정(Request)")
    @PatchMapping("/notification/{notIdx}")
    public ResponseEntity setNotification(@Parameter(description = "알람설정('on' or 'off'")@PathVariable Integer notIdx){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "찜목록조회(미완)", description = "찜목록조회(Request)")
    @GetMapping("/like")
    public ResponseEntity getLikeList(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "리뷰작성(미완)", description = "리뷰작성(Request)")
    @PostMapping("/review/{grade}/{contents}")
    public ResponseEntity reviewTrainer(@Parameter(description = "별점")@PathVariable Integer grade,
                                        @Parameter(description = "내용")@PathVariable String contents){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "매칭위치설정(미완)", description = "매칭위치설정(Request)")
    @PatchMapping("/location/{location}")
    public ResponseEntity modifyMatchingLocation(@Parameter(description = "위치")@PathVariable String location){
        return ResponseEntity.ok().build();
    }
}
