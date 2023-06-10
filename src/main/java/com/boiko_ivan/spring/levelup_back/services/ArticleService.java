package com.boiko_ivan.spring.levelup_back.services;

import com.boiko_ivan.spring.levelup_back.entity.Article;

import java.util.List;

public interface ArticleService {
    List<Article> getAllArticles();
    Article getArticleByID(int id);
    void saveArticle(Article article);
    void deleteArticle(int id);
    List<Article> top4ArticlesByLikes();
}
