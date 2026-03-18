package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.AttorneyRequest;
import br.com.juristrack.Juris.Track.dto.request.AttorneyUpdateRequest;
import br.com.juristrack.Juris.Track.dto.response.AttorneyResponse;
import br.com.juristrack.Juris.Track.model.entity.Attorney;
import org.mapstruct.*;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AttorneyMapper {
    Attorney toAttorney(AttorneyRequest lawyerRequest);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    void toUpdateAttorney(AttorneyUpdateRequest lawyerUpdateRequest, @MappingTarget Attorney lawyer);

    @Mapping(target = "createdAt", source = "user.createdAt")
    @Mapping(target = "email", source = "user.email")
    AttorneyResponse toAttorneyResponse(Attorney lawyer);

    List<AttorneyResponse> toAttorneysResponse(List<Attorney> lawyers);
}
