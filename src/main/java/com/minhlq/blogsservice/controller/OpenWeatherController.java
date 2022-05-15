package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnitsEnum;
import com.minhlq.blogsservice.service.OpenWeatherService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/weathers")
@RequiredArgsConstructor
public class OpenWeatherController {

  private final OpenWeatherService openWeatherService;

  @GetMapping("/current")
  public OpenWeatherDto getCurrentWeather(
      @RequestParam(value = "lat") String latitude,
      @RequestParam(value = "long") String longitude,
      @RequestParam(value = "units", required = false, defaultValue = "IMPERIAL")
          WeatherMeasurementUnitsEnum units,
      @RequestParam(value = "lang", required = false, defaultValue = "en") String language) {
    return openWeatherService.getCurrentWeather(latitude, longitude, units, language);
  }
}
