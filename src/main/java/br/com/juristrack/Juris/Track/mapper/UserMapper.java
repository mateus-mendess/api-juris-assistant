package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.UserRequest;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.model.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserMapper {
    User toUser(UserRequest request, AuthProviderType authProviderType);
}
