package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.model.Category;
import ru.skillbox.rest_news_service.repository.CategoryRepository;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    @Override
    public Page<Category> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable);
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Категория с ID {0} не найдена", id)));
    }

    @Override
    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    @Override
    public Category update(Category category) {
        Category existedCategory = findById(category.getId());
        BeanUtils.copyNonNullProperties(category, existedCategory);
        return categoryRepository.save(existedCategory);
    }

    @Override
    public void deleteById(Long id) {
        categoryRepository.deleteById(id);
    }

    @Override
    public void deleteByIdIn(List<Long> ids) {
        categoryRepository.deleteAllById(ids);
    }
}
