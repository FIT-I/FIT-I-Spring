package fit.fitspring.service;

import fit.fitspring.controller.dto.redBell.ReasonListRes;
import fit.fitspring.controller.dto.redBell.RedBellCustomerReq;
import fit.fitspring.controller.dto.redBell.RedBellReq;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.redBell.*;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import fit.fitspring.exception.common.BusinessException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Stream;
import static fit.fitspring.exception.common.ErrorCode.*;



@RequiredArgsConstructor
@Service
public class RedBellService {

    private final RedBellRepository redBellRepository;
    private final AccountRepository accountRepository;
    private final TrainerRepository trainerRepository;

    public List<ReasonListRes> getReasonList(){
        List<ReasonListRes> reasonListResList = new ArrayList();
        Stream.of(ReasonForRedBell.values()).forEach(r-> reasonListResList.add(new ReasonListRes(r)));
        return reasonListResList;
    }

    @Transactional
    public void saveRedBell(Long custId, RedBellReq req){
        if(checkLastRedBell(custId, req.getTrainerId(), "trainer")){
            Account customer = accountRepository.getReferenceById(custId);
            Trainer trainer = trainerRepository.getReferenceById(req.getTrainerId());
            RedBell redBell = RedBell.builder().customer(customer).trainer(trainer).reason(req.getReason()).target("trainer").build();
            redBellRepository.save(redBell);
            if(redBellRepository.countByTrainerAndTarget(trainer, "trainer").compareTo(4L)==1){ //5번 이상 신고 당할 경우
                //trainer의 state "D"로 변경
                trainer.getUser().block();
            }
        }else{
            throw new BusinessException(WAITING_24HOURS);
        }
    }

    @Transactional
    public void saveRedBellCustomer(Long trainerId, RedBellCustomerReq req){
        if(checkLastRedBell(req.getCustomerId(), trainerId, "customer")){
            Account customer = accountRepository.getReferenceById(req.getCustomerId());
            Trainer trainer = trainerRepository.getReferenceById(trainerId);
            RedBell redBell = RedBell.builder().customer(customer).trainer(trainer).reason(req.getReason()).target("customer").build();
            redBellRepository.save(redBell);
            if(redBellRepository.countByCustomerAndTarget(customer,"customer").compareTo(4L)==1){ //5번 이상 신고 당할 경우
                //customer의 state "D"로 변경
                customer.block();
            }
        }else{
            throw new BusinessException(WAITING_24HOURS);
        }
    }

    @Transactional
    public boolean checkLastRedBell(Long custId, Long trainerId, String target){
        Account customer = accountRepository.getReferenceById(custId);
        Trainer trainer = trainerRepository.getReferenceById(trainerId);
        Optional<RedBell> lastRedBell = redBellRepository.findFirstByCustomerAndTrainerAndTargetOrderByCreatedDateDesc(customer,trainer, target);
        if(lastRedBell.isPresent()){
            long hourGap = ChronoUnit.HOURS.between(lastRedBell.get().getCreatedDate(), LocalDateTime.now());
            if(hourGap<24){//트레이너를 신고한지 24시간이 지나지 않음
                return false;
            }
        }
        return true;
    }

}
