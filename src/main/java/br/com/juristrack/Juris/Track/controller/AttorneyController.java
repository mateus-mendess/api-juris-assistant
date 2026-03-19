package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.request.AttorneyUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.service.AttorneyService;
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

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/attorney")
public class AttorneyController {

    private final AttorneyService attorneyService;

    @GetMapping("/list")
    public ResponseEntity<List<AttorneyResponse>> findAll() {
        return ResponseEntity.ok().body(attorneyService.findAll());
    }

    @PostMapping
    public ResponseEntity<AttorneyResponse> create(@RequestBody @Valid AttorneyRequest attorneyRequest) {
        AttorneyResponse attorneyResponse = attorneyService.create(attorneyRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(attorneyResponse.id()).toUri();

        return ResponseEntity.created(uri).build();
    }

    @PatchMapping("/photo")
    public ResponseEntity<Void> uploadPhoto(@AuthenticationPrincipal Jwt jwt, @RequestParam(name = "filePhoto", required = false) MultipartFile filePhoto) {

        attorneyService.uploadPhoto(jwt, filePhoto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestPart(value = "attorneyUpdateRequest", required = false) @Valid AttorneyUpdateRequest attorneyUpdateRequest,
                                 @AuthenticationPrincipal Jwt jwt) {

        attorneyService.update(attorneyUpdateRequest, jwt);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal Jwt jwt) {
        attorneyService.delete(jwt);

        return ResponseEntity.noContent().build();
    }
}
