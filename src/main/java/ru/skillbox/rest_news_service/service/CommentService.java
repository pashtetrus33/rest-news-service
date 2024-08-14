package ru.skillbox.rest_news_service.service;

import ru.skillbox.rest_news_service.model.Comment;


public interface CommentService {
    Comment findById(Long id);

    Comment save(Comment comment);

    Comment update(Comment comment);

    void deleteById(Long id);
}