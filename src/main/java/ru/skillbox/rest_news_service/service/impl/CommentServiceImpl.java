package ru.skillbox.rest_news_service.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.skillbox.rest_news_service.aop.CheckOwnershipable;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.CommentMapper;
import ru.skillbox.rest_news_service.mapper.NewsMapper;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.Comment;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.repository.AuthorRepository;
import ru.skillbox.rest_news_service.repository.CommentRepository;
import ru.skillbox.rest_news_service.service.CommentService;
import ru.skillbox.rest_news_service.service.NewsService;
import ru.skillbox.rest_news_service.utils.BeanUtils;
import ru.skillbox.rest_news_service.web.model.CommentResponse;
import ru.skillbox.rest_news_service.web.model.UpsertCommentRequest;
import ru.skillbox.rest_news_service.web.model.UpsertNewsRequest;

import java.text.MessageFormat;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final NewsService newsService;
    private final AuthorRepository authorRepository;
    private final CommentMapper commentMapper;
    private final NewsMapper newsMapper;

//    @Override
//    public List<Comment> findAll() {
//        return commentRepository.findAll();

//    }

    @Override
    public CommentResponse findById(Long id) {
        return commentMapper.commentToResponse(commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден", id))));
    }

    @Override
    public Comment findCommentById(Long id) {
        return commentRepository.findById(id).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден", id)));
    }

    @Override
    public CommentResponse save(UpsertCommentRequest request) {
        Comment comment = commentMapper.requestToComment(request);
        Author author = authorRepository.findById(comment.getId()).orElseThrow(() ->
                new EntityNotFoundException(MessageFormat.format("Комментарий с ID {0} не найден", comment.getId())));
        News news = newsService.findNewsById(comment.getNews().getId());
        news.setComment(comment);
        comment.setNews(news);
        comment.setAuthor(author);
        return commentMapper.commentToResponse(commentRepository.save(comment));
    }

    @Override
    @CheckOwnershipable
    public CommentResponse update(Long commentId, UpsertCommentRequest request) {

        // Найдем существующий комментарий по commentId
        Comment existingComment = findCommentById(commentId);

        // Обновим поля комментария на основе запроса
        Comment comment = commentMapper.requestToComment(commentId, request);
        BeanUtils.copyCommentNonNullProperties(existingComment,comment);

        // Найдем автора комментария по ID
        Author author = authorRepository.findById(request.getAuthorId())
                .orElseThrow(() -> new EntityNotFoundException(MessageFormat.format("Автор с ID {0} не найден", request.getAuthorId())));

        existingComment.setAuthor(author);


        News news = newsService.findNewsById(request.getNewsId());
        existingComment.setNews(news);


        // Сохраняем обновленный комментарий
        commentRepository.save(existingComment);

        // Обновляем новость, если это необходимо
        if (existingComment.getNews() != null) {
            news = existingComment.getNews();
            news.setComment(existingComment);
            UpsertNewsRequest upsertNewsRequest = new UpsertNewsRequest();
            upsertNewsRequest.setNewsText(news.getNewsText());
            // Дополните остальными необходимыми полями
            newsService.save(upsertNewsRequest);
        }

        return commentMapper.commentToResponse(existingComment);
    }


    @Override
    @CheckOwnershipable
    public void deleteById(Long id) {
        commentRepository.deleteById(id);
    }
}
