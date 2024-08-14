package ru.skillbox.rest_news_service.model;

import jakarta.persistence.*;
import lombok.*;
import org.apache.commons.lang3.builder.ToStringExclude;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

@Data
@Entity(name = "news")
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class News {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String newsText;

    @ManyToOne
    @JoinColumn(name = "author_id")
    @ToString.Exclude
    private Author author;

    @OneToMany(mappedBy = "news", cascade = CascadeType.ALL)
    @ToStringExclude
    @Builder.Default
    private List<Comment> comments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "category_id")
    @ToString.Exclude
    private Category category;

    @CreationTimestamp
    private Instant created;

    @UpdateTimestamp
    private Instant updatedAt;

    public void setComment(Comment comment) {
        comments.add(comment);
    }
}