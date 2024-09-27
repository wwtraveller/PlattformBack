package de.ait.platform.article.repository;

import de.ait.platform.article.entity.Article;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArticleRepository extends JpaRepository<Article, Long> {
Article findByTitle(String title);

}
