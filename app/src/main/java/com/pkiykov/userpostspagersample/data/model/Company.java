
package com.pkiykov.userpostspagersample.data.model;

import android.support.annotation.Nullable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;
import com.pkiykov.userpostspagersample.CompaniesModel;
import com.pkiykov.userpostspagersample.GeosModel;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
        "name",
        "catchPhrase",
        "bs"
})
public class Company implements CompaniesModel{
    @JsonIgnore
    private long _id;

    @JsonProperty("name")
    private String name;
    @JsonProperty("catchPhrase")
    private String catchPhrase;
    @JsonProperty("bs")
    private String bs;

    @JsonProperty("name")
    public String getName() {
        return name;
    }

    @JsonProperty("name")
    public void setName(String name) {
        this.name = name;
    }

    @JsonProperty("catchPhrase")
    public String getCatchPhrase() {
        return catchPhrase;
    }

    @JsonProperty("catchPhrase")
    public void setCatchPhrase(String catchPhrase) {
        this.catchPhrase = catchPhrase;
    }

    @JsonProperty("bs")
    public String getBs() {
        return bs;
    }

    @JsonProperty("bs")
    public void setBs(String bs) {
        this.bs = bs;
    }



   // public static final Factory FACTORY = new Factory<>(Company::new);

    public Company(long _id, String name, String catchPhrase, String bs) {
        this._id = _id;
        this.name = name;
        this.catchPhrase = catchPhrase;
        this.bs = bs;
    }

    @Override
    public long _id() {
        return _id;
    }

    @Nullable
    @Override
    public String name() {
        return name;
    }

    @Nullable
    @Override
    public String catchPhrase() {
        return catchPhrase;
    }

    @Nullable
    @Override
    public String bs() {
        return bs;
    }

    public Company() {
    }
}
