package io.square1.richtextlib.v2.parser.markers;

import java.util.HashMap;

/**
 * Created by roberto on 07/09/15.
 */
public class Marker extends HashMap<String,String> {

    private static String KEY_ID  = "KEY_ID";

    public Marker(String id){
        put(KEY_ID,id);
    }

    public final String getId(){
        return get(KEY_ID);
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;

        Marker marker = (Marker) o;

        return !(getId() != null ? !getId().equals(marker.getId()) : marker.getId() != null);

    }

    @Override
    public int hashCode() {
        int result = super.hashCode();
        result = 31 * result + (getId() != null ? getId().hashCode() : 0);
        return result;
    }
}
