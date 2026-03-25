package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.request.AttorneyUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.dto.response.UploadResponse;
import br.com.juristrack.Juris.Track.service.AttorneyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Map;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attorney")
@Tag(name = "Attorneys", description = "Operations related to attorney management, including registration, profile updates, and account deletion.")
public class AttorneyController {

    private final AttorneyService attorneyService;

    @Operation(summary = "Register attorney",
            description = """
                    Registers a new attorney in the system. The CPF (Brazilian individual taxpayer registry) and 
                    OAB (Brazilian Bar Association registration number) must be unique and not previously registered.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Attorney created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data"),
            @ApiResponse(responseCode = "409", description = "CPF or OAB already registered")
    })
    @PostMapping
    public ResponseEntity<Void> create(@RequestBody @Valid AttorneyRequest attorneyRequest) {
        AttorneyResponse attorneyResponse = attorneyService.create(attorneyRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(attorneyResponse.id()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Upload or update attorney profile photo", description = "Uploads or replaces the profile picture of the authenticated attorney.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo added or updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid file"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping("/photo")
    public ResponseEntity<UploadResponse> uploadPhoto(@AuthenticationPrincipal Jwt jwt,
                                                      @Parameter(description = "Photo file to be added or updated", required = true)
                                                      @RequestParam(name = "file") MultipartFile filePhoto) throws Exception {

        UploadResponse uploadResponse = attorneyService.uploadPhoto(jwt, filePhoto);

        return ResponseEntity.ok().body(uploadResponse);
    }

    @Operation(summary = "Update attorney profile", description = "Updates profile information for the authenticated attorney.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Data updated successfully."),
            @ApiResponse(responseCode = "400", description = "Invalid update"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    @SecurityRequirement(name = "bearerAuth")
    @PatchMapping
    public ResponseEntity<Void> update(@RequestBody @Valid AttorneyUpdateRequest attorneyUpdateRequest,
                                 @AuthenticationPrincipal Jwt jwt) {

        attorneyService.update(attorneyUpdateRequest, jwt);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete attorneys", description = "Delete attorneys' accounts using the authentication token.")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Attorney deleted successfully")
    })
    @SecurityRequirement(name = "bearerAuth")
    @DeleteMapping
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal Jwt jwt) {
        attorneyService.delete(jwt);

        return ResponseEntity.noContent().build();
    }
}
