package fit.fitspring.service;

import fit.fitspring.controller.dto.communal.AnnouncementDto;
import fit.fitspring.controller.dto.communal.TermDto;
import fit.fitspring.controller.dto.customer.WishDto;
import fit.fitspring.domain.account.*;
import fit.fitspring.domain.matching.WishList;
import fit.fitspring.domain.trainer.Trainer;
import fit.fitspring.domain.trainer.UserImg;
import fit.fitspring.exception.common.BusinessException;
import fit.fitspring.exception.common.ErrorCode;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CommunalService {

    private final AnnouncementRepository announcementRepository;
    private final TermRepository termRepository;

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
}
