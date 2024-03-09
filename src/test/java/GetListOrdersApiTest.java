import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.RestAssured;
import org.hamcrest.MatcherAssert;
import org.junit.Before;
import org.junit.Test;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class GetListOrdersApiTest {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @Step("Проверить, что список заказов не пустой")
    public ListOrders requestOrderList () {
        ListOrders listOrders =
                given()
                        .header("Content-type", "application/json")
                        .queryParam("limit","10")
                        .queryParam("page","0")
                        .queryParam("nearestStation","4")
                        .get("/api/v1/orders")
                        .body().as(ListOrders.class);
        MatcherAssert.assertThat(listOrders, notNullValue());
        return listOrders;
    }

    @Attachment(value = "Вложение", type = "application/json", fileExtension = ".txt")
    @Step("Вывести тело ответа")
    public String outputBodyRequest(){
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        String json = gson.toJson(requestOrderList());
        return json;
    }

    @Test
    public void getListOrdersTest(){
        requestOrderList();
        outputBodyRequest();
    }
}
