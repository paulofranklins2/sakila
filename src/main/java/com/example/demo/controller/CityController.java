package com.example.demo.controller;

import com.example.demo.dao.CityDao;
import com.example.demo.model.City;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class CityController {
    private final CityDao cityDao;

    public CityController(CityDao cityDao) {
        this.cityDao = cityDao;
    }

    @GetMapping("city")
    public List<City> getCities() {
        return cityDao.findAll();
    }

    @GetMapping("/city/{id}")
    public City getCity(@PathVariable int id) {
        return this.cityDao.findById(id);
    }
}
