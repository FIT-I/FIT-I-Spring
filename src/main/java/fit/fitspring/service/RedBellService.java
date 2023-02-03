package fit.fitspring.service;

import fit.fitspring.controller.dto.redBell.ReasonListRes;
import fit.fitspring.controller.dto.redBell.RedBellReq;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.redBell.ReasonForRedBell;
import fit.fitspring.domain.redBell.RedBell;
import fit.fitspring.domain.redBell.RedBellRepository;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

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
        Account customer = accountRepository.getReferenceById(custId);
        Trainer trainer = trainerRepository.getReferenceById(req.getTrainerId());
        RedBell redBell = RedBell.builder().customer(customer).trainer(trainer).reason(req.getReason()).build();
        redBellRepository.save(redBell);
    }

}
