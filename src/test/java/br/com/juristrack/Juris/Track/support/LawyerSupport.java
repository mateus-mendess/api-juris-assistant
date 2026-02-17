package br.com.juristrack.Juris.Track.support;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.request.LawyerUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;

import java.time.Instant;
import java.time.LocalDate;
import java.util.UUID;

public class LawyerSupport {

    public static LawyerRequest validRequest() {
        return new LawyerRequest(
                "Teste",
                "123.345.789-50",
                "12345",
                "AL",
                LocalDate.parse("1995-06-30"),
                "82",
                "99450-6070",
                UserAccountSupport.validRequest()
        );
    }

    public static Lawyer validEntity() {
        return new Lawyer(
                UserAccountSupport.validEntity().getId(),
                UserAccountSupport.validEntity(),
                "Teste",
                "123.345.789-50",
                "12345",
                "AL",
                "82",
                "99450-6070",
                "folder/nomeArquivo.type"
        );
    }

    public static LawyerResponse validResponse(Lawyer lawyer) {
        return new LawyerResponse(
                lawyer.getId(),
                lawyer.getName(),
                lawyer.getUserAccount().getEmail(),
                Instant.now()
        );
    }

    public static LawyerUpdateRequest validUpdateRequest() {
        return new LawyerUpdateRequest(
                "83",
                "99240-1010"
        );
    }
}
