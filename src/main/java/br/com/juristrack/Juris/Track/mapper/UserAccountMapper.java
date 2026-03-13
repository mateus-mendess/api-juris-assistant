package br.com.juristrack.Juris.Track.mapper;

import br.com.juristrack.Juris.Track.dto.request.UserAccountRequest;
import br.com.juristrack.Juris.Track.enums.AuthProviderType;
import br.com.juristrack.Juris.Track.model.entity.UserAccount;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface UserAccountMapper {

    UserAccount toUserAccount(UserAccountRequest request, AuthProviderType authProviderType);
}
