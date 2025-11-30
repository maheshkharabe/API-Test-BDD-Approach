/********************************************************************************************
 Exploring API testing using RestAssured library and TestNG framework.
 There are few service exposed on https://petstore.swagger.io/ which can be used for hands on
 *********************************************************************************************/
package Utils;
import Configs.TestConfigsForAPIPetStore;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;

import static io.restassured.RestAssured.given;


public class PetStoreMethods {

    ApiPetStorePayloadGenerator objPayloadGenerator = new ApiPetStorePayloadGenerator();


    public Response addPetMethod(String hrdAccept,String hrdAcceptVal, String hrdContentType,String hrdContentTypeVal,String requestBody){

        String addPetEndPointURL = TestConfigsForAPIPetStore.BASE_URL + TestConfigsForAPIPetStore.ADD_PET_PATH ;
        System.out.println("***** Hitting add pet URL:"+addPetEndPointURL);
        System.out.println("*** Headers Received"+hrdAccept+":"+hrdAcceptVal+":"+hrdContentType+":"+hrdContentTypeVal);
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .addHeader(hrdAccept,hrdAcceptVal)
                .addHeader(hrdContentType,hrdContentTypeVal)
                .build();

        Response response=
                    given()
                        .spec(reqSpec)
                        .body(requestBody)
                    .when()
                        .post(addPetEndPointURL)
                    .then()
                        .log().all()
                        .extract().response();

        //System.out.println("Response received from -Add Pet- service:"+response.prettyPrint());

        return response;

    }//end addPetEndPoint

    public Response getPetData(String id){

        String getPetEndPointURL = TestConfigsForAPIPetStore.BASE_URL + TestConfigsForAPIPetStore.GET_PET_PATH +"{petId}" ;
        System.out.println("****** Hitting Get Pet ByID:"+getPetEndPointURL);
        Response response=
                given()
                    .pathParam("petId",id)
                .when()
                    .get(getPetEndPointURL)
                .then()
                    .log().all()
                    .extract().response();

        return response;
    }//end getPetData

}//end ExploreAPIsFromReqRes
