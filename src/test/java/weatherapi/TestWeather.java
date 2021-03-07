package weatherapi;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestWeather {
    public static final String API_KEY = "ce7f8cf55dc2406e816112356210703";
    public static final String BASE_URI = "http://api.weatherapi.com/";
    public static final String end = "v1/current.json";
    public static RequestSpecification spec;

    @BeforeAll
    static void setUp(){
        spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .log(LogDetail.ALL)
                .setAccept(ContentType.JSON)
                .addQueryParam("key", API_KEY)
                .build();
    }

    @Order(1)
    @DisplayName("Проверка правильных ответов для разных городов")
    @ParameterizedTest
    @ValueSource(strings = {"London","Paris","Milan","Rome"})
    void checkParametrized(String input){
        given().spec(spec).queryParam("q",input)
                .when().get(end)
                .then().log().all()
                .statusCode(200);
    }

    @Order(2)
    @DisplayName("Проверка наличия заголовка дата")
    @Test
    void checkDateHeader(){
        given().spec(spec).queryParam("q", "London")
                .when().get(end)
                .then().log().all().header("Date", Matchers.notNullValue());
    }

    @Order(3)
    @DisplayName("Проверка полей города и страны")
    @Test
    void checkLocation(){
        given().spec(spec).queryParam("q", "London")
                .when().get(end)
                .then().log().all()
                .body("location.name", Matchers.equalTo("London"))
                .body("location.country", Matchers.equalTo("United Kingdom"));
    }
}
