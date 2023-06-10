package com.boiko_ivan.spring.levelup_back.controllers;

import com.boiko_ivan.spring.levelup_back.entity.Article;
import com.boiko_ivan.spring.levelup_back.services.ArticleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/articles")
public class ArticleController {
    @Autowired
    private ArticleService articleService;

    @GetMapping("/auth")
    public List<Article> getAllArticles() {
        return articleService.getAllArticles();
    }

    @PostMapping("/")
    public void saveArticle(@RequestBody Article article) {
        articleService.saveArticle(article);
    }

    @PutMapping("/")
    public void updateArticle(@RequestBody Article article) {
        articleService.saveArticle(article);
    }

    @GetMapping("/auth/{id}")
    public Article getArticleByID(@PathVariable int id) {
        return articleService.getArticleByID(id);
    }

    @DeleteMapping("/{id}")
    public void deleteArticleByID(@PathVariable int id) {
        articleService.deleteArticle(id);
    }

    @GetMapping("/auth/top")
    public List<Article> top4ArticlesByLikes() {
        return articleService.top4ArticlesByLikes();
    }
}
