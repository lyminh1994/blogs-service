package com.minhlq.blogsservice.controller;

import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnits;
import com.minhlq.blogsservice.service.OpenWeatherService;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * This controller handles all requests relating to external resource from weather api.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@SecurityRequirements
@RestController
@RequestMapping("/weathers")
@RequiredArgsConstructor
public class OpenWeatherController {

  private final OpenWeatherService openWeatherService;

  /**
   * Get current weather with provide location info.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @param units the measurement units
   * @param language the language
   * @return weather information
   */
  @GetMapping("/current")
  public OpenWeatherDto getCurrentWeather(
      @RequestParam(value = "lat") String latitude,
      @RequestParam(value = "long") String longitude,
      @RequestParam(value = "units", required = false, defaultValue = "IMPERIAL")
          WeatherMeasurementUnits units,
      @RequestParam(value = "lang", required = false, defaultValue = "en") String language) {
    return openWeatherService.getCurrentWeather(latitude, longitude, units, language);
  }
}
