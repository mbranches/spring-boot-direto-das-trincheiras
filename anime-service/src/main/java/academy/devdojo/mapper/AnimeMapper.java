package academy.devdojo.mapper;

import academy.devdojo.model.Anime;
import academy.devdojo.model.Producer;
import academy.devdojo.requests.AnimePostRequest;
import academy.devdojo.requests.AnimePutRequest;
import academy.devdojo.response.AnimeGetResponse;
import academy.devdojo.response.AnimePostResponse;
import academy.devdojo.response.ProducerPostResponse;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.factory.Mappers;
import org.springframework.context.annotation.Primary;

import java.util.List;

@Primary
@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface AnimeMapper {
    AnimeMapper INSTANCE = Mappers.getMapper(AnimeMapper.class);

    @Mapping(target = "id", expression = "java(java.util.concurrent.ThreadLocalRandom.current().nextLong(1, 10))")
    Anime toAnime(AnimePostRequest postRequest);

    Anime toAnime(AnimePutRequest putRequest);

    AnimeGetResponse toAnimeGetResponse(Anime anime);

    AnimePostResponse toAnimePostResponse(Anime anime);

    List<AnimeGetResponse> toAnimeGetResponseList(List<Anime> animeList);
}
