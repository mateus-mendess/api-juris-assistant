package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.service.UploadService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.UUID;

@CrossOrigin(origins = "*")
@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documents")
public class UploadController {

    private final UploadService uploadService;

    @PostMapping("/{id}")
    public ResponseEntity<Void> uploadDocuments(@PathVariable UUID id,
                                                      @RequestParam(name = "fileName") String fileName,
                                                      @RequestParam(name = "file") MultipartFile file,
                                                      @RequestParam(name = "type") FileType type) {

        uploadService.upload(id, fileName, file, type);

        return ResponseEntity.noContent().build();
    }

    @PostMapping("/upload-chunk")
    public ResponseEntity<String> uploadChunk(
            @RequestParam(name = "fileName") String fileName,
            @RequestParam(name = "chunk") MultipartFile chunk,
            @RequestParam(name = "chunkIndex") Integer chunkIndex
    ) throws IOException {

        uploadService.uploadChunk(fileName, chunk, chunkIndex);

        return ResponseEntity.ok().body("chunkIndex: " + chunkIndex + " sent");
    }

    @PostMapping("/merge-chunks")
    public ResponseEntity<String> mergeChunk(
            @RequestParam(name = "fileName") String fileName
    ) throws IOException {

        uploadService.mergeChunk(fileName);

        return ResponseEntity.ok().body("File merged successfully.");
    }
}
