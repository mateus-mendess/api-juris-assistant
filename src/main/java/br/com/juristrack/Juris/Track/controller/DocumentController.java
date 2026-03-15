package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.service.DocumentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/documents")
public class DocumentController {

    private final DocumentService documentService;

    @PostMapping("/{id}/power-of-attorney")
    public ResponseEntity<Void> uploadPowerOfAttorney(@PathVariable UUID id,
                                                      @RequestParam(name = "data") String nameFile,
                                                      @RequestParam(name = "file") MultipartFile file) {

        documentService.uploadPowerOfAttorney(id, nameFile, file);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/declaration-term")
    public ResponseEntity<Void> uploadDeclarationTerm(@PathVariable UUID id,
                                                      @RequestParam(name = "data") String nameFile,
                                                      @RequestParam(name = "file") MultipartFile file) {

        documentService.uploadDeclarationTerm(id, nameFile, file);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{id}/contract")
    public ResponseEntity<Void> uploadContract(@PathVariable UUID id,
                                               @RequestPart(name = "data") String nameFile,
                                               @RequestPart(name = "file") MultipartFile file) {

        documentService.uploadContract(id, nameFile, file);

        return ResponseEntity.noContent().build();
    }
}
