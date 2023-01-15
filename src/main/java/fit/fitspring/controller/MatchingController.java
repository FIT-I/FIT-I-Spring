package fit.fitspring.controller;

import fit.fitspring.controller.dto.customer.SearchTrainerDto;
import fit.fitspring.controller.dto.customer.TrainerDto;
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
@Tag(name = "매칭 API")
@RequestMapping("/api/matching")
@RequiredArgsConstructor
public class MatchingController {
    @Operation(summary = "고객 매칭목록조회(미완)", description = "고객 매칭목록조회(Request/Response)")
    @GetMapping("/customer")
    public ResponseEntity getCustomerMatchingList(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "트레이너 매칭목록조회(미완)", description = "트레이너 매칭목록조회(Request/Response)")
    @GetMapping("/trainer")
    public ResponseEntity getTrainerMatchingList(){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "매칭정보조회(미완)", description = "매칭정보조회(Request/Response)")
    @GetMapping("/{matchingIdx}")
    public ResponseEntity getMatchingInformation(@Parameter(description = "매칭신청식별자")@PathVariable Integer matchingIdx){
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "매칭요청답변(수락/거절)(미완)", description = "매칭요청답변(Request)")
    @PatchMapping("/{answer}")
    public ResponseEntity answerMatchingRequest(@Parameter(description = "답변('수락' or '거절')")@PathVariable String answer){
        return ResponseEntity.ok().build();
    }
}
