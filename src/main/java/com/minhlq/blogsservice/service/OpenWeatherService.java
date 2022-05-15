package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnitsEnum;

public interface OpenWeatherService {

  OpenWeatherDto getCurrentWeather(
      String latitude, String longitude, WeatherMeasurementUnitsEnum units, String language);
}
