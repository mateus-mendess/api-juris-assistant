package br.com.juristrack.Juris.Track.controller;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.service.LawyerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.UUID;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/lawyers")
public class LawyerController {

    private final LawyerService lawyerService;

    @GetMapping
    public ResponseEntity findAll() {
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{id}")
    public ResponseEntity findById(@PathVariable UUID id) {
        return ResponseEntity.ok().build();
    }

    @PostMapping
    public ResponseEntity<LawyerResponse> create(@RequestPart("lawyer request") @Valid LawyerRequest lawyerRequest,
                                                 @RequestPart(name = "photo", required = false) MultipartFile photo) {

        System.out.printf(lawyerRequest.name());
        LawyerResponse lawyerResponse = lawyerService.create(lawyerRequest, photo);

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(lawyerResponse.id()).toUri();
        return ResponseEntity.created(uri).body(lawyerResponse);
    }

    @DeleteMapping
    public ResponseEntity deleteById() {
        return ResponseEntity.ok().build();
    }
}
