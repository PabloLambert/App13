package com.lambertsoft.app13;

import com.google.api.client.json.GenericJson;
import com.google.api.client.util.Key;
import com.kinvey.java.model.KinveyMetaData;

public class Students extends GenericJson { // For Serialization

    @Key("_id")
    private String id;
    @Key
    private String first_name;
    @Key
    private String last_name;
    @Key
    private String email;
    @Key
    private String photo;
    @Key
    private String id_user;
    @Key
    private String institution;
    @Key("_kmd")
    private KinveyMetaData meta;
    @Key("_acl")
    private KinveyMetaData.AccessControlList acl;

    public Students(){}  //GenericJson classes must have a public empty constructor


    public void setData(String _first_name, String _last_name, String _id_user) {
        first_name = _first_name;
        last_name = _last_name;
        id_user = _id_user;
    }

    public String getFirst_name() {
        return first_name;
    }

    public String getId() { return id;}
}
