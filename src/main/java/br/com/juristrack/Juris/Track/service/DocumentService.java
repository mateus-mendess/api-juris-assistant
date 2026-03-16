package br.com.juristrack.Juris.Track.service;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.exception.FileRequiredException;
import br.com.juristrack.Juris.Track.mapper.DocumentMapper;
import br.com.juristrack.Juris.Track.model.entity.Client;
import br.com.juristrack.Juris.Track.model.entity.Contract;
import br.com.juristrack.Juris.Track.model.entity.DeclarationTerm;
import br.com.juristrack.Juris.Track.model.entity.PowerOfAttorney;
import br.com.juristrack.Juris.Track.model.repository.ContractRepository;
import br.com.juristrack.Juris.Track.model.repository.DeclarationTermRepository;
import br.com.juristrack.Juris.Track.model.repository.PowerOfAttorneyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@Service
public class DocumentService {

    private final PowerOfAttorneyRepository powerOfAttorneyRepository;
    private final DeclarationTermRepository declarationTermRepository;
    private final ContractRepository contractRepository;
    private final DocumentMapper documentMapper;
    private final FileStorageService fileStorageService;
    private final ClientService clientService;

    @Transactional
    public void uploadPowerOfAttorney(UUID id, String nameFile, MultipartFile file) {
        Client client = clientService.findById(id);

        if (file.isEmpty()) {
            throw new FileRequiredException("Power of Attorney required.");
        }

        String relativePath = fileStorageService.save(file, FileType.POWER_OF_ATTORNEY);

        PowerOfAttorney powerOfAttorney = documentMapper.toPowerOfAttorney(nameFile, relativePath);

        powerOfAttorney.linkClient(client);

        powerOfAttorneyRepository.save(powerOfAttorney);
    }

    @Transactional
    public void uploadDeclarationTerm(UUID id, String nameFile, MultipartFile file) {
        Client client = clientService.findById(id);

        String relativePath = fileStorageService.save(file, FileType.DECLARATION_TERM);

        DeclarationTerm declarationTerm = documentMapper.toDeclarationTerm(nameFile, relativePath);

        declarationTerm.linkClient(client);

        declarationTermRepository.save(declarationTerm);
    }

    @Transactional
    public void uploadContract(UUID id, String nameFile, MultipartFile file) {
        Client client = clientService.findById(id);

        String relativePath = fileStorageService.save(file, FileType.CONTRACT);

        Contract contract = documentMapper.toContract(nameFile, relativePath);

        contract.linkClient(client);

        contractRepository.save(contract);
    }
}
