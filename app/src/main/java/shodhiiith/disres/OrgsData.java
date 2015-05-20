package shodhiiith.disres;

/**
 * Created by rajat on 4/14/15.
 */
import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class OrgsData {
    public long id;
    public String org_name;
    public String org_type;
    public String mobile;

    public OrgsData() {

    }

    public List<OrgsData> savedOrgList(String json){
        GsonBuilder gsonBuilder = new GsonBuilder();
        Gson gson = gsonBuilder.create();
        List<OrgsData> orgs = new ArrayList<OrgsData>();
        orgs = Arrays.asList(gson.fromJson(json, OrgsData[].class));
        return orgs;
    }
}