import com.google.gson.Gson;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import static io.restassured.RestAssured.given;

@RunWith(Parameterized.class)
public class CreateOrderApiTest extends Steps {
    Order order;

    public CreateOrderApiTest(Order order){
        this.order = order;
    }

    @Parameterized.Parameters
    public static Object[][]createOrders() {
        String colorBlack[] = {"Black"};
        String colorGrey[] = {"Grey"};
        String colorBlackAndGrey[] = {"Black","Grey"};
        String withNotColor[] = {""};
        Order orderBlack = new Order("Naruto", "Uchiha",
                "Konoha, 142 apt.", 4, "+7 800 355 35 45",
                5, "2020-06-06", "Saske, come back to Konoha", colorBlack);
        Order orderGrey = new Order("Naruto", "Uchiha",
                "Konoha, 142 apt.", 4, "+7 800 355 35 45",
                5, "2020-06-06", "Saske, come back to Konoha", colorGrey);
        Order orderBlackAndGrey = new Order("Naruto", "Uchiha",
                "Konoha, 142 apt.", 4, "+7 800 355 35 45",
                5, "2020-06-06", "Saske, come back to Konoha", colorBlackAndGrey);
        Order orderWithNotColor = new Order("Naruto", "Uchiha",
                "Konoha, 142 apt.", 4, "+7 800 355 35 45",
                5, "2020-06-06", "Saske, come back to Konoha",withNotColor);

        return new Object[][]{
                {orderBlack},
                {orderGrey},
                {orderBlackAndGrey},
                {orderWithNotColor},
        };
    }
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

    }
    @Step("Пoлучение body после POST запроса на api/v1/orders")
    public ResultCreateOrder getBody(){
        ResultCreateOrder resultCreateOrder =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .post("/api/v1/orders")
                        .body().as(ResultCreateOrder.class);
        return resultCreateOrder;
    }

    @Step("Создание заказа")
    public Response createOrd(){
        Response response =
                given()
                        .header("Content-type", "application/json")
                        .body(order)
                        .post("/api/v1/orders");
        return response;
    }

    //Отмена заказа не работает

    @DisplayName("Create order")
    @Description("Параметризованный тест создания заказа")
    @Test
    public void createOrder(){
        Response response = createOrd();
        checkCode(response);
        ResultCreateOrder resultCreateOrder = getBody();
        checkContainsTrack(resultCreateOrder);
        cancelOrder(resultCreateOrder);
    }




}
