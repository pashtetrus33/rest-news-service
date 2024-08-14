package ru.skillbox.rest_news_service.service;

import org.springframework.data.domain.Page;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.News;

import java.util.List;

public interface AuthorService {
    Page<Author> findAll(int page, int size);

    Author findById(Long id);

    Author save(Author author);

    Author update(Author author);

    void deleteById(Long id);

    Author saveWithNews(Author author, List<News> news);
}
