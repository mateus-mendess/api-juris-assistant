package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;

@Mapper(componentModel = "spring")
public interface LawyerMapper {

    @Mapping(target = "profilePhoto", ignore = true)
    Lawyer toLawyer(LawyerRequest request);

    LawyerResponse toLawyerResponse(Lawyer lawyer);
}
