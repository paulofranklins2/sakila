package com.example.demo.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class City {
    private int cityId;
    private String cityName;
    private int countryId;
}
