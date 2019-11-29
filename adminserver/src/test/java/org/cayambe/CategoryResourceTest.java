package org.cayambe;

import io.restassured.http.ContentType;
import io.restassured.path.json.JsonPath;
import io.restassured.response.Response;
import org.cayambe.model.Category;
import org.cayambe.model.TestCategoryObject;
import org.jboss.arquillian.container.test.api.RunAsClient;
import org.jboss.arquillian.junit.Arquillian;
import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.wildfly.swarm.arquillian.DefaultDeployment;

import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.fest.assertions.Assertions.assertThat;

/**
 * Der Aufbau ist ganz normal für einen Arquillian-Test. Zusätzlich gibt es noch die @DefaultDeployment Annotation. Sie stammt aus dem Thorntail-Dependencies
 * und erlaubt ein Deployment der Test-Klasse und aller Klassen, die im Package und in den Unterpackages der Testklasse liegen. Standard ist ein WAR-Deployment.
 * Wichtig ist, dass die Klasse auch richtig in einer Package-Struktur liegt. Die nötige Main für das Thorntail-Deployment wird dann  durch die Annotation hinzugefügt.
 * Für ein eigenes Deployment lässt man die Annotation wohl einfach weg. Thorntail liefert auch selbst einen Test-Container für Arquillian
 */
@RunWith(Arquillian.class)
@DefaultDeployment
// Ein Client Test
@RunAsClient
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class CategoryResourceTest {


    @Test
    public void aRetrieveAllCategories() throws Exception {
        // der Test nutzt RestAssured. Mit dem Framework lässt sich relativ leicht eine REST-Anfrage zusammenbauen und auswerten. Wichtig ist, dass die Datenbank im Hintergrund läuft
        Response reponse = when().get("/admin/category").then().extract().response();

        String jsonAsString = reponse.asString();
        List<Map<String, ?>> jsonAsList = JsonPath.from(jsonAsString).getList("");

        assertThat(jsonAsList.size()).isEqualTo(21);

        Map<String, ?> record1 = jsonAsList.get(0);
        assertThat(record1.get("id")).isEqualTo(1);
        assertThat(record1.get("parent")).isNull();
        assertThat(record1.get("name")).isEqualTo("Top");

    }


    @Test
    public void bRetrieveCategory() throws Exception {
        Response response = given()
                .pathParam("categoryId", 1014)
                .when()
                .get("/admin/category/{categoryId}")
                .then()
                .extract().response();

        // der json string wird direkt extrahiert
        String jsonAsString = response.asString();

        // im letzten Test wurde direkt das Json geprüft. Hier transformieren wir das Objekt und überprüfen dieses
        Category category = JsonPath.from(jsonAsString).getObject("", Category.class);

        assertThat(category.getId()).isEqualTo(1014);
        assertThat(category.getParent().getId()).isEqualTo(1011);
        assertThat(category.getName()).isEqualTo("Ford SUVs");
        assertThat(category.isVisible()).isEqualTo(Boolean.TRUE);

    }


    @Test
    public void cCreateCategory() throws Exception {

        Category bmwCategory = new Category();
        bmwCategory.setName("BMW");
        bmwCategory.setVisible(Boolean.TRUE);
        bmwCategory.setHeader("header");
        bmwCategory.setImagePath("n/a");
        bmwCategory.setParent(new TestCategoryObject(1009));

        // erzeuge einen POST request
        Response response = given()
                .contentType(ContentType.JSON)
                .body(bmwCategory)
                .when()
                .post("/admin/category");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(201);

        // Die Response des Services enthält die url zur neuen Location
        String locationUrl = response.getHeader("Location");
        Integer categoryId = Integer.valueOf(locationUrl.substring(locationUrl.lastIndexOf('/') + 1));


        response = when()
                .get("/admin/category")
                .then()
                .extract().response();

        String jsonAsString = response.asString();
        List<Map<String, ?>> jsonAsList = JsonPath.from(jsonAsString).getList("");

        assertThat(jsonAsList.size()).isEqualTo(22);

        response = given()
                .pathParam("categoryId", categoryId)
                .when()
                .get("/admin/category/{categoryId}")
                .then()
                .extract()
                .response();

        jsonAsString = response.asString();

        Category category = JsonPath.from(jsonAsString).getObject("", Category.class);
        assertThat(category.getId()).isEqualTo(categoryId);

    }


    @Test
    public void dFailToCreateCategoryFromNullName() throws Exception {

        Category badCategory = new Category();
        badCategory.setVisible(Boolean.TRUE);
        badCategory.setHeader("header");
        badCategory.setImagePath("n/a");
        badCategory.setParent(new TestCategoryObject(1009));

        Response response = given().
                contentType(ContentType.JSON)
                .body(badCategory)
                .when()
                .post("/admin/category");

        assertThat(response).isNotNull();
        assertThat(response.getStatusCode()).isEqualTo(400);

        response = when()
                .get("/admin/category")
                .then()
                .extract().response();

        String jsonAsString = response.asString();
        List<Map<String, ?>> jsonAsList = JsonPath.from(jsonAsString).getList("");

        assertThat(jsonAsList.size()).isEqualTo(22);

    }
}