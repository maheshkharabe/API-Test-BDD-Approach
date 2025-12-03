package Implementation;

import Configs.TestConfigsForAPIPetStore;
import Utils.DateTimeUtils;
import Utils.ExcelDataManager;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;

public class PetStore{

    String[] dataSet = null;
    ExcelDataManager objExcelDataManager = new ExcelDataManager();

    public String[] getExcelPetData(String uniqueTestID,String sheetName){
        dataSet = objExcelDataManager.getDataFromExcel(uniqueTestID,sheetName);
        /*for (int i=0;i<dataSet.length;i++){
            System.out.println("Data:"+dataSet[i]);
        }*/

        return dataSet;
    }//end getExcelPetData

    public Response addPetMethod(String[] addPetData, String requestBody){

        String addPetEndPointURL = TestConfigsForAPIPetStore.BASE_URL + TestConfigsForAPIPetStore.ADD_PET_PATH ;
        System.out.println("***** Hitting add pet URL:"+addPetEndPointURL);
        RequestSpecification reqSpec = new RequestSpecBuilder()
                .addHeader(addPetData[1],addPetData[2])
                .addHeader(addPetData[3],addPetData[4])
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

    public Response getPetWithPetIDMethod(String id){

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

    public String generateAddPetRequestBody(String[] excelData){

       String strBody="";

        Map mpBody = new HashMap();
        mpBody.put("petID",excelData[6]);
        mpBody.put("petCategoryIdVal",excelData[7]);
        mpBody.put("petCategoryTypeVal",excelData[8]);
        mpBody.put("petNameVal",excelData[9]);
        mpBody.put("petPhotoUrl",excelData[10]);
        mpBody.put("petTagId",excelData[11]);
        mpBody.put("petTagName",excelData[12]);
        mpBody.put("petStatusVal",excelData[13]);

        //VelocityContext- implementation that allows for the storage and retrieval of data.
        VelocityContext vc = new VelocityContext();
        vc.put("PetBody",mpBody);

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        //Load the template
        String pathToTemplate="";
        String typeOfPayload = excelData[5];
        if(typeOfPayload.equalsIgnoreCase("XML")){
            pathToTemplate = TestConfigsForAPIPetStore.pathAddPetRequestBodyXML;
        }else if(typeOfPayload.equalsIgnoreCase("JSON")){
            pathToTemplate = TestConfigsForAPIPetStore.pathAddPetRequestBodyJson;
        }else{
            System.out.println("********* Please provide payload type either XML or JSON");
        }
        Template vt = ve.getTemplate(pathToTemplate);

        StringWriter writer = new StringWriter(); //write strings to an in-memory string buffer
        vt.merge(vc,writer);

        strBody = writer.toString();

        System.out.println("********* Add Pet-Request Payload Generated:\n"+strBody);

        return strBody;
    }//adPetRequestBody


}// end class PetSore

