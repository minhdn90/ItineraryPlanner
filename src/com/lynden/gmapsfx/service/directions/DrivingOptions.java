/*
 * Copyright 2015 Andre.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.lynden.gmapsfx.service.directions;

import com.lynden.gmapsfx.javascript.JavascriptObject;
import com.lynden.gmapsfx.javascript.object.GMapObjectType;
import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 *
 * @author Andre
 */
public class DrivingOptions extends JavascriptObject{
    
    public DrivingOptions(LocalDateTime departureTime){
        this(departureTime, TrafficModel.BEST_GUESS);
    }
    
    private static String convertToJavascriptString(LocalDateTime departureTime, TrafficModel trafficModel) {
    	StringBuilder builder = new StringBuilder();
    	boolean something = false;
        builder.append("{");
        if (departureTime != null) {
            long milisSinceEpoch = departureTime.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            builder.append("departureTime: new Date(").append(milisSinceEpoch).append(")");
            something = true;
        }
        if(trafficModel != null) {
        	builder.append(something ? ", " : "");
        	builder.append("trafficModel: ").append("google.maps.TrafficModel.").append(TrafficModel.BEST_GUESS.toString());
        }
        builder.append("}");
        return builder.toString();
    }
    
    public DrivingOptions(LocalDateTime departureTime, TrafficModel trafficModel){
        super(GMapObjectType.DRIVING_OPTIONS, convertToJavascriptString(departureTime, trafficModel)); 
    }
        
}
