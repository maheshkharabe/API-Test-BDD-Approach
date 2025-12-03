package Implementation;

import Configs.TestConfigsForAPIPetStore;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.*;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class GetPetServicesImplementation {

    PetStore objPetStore = new PetStore();
    String[] findPetDataReceived=null;
    String petIDToFind=null;
    Response getPetByIdMethodResponse=null;
    SoftAssert sftAssert=null;


    @But("Conditional_Environment_SetUp")
    public void Conditional_Environment_SetUp(){
        if(TestConfigsForAPIPetStore.ENV_FLAG.isEmpty()){
            TestConfigsForAPIPetStore.setConfigsForPetStore();
        }
    }// end Conditional_Environment_SetUp

    @BeforeStep
    public void setSftAssert(){
        sftAssert= new SoftAssert();
    }
    @AfterStep
    public void exeSoftAssert(){
        sftAssert.assertAll();
    }


    @Given("user_provides_pet_information\\({string},{string})")
    public void user_provides_pet_information(String uniqueTestID, String sheetName){
        findPetDataReceived = objPetStore.getExcelPetData(uniqueTestID,sheetName);
    }

    @Given("user_calls_findPetByID_Service")
    public void user_calls_findPetByID_Service() {
        petIDToFind = findPetDataReceived[6];//retrieve PetID from Excel
        getPetByIdMethodResponse =objPetStore.getPetWithPetIDMethod(petIDToFind);
        System.out.println("*******Get Pet By ID service response\n"+getPetByIdMethodResponse.asPrettyString());
    }

    @Then("getPet_service_returns_{int}")
    public void getPet_service_returns(int expStatusCode) {
        Assert.assertEquals(getPetByIdMethodResponse.getStatusCode(),expStatusCode,"Incorrect status code");
    }

    @Then("user_receives_pet_data_in_response_body")
    public void user_receives_pet_data_in_response_body() {
        //Add assertions to check the fetched details are as expected
        String actualPetID=getPetByIdMethodResponse.path("id").toString();
        String actualPetName=getPetByIdMethodResponse.path("name");
        String actualCategoryName=getPetByIdMethodResponse.path("category.name");

        sftAssert.assertEquals(actualPetID,findPetDataReceived[6],"Incorrect Pet ID");
        sftAssert.assertEquals(actualPetName,findPetDataReceived[9],"Incorrect Pet Name");
        sftAssert.assertEquals(actualCategoryName,findPetDataReceived[8],"Incorrect Pet Category");
    }

    @And("user_received_feedback_message_for_invalid_data\\({string},{string},{string})")
    public void user_received_feedback_message_for_invalid_data(String expCode, String expError, String expMessage) {
        String actCode= getPetByIdMethodResponse.path("code").toString();
        String actError= getPetByIdMethodResponse.path("type");
        String actMessage= getPetByIdMethodResponse.path("message");
        sftAssert.assertEquals(actCode,expCode);
        sftAssert.assertEquals(actError,expError);
        sftAssert.assertEquals(actMessage,expMessage);
    }

}//end class Implementation.AddPetServiceImplementation

