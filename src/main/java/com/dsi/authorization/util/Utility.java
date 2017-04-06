package com.dsi.authorization.util;

import com.dsi.authorization.exception.CustomException;
import com.dsi.authorization.exception.ErrorContext;
import com.dsi.authorization.exception.ErrorMessage;
import com.dsi.authorization.model.User;
import org.apache.log4j.Logger;
import org.codehaus.jettison.json.JSONException;
import org.codehaus.jettison.json.JSONObject;

import java.util.Base64;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Created by sabbir on 6/13/16.
 */
public class Utility {

    private static final Logger logger = Logger.getLogger(Utility.class);

    public static boolean isNullOrEmpty(String s){

        if(s==null ||s.isEmpty() ){
            return true;
        }
        return false;
    }

    public static boolean isNullOrEmpty(List list){

        if(list==null || list.size() == 0 ){
            return true;
        }
        return false;
    }

    public static final Date today() {
        return new Date();
    }

    public static final String validation(JSONObject requestObj, String str) throws CustomException {

        ErrorContext errorContext;
        try {
            if (requestObj.has(str)) {
                return requestObj.getString(str);

            } else {
                errorContext = new ErrorContext(str, null, "Params: '"+ str +"' are missing.");
            }
        } catch (Exception e){
            errorContext = new ErrorContext(str, null, e.getMessage());
        }
        ErrorMessage errorMessage = new ErrorMessage(Constants.AUTHORIZATION_SERVICE_0008,
                Constants.AUTHORIZATION_SERVICE_0008_DESCRIPTION, errorContext);
        throw new CustomException(errorMessage);
    }

    public static final String getLoginObject(User user) throws JSONException {
        JSONObject loginObject = new JSONObject();
        loginObject.put("firstName", user.getFirstName());
        loginObject.put("lastName", user.getLastName());
        loginObject.put("email", user.getEmail());
        loginObject.put("createdBy", user.getCreateBy());
        loginObject.put("modifiedBy", user.getModifiedBy());
        loginObject.put("userId", user.getUserId());
        loginObject.put("version", 1);

        return loginObject.toString();
    }
}
