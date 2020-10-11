
import data.*;
import org.testng.Assert;
import org.testng.annotations.Test;

import java.util.*;
import java.util.stream.Collectors;

import static io.restassured.RestAssured.given;

public class Tests {

    private static final String AVATAR_ID_7 = "https://s3.amazonaws.com/uifaces/faces/twitter/follettkyle/128.jpg";
    private static final String AVATAR_ID_8 = "https://s3.amazonaws.com/uifaces/faces/twitter/araa3185/128.jpg";
    private static final String AVATAR_ID_9 = "https://s3.amazonaws.com/uifaces/faces/twitter/vivekprvr/128.jpg";
    private static final String AVATAR_ID_10 = "https://s3.amazonaws.com/uifaces/faces/twitter/russoedu/128.jpg";
    private static final String AVATAR_ID_11 = "https://s3.amazonaws.com/uifaces/faces/twitter/mrmoiree/128.jpg";
    private static final String AVATAR_ID_12 = "https://s3.amazonaws.com/uifaces/faces/twitter/hebertialmeida/128.jpg";


    @Test
    public void avatarOfUsers() {
        List<String> avatarReferences = Arrays.asList(
                AVATAR_ID_7,
                AVATAR_ID_8,
                AVATAR_ID_9,
                AVATAR_ID_10,
                AVATAR_ID_11,
                AVATAR_ID_12);

        List<DataUsers> dataUsersList = given()
                .when()
                .get("https://reqres.in/api/users?page=2")
                .then()
                .statusCode(200)
                .log().body()
                .extract().body().jsonPath().getList("data", DataUsers.class);

        Assert.assertNotNull(dataUsersList);
        Assert.assertFalse(dataUsersList.isEmpty());
        Assert.assertEquals(6, dataUsersList.size());
        List<String> avatars = dataUsersList.stream().map(DataUsers::getAvatar).collect(Collectors.toList());
        Assert.assertEquals(avatarReferences, avatars);
    }


    @Test
    public void registerSuccessful() {
        Register register = new Register("eve.holt@reqres.in", "pistol");
        RegisterSuccess registerSuccess = given()
                .contentType("application/json")
                .body(register)
                .log().all()
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(200)
                .log().all()
                .extract().as(RegisterSuccess.class);

        Assert.assertNotNull(registerSuccess.getId());
        Assert.assertNotNull(registerSuccess.getToken());
        Assert.assertEquals(Integer.valueOf(4), registerSuccess.getId());
        Assert.assertEquals("QpwL5tke4Pnpja7X4", registerSuccess.getToken());
    }

    @Test
    public void registerUnSuccessful() {
        Register register = new Register("sydney@fife", null);
        RegisterUnSuccess registerUnSuccess = given()
                .contentType("application/json")
                .body(register)
                .log().all()
                .when()
                .post("https://reqres.in/api/register")
                .then()
                .statusCode(400)
                .log().all()
                .extract().as(RegisterUnSuccess.class);

        Assert.assertNotNull(registerUnSuccess.getError());
        Assert.assertEquals("Missing password", registerUnSuccess.getError());

    }

    @Test
    public void sortByYear() {

        List<DataResource> dataResourceList = given()
                .when()
                .get("https://reqres.in/api/unknown")
                .then()
                .statusCode(200)
                .log().body()
                .extract().body().jsonPath().getList("data", DataResource.class);

        Assert.assertFalse(dataResourceList.isEmpty());
        Assert.assertEquals(6, dataResourceList.size());

        List<Integer> years = dataResourceList.stream().map(DataResource::getYear).collect(Collectors.toList());
        List yearsSort = new ArrayList(years);
        Collections.sort(yearsSort);
        Assert.assertEquals(yearsSort, years);
    }


}
