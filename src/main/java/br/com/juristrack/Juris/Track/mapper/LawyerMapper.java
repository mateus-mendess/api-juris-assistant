package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.request.LawyerUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import org.mapstruct.BeanMapping;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;
import org.mapstruct.NullValuePropertyMappingStrategy;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LawyerMapper {
    Lawyer toLawyer(LawyerRequest lawyerRequest, String profilePhotoPath);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateLawyer(LawyerUpdateRequest lawyerUpdateRequest, String profilePhotoPath, @MappingTarget Lawyer lawyer);

    LawyerResponse toLawyerResponse(Lawyer lawyer);

    List<LawyerResponse> toLawyersResponse(List<Lawyer> lawyers);
}
