package tests;

import com.codeborne.selenide.Configuration;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvFileSource;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import tests.data.City;

import static com.codeborne.selenide.CollectionCondition.sizeGreaterThan;
import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.*;

public class CianTest {

    @BeforeAll
    static void beforeAll() {
        Configuration.browserSize = "1920x1080";
        Configuration.baseUrl = "https://spb.cian.ru";
        Configuration.pageLoadStrategy = "eager";
    }

    @DisplayName("Выдача по ЖК не пустая")
    @ValueSource(strings = {
            "ЖК «ALPEN»",
            "ЖК «Дом Хороших квартир»"
    })
    @ParameterizedTest(name = "Для поискового запроса {0} должен отдавать не пустой список ЖК")
    @Tag("BLOCKER")
    void searchResultsShouldNotBeEmptyTest(String searchQuery) {
        open("/");
        $("#geo-suggest-input").setValue(searchQuery);
        $("[data-group = 'newbuildings']").click();
        $("[data-mark = 'FiltersSearchButton']").click();
        $$("._93444fe79c--wrapper--W0WqH")
                .shouldBe(sizeGreaterThan(0));
    }

    @DisplayName("Соответствие ЖК и метро")
    @CsvFileSource(resources = "/test_data/searchResultsShouldContainExpectedLocation.csv")
    @ParameterizedTest(name = "Для поискового запроса {0} в первой карточке должна быть локация {1}")
    @Tag("SMOKE")
    void searchResultsShouldContainExpectedLocationTest(String searchQuery,String expectedLocation) {
        open("/");
        $("#geo-suggest-input").setValue(searchQuery);
        $("[data-name = 'SuggestionPopup']").click();
        $("[data-mark = 'FiltersSearchButton']").click();
        $("[data-name = 'Underground']")
                .shouldHave(text(expectedLocation));
    }

    @DisplayName("Отображение городов в списке")
    @EnumSource(City.class)
    @ParameterizedTest(name = "Проверка городов {0}")
    @Tag("SMOKE")

    void checkApartmentsWithEnumTest(City city) {
        open("/");
        $("._025a50318d--text--SCFDt").click();
        $("[data-name = 'GeoSwitcherBody']")
                .shouldHave(text(city.description));
    }
}