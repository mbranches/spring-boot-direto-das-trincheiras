package academy.devdojo.mapper;

import academy.devdojo.model.UserProfile;
import academy.devdojo.response.UserProfileGetResponse;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Primary
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface UserProfileMapper {

    List<UserProfileGetResponse> toUserProfileGetResponseList(List<UserProfile>userProfileList);
}
