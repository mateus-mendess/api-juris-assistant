package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.request.LawyerUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.service.LawyerService;
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
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lawyers")
public class LawyerController {

    private final LawyerService lawyerService;

    @GetMapping("/list")
    public ResponseEntity<List<LawyerResponse>> findAll() {
        return ResponseEntity.ok().body(lawyerService.findAll());
    }

    @GetMapping
    public ResponseEntity<LawyerResponse> findById(Jwt jwt) {
        return ResponseEntity.ok().body(lawyerService.findByLawyer(jwt));
    }

    @PostMapping
    public ResponseEntity<LawyerResponse> create(@RequestBody @Valid LawyerRequest lawyerRequest) {
        LawyerResponse lawyerResponse = lawyerService.create(lawyerRequest);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lawyerResponse.id()).toUri();

        return ResponseEntity.created(uri).body(lawyerResponse);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<Void> uploadPhoto(@PathVariable UUID id, @RequestParam(name = "filePhoto", required = false) MultipartFile filePhoto) {

        lawyerService.uploadPhoto(id, filePhoto);

        return ResponseEntity.noContent().build();
    }

    @PatchMapping
    public ResponseEntity<Void> update(@RequestPart(value = "lawyer update request", required = false) @Valid LawyerUpdateRequest lawyerUpdateRequest,
                                 @AuthenticationPrincipal Jwt jwt) {

        lawyerService.update(lawyerUpdateRequest, jwt);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity<Void> deleteById(@AuthenticationPrincipal Jwt jwt) {
        lawyerService.delete(jwt);

        return ResponseEntity.noContent().build();
    }
}
