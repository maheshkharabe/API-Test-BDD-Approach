Feature: Add Pet to store

  Background: conditional_init
    But Conditional_Environment_SetUp

  Scenario Outline: Add new pet to store's catalog with valid pet details in Json/XML format

    Given user_provides_pet_information('<PayloadType>','<Pet_id>','<Pet_Category_id>','<Pet_Category_name>','<Pet_name>','<Pet_photoUrls_photoUrl>','<Pet_tags_Tag_id>','<Pet_tags_Tag_Name>','<Pet_status>')
    When user_calls_addPet_Service('<HRD_ACCEPT_NAME>','<HRD_ACCEPT_VALUE>','<HRD_CONTENTTYPE_NAME>','<HRD_CONTENTTYPE_VALUE>')
    Then addPet_service_returns_<ExpectedStatusCode>
    And user_receives_petID_as_Confirmation_of_success('<PayloadType>','<Pet_id>')

    Examples:
      |TestName          |HRD_ACCEPT_NAME|HRD_ACCEPT_VALUE|HRD_CONTENTTYPE_NAME|HRD_CONTENTTYPE_VALUE|PayloadType|Pet_id|Pet_Category_id|Pet_Category_name|Pet_name|Pet_photoUrls_photoUrl|Pet_tags_Tag_id|Pet_tags_Tag_Name|Pet_status|ExpectedStatusCode|
      |TC_01_Json_Body   |	accept	     |application/json|	Content-Type       |	application/json |	Json     | 100  |	1           |	Dog1          |MyDog2  |	MyDogPicURL3      |	1             |	Domestic1       |available |200               |
      |TC_02_XML_Body    |	accept	     |application/xml |	Content-Type       |	application/xml  |	XML      | 500  |	2           |	Cat1          |MyCat1  |	MyCatPicURL1      |	1             |	Domestic1       |available |200               |

  Scenario Outline: Check Pet doesn't get added to store's catalog when service is invoked with invalid details

    Given user_provides_pet_information('<PayloadType>','<Pet_id>','<Pet_Category_id>','<Pet_Category_name>','<Pet_name>','<Pet_photoUrls_photoUrl>','<Pet_tags_Tag_id>','<Pet_tags_Tag_Name>','<Pet_status>')
    When user_calls_addPet_Service('<HRD_ACCEPT_NAME>','<HRD_ACCEPT_VALUE>','<HRD_CONTENTTYPE_NAME>','<HRD_CONTENTTYPE_VALUE>')
    Then addPet_service_returns_<ExpectedStatusCode>

    #### Sample Negative cases
    ### Test missing headers,incorrect header details; schema validation- mandatory tags, edge cases
    ### Pet status is enum [ available, pending, sold ], anything else should result in 405-Invalid input etc.
    Examples:
      |TestName                       |HRD_ACCEPT_NAME|HRD_ACCEPT_VALUE|HRD_CONTENTTYPE_NAME|HRD_CONTENTTYPE_VALUE|PayloadType|Pet_id|Pet_Category_id|Pet_Category_name|Pet_name|Pet_photoUrls_photoUrl|Pet_tags_Tag_id|Pet_tags_Tag_Name|Pet_status|ExpectedStatusCode|
      |TC_01_Json_Body_MissingHeader  |	     	     |                |	Content-Type       |	application/json |	Json     | 100  |	1           |	Dog1          |MyDog2  |	MyDogPicURL3      |	1             |	Domestic1       |available  |400               |
      |TC_02_Json_Body_InvalidStatus  |	accept	     |application/json|	Content-Type       |	application/json |	Json     | 100  |	1           |	Dog1          |MyDog2  |	MyDogPicURL3      |	1             |	Domestic1       |hold       |405               |

