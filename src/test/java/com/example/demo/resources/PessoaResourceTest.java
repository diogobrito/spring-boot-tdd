package com.example.demo.resources;

import com.example.demo.DemoApplicationTests;
import org.apache.http.HttpStatus;
import org.hamcrest.CoreMatchers;
import org.junit.Test;

import static io.restassured.RestAssured.given;

public class PessoaResourceTest extends DemoApplicationTests {

    @Test
    public void deve_procurar_pessoa_pelo_ddd_e_numero_de_telefone() {

        given()
            .pathParam("ddd", "86")
            .pathParam("numero", "35006330")
        .get("/pessoas/{ddd}/{numero}")
        .then()
            .log().body().and()
            .statusCode(HttpStatus.SC_OK)
            .body("codigo", CoreMatchers.equalTo(3),
                    "nome", CoreMatchers.equalTo("Cahuê"),
                    "cpf", CoreMatchers.equalTo("38767897100"));
    }
}
