package ru.skillbox.rest_news_service.mapper;

import lombok.NoArgsConstructor;
import org.springframework.context.annotation.Lazy;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.web.model.UpsertNewsRequest;

@NoArgsConstructor
public abstract class NewsMapperDelegate implements NewsMapper {


    private AuthorService authorService;
    private CategoryService categoryService;


    public NewsMapperDelegate(@Lazy AuthorService authorService, CategoryService categoryService) {
        this.authorService = authorService;
        this.categoryService = categoryService;
    }

    @Override
    public News requestToNews(UpsertNewsRequest request) {
        News news = new News();
        news.setNewsText(request.getNewsText());
        news.setCategory(categoryService.findCategoryById(request.getCategoryId()));
        news.setAuthor(authorService.findAuthorById(request.getAuthorId()));
        return news;
    }

    @Override
    public News requestToNews(Long orderId, UpsertNewsRequest request) {
        News news = requestToNews(request);
        news.setId(orderId);
        return news;
    }
}




