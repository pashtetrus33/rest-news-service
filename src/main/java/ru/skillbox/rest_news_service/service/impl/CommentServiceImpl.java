package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.aop.CheckOwnershipable;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.repository.CommentRepository;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CommentService;
import ru.skillbox.rest_news_service.service.NewsService;
import ru.skillbox.rest_news_service.utils.BeanUtils;

import java.text.MessageFormat;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final AuthorService authorService;
    private final NewsService newsService;

//    @Override
//    public List<Comment> findAll() {
//        return commentRepository.findAll();
//    }

    @Override
    public Comment findById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден", id)));
    }

    @Override
    public Comment save(Comment comment) {
        Author author = authorService.findById(comment.getAuthor().getId());
        News news = newsService.findById(comment.getNews().getId());
        news.setComment(comment);
        comment.setNews(news);
        comment.setAuthor(author);
        return commentRepository.save(comment);
    }

    @Override
    @CheckOwnershipable
    public Comment update(Comment comment) {
        Author author = authorService.findById(comment.getAuthor().getId());
        News news = newsService.findById(comment.getNews().getId());

        // Обновляем комментарий
        Comment existingComment = findById(comment.getId());
        BeanUtils.copyNonNullProperties(comment, existingComment);
        existingComment.setAuthor(author);
        existingComment.setNews(news);
        commentRepository.save(existingComment);

        // Обновляем новость, если это необходимо
        news.setComment(existingComment);
        newsService.save(news);

        return existingComment;
    }


    @Override
    @CheckOwnershipable
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
