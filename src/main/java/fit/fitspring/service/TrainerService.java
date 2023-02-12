package fit.fitspring.service;

import fit.fitspring.controller.dto.communal.ReviewDto;
import fit.fitspring.controller.dto.trainer.EtcImgList;
import fit.fitspring.controller.dto.trainer.TrainerInformationDto;
import fit.fitspring.controller.dto.trainer.TrainerMainRes;
import fit.fitspring.controller.dto.trainer.UpdateTrainerInfoReq;
import fit.fitspring.domain.trainer.*;
import fit.fitspring.domain.account.Account;
import fit.fitspring.domain.account.AccountRepository;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import fit.fitspring.domain.trainer.UserImg;
import fit.fitspring.domain.trainer.UserImgRepository;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import fit.fitspring.exception.trainer.TrainerException;
import fit.fitspring.utils.S3Uploader;
import fit.fitspring.utils.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static fit.fitspring.exception.common.ErrorCode.*;

@RequiredArgsConstructor
@Service
public class TrainerService {

    private final TrainerRepository trainerRepository;
    private final S3Uploader s3Uploader;
    private final UserImgRepository userImgRepository;
    private final AccountRepository accountRepository;
    private final EtcImgRepository etcImgRepository;
    private final TrainerCertRepository trainerCertRepository;
    private final CommunalService communalService;

    @Transactional
    public void updateTrainerInfo(Long trainerIdx, UpdateTrainerInfoReq req) {
        Trainer trainer = trainerRepository.findById(trainerIdx)
                .orElseThrow(TrainerException::new);
        trainer.getUser().updateName(req.getName());
        trainer.updateInfo(Integer.parseInt(req.getCostHour()), req.getIntro(), req.getServiceDetail() );
    }

    public void modifyTrainerProfile(MultipartFile profileImg) throws BusinessException, IOException {
        Optional<Account> optionalA = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optionalA.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        UserImg userImg = optionalA.get().getTrainer().getUserImg();
        try{
            String userProfileUrl = s3Uploader.upload(profileImg, "profile");
            userImg.modifyProfile(userProfileUrl);
            userImgRepository.save(userImg);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.AWS_S3UPLOADER_ERROR);
        }
    }

    @Transactional
    public void modifyTrainerBgImage(Long trainerIdx, MultipartFile image) throws BusinessException, IOException {
        System.out.println("유저아이디"+trainerIdx);
        Trainer trainer = trainerRepository.findById(trainerIdx)
                .orElseThrow(()-> new BusinessException(ErrorCode.IS_NOT_TRAINER));
        UserImg userImg = userImgRepository.findByTrainer(trainer)
                .orElseThrow(()-> new BusinessException(ErrorCode.IS_NOT_TRAINER));
        if(!image.isEmpty()){
            String userBgImgUrl = s3Uploader.upload(image, "bgImg");
            userImg.modifyBgImg(userBgImgUrl);
        }
    }
    @Transactional
    public void addTrainerEctImage(Long trainerIdx, MultipartFile image) throws BusinessException, IOException {
        Trainer trainer = trainerRepository.findById(trainerIdx)
                .orElseThrow(()-> new BusinessException(ErrorCode.IS_NOT_TRAINER));
        if(!image.isEmpty()){
            String userEctImgUrl = s3Uploader.upload(image, "ectImg");
            EtcImg ectImg = EtcImg.builder().userImg(trainer.getUserImg()).etcImg(userEctImgUrl).build();
            etcImgRepository.save(ectImg);
        }
    }

    @Transactional
    public void deleteTrainerProfile() throws BusinessException{
        Optional<Account> optional = accountRepository.findById(SecurityUtil.getLoginUserId());
        if(optional.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }
        UserImg userImg = optional.get().getTrainer().getUserImg();
        try{
            userImg.modifyProfile("trainerProfile");
            userImgRepository.save(userImg);
        } catch (Exception e){
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }
    }

    @Transactional
    public void deleteEtcImg(Long trainerIdx, Long etcImgIdx) throws BusinessException {
        Trainer trainer = trainerRepository.getReferenceById(trainerIdx);
        EtcImg etcImg = etcImgRepository.findById(etcImgIdx).orElseThrow(()-> new BusinessException(NOT_FOUND_IMG));
        if(!etcImg.getUserImg().getTrainer().equals(trainer)){
            throw new BusinessException(PERMISSION_DENIED);
        }
        etcImgRepository.delete(etcImg);
    }

    @Transactional
    public String modifyMatching(Long userIdx){
        Account user = accountRepository.findById(userIdx).orElseThrow();

        // on(Active), off(Inactive) 상태 확인하기
        String status = accountRepository.findById(userIdx).get().getUserState();
        try{
            if(status.equals("A")){
                user.modifyState("I");
                return "off";
            }
            else {
                user.modifyState("A");
                return "on";
            }
        } catch(Exception e){
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }
    }

    @Transactional
    public void modifyCategory(Long trainerIdx, String categoryStr) throws BusinessException {
        Trainer trainer = trainerRepository.findById(trainerIdx).orElseThrow(()-> new BusinessException(ACCOUNT_NOT_FOUND));
        Category category = Category.PERSONAL_PT;
        if(categoryStr.equals("pt")){
            category=Category.PERSONAL_PT;
        }else if(categoryStr.equals("food")){
            category=Category.FOOD_CHECK;
        }else if(categoryStr.equals("diet")){
            category=Category.DIET;
        }else if(categoryStr.equals("rehab")){
            category=Category.REHAB;
        }else if(categoryStr.equals("friend")){
            category=Category.FIT_MATE;
        }
        trainer.modifyCategory(category);
    }

    @Transactional
    public TrainerMainRes trainerMain(Long trainerIdx) throws BusinessException {
        Trainer trainer = trainerRepository.findById(trainerIdx).orElseThrow(()-> new BusinessException(ACCOUNT_NOT_FOUND));
        String category = communalService.convertCategoryForClient(trainer.getCategory());
        Long certificateNum = trainerCertRepository.countByTrainer(trainer);
        return new TrainerMainRes(trainer,category,certificateNum);
    }


    public Trainer getById(Long trainerIdx) {
        return trainerRepository.getReferenceById(trainerIdx);
    }

    @Transactional
    public TrainerInformationDto getTrainerInformation(Long trainerIdx){
        Optional<Trainer> optional = trainerRepository.findById(trainerIdx);
        if(optional.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
        }
        Optional<UserImg> userImg = userImgRepository.findByTrainer(optional.get());
        TrainerInformationDto trainerInfo = new TrainerInformationDto();
        trainerInfo.setName(optional.get().getUser().getName());
        trainerInfo.setProfile(userImg.get().getProfile());
        trainerInfo.setBackground(userImg.get().getBackGround());
        trainerInfo.setLevelName(optional.get().getLevel().getName());
        trainerInfo.setSchool(optional.get().getSchool());
        trainerInfo.setGrade(optional.get().getGrade());
        trainerInfo.setCost(String.valueOf(optional.get().getPriceHour()));
        trainerInfo.setIntro(optional.get().getIntro());
        trainerInfo.setService(optional.get().getService());
        List<ReviewDto> reviewList = communalService.getTrainerReviewList(trainerIdx);
        if(reviewList.toArray().length > 3){
            reviewList = reviewList.subList(0, 3);
        }
        trainerInfo.setReviewDto(reviewList);
        List<EtcImg> etcImgList = userImg.get().getEtcImgList();
        List<EtcImgList> imageList = new ArrayList<>();
        for(EtcImg i : etcImgList){
            imageList.add(new EtcImgList(i));
        }
        trainerInfo.setImageList(imageList);
        trainerInfo.setMatching_state(optional.get().getUser().getUserState().equals("A"));
        trainerInfo.setCategory(communalService.convertCategoryForClient(optional.get().getCategory()));
        trainerInfo.setOpenChatLink(optional.get().getOpenChatLink());
        trainerInfo.setState(optional.get().getUser().getUserState());
        return trainerInfo;
    }

    @Transactional
    public void modifyOpenChatLink(Long userIdx, String openChatLink){
        String originalLink = openChatLink.replace("#", "/");
        Account user = accountRepository.findById(userIdx).orElseThrow(() -> new BusinessException(INVALID_USERIDX));
        user.getTrainer().modifyOpenChatLink(originalLink);
    }
}
