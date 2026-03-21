package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.service.UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Uploads", description = "Controller responsible for managing document uploads in the system.")
public class UploadController {

    private final UploadService uploadService;

    @Operation(summary = "Register files", description = "It records documents related to clients who are registered in the system.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "File upload successfully")
    })
    @PostMapping("/{id}")
    public ResponseEntity<Map<String, String>> uploadDocuments(@PathVariable UUID id,
                                               @RequestParam(name = "fileName") String fileName,
                                               @RequestParam(name = "file") MultipartFile file,
                                               @RequestParam(name = "fileType") FileType type) {

        String relativePath = uploadService.upload(id, fileName, file, type);

        return ResponseEntity.ok().body(Map.of("FilePath", relativePath));
    }
}
