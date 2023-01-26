package fit.fitspring.service;

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
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.security.Principal;
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

    @Transactional
    public void updateTrainerInfo(Long trainerIdx, UpdateTrainerInfoReq req) {
        Trainer trainer = trainerRepository.findById(trainerIdx)
                .orElseThrow(TrainerException::new);
        trainer.getUser().updateName(req.getName());
        trainer.updateInfo(req.getCostHour(), req.getIntro(), req.getServiceDetail() );
    }

    public void modifyTrainerProfile(Principal principal, MultipartFile profileImg) throws BusinessException, IOException {
        Optional<Account> optionalA = accountRepository.findByEmail(principal.getName());
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
    public String modifyMatching(Principal principal){
        Optional<Account> account = accountRepository.findByEmail(principal.getName());
        if(account.isEmpty()){
            throw new BusinessException(ErrorCode.INVALID_USERIDX);
        }

        // on(Active), off(Inactive) 상태 확인하기
        String status = accountRepository.findByEmail(principal.getName()).get().getUserState();
        try{
            if(status.equals("A")){
                account.get().modifyMatchingState("I");
                return "off";
            }
            else {
                account.get().modifyMatchingState("A");
                return "on";
            }
        } catch(Exception e){
            throw new BusinessException(ErrorCode.DB_MODIFY_ERROR);
        }
    }

    public void deleteTrainerProfile(Principal principal) throws BusinessException{
        Optional<Account> optional = accountRepository.findByEmail(principal.getName());
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
        if(etcImg.getUserImg().getTrainer()!=trainer){
            throw new BusinessException(PERMISSION_DENIED);
        }
        etcImgRepository.delete(etcImg);
    }
}
