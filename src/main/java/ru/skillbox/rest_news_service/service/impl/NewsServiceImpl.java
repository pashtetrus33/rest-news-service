package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.aop.CheckOwnershipable;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.repository.AuthorRepository;
import ru.skillbox.rest_news_service.repository.NewsRepository;
import ru.skillbox.rest_news_service.repository.NewsSpecification;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.NewsService;
import ru.skillbox.rest_news_service.utils.BeanUtils;
import ru.skillbox.rest_news_service.web.model.NewsFilter;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NewsServiceImpl implements NewsService {

    private final NewsRepository newsRepository;
    private final AuthorService authorService;

    @Override
    public Page<News> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return newsRepository.findAll(pageable);
    }

    @Override
    public News findById(Long id) {
        return newsRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Новость с ID {0} не найдена", id)));
    }

    @Override
    public News save(News news) {
        return newsRepository.save(news);
    }

    @Override
    @CheckOwnershipable
    public News update(News news) {
        Author author = authorService.findById(news.getAuthor().getId());
        News exictedNews = findById(news.getId());
        BeanUtils.copyNonNullProperties(news, exictedNews);
        exictedNews.setAuthor(author);
        return newsRepository.save(exictedNews);
    }

    @Override
    @CheckOwnershipable
    public void deleteById(Long id) {
        newsRepository.deleteById(id);
    }

    @Override
    public Page<News> filterBy(NewsFilter filter) {
        return newsRepository.findAll(NewsSpecification.withFilter(filter), PageRequest.of(
                        filter.getPage(), filter.getSize()));
    }
}





