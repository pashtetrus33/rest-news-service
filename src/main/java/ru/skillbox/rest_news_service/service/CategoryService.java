package ru.skillbox.rest_news_service.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import ru.skillbox.rest_news_service.model.Category;

import java.util.List;

public interface CategoryService {
    Page<Category> findAll(int page, int size);

    Category findById(Long id);

    Category save(Category category);

    Category update(Category category);

    void deleteById(Long id);

    void deleteByIdIn(List<Long> ids);
}
