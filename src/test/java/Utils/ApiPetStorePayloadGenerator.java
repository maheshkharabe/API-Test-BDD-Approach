package Utils;

import Configs.TestConfigsForAPIPetStore;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;
import org.apache.velocity.app.VelocityEngine;

import java.io.StringWriter;
import java.util.HashMap;
import java.util.Map;

public class ApiPetStorePayloadGenerator {


    public String getAdPetRequestBody(String[] excelData){

        String strBody="";

        Map mpBody = new HashMap();
        mpBody.put("petID",excelData[1]);
        mpBody.put("petCategoryIdVal",excelData[2]);
        mpBody.put("petCategoryTypeVal",excelData[3]);
        mpBody.put("petNameVal",excelData[4]);
        mpBody.put("petPhotoUrl",excelData[5]);
        mpBody.put("petTagId",excelData[6]);
        mpBody.put("petTagName",excelData[7]);
        mpBody.put("petStatusVal",excelData[8]);

        //VelocityContext- implementation that allows for the storage and retrieval of data.
        VelocityContext vc = new VelocityContext();
        vc.put("PetBody",mpBody);

        VelocityEngine ve = new VelocityEngine();
        ve.init();

        //Load the template
        String pathToTemplate="";
        String typeOfPayload = excelData[0];//
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
    }//getAdPetRequestBody

}// end class
