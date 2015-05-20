package shodhiiith.disres;

import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class SOS extends Fragment {

    public static final String cookie = "cookie";
    public static final String msg = "Emergency SOS";

	public SOS() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


		View rootView = inflater.inflate(R.layout.sos, container, false);
        Button btn = (Button) rootView.findViewById(R.id.button);
        btn.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isConnected()) {
                    earthquake_sos(v);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn2 = (Button) rootView.findViewById(R.id.button2);
        btn2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isConnected()) {
                    flood_sos(v);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn3 = (Button) rootView.findViewById(R.id.button3);
        btn3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isConnected()) {
                    landslide_sos(v);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn4 = (Button) rootView.findViewById(R.id.button4);
        btn4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isConnected()) {
                    fire_sos(v);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn5 = (Button) rootView.findViewById(R.id.button5);
        btn5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                if(isConnected()) {
                    tsunami_sos(v);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }
        });

        Button btn6 = (Button) rootView.findViewById(R.id.button6);
        btn6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                if (isConnected()) {
                    cyclone_sos(v);
                }
                else{
                    Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
                }
            }

        });
		return rootView;
	}




    private Location getCurrentPosition(){
        // location details
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        System.out.println(location);
       // return location;
        Log.d("check", location.toString());
        return location;
    }

    public void earthquake_sos(View v)
    {
        Toast.makeText(getActivity(), "Sending Earthquake SOS", Toast.LENGTH_SHORT).show();
        String url = SharedData.getAppUrl();
        url = url + "sos/";
        // call AsynTask to perform network operation on separate thread
        Location location = getCurrentPosition();
        Log.d("hey","hello");
        String lati = String.format("%.6f",location.getLatitude());
        String longi = String.format("%.6f",location.getLongitude());
        new HttpAsyncTask()
                .execute(url,"EQ", lati, longi);

    }
    public void fire_sos(View v)
    {

        Toast.makeText(getActivity(), "Fire SOS sent ", Toast.LENGTH_SHORT).show();
        String url = SharedData.getAppUrl();
        url = url + "sos/";
        // call AsynTask to perform network operation on separate thread
        Location location = getCurrentPosition();
        String lati = (String.format("%.6f",location.getLatitude()));
        String longi = String.format("%.6f",location.getLongitude());
        new HttpAsyncTask()
                .execute(url,"FI", lati, longi);
    }
    public void landslide_sos(View v)
    {
        Toast.makeText(getActivity(), "Landslide SOS sent ", Toast.LENGTH_SHORT).show();
        String url = SharedData.getAppUrl();
        url = url + "sos/";
        // call AsynTask to perform network operation on separate thread
        Location location = getCurrentPosition();
        String lati = (String.format("%.6f",location.getLatitude()));
        String longi = String.format("%.6f",location.getLongitude());
        new HttpAsyncTask()
                .execute(url,"LS", lati, longi);
    }
    public void cyclone_sos(View v)
    {
        Toast.makeText(getActivity(), "Cyclone SOS sent ", Toast.LENGTH_SHORT).show();
        String url = SharedData.getAppUrl();
        url = url + "sos/";
        Log.e("ds", "kkkkkkkkkkd");
        // call AsynTask to perform network operation on separate thread
        Location location = getCurrentPosition();
        Log.e("ds","sd");
        String lati = (String.format("%.6f",location.getLatitude()));
        String longi = String.format("%.6f",location.getLongitude());
        new HttpAsyncTask()
                .execute(url,"CYC", lati, longi);
    }
    public void tsunami_sos(View v)
    {
        Toast.makeText(getActivity(), "Tsunami SOS sent ", Toast.LENGTH_SHORT).show();
        String url = SharedData.getAppUrl();
        url = url + "sos/";
        // call AsynTask to perform network operation on separate thread
        Location location = getCurrentPosition();
        String lati = (String.format("%.6f",location.getLatitude()));
        String longi = String.format("%.6f",location.getLongitude());
        new HttpAsyncTask()
                .execute(url,"TSU", lati, longi);
    }
    public void flood_sos(View v)
    {
        Toast.makeText(getActivity(), "Flood SOS sent ", Toast.LENGTH_SHORT).show();
        String url = SharedData.getAppUrl();
        url = url + "sos/";
        // call AsynTask to perform network operation on separate thread
        Location location = getCurrentPosition();
        String lati = (String.format("%.6f",location.getLatitude()));
        String longi = String.format("%.6f",location.getLongitude());
        new HttpAsyncTask()
                .execute(url,"FL", lati, longi);
    }


    private void response(String responsedata){

        JSONObject json;
        JSONArray arr;
        JSONObject jObj = null;
        String status="";
        Log.d("Check Response = ",responsedata);
        try {
            jObj = new JSONObject(responsedata);
            status = jObj.getString("message");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equalsIgnoreCase("Emergency SOS")){
            Toast.makeText(getActivity().getApplicationContext(), "SOS successfully Sent", Toast.LENGTH_SHORT).show();
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private static String convertInputStreamToString(InputStream inputStream) throws IOException {
        BufferedReader bufferedReader = new BufferedReader( new InputStreamReader(inputStream));
        String line = "";
        String result = "";
        while((line = bufferedReader.readLine()) != null)
            result += line;

        inputStream.close();
        return result;

    }


    public static String POST(String url, String type, String lati, String longi){
        InputStream inputStream = null;
        String result = "";
        Log.d("Check Url = ", url);
        try {

            // 1. create HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            String json = "";

            // 3. build jsonObject
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("dis_type", type);
            jsonObject.put("message", msg);
            jsonObject.put("latitude", lati);
            jsonObject.put("longitude",longi);
            // 4. convert JSONObject to JSON to String
            json = jsonObject.toString();

            // ** Alternative way to convert Person object to JSON string usin Jackson Lib
            // ObjectMapper mapper = new ObjectMapper();
            // json = mapper.writeValueAsString(person);

            // 5. set json to StringEntity
            StringEntity se = new StringEntity(json, HTTP.UTF_8);
            // 6. set httpPost Entity
            httpPost.setEntity(se);
            String csrfToken = SharedData.getCookie();
            String session = SharedData.getSessionid();
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("X-CSRFToken", csrfToken);
            String urlauth = SharedData.getAppUrl();
            urlauth = urlauth + "auth/";
            httpPost.setHeader("Referer", urlauth);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");


            final BasicCookieStore cookieStore =  new BasicCookieStore();

            BasicClientCookie csrf_cookie = new BasicClientCookie("csrftoken", csrfToken);
            BasicClientCookie csrf_cookie1 = new BasicClientCookie("sessionid", session);
            urlauth = SharedData.getAppUrl();
            csrf_cookie.setDomain("disres.pythonanywhere.com");
            csrf_cookie1.setDomain("disres.pythonanywhere.com");
            cookieStore.addCookie(csrf_cookie);
            cookieStore.addCookie(csrf_cookie1);

            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);



            HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
           httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            // 8. Execute POST request to the given URL
            HttpResponse httpResponse = httpclient.execute(httpPost, localContext);

            // 9. receive response as inputStream
            inputStream = httpResponse.getEntity().getContent();

            // 10. convert inputstream to string
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";


            Log.d("Check Result = ",result);

        } catch (Exception e) {
            Log.d("Check InputStream", e.getLocalizedMessage());
        }

        // 11. return result
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0],urls[1], urls[2], urls[3]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            response(result);
        }
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getActivity().getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }



}
