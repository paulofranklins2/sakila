package com.example.demo.controller;

import com.example.demo.dao.LanguageDao;
import com.example.demo.model.Language;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class LanguageController {
    private final LanguageDao languageDao;

    public LanguageController(LanguageDao languageDao) {
        this.languageDao = languageDao;
    }

    @GetMapping("language")
    public List<Language> getLanguages() {
        return this.languageDao.findAll();
    }

    @GetMapping("language/{id}")
    public Language getLanguage(@PathVariable int id) {
        return this.languageDao.finById(id);
    }
}
