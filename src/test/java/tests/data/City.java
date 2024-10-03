package tests.data;

public enum City {
    Spb("Санкт-Петербург"),
    Msk("Москва"),
    Nsk("Новосибирск");


public final String description;
City(String description) {
    this.description = description;
  }
}