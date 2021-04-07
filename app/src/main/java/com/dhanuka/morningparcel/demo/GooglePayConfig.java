package com.dhanuka.morningparcel.demo;

import org.json.JSONObject;

public class GooglePayConfig {

    public JSONObject getTokenizationSpecification(){
        JSONObject jsonObject=new JSONObject();
        JSONObject jsonObject1=new JSONObject();
        JSONObject jsonObject2=new JSONObject();
try {
    jsonObject1.put("type","PAYMENT_GATEWAY");
    jsonObject2.put("gateway","stripe");
    jsonObject2.put("stripe:version","2018-10-31");
    jsonObject2.put("stripe:publishableKey","pk_test_EbCpH358aTH3p8TSFGJf1aCe00VuQ0Q8qB");
    jsonObject1.put("parameters",jsonObject2);

    jsonObject.put("tokenizationSpecification", jsonObject1);

}catch (Exception e){
    e.printStackTrace();
}
       return jsonObject;
    }


}
