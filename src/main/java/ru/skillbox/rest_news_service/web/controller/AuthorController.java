package ru.skillbox.rest_news_service.web.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.skillbox.rest_news_service.exception.EntityNotFoundException;
import ru.skillbox.rest_news_service.mapper.AuthorMapper;
import ru.skillbox.rest_news_service.model.Author;
import ru.skillbox.rest_news_service.model.News;
import ru.skillbox.rest_news_service.service.AuthorService;
import ru.skillbox.rest_news_service.service.CategoryService;
import ru.skillbox.rest_news_service.web.model.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/author")
@RequiredArgsConstructor
@Tag(name = "Author v1", description = "Author API version V1")
public class AuthorController {

    private final AuthorService authorService;
    private final AuthorMapper authorMapper;
    private final CategoryService categoryService;

    @Operation(summary = "Get authors", description = "Get all authors", tags = {"author"})
    @GetMapping
    public ResponseEntity<AuthorListResponse> findAll(@RequestParam(defaultValue = "0") int page,
                                                      @RequestParam(defaultValue = "10") int size) {

        return ResponseEntity.ok(
                authorMapper.authorListToAuthorResponseList(authorService.findAll(page, size))
        );
    }

    @Operation(summary = "Get author by id",
            description = "Get author by id. Return id, list of news and list of comments",
            tags = {"author, id"})
    @ApiResponses({
            @ApiResponse(responseCode = "200",
                    content = {@Content(schema = @Schema(implementation = AuthorResponse.class), mediaType = "application/json")
                    }
            ),
            @ApiResponse(responseCode = "404",
                    content = {@Content(schema = @Schema(implementation = ErrorResponse.class), mediaType = "application/json")
                    })
    })
    @GetMapping("/{id}")
    public ResponseEntity<AuthorResponse> findById(@PathVariable Long id) {

        return ResponseEntity.ok(
                authorMapper.authorToResponse(authorService.findById(id))
        );
    }

    @PostMapping
    public ResponseEntity<AuthorResponse> create(@RequestBody @Valid UpsertAuthorRequest request) {
        Author newAuthor = authorService.save(authorMapper.requestToAuthor(request));

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(authorMapper.authorToResponse(newAuthor));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AuthorResponse> update(@PathVariable("id") Long authorId,
                                                 @RequestBody @Valid UpsertAuthorRequest request) {
        Author updatedAuthor = authorService.update(authorMapper.requestToAuthor(authorId, request));

        return ResponseEntity.ok(authorMapper.authorToResponse(updatedAuthor));
    }

    @Operation(summary = "Delete author by id",
            description = "Delete author by id",
            tags = {"author, id"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        authorService.deleteById(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @PostMapping("/save-with-news")
    public ResponseEntity<AuthorResponse> createWithNews(@RequestBody @Valid CreateAuthorWithNewsRequest request) {
        Author author = Author.builder().name(request.getName()).build();

        List<News> newsList = request.getNewsList().stream().map(newsRequest ->
                News.builder()
                        .newsText(newsRequest.getNewsText())
                        .category(categoryService.findById(newsRequest.getCategoryId()))
                        .build()).toList();
        return ResponseEntity.status(HttpStatus.CREATED).body(authorMapper.authorToResponse(authorService.saveWithNews(author, newsList)));
    }
}