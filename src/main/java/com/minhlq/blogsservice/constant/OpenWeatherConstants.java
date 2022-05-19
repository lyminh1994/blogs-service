package com.minhlq.blogsservice.constant;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * This class holds all api suffix used by the Open Weather Services.
 *
 * @author Minh Lys
 * @version 1.0
 * @since 1.0
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class OpenWeatherConstants {

  public static final String CURRENT_WEATHER_DATA = "/data/2.5/weather";
}
