package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.LawyerRequest;
import br.com.juristrack.Juris.Track.dto.request.LawyerUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.LawyerResponse;
import br.com.juristrack.Juris.Track.model.entity.Lawyer;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface LawyerMapper {
    Lawyer toLawyer(LawyerRequest lawyerRequest, String profilePhotoPath);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateLawyer(LawyerUpdateRequest lawyerUpdateRequest, String profilePhotoPath, @MappingTarget Lawyer lawyer);

    @Mapping(target = "createdAt", source = "userAccount.createdAt")
    @Mapping(target = "email", source = "userAccount.email")
    LawyerResponse toLawyerResponse(Lawyer lawyer);

    List<LawyerResponse> toLawyersResponse(List<Lawyer> lawyers);
}
