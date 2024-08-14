package ru.skillbox.rest_news_service.service;

import org.springframework.data.domain.Page;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.web.model.NewsFilter;


public interface NewsService {
    Page<News> findAll(int page, int size);

    News findById(Long id);

    News save(News news);

    News update(News news);

    void deleteById(Long id);

    Page<News> filterBy(NewsFilter filter);
}
