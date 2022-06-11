package com.minhlq.blogsservice.service;

import com.minhlq.blogsservice.dto.OpenWeatherDto;
import com.minhlq.blogsservice.enumdef.WeatherMeasurementUnits;

/**
 * The open weather service to provide for the request open weather apis operations.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
public interface OpenWeatherService {

  /**
   * Get current location weather information.
   *
   * @param latitude the latitude
   * @param longitude the longitude
   * @param units the measurement unit
   * @param language the response language
   * @return weather information
   */
  OpenWeatherDto getCurrentWeather(
      String latitude, String longitude, WeatherMeasurementUnits units, String language);
}
