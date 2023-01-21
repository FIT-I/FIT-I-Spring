package fit.fitspring.service;

import fit.fitspring.controller.dto.customer.*;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.account.AccountType;
import fit.fitspring.domain.matching.*;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.review.ReviewRepository;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import fit.fitspring.domain.trainer.UserImg;
import fit.fitspring.domain.trainer.UserImgRepository;
import fit.fitspring.exception.account.DuplicatedAccountException;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
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
    private final ReviewRepository reviewRepository;
    private final UserImgRepository userImgRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String awsStatic;

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

    @Transactional
    public void registerReview(Long userIdx, RegisterReviewDto reviewDto) throws BusinessException {
        Optional<Account> optionalC = accountRepository.findById(userIdx);
        Optional<Trainer> optionalT = trainerRepository.findById(reviewDto.getTrainerIdx());
        if(optionalC.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        if(optionalT.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
        }
        Review review = reviewDto.toEntity(optionalC.get(), optionalT.get());
        try{
            reviewRepository.save(review);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.DB_INSERT_ERROR);
        }
    }

    @Transactional
    public void modifyUserLocation(Long userIdx, String location) throws BusinessException{
        Optional<Account> optional = accountRepository.findById(userIdx);
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        optional.get().modifyLocation(location);
        try{
            accountRepository.save(optional.get());
        } catch (Exception e){
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }
    }

    @Transactional
    public List<WishDto> getWishList(Long userIdx) throws BusinessException{
        Optional<Account> optional = accountRepository.findById(userIdx);
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        List<WishList> wishList = optional.get().getWishListList();
        List<WishDto> wishDtoList = new ArrayList<>();
        for(WishList i : wishList){
            Trainer trainer = i.getTrainer();
            Optional<UserImg> userImg = userImgRepository.findByTrainer(trainer);
            String image = "none";
            if (userImg.isPresent()){
                image = userImg.get().getProfile();
            }
            WishDto wishDto = new WishDto(trainer.getId(), trainer.getUser().getName(),
                    image, trainer.getGrade(), i.getTrainer().getSchool(),
                    i.getCreatedDate().toLocalDate());
            wishDtoList.add(wishDto);
        }
        return wishDtoList;
    }

    public void modifyCustomerProfile(Long userIdx, String customerProfile) throws BusinessException {
        Optional<Account> optional = accountRepository.findById(userIdx);
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        optional.get().setProfile(customerProfile);
        accountRepository.save(optional.get());
    }
}

