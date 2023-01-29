package fit.fitspring.service;

import fit.fitspring.controller.dto.communal.SliceResDto;
import fit.fitspring.controller.dto.customer.*;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.account.AccountType;
import fit.fitspring.domain.matching.*;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.review.ReviewRepository;
import fit.fitspring.domain.trainer.*;
import fit.fitspring.exception.account.DuplicatedAccountException;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.Principal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fit.fitspring.exception.common.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class CustomerService {

    private final TrainerRepository trainerRepository;
    private final AccountRepository accountRepository;
    private final WishListRepository wishListRepository;
    private final MatchingOrderRepository matchingOrderRepository;
    private final ReviewRepository reviewRepository;
    private final UserImgRepository userImgRepository;
    private final TrainerCertRepository trainerCertRepository;

    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    @Value("${cloud.aws.region.static}")
    private String awsStatic;

    @Transactional
    public SliceResDto<TrainerDto> getTrainerList(String category, Long lastTrainerId, Pageable pageable){
        Category category_enum;
        if(category.equals("pt")){
            category_enum = Category.PERSONAL_PT;
        }else if(category.equals("food")){
            category_enum = Category.FOOD_CHECK;
        }else if(category.equals("diet")){
            category_enum = Category.DIET;
        }else if(category.equals("rehab")){
            category_enum = Category.REHAB;
        }else{//pt가 기본값
            category_enum = Category.PERSONAL_PT;
        }
        String sortBy = pageable.getSort().get().findFirst().orElseThrow().getProperty();
        Sort.Direction direction = pageable.getSort().get().findFirst().orElseThrow().getDirection();
        Slice<Trainer> sliceTrainerList;
        if(sortBy.equals("recent")){
            sliceTrainerList = trainerRepository.findByCategoryOrderByIdDesc(category_enum, lastTrainerId, pageable);
        }else if(sortBy.equals("level")){
            Long lastLevelId;
            if(lastTrainerId==null)
                lastLevelId=null;
            else
                lastLevelId= trainerRepository.findById(lastTrainerId).orElseThrow().getLevel().getId();
            sliceTrainerList = trainerRepository.findByCategoryOrderByLevelDesc(category_enum, lastTrainerId,lastLevelId, pageable);
        }else if(sortBy.equals("price") && direction.isAscending()){
            Integer lastPrice = null;
            if(lastTrainerId!=null){
                lastPrice = trainerRepository.findById(lastTrainerId).orElseThrow().getPriceHour();
            }
            sliceTrainerList = trainerRepository.findByCategoryOrderByPriceAsc(category_enum,lastTrainerId,lastPrice,pageable);
        }else if(sortBy.equals("price") && direction.isDescending()){
            Integer lastPrice = null;
            if(lastTrainerId!=null){
                lastPrice = trainerRepository.findById(lastTrainerId).orElseThrow().getPriceHour();
            }
            sliceTrainerList = trainerRepository.findByCategoryOrderByPriceDesc(category_enum,lastTrainerId,lastPrice,pageable);
        } else{
            sliceTrainerList = trainerRepository.findByCategoryOrderByIdDesc(category_enum, lastTrainerId, pageable);
        }
        List<TrainerDto> trainerList = new ArrayList<>();
        for(Trainer trainer : sliceTrainerList){
            Long certificateNum = trainerCertRepository.countByTrainer(trainer);
            trainerList.add(new TrainerDto(trainer,certificateNum));
        }
        return new SliceResDto<>(sliceTrainerList.getNumberOfElements(), sliceTrainerList.hasNext(), trainerList);

    }

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
        if(wishListRepository.existsByCustomerAndTrainer(customer,trainer)){
            throw new BusinessException(ALREADY_LIKE);
        }
        WishList wishList = WishList.builder().customer(customer).trainer(trainer).build();
        wishListRepository.save(wishList);
    }

    @Transactional
    public void undoLikeTrainer(Long custIdx, Long trainerIdx){
        Account customer = accountRepository.getReferenceById(custIdx);
        Trainer trainer = trainerRepository.getReferenceById(trainerIdx);
        if(!wishListRepository.existsByCustomerAndTrainer(customer,trainer)){
            return;
        }
        WishList wishList = wishListRepository.findByCustomerAndTrainer(customer,trainer).orElseThrow();
        wishListRepository.delete(wishList);
    }

    @Transactional
    public void saveMatchingOrder(Long custIdx, Long trainerIdx, MatchingRequestDto request){
        Account customer = accountRepository.getReferenceById(custIdx);
        Trainer trainer = trainerRepository.getReferenceById(trainerIdx);
        if(matchingOrderRepository.existsByCustomerAndTrainer(customer, trainer)){
            throw new BusinessException(ALREADY_MATCHING);
        }
        PickUpType pickUpType;
        if(request.getType().equals("CUSTOMER_GO")){
            pickUpType = PickUpType.CUSTOMER_GO;
        }else{
            pickUpType = PickUpType.TRAINER_GO;
        }
        MatchingOrder matchingOrder = MatchingOrder.builder()
                .customer(customer).trainer(trainer)
                .startAt(request.getStartAt()).finishAt(request.getFinishAt())
                .pickUpType(pickUpType).isComplete("NONE").build();
        matchingOrderRepository.save(matchingOrder);
    }

    @Transactional
    public void registerReview(RegisterReviewDto reviewDto) throws BusinessException {
        Optional<Account> optionalC = accountRepository.findById(SecurityUtil.getLoginUserId());
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
        List<Review> reviewList = optionalT.get().getReviewList();
        Float reviewGrade = 0f;
        for(Review i : reviewList){
            reviewGrade += i.getGrade();
        }
        optionalT.get().setGrade(Float.parseFloat(String.format("%.1f", (reviewGrade / reviewList.toArray().length))));
        try{
            trainerRepository.save(optionalT.get());
        } catch (Exception e){
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }
    }

    @Transactional
    public void modifyUserLocation(String location) throws BusinessException{
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
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
    public List<WishDto> getWishList() throws BusinessException{
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        List<WishList> wishList = optional.get().getWishListList();
        List<WishDto> wishDtoList = new ArrayList<>();
        for(WishList i : wishList){
            Trainer trainer = i.getTrainer();
            UserImg userImg = trainer.getUserImg();
            WishDto wishDto = new WishDto(trainer.getId(), trainer.getUser().getName(),
                    userImg.getProfile(), trainer.getGrade(), trainer.getSchool(),
                    i.getCreatedDate().toLocalDate());
            wishDtoList.add(wishDto);
        }
        return wishDtoList;
    }

    public void modifyCustomerProfile(String customerProfile) throws BusinessException {
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        optional.get().setProfile(customerProfile);
        accountRepository.save(optional.get());
    }
    @Transactional
    public void notificationOff(Long userIdx){
        Account user = accountRepository.findById(userIdx).orElseThrow();
        user.alarmOff();
    }
    @Transactional
    public void notificationOn(Long userIdx){
        Account user = accountRepository.findById(userIdx).orElseThrow();
        user.alarmOn();
    }
}

