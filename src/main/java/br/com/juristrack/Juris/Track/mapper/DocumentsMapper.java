package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.response.UploadResponse;
import br.com.juristrack.Juris.Track.enums.FileType;
import br.com.juristrack.Juris.Track.model.entity.Document;
import org.mapstruct.Mapper;

import java.util.UUID;

@Mapper(componentModel = "spring")
public interface DocumentsMapper {
    Document toDocument(String fileName, String filePath, FileType fileType);

    UploadResponse toUploadResponse(UUID id, String relativePath);
}
