package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.model.entity.Document;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DocumentsMapper {
    Document toDocument(String title, String storagePath, FileType type);
}
