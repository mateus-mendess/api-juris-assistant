package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.request.AttorneyUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.enums.RolesType;
import br.com.juristrack.Juris.Track.exception.CpfAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.NotFoundException;
import br.com.juristrack.Juris.Track.exception.OabAlreadyExistsException;
import br.com.juristrack.Juris.Track.exception.PhoneAlreadyExistsException;
import br.com.juristrack.Juris.Track.mapper.AttorneyMapper;
import br.com.juristrack.Juris.Track.model.entity.Address;
import br.com.juristrack.Juris.Track.model.entity.Attorney;
import br.com.juristrack.Juris.Track.model.entity.User;
import br.com.juristrack.Juris.Track.model.repository.AttorneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class AttorneyService {

    private final AttorneyRepository attorneyRepository;
    private final AttorneyMapper attorneyMapper;
    private final UserService userService;
    private final FileStorageService fileStorageService;
    private final AddressService addressService;

    public List<AttorneyResponse> findAll() {
        return attorneyMapper.toAttorneysResponse(attorneyRepository.findAll());
    }

    public Attorney getAuthenticatedLawyer(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        return attorneyRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("Attorney not found."));
    }

    public AttorneyResponse findByLawyer(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        return attorneyMapper.toAttorneyResponse(attorneyRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found.")));
    }

    @Transactional
    public AttorneyResponse create(AttorneyRequest attorneyRequest) {
        validateRegistrationData(attorneyRequest);

        User user = userService.create(attorneyRequest.userRequest(), AuthProviderType.LOCAL, RolesType.ROLE_ATTORNEY);

        Attorney attorney = attorneyMapper.toAttorney(attorneyRequest);

        Address address = addressService.buildAddress(attorneyRequest.addressRequest());

        attorney.linkUserAccountAndAddress(user, address);

        return attorneyMapper.toAttorneyResponse(attorneyRepository.save(attorney));
    }

    @Transactional
    public void uploadPhoto(Jwt jwt, MultipartFile filePhoto) {
        UUID id = UUID.fromString(jwt.getSubject());

        Attorney attorney = attorneyRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("User not found."));

        if (attorney.getProfilePhotoPath() != null) {
            fileStorageService.update(filePhoto, FileType.AVATAR, attorney.getProfilePhotoPath());
        }

        String relativePath = fileStorageService.save(filePhoto, FileType.AVATAR);

        attorney.setProfilePhotoPath(relativePath);

        attorneyRepository.save(attorney);
    }

    @Transactional
    public void update(AttorneyUpdateRequest lawyerUpdateRequest, Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        Attorney attorney = attorneyRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        if (attorneyRepository.existsByPhone(lawyerUpdateRequest.phone())) {
            throw new PhoneAlreadyExistsException(lawyerUpdateRequest.phone(), "phone");
        }

        attorneyMapper.toUpdateAttorney(lawyerUpdateRequest, attorney);
    }

    @Transactional
    public void delete(Jwt jwt) {
        UUID id = UUID.fromString(jwt.getSubject());

        Attorney attorney = attorneyRepository.findByUserId(id)
                .orElseThrow(() -> new NotFoundException("User not found."));

        fileStorageService.delete(attorney.getProfilePhotoPath());
        attorneyRepository.delete(attorney);
    }

    private void validateRegistrationData(AttorneyRequest attorneyRequest) {
        if (attorneyRepository.existsByCpf(attorneyRequest.cpf())) {
            throw new CpfAlreadyExistsException(attorneyRequest.cpf(), "CPF");
        }

        if (attorneyRepository.existsByOabNumberAndOabState(attorneyRequest.oabNumber(), attorneyRequest.oabState())) {
            throw new OabAlreadyExistsException(attorneyRequest.oabNumber(), attorneyRequest.oabState(), "OAB");
        }

        if (attorneyRepository.existsByPhone(attorneyRequest.phone())) {
            throw new PhoneAlreadyExistsException(attorneyRequest.phone(), "phone");
        }
    }
}
