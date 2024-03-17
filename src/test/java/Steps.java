import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import io.qameta.allure.Attachment;
import io.qameta.allure.Step;
import io.restassured.response.Response;
import org.hamcrest.CoreMatchers;
import org.hamcrest.MatcherAssert;
import org.junit.Assert;

import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class Steps {
    @Step("Создание курьера")
    public Response create (Courier courier) {
        Response response = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("api/v1/courier");
        return response;
    }

    @Step("Сравнение кода ответа")
    public void compareCode(Response response, int code) {
        response.then().statusCode(code);
    }

    @Step("Получение ответа в формате JSON после создания курьера")
    public ResultCreateCourierJSON getResponseCourier(Courier courier) {
        ResultCreateCourierJSON resultCreateCourierJSON =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .post("api/v1/courier")
                        .body().as(ResultCreateCourierJSON.class);
        return resultCreateCourierJSON;
    }
    @Step("Проверка body ответа при успешном создании курьера")
    public void checkBody(ResultCreateCourierJSON resultCreateCourierJSON, boolean var) {
        Assert.assertEquals(resultCreateCourierJSON.isOk(), var);
    }

    @Step("Проверка кода и сообщения ")
    public void checkCodeAndMessege(ResultCreateCourierJSON resultCreateCourierJSON, int code, String messge) {
        Assert.assertEquals(resultCreateCourierJSON.getCode(), code);
        Assert.assertEquals(resultCreateCourierJSON.getMessage(), messge);
    }
    @Step("Взять id у курьера")
    public int getCourierID(Courier courier){
        int courierID = given()
                .header("Content-type", "application/json")
                .body(courier)
                .when()
                .post("/api/v1/courier/login")
                .then().extract().body().path("id");
        return courierID;
    }

    @Step("Удаление курьера")
    public void deleteCourier(Courier courier) {
        given()
                .header("Content-type", "application/json")
                .delete("/api/v1/courier/{courierID}", getCourierID(courier))
                .then().assertThat().statusCode(200);
    }

    @Step("Получение ответа в формате JSON после авторизации")
    public ResultCreateCourierJSON getResponseLogin(Courier courier) {
        ResultCreateCourierJSON resultCreateCourierJSON =
                given()
                        .header("Content-type", "application/json")
                        .body(courier)
                        .post("api/v1/courier/login")
                        .body().as(ResultCreateCourierJSON.class);
        return resultCreateCourierJSON;
    }

    @Step("Получение ответа после отправки запроса на авторизаию")
    public Response requestPostLogin(Courier courier){
        Response response =
                given()
                        .header("Content-type","application/json")
                        .body(courier)
                        .when()
                        .post("api/v1/courier/login");
        return  response;
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


    @Step("Проверка кода после Post запроса /api/v1/orders")
    public void checkCode(Response response){
        response.then().statusCode(201);
    }



    @Step("Перевод JSON в строку")
    public void checkContainsTrack(ResultCreateOrder resultCreateOrder) {
        Gson gson = new Gson();
        String json = gson.toJson(resultCreateOrder);
        Assert.assertThat(json, CoreMatchers.containsString("track"));
    }
    @Step("Отмена заказа")
    public void cancelOrder(ResultCreateOrder resultCreateOrder){
        given()
                .header("Content-type", "application/json")
                .body(resultCreateOrder)
                .put("/api/v1/orders/cancel")
                .then().statusCode(200);
    }
}
