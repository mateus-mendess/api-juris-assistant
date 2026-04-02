package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.response.UploadResponse;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.service.DocumentService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/documents")
@Tag(name = "Documents", description = "Operations related to document management for clients.")
public class UploadController {

    private final DocumentService documentService;

    @Operation(summary = "Upload client document", description = "Uploads a document associated with a specific client.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Document uploaded successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid request data"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Client not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PostMapping("/{clientId}")
    public ResponseEntity<UploadResponse> uploadDocuments(@Parameter(description = "Unique identifier of the client to which the uploaded files belong", required = true) @PathVariable UUID clientId,
                                                          @Parameter(description = "File to be uploaded (client document)", required = true)
                                                          @RequestParam(name = "file") MultipartFile file,
                                                          @Parameter(description = "Type of the document being uploaded", required = true)
                                                          @RequestParam(name = "fileType") FileType fileType) throws Exception{

        UploadResponse uploadResponse = documentService.uploadFileFromS3(clientId, file, fileType);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(uploadResponse.id()).toUri();

        return ResponseEntity.created(uri).body(uploadResponse);
    }

    @Operation(summary = "delete client document")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Document deleted successfully"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Document not found")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteDocuments(@PathVariable UUID id) {
        documentService.removeFileFromS3(id);

        return ResponseEntity.noContent().build();
    }
}
