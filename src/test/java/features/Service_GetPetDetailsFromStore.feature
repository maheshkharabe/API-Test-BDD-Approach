Feature: Get Pet details from store

  Background: conditional_init

    But Conditional_Environment_SetUp

  Scenario Outline: Retrieve pet details from store with VALID data

    Given user_calls_findPetByID_Service('<PayloadType>','<Pet_id>')
    When getPet_service_returns_<ExpectedStatusCode>
    Then pet_details_are_correctly_retrieved('<PayloadType>','<Pet_id>','<Pet_Category_id>','<Pet_Category_name>','<Pet_name>','<Pet_photoUrls_photoUrl>','<Pet_tags_Tag_id>','<Pet_tags_Tag_Name>','<Pet_status>')

    Examples:
      |TestName           |PayloadType|Pet_id|Pet_Category_id|Pet_Category_name|Pet_name|Pet_photoUrls_photoUrl|Pet_tags_Tag_id|Pet_tags_Tag_Name|Pet_status|ExpectedStatusCode|
      |TC_01_Json_PetFound|	Json      | 100  |	1            |	Dog1           |MyDog2  |	MyDogPicURL3       |	1          |	Domestic1    |available |200               |

  Scenario Outline: Retrieve pet details from store with InValid data

    Given user_calls_findPetByID_Service('<PayloadType>','<Pet_id>')
    Then getPet_service_returns_<ExpectedStatusCode>
    And user_received_feedback_message('<stdCode>','<stdType>','<stdMessage>')

    Examples:
      |TestName           |PayloadType|Pet_id|ExpectedStatusCode|stdCode|stdType|stdMessage   |
      |TC_01_Json_NotFound|	Json      | 888  |404               |1      |error  |Pet not found|
