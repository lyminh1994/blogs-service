package com.minhlq.blogsservice.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnits;

public interface OpenWeatherService {

  OpenWeatherDto getCurrentWeather(
      String latitude, String longitude, WeatherMeasurementUnits units, String language)
      throws JsonProcessingException;
}
