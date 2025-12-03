package Implementation;

import Utils.DateTimeUtils;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.And;
import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;


public class AddPetServiceImplementation {

    PetStore objPetStore = new PetStore();
    DateTimeUtils objDateTimeUtil = new DateTimeUtils();
    String [] addPetDataReceived=null;
    String peIDUsedForAddPetService=null;
    String addPetRequestBody=null;
    Response addPathMethodResponse=null;
    SoftAssert sftAssert=null;

    @BeforeStep
    public void setSftAssert(){
        sftAssert= new SoftAssert();
    }
    @AfterStep
    public void exeSoftAssert(){
        sftAssert.assertAll();
    }

    @Given("user_provides_information\\({string},{string})")
    public void user_provides_information(String uniqueTestID,String sheetName){
        addPetDataReceived = objPetStore.getExcelPetData(uniqueTestID,sheetName);
    }

    @When("user_calls_addPet_Service")
    public void user_calls_add_pet_service() {
        peIDUsedForAddPetService = addPetDataReceived[6];//retrieve PetID from Excel
        if (peIDUsedForAddPetService.toUpperCase().equals("FALSE")){//If petID val is FALSE in Excel, create new PetID
            peIDUsedForAddPetService=objDateTimeUtil.currentDate_yyyyMMdd+objDateTimeUtil.currentTime_hhMMssSSS;
        }
        addPetDataReceived[6] = peIDUsedForAddPetService;
        addPetRequestBody= objPetStore.generateAddPetRequestBody(addPetDataReceived);
        addPathMethodResponse = objPetStore.addPetMethod(addPetDataReceived,addPetRequestBody);
    }
    @Then("addPet_service_returns_code_{int}")
    public void addPet_service_returns_code_(int expStatusCode) {
        Assert.assertEquals(addPathMethodResponse.getStatusCode(),expStatusCode,"Incorrect status code");
    }

    @And("addPet_service_returns_response_body")
    public void validateAddPetResponseBody(){
        String typeOfPayload = addPetDataReceived[5];//read Payload Type
        String petIdInResponseBody="";
        if(typeOfPayload.equalsIgnoreCase("json")) {
            JsonPath jsonRespBody = addPathMethodResponse.jsonPath();
            petIdInResponseBody = jsonRespBody.getString("id");
            System.out.println("PetID from response body:" + petIdInResponseBody);
        }
        else if(typeOfPayload.equalsIgnoreCase("xml")) {
            XmlPath path = addPathMethodResponse.xmlPath();
            petIdInResponseBody = path.getString("Pet/id/text()");
            System.out.println("PetID from response body:" + petIdInResponseBody);
        }

        sftAssert.assertTrue(!petIdInResponseBody.isEmpty(),"Pet ID on response is null");
        sftAssert.assertTrue(petIdInResponseBody.contains(peIDUsedForAddPetService));

    }

}//end class
