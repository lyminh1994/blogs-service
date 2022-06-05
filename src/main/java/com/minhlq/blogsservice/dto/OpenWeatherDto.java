package com.minhlq.blogsservice.dto;

import java.io.Serializable;
import java.util.List;
import lombok.Data;

/**
 * This class models the format of the open weather api response produced.
 *
 * @author Eric Opoku
 * @version 1.0
 * @since 1.0
 */
@Data
public class OpenWeatherDto implements Serializable {

  private int cod;
  private String name;
  private int id;
  private int timezone;
  private Sys sys;
  private int dt;
  private Clouds clouds;
  private Wind wind;
  private int visibility;
  private Main main;
  private String base;
  private List<Weather> weather;
  private Coord coord;

  @Data
  public static class Sys {

    private int sunset;
    private int sunrise;
    private String country;
    private int id;
    private int type;
  }

  @Data
  public static class Clouds {

    private int all;
  }

  @Data
  public static class Wind {

    private double gust;
    private int deg;
    private double speed;
  }

  @Data
  public static class Main {

    private int humidity;
    private int pressure;
    private double tempMax;
    private double tempMin;
    private double feelsLike;
    private double temp;
  }

  @Data
  public static class Weather {

    private String icon;
    private String description;
    private String main;
    private int id;
  }

  @Data
  public static class Coord {

    private int lat;
    private int lon;
  }
}
