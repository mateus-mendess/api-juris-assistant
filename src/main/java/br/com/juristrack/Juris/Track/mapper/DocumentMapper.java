package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.model.entity.Contract;
import br.com.juristrack.Juris.Track.model.entity.DeclarationTerm;
import br.com.juristrack.Juris.Track.model.entity.PowerOfAttorney;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentMapper {
    PowerOfAttorney toPowerOfAttorney(String name, String filePath);

    DeclarationTerm toDeclarationTerm(String name, String filePath);

    Contract toContract(String name, String filePath);
}
