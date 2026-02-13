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
        return ResponseEntity.ok().body(lawyerService.findById(jwt));
    }

    @PostMapping
    public ResponseEntity<LawyerResponse> create(@RequestPart("lawyer request") @Valid LawyerRequest lawyerRequest,
                                                 @RequestPart(name = "photo", required = false) MultipartFile photo) {

        LawyerResponse lawyerResponse = lawyerService.create(lawyerRequest, photo);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lawyerResponse.id()).toUri();

        return ResponseEntity.created(uri).body(lawyerResponse);
    }

    @PatchMapping
    public ResponseEntity update(@RequestPart(value = "lawyer update request", required = false) @Valid LawyerUpdateRequest lawyerUpdateRequest,
                                 @RequestPart(value = "profilePhoto", required = false) MultipartFile profilePhoto,
                                 @AuthenticationPrincipal Jwt jwt) {

        lawyerService.update(lawyerUpdateRequest, profilePhoto, jwt);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping
    public ResponseEntity deleteById(@AuthenticationPrincipal Jwt jwt) {
        lawyerService.delete(jwt);

        return ResponseEntity.noContent().build();
    }
}
