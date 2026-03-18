package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/{id}")
    public ResponseEntity<Void> uploadDocuments(@PathVariable UUID id,
                                                      @RequestParam(name = "fileName") String fileName,
                                                      @RequestParam(name = "file") MultipartFile file,
                                                      @RequestParam(name = "fileType") FileType type) {

        uploadService.upload(id, fileName, file, type);

        return ResponseEntity.noContent().build();
    }
}
