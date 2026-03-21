package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.request.AttorneyUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.service.AttorneyService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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
@Tag(name = "Attorneys", description = "Controller responsible for managing functions involving the attorney.")
public class AttorneyController {

    private final AttorneyService attorneyService;

    @Operation(summary = "Register attorney",
            description = """
                    Registers a new attorney in the system. The CPF (Brazilian individual taxpayer registry) and 
                    OAB (Brazilian Bar Association registration number) must be unique and not previously registered.
                    """)
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Attorney created successfully"),
            @ApiResponse(responseCode = "400", description = "Invalid data")
    })
    @PostMapping
    public ResponseEntity<AttorneyResponse> create(@RequestBody @Valid AttorneyRequest attorneyRequest) {
        AttorneyResponse attorneyResponse = attorneyService.create(attorneyRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(attorneyResponse.id()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @Operation(summary = "Add/Update photo", description = "Add or update profile picture for attorneys' accounts.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Photo added or updated successfully.")
    })
    @PatchMapping("/photo")
    public ResponseEntity<Map<String, String>> uploadPhoto(@AuthenticationPrincipal Jwt jwt, @RequestParam(name = "filePhoto", required = false) MultipartFile filePhoto) {

        String relativePath = attorneyService.uploadPhoto(jwt, filePhoto);

        return ResponseEntity.ok().body(Map.of("profilePhotoPath", relativePath));
    }

    @Operation(summary = "Updated attorney data", description = "It updates certain profile data for attorneys.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Data updated successfully.")
    })
    @PatchMapping
    public ResponseEntity<Void> update(@RequestPart(value = "attorneyUpdateRequest", required = false) @Valid AttorneyUpdateRequest attorneyUpdateRequest,
                                 @AuthenticationPrincipal Jwt jwt) {

        attorneyService.update(attorneyUpdateRequest, jwt);

        return ResponseEntity.noContent().build();
    }

    @Operation(summary = "Delete attorneys", description = "Delete attorneys' accounts using the authentication token.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Attorney successfully excluded.")
    })
    @DeleteMapping
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal Jwt jwt) {
        attorneyService.delete(jwt);

        return ResponseEntity.noContent().build();
    }
}
