package fit.fitspring.service;

import fit.fitspring.controller.dto.communal.AnnouncementDto;
import fit.fitspring.controller.dto.communal.ReviewDto;
import fit.fitspring.controller.dto.communal.TermDto;
import fit.fitspring.controller.dto.customer.WishDto;
import fit.fitspring.domain.account.*;
import fit.fitspring.domain.matching.WishList;
import fit.fitspring.domain.review.Review;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.TrainerRepository;
import fit.fitspring.domain.trainer.UserImg;
import fit.fitspring.domain.trainer.UserImgRepository;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunalService {

    private final AnnouncementRepository announcementRepository;
    private final TermRepository termRepository;
    private final TrainerRepository trainerRepository;
    private final UserImgRepository userImgRepository;
    private final AccountRepository accountRepository;

    @Transactional
    public List<AnnouncementDto> getAnnouncementList() {
        List<Announcement> annoList = announcementRepository.findAll();
        List<AnnouncementDto> annoDtoList = new ArrayList<>();
        for(Announcement i : annoList){
            AnnouncementDto annoDto = new AnnouncementDto(i.getTitle(), i.getContent(), i.getModifiedDate().toLocalDate());
            annoDtoList.add(annoDto);
        }
        return annoDtoList;
    }
    @Transactional
    public List<TermDto> getTermList(){
        List<Term> termList = termRepository.findAll();
        List<TermDto> termDtoList = new ArrayList<>();
        for(Term i : termList){
            TermDto termDto = new TermDto(i.getId(), i.getName(), i.getDetail());
            termDtoList.add(termDto);
        }
        return termDtoList;
    }

    @Transactional
    public List<ReviewDto> getTrainerReviewList(Long trainerIdx){
        Optional<Trainer> optional = trainerRepository.findById(trainerIdx);
        if(optional.isEmpty()) {
            throw new BusinessException(ErrorCode.INVALID_TRAINERIDX);
        }
        List<Review> reviewList = optional.get().getReviewList();
        List<ReviewDto> reviewDtoList = new ArrayList<>();
        for(Review i : reviewList){
            Optional<UserImg> userImg = userImgRepository.findByTrainer(optional.get());
            String image = "none";
            if (userImg.isPresent()){
                image = userImg.get().getProfile();
            }
            ReviewDto reviewDto = new ReviewDto();
            reviewDto.setName(i.getCustomer().getName());
            reviewDto.setProfile(image);
            reviewDto.setGrade(i.getGrade());
            reviewDto.setContents(i.getContent());
            reviewDtoList.add(reviewDto);
        }
        return reviewDtoList;
    }
}
