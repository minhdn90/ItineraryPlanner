package com.lynden.gmapsfx.service.directions;

import com.lynden.gmapsfx.javascript.JavascriptObject;
import com.lynden.gmapsfx.javascript.object.GMapObjectType;
import java.time.LocalDateTime;
import java.time.ZoneId;

public class TransitOptions extends JavascriptObject{
    
    public TransitOptions(LocalDateTime departureTime){
    	super(GMapObjectType.TRANSIT_OPTIONS, convertToJavascriptString(departureTime));
    }
    
    private static String convertToJavascriptString(LocalDateTime departureTime) {
    	StringBuilder builder = new StringBuilder();
        builder.append("{");
        if (departureTime != null) {
            long milisSinceEpoch = departureTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            builder.append("departureTime: new Date(").append(milisSinceEpoch).append(")");
        }
        builder.append("}");
        return builder.toString();
    }
}
