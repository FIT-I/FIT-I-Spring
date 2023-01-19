package fit.fitspring.service;

import fit.fitspring.controller.dto.customer.MatchingRequestDto;
import fit.fitspring.controller.dto.customer.SearchTrainerDto;
import fit.fitspring.controller.dto.customer.TrainerDto;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.account.AccountType;
import fit.fitspring.domain.matching.*;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import fit.fitspring.exception.account.DuplicatedAccountException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final TrainerRepository trainerRepository;
    private final AccountRepository accountRepository;
    private final WishListRepository wishListRepository;
    private final MatchingOrderRepository matchingOrderRepository;

    /*@Transactional
    public SliceResDto<TrainerDto> getTrainerList(SearchTrainerDto category){

        Slice<Trainer> sliceTrainerList = trainerRepository.findTrainerListOrderByUploadDateDesc(pageable);
    }*/

    @Transactional
    public boolean isTrainer(Long userIdx){
        Account user = accountRepository.findById(userIdx)
                .orElseThrow(DuplicatedAccountException::new);
        if(user.getAccountType()==AccountType.TRAINER){
            if(trainerRepository.existsById(userIdx)){
                return true;
            }
        }
        return false;
    }

    @Transactional
    public void saveLikeTrainer(Long custIdx, Long trainerIdx){
        Account customer = accountRepository.getReferenceById(custIdx);
        Trainer trainer = trainerRepository.getReferenceById(trainerIdx);
        WishList wishList = WishList.builder().customer(customer).trainer(trainer).build();
        wishListRepository.save(wishList);
    }

    @Transactional
    public void saveMatchingOrder(Long custIdx, Long trainerIdx, MatchingRequestDto request){
        Account customer = accountRepository.getReferenceById(custIdx);
        Trainer trainer = trainerRepository.getReferenceById(trainerIdx);
        PickUpType pickUpType;
        if(request.getType().equals("customer_go")){
            pickUpType = PickUpType.CUSTOMER_GO;
        }else{
            pickUpType = PickUpType.TRAINER_GO;
        }
        MatchingOrder matchingOrder = MatchingOrder.builder()
                .customer(customer).trainer(trainer)
                .startAt(request.getStartAt()).finishAt(request.getFinishAt())
                .pickUpType(pickUpType).isComplete("N").build();
        matchingOrderRepository.save(matchingOrder);
    }


}
