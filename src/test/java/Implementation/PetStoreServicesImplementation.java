package Implementation;

import Configs.TestConfigsForAPIPetStore;
import Utils.ApiPetStorePayloadGenerator;
import Utils.PetStoreMethods;
import io.cucumber.java.AfterStep;
import io.cucumber.java.BeforeStep;
import io.cucumber.java.en.*;
import io.restassured.path.json.JsonPath;
import io.restassured.path.xml.XmlPath;
import io.restassured.response.Response;
import org.testng.Assert;
import org.testng.asserts.SoftAssert;

public class PetStoreServicesImplementation extends PetStore {

    String addPetRequestBody=null;
    ApiPetStorePayloadGenerator objPayloadGenerator = new ApiPetStorePayloadGenerator();
    PetStoreMethods petMethods = new PetStoreMethods();
    Response addPetMethodResponse=null;
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

    @Given("user_provides_pet_information\\({string},{string},{string},{string},{string},{string},{string},{string},{string})")
    public void user_provides_pet_information(String PayloadType,String Pet_id,String Pet_Category_id,String Pet_Category_name,String Pet_name,String Pet_photoUrls_photoUrl,String Pet_tags_Tag_id,String Pet_tags_Tag_Name,String Pet_status) {
        String allPetData[]={PayloadType,Pet_id,Pet_Category_id,Pet_Category_name,Pet_name,Pet_photoUrls_photoUrl,Pet_tags_Tag_id,Pet_tags_Tag_Name,Pet_status};
        addPetRequestBody = objPayloadGenerator.getAdPetRequestBody(allPetData);
        //System.out.println("Request body to add new Pet is:"+addPetRequestBody);
    }

    @When("user_calls_addPet_Service\\({string},{string},{string},{string})")
    public void user_calls_add_pet_service(String hdr1, String hdr1Val, String hdr2, String hdr2Val)  {
        addPetMethodResponse = petMethods.addPetMethod(hdr1,hdr1Val,hdr2,hdr2Val,addPetRequestBody);
        //System.out.println("Add pet service response:"+addPetMethodResponse.asPrettyString());
    }

    @Then("addPet_service_returns_{int}")
    public void addPet_service_returns(int expStatusCode) {
        Assert.assertEquals(addPetMethodResponse.getStatusCode(),expStatusCode,"Incorrect status code");

    }

    @And("user_receives_petID_as_Confirmation_of_success\\({string},{string})")
    public void user_receives_pet_id_as_confirmation_of_success(String typeOfPayload, String expPetID) {
        String petIDGenerated="";
        if(typeOfPayload.equalsIgnoreCase("json")) {
            JsonPath jsonRespBody = addPetMethodResponse.jsonPath();
            petIDGenerated = jsonRespBody.getString("id");
            System.out.println("PetID generated:" + petIDGenerated);
        }
        else if(typeOfPayload.equalsIgnoreCase("xml")) {
            XmlPath path = addPetMethodResponse.xmlPath();
            petIDGenerated = path.getString("Pet/id/text()");
            System.out.println("PetID generated:" + petIDGenerated);
        }

        sftAssert.assertTrue(!petIDGenerated.isEmpty(),"Pet ID on response is null");
        sftAssert.assertTrue(petIDGenerated.contains(expPetID));
    }

    @Given("user_calls_findPetByID_Service\\({string},{string})")
    public void user_calls_find_pet_by_id_service(String responseContentType, String petIdToFind) {
        getPetByIdMethodResponse =petMethods.getPetData(petIdToFind);
        System.out.println("*******Get Pet By ID service response\n"+getPetByIdMethodResponse.asPrettyString());
    }

    @When("getPet_service_returns_{int}")
    public void getPet_service_returns(int expStatusCode) {
        sftAssert.assertEquals(getPetByIdMethodResponse.getStatusCode(),expStatusCode,"Incorrect status code");

    }
    @Then("pet_details_are_correctly_retrieved\\({string},{string},{string},{string},{string},{string},{string},{string},{string})")
    public void pet_details_are_correctly_retrieved(String PayloadType,String Pet_id,String Pet_Category_id,String Pet_Category_name,String Pet_name,String Pet_photoUrls_photoUrl,String Pet_tags_Tag_id,String Pet_tags_Tag_Name,String Pet_status) {
            //Add assertions to check the fetched details are as expected
        String actualPetID=getPetByIdMethodResponse.path("id").toString();
        String actualPetName=getPetByIdMethodResponse.path("name");
        String actualCategoryName=getPetByIdMethodResponse.path("category.name");
        System.out.println("Details fetched from get:"+actualCategoryName+":"+actualPetName);

        sftAssert.assertEquals(actualPetID,Pet_id,"Incorrect Pet ID");
        sftAssert.assertEquals(actualPetName,Pet_name,"Incorrect Pet Name");
        sftAssert.assertEquals(actualCategoryName,Pet_Category_name,"Incorrect Pet Category");
    }

    @Then("user_received_feedback_message\\({string},{string},{string})")
    public void user_received_feedback_message(String expCode, String expError, String expMessage) {
         String actCode= getPetByIdMethodResponse.path("code").toString();
        String actError= getPetByIdMethodResponse.path("type");
        String actMessage= getPetByIdMethodResponse.path("message");
        sftAssert.assertEquals(actCode,expCode);
        sftAssert.assertEquals(actError,expError);
        sftAssert.assertEquals(actMessage,expMessage);
    }

}//end class Implementation.AddPetServiceImplementation

