package ru.skillbox.rest_news_service.mapper;

import org.mapstruct.DecoratedWith;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.ReportingPolicy;
import org.springframework.data.domain.Page;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.web.model.*;

import java.util.List;

@DecoratedWith(NewsMapperDelegate.class)
@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE, uses = CommentMapper.class)
public interface NewsMapper {
    News requestToNews(UpsertNewsRequest request);

    @Mapping(source = "newsId", target = "id")
    News requestToNews(Long newsId, UpsertNewsRequest request);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    NewsResponse newsToResponse(News news);

    @Mapping(source = "author.id", target = "authorId")
    @Mapping(source = "category.id", target = "categoryId")
    @Mapping(source = "comments", target = "comments")
    NewsResponseWithComments newsToResponseWithComments(News news);

    List<NewsResponse> newsListToReponseList(List<News> newsList);

    default NewsListResponse newsListToNewsListResponse(Page<News> newsPage) {
        NewsListResponse response = new NewsListResponse();
        response.setNews(newsPage.getContent().stream()
                .map(this::newsToResponse)
                .toList());

        response.setTotalElements(newsPage.getTotalElements());
        response.setTotalPages(newsPage.getTotalPages());
        response.setCurrentPage(newsPage.getNumber());
        response.setPageSize(newsPage.getSize());
        return response;
    }

    default Long countComments(List<Comment> comments) {
        return (long) comments.size();
    }
}