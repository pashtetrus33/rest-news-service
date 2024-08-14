package ru.skillbox.rest_news_service.mapper;

import org.springframework.beans.factory.annotation.Autowired;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.web.model.NewsResponse;
import ru.skillbox.rest_news_service.web.model.UpsertNewsRequest;

public abstract class NewsMapperDelegate implements NewsMapper {

    @Autowired
    private AuthorService authorService;
    @Autowired
    private CategoryService categoryService;

    @Override
    public News requestToNews(UpsertNewsRequest request) {
        News news = new News();
        news.setNewsText(request.getNewsText());
        news.setCategory(categoryService.findById(request.getCategoryId()));
        news.setAuthor(authorService.findById(request.getAuthorId()));
        return news;
    }

    @Override
    public News requestToNews(Long orderId, UpsertNewsRequest request) {
        News news = requestToNews(request);
        news.setId(orderId);
        return news;
    }
}




