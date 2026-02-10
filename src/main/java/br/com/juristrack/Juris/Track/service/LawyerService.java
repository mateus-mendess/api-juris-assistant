package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.enums.Provider;
import br.com.juristrack.Juris.Track.exception.CpfAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.OabAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.PhoneAlreadyExistsException;
import br.com.juristrack.Juris.Track.mapper.LawyerMapper;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.LawyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Service
public class LawyerService {

    private final LawyerRepository lawyerRepository;
    private final LawyerMapper lawyerMapper;
    private final UserAccountService userAccountService;
    private final FileStorageService fileStorageService;

    @Transactional
    public LawyerResponse create(LawyerRequest lawyerRequest, MultipartFile photo) {
        validateRegistrationData(lawyerRequest);

        UserAccount userAccount = userAccountService.create(lawyerRequest.userAccountRequest(), Provider.LOCAL);

        Lawyer lawyer = lawyerMapper.toLawyer(lawyerRequest);

        String file = fileStorageService.save(photo, "lawyer/");

        lawyer.setProfilePhoto(file);
        lawyer.setUserAccount(userAccount);

        userAccount.setLawyer(lawyer);

        return lawyerMapper.toLawyerResponse(lawyerRepository.save(lawyer));
    }

    private void validateRegistrationData(LawyerRequest lawyerRequest) {
        if (lawyerRepository.existsByCpf(lawyerRequest.cpf())) {
            throw new CpfAlreadyExistsException(lawyerRequest.cpf());
        }

        if (lawyerRepository.existsByOabNumberAndOabState(lawyerRequest.oabNumber(), lawyerRequest.oabState())) {
            throw new OabAlreadyExistsException(lawyerRequest.oabNumber(), lawyerRequest.oabState());
        }

        if (lawyerRepository.existsByPhone(lawyerRequest.phone())) {
            throw new PhoneAlreadyExistsException(lawyerRequest.phone());
        }
    }
}
