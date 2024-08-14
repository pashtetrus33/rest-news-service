package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.repository.AuthorRepository;
import ru.skillbox.rest_news_service.repository.CommentRepository;
import ru.skillbox.rest_news_service.repository.NewsRepository;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;
import java.util.Optional;


@Service
@RequiredArgsConstructor
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private final NewsRepository newsRepository;
    private final CommentRepository commentRepository;

    @Override
    @Transactional
    public Author saveWithNews(Author author, List<News> newsList) {
        Author savedAuthor = authorRepository.save(author);

        for (News news : newsList) {
            news.setAuthor(savedAuthor);
            var savedNews = newsRepository.save(news);
            savedAuthor.addNews(savedNews);
        }
        return savedAuthor;
    }

    @Override
    public Page<Author> findAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return authorRepository.findAll(pageable);
    }

    @Override
    public Author findById(Long id) {
        return authorRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Автор с ID {0} не найден", id)));
    }

    @Override
    public Author save(Author author) {
        return authorRepository.save(author);
    }

    @Override
    public Author update(Author author) {
        Author existedAuthor = findById(author.getId());
        BeanUtils.copyNonNullProperties(author, existedAuthor);

        return authorRepository.save(author);
    }

    @Override
    public void deleteById(Long id) {
        authorRepository.deleteById(id);
    }
}