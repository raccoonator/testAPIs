package newsapi;

import io.restassured.builder.RequestSpecBuilder;
import io.restassured.filter.log.LogDetail;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.*;

import static io.restassured.RestAssured.given;

@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class TestNews {
    public static final String API_KEY = "931cf88b22f24f71939c1d76c4aa64d8";
    public static final String BASE_URI = "https://newsapi.org/";
    public static final String end = "v2/top-headlines";
    public static RequestSpecification spec;

    @BeforeAll
    static void setUp(){
        spec = new RequestSpecBuilder()
                .setBaseUri(BASE_URI)
                .log(LogDetail.ALL)
                .setAccept(ContentType.JSON)
                .addQueryParam("apiKey", API_KEY)
                .build();
    }

    @Order(1)
    @Test
    @DisplayName("Проверяется загрузка страницы")
    void checkOpen(){
        given().spec(spec).queryParam("country","us")
                .when().get(end)
                .then().log().all()
                .statusCode(200);
    }

    @Order(2)
    @Test
    @DisplayName("Проверка заголовка content type")
    void checkHeader(){
        given().spec(spec)
                .when().get(end)
                .then().log().all().contentType("application/json");
    }

    @Order(3)
    @Test
    @DisplayName("Проверяем ответы базовых полей")
    void checkFields(){
        given().spec(spec).queryParam("country","us")
                .when().get(end)
                .then().log().all()
                .body("status", Matchers.equalTo("ok"))
                .body("totalResults", Matchers.notNullValue());
    }
}
