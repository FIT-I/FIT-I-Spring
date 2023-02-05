package fit.fitspring.service;

import fit.fitspring.chat.ChatService;
import fit.fitspring.controller.dto.matching.MatchingInfo;
import fit.fitspring.controller.dto.matching.MatchingListForCust;
import fit.fitspring.controller.dto.matching.MatchingListForTrainer;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.account.AccountType;
import fit.fitspring.domain.matching.MatchingOrder;
import fit.fitspring.domain.matching.MatchingOrderRepository;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Period;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import static fit.fitspring.exception.common.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class MatchingService {

    private final MatchingOrderRepository matchingOrderRepository;
    private final AccountRepository accountRepository;
    private final TrainerRepository trainerRepository;

    @Transactional
    public List<MatchingListForCust> getMatchingListForCust(Long custIdx){
        List<MatchingListForCust> matchingListForCustList = new ArrayList<>();
        Account customer = accountRepository.getReferenceById(custIdx);
        List<MatchingOrder> matchingOrderList = matchingOrderRepository.findAllByCustomerAndIsCompleteLikeOrderByIdDesc(customer,"NONE");
        for(MatchingOrder matchingOrder : matchingOrderList){
            LocalDate orderDate = matchingOrder.getCreatedDate().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String orderDateStr = orderDate.format(formatter);
            LocalDate today = LocalDate.now();
            Period period = Period.between(orderDate, today);
            int gap = period.getDays();
            matchingListForCustList.add(new MatchingListForCust(matchingOrder,orderDateStr,gap));
        }
        return matchingListForCustList;
    }

    @Transactional
    public List<MatchingListForTrainer> getMatchingListForTrainer(Long trainerIdx){
        List<MatchingListForTrainer> matchingListForTrainerList = new ArrayList<>();
        Trainer trainer = trainerRepository.getReferenceById(trainerIdx);
        List<MatchingOrder> matchingOrderList = matchingOrderRepository.findAllByTrainerAndIsCompleteLikeOrderByIdDesc(trainer,"NONE");
        for(MatchingOrder matchingOrder : matchingOrderList){
            LocalDate orderDate = matchingOrder.getCreatedDate().toLocalDate();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
            String orderDateStr = orderDate.format(formatter);
            LocalDate today = LocalDate.now();
            Period period = Period.between(orderDate, today);
            int gap = period.getDays();
            matchingListForTrainerList.add(new MatchingListForTrainer(matchingOrder,orderDateStr,gap));
        }
        return matchingListForTrainerList;
    }

    @Transactional
    public MatchingInfo getMatchingInfo(Long matchingIdx) throws BusinessException{
        MatchingOrder matchingOrder = matchingOrderRepository.findById(matchingIdx).orElseThrow(() -> new BusinessException(MATCHING_NOT_FOUND));
        LocalDate matchingStart = matchingOrder.getStartAt();
        LocalDate matchingFinish = matchingOrder.getFinishAt();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy.MM.dd");
        String matchingStartStr = matchingStart.format(formatter);
        String matchingFinishStr = matchingFinish.format(formatter);
        int matchingPeriod = Period.between(matchingStart, matchingFinish).getDays()+1;
        MatchingInfo matchingInfo = new MatchingInfo(matchingOrder,matchingStartStr,matchingFinishStr, matchingPeriod);
        return matchingInfo;
    }

    @Transactional
    public void matchingAccept(Long trainerIdx, Long matchingIdx, String openChatLink){
        MatchingOrder matchingOrder = matchingOrderRepository.findById(matchingIdx)
                .orElseThrow(()->new BusinessException(MATCHING_NOT_FOUND));
        if(!matchingOrder.getTrainer().getId().equals(trainerIdx))
            throw new BusinessException(PERMISSION_DENIED);
        if(openChatLink != null){
            matchingOrder.acceptMatching();
            matchingOrder.setOpenChatLink(openChatLink);
        }
    }
    @Transactional
    public void matchingReject(Long trainerIdx, Long matchingIdx){
        MatchingOrder matchingOrder = matchingOrderRepository.findById(matchingIdx).orElseThrow(()->new BusinessException(MATCHING_NOT_FOUND));
        if(!matchingOrder.getTrainer().getId().equals(trainerIdx))
            throw new BusinessException(PERMISSION_DENIED);
        matchingOrder.rejectMatching();
    }


    /*
    @Transactional
    public MatchingInfo getMatchingInfo(Long matchingIdx){
        MatchingOrder
        return matchingListForTrainerList;
    }*/

    public List<MatchingOrder> getOwnMatchingList() {
        Account account = accountRepository.findById(SecurityUtil.getLoginUserId())
                .orElseThrow(() -> new BusinessException(ACCOUNT_NOT_FOUND));
        return account.getAccountType().equals(AccountType.CUSTOMER) ?
                matchingOrderRepository.findAllByCustomerAndIsCompleteLikeOrderByIdDesc(account, "ACCEPT") :
                matchingOrderRepository.findAllByTrainerAndIsCompleteLikeOrderByIdDesc(account.getTrainer(),"ACCEPT");
    }

}
