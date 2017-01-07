
package com.pkiykov.userpostspagersample.data.model;
import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pkiykov.userpostspagersample.GeosModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    "lat",
    "lng"
})
public class Geo implements GeosModel{
    @JsonIgnore
    private long _id;

    @JsonProperty("lat")
    private String lat;
    @JsonProperty("lng")
    private String lng;

    @JsonProperty("lat")
    public String getLat() {
        return lat;
    }

    @JsonProperty("lat")
    public void setLat(String lat) {
        this.lat = lat;
    }

    @JsonProperty("lng")
    public String getLng() {
        return lng;
    }

    @JsonProperty("lng")
    public void setLng(String lng) {
        this.lng = lng;
    }

    @Override
    public long _id() {
        return _id;
    }

    @Nullable
    @Override
    public String lat() {
        return lat;
    }

    @Nullable
    @Override
    public String lng() {
        return lng;
    }

    public Geo() {
    }

    public Geo(long _id, String lat, String lng) {
        this._id = _id;
        this.lat = lat;
        this.lng = lng;
    }



    public static final Factory FACTORY = new Factory<>(Geo::new);
}
