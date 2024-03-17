import io.restassured.RestAssured;
import org.junit.Before;
import io.qameta.allure.Description;
import io.qameta.allure.Step;
import io.qameta.allure.junit4.DisplayName;
import io.restassured.response.Response;
import org.junit.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.CoreMatchers.notNullValue;

public class LoginCourierApiTest extends Steps {

    @Before
    public void setUp() {
        RestAssured.baseURI = "http://qa-scooter.praktikum-services.ru";
    }

    @DisplayName("Login courier")
    @Description("Успешная авторизация курьера, проверка кода и тела ответа")
    @Test
    public void loginCourier(){
        Courier courier = new Courier("vladkosyakov","1234");
        Response response = requestPostLogin(courier);
        response.then().statusCode(200)
                .and().body("id",notNullValue());
    }

    @DisplayName("Failed login courier")
    @Description("Авторизация с неправильным логином")
    @Test
    public  void autoFailedLogin(){
        Courier courier = new Courier("vladkosyakov-failed","1234");
        Response response = requestPostLogin(courier);
        response.then().statusCode(404);

        ResultCreateCourierJSON resultCreateCourierJSON = getResponseLogin(courier);
        Assert.assertEquals(resultCreateCourierJSON.getMessage(),"Учетная запись не найдена");
    }

    @DisplayName("Failed password courier")
    @Description("Авторизация с неправильным паролем")
    @Test
    public  void autoFailedPassword(){
        Courier courier = new Courier("vladkosyakov","12345");
        Response response = requestPostLogin(courier);
        response.then().statusCode(404);

        ResultCreateCourierJSON resultCreateCourierJSON = getResponseLogin(courier);
        Assert.assertEquals(resultCreateCourierJSON.getMessage(),"Учетная запись не найдена");
    }

    @DisplayName("Autorization withnot login")
    @Description("Авторизация с без логина")
    @Test
    public  void autoWithNotLogin(){
        Courier courier = new Courier("","12345");
        Response response = requestPostLogin(courier);
        response.then().statusCode(400);

        ResultCreateCourierJSON resultCreateCourierJSON = getResponseLogin(courier);
        Assert.assertEquals(resultCreateCourierJSON.getMessage(),"Недостаточно данных для входа");
    }


}
