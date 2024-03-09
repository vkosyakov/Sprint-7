import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.junit.*;
import static io.restassured.RestAssured.given;

public class CreateCourierApiTest {
    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";

    }

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


    @Test
    @DisplayName("Create new courier")
    @Description("Создание нового курьера,удаление курьера")
    public void createNewCourierTest() {
        Courier courier = new Courier("vkosyakov", "1234", "Vlad");
        Response response = create(courier);
        compareCode(response, 201);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Create double courier")
    @Description("Проверка на создание дубля")
    public void createDoubleCourierTest() {
        Courier courier = new Courier("ninja", "1234", "saske");
        ResultCreateCourierJSON resultCreateCourierJSON = getResponseCourier(courier);
        //Проверка кода и сообщения
        checkCodeAndMessege(resultCreateCourierJSON, 409, "Этот логин уже используется. Попробуйте другой.");
    }

    //успешный запрос возвращает true;
    @Test
    @DisplayName("Check request when create new courier")
    @Description("Проверка тела ответа при создании курьера, удаление курьера")
    public void successfulRequest() {
        Courier courier = new Courier("vkosyakov", "1234", "Vlad");
        ResultCreateCourierJSON resultCreateCourierJSON = getResponseCourier(courier);
        checkBody(resultCreateCourierJSON, true);
        deleteCourier(courier);
    }

    //если нет password, запрос возвращает ошибку;
    @Test
    @DisplayName("Create courier withnot password")
    @Description("Создание курьера без пароля")
    public void notWithPassword() {
        Courier courier = new Courier("vkosyakov", "", "Vlad");
        ResultCreateCourierJSON resultCreateCourierJSON = getResponseCourier(courier);
        checkCodeAndMessege(resultCreateCourierJSON, 400, "Недостаточно данных для создания учетной записи");
    }
    @Test
    @DisplayName("Create courier withnot login")
    @Description("Создание курьера без пароля")
    public void notWithLogin() {
        Courier courier = new Courier("", "1234", "Vlad");
        ResultCreateCourierJSON resultCreateCourierJSON = getResponseCourier(courier);
        checkCodeAndMessege(resultCreateCourierJSON, 400, "Недостаточно данных для создания учетной записи");
    }

    @Test
    @DisplayName("Create courier withnot firstname")
    @Description("Создание курьера без имени")
    public void notWithFirstname() {
        Courier courier = new Courier("vkosyakov", "1234", "");
        Response response = create(courier);
        compareCode(response,201);
        deleteCourier(courier);
    }

    @Test
    @DisplayName("Create courier with double login")
    @Description("Создание курьера с логином который уже зарегестрирован")
    public void createCourierWithDoubleLogin(){
        Courier courier = new Courier("ninja","1234", "Vlad");
        ResultCreateCourierJSON resultCreateCourierJSON = getResponseCourier(courier);
        checkCodeAndMessege(resultCreateCourierJSON,409,"Этот логин уже используется. Попробуйте другой.");
    }



}


