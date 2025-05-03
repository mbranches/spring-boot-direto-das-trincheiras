package academy.devdojo.mapper;

import academy.devdojo.model.Profile;
import academy.devdojo.request.ProfilePostRequest;
import academy.devdojo.response.ProfileGetResponse;
import academy.devdojo.response.ProfilePostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Primary
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ProfileMapper {
    List<ProfileGetResponse> toProfileGetResponseList(List<Profile> profileList);

    Profile toProfile(ProfilePostRequest profilePostRequest);

    ProfilePostResponse toProfilePostResponse(Profile profile);

    ProfileGetResponse toProfileGetResponse(Profile profile);
}
