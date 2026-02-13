package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.request.LawyerUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.enums.Provider;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.CpfAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.exception.OabAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.PhoneAlreadyExistsException;
import br.com.juristrack.Juris.Track.mapper.LawyerMapper;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import br.com.juristrack.Juris.Track.model.repository.LawyerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class LawyerService {

    private final LawyerRepository lawyerRepository;
    private final LawyerMapper lawyerMapper;
    private final UserAccountService userAccountService;
    private final FileStorageService fileStorageService;

    public List<LawyerResponse> findAll() {
        return lawyerMapper.toLawyersResponse(lawyerRepository.findAll());
    }

    public LawyerResponse findById(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        return lawyerMapper.toLawyerResponse(lawyerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found.")));
    }

    @Transactional
    public LawyerResponse create(LawyerRequest lawyerRequest, MultipartFile photo) {
        validateRegistrationData(lawyerRequest);

        UserAccount userAccount = userAccountService.create(lawyerRequest.userAccountRequest(), Provider.LOCAL, RolesType.ROLE_LAWYER);

        String profilePhoto = fileStorageService.save(photo);

        Lawyer lawyer = lawyerMapper.toLawyer(lawyerRequest, profilePhoto);
        lawyer.linkUserAccount(userAccount);

        return lawyerMapper.toLawyerResponse(lawyerRepository.save(lawyer));
    }

    @Transactional
    public void update(LawyerUpdateRequest lawyerUpdateRequest, MultipartFile profilePhoto, Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        String profilePhotoPath = fileStorageService.update(profilePhoto, lawyer.getProfilePhotoPath());
        lawyerMapper.toUpdateLawyer(lawyerUpdateRequest, profilePhotoPath, lawyer);
    }

    @Transactional
    public void delete(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        Lawyer lawyer = lawyerRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found."));

        fileStorageService.delete(lawyer.getProfilePhotoPath());
        lawyerRepository.delete(lawyer);
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
