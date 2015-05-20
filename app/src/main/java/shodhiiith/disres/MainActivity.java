package shodhiiith.disres;

import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import android.widget.EditText;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.widget.Toast;

import shodhiiith.disres.R;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.MapFragment;

import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.CookieStore;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.cookie.Cookie;
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

public class MainActivity extends Activity {


 
    // used to store app title
    private CharSequence mTitle;
    private EditText username,password,usernamereg,passwordreg,emailreg;
    public static final String MyPREFERENCES = "MyPrefs" ;
    public static final String name = "name";
    public static final String pass = "password";
    public static final String cookie = "cookie";
    public static final String namereg = "namereg";
    public static final String passreg = "passwordreg";
    public static final String emailregi = "emailregi";
    public static final String session = "session";
    SharedPreferences sharedpreferences;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.login);
        username = (EditText)findViewById(R.id.usernametxt);
        password = (EditText)findViewById(R.id.passwordtxt);
        usernamereg = (EditText)findViewById(R.id.usernametxtreg);
        passwordreg = (EditText)findViewById(R.id.passwordtxtreg);
        emailreg = (EditText)findViewById(R.id.emailtxtreg);

	}

    @Override
    protected void onResume() {
        sharedpreferences=getSharedPreferences(MyPREFERENCES,
                Context.MODE_PRIVATE);
        if (sharedpreferences.contains(cookie))
        {
                Intent i = new Intent(this,shodhiiith.disres.
                        Welcome.class);
                String csrfToken = sharedpreferences.getString(cookie, null);
                String sessionid = sharedpreferences.getString(session, null);
                SharedData.setCookie(csrfToken);
                SharedData.setSessionid(sessionid);
                if(isConnected()) {
                    new HttpAsyncGetOrgs().execute(SharedData.getAppUrl() + "organisations/");
                }
                startActivity(i);
                finish();
        }
        super.onResume();
    }

    public void login(View view){

        String u = username.getText().toString();
        String p = password.getText().toString();
        Editor editor = sharedpreferences.edit();
        editor.putString(name, u);
        editor.putString(pass, p);
        editor.commit();
        String url = SharedData.getAppUrl();
        url = url + "auth/";
        // call AsynTask to perform network operation on separate thread
        if(isConnected()) {
            new HttpAsyncTask()
                    .execute(url);
        }
        else
        {
            Toast.makeText(getBaseContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
        }

    }

    private void response(String responsedata){

        JSONObject json;
        JSONArray arr;
        JSONObject jObj = null;
        String status="";
        Log.d("Check Response = ",responsedata);
        try {
           // arr = new JSONArray(responsedata);
            jObj = new JSONObject(responsedata);
            status = jObj.getString("status");
            Log.d("Check status = ",status);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equalsIgnoreCase("invalid credentials")){
            Toast.makeText(getBaseContext(), "Invalid credentials, Try again", Toast.LENGTH_SHORT).show();
        }
        else if (status.equalsIgnoreCase("logged in")){
            Toast.makeText(getBaseContext(), "Successful Login", Toast.LENGTH_SHORT).show();
            Intent i = new Intent(this,shodhiiith.disres.
                    Welcome.class);
            String csrftoken  = SharedData.getCookie();
            String sessionid = SharedData.getSessionid();
            Editor editor = sharedpreferences.edit();
            editor.putString(cookie, csrftoken);
            editor.putString(session, sessionid);
            editor.commit();
            startActivity(i);
            finish();
        }
        else{
            Toast.makeText(getBaseContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
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
    public String POST(String url, String u, String p){
        InputStream inputStream = null;
        String result = "";
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", u);
            jsonObject.put("password", p);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
           httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
            HttpGet httpGet = new HttpGet(url);
            httpclient.execute(httpGet);
            String csrfToken = new String();
            String sessionid = new String();
            CookieStore cookieStore = httpclient.getCookieStore();
            SharedData.setcookiestore(cookieStore);
            List <Cookie> cookies =  cookieStore.getCookies();
            for (Cookie cookie: cookies) {
                if (cookie.getName().equals("csrftoken")) {
                    csrfToken = cookie.getValue();
                }
                else{
                    sessionid = cookie.getValue();
                }
                Log.d("Domains = " ,cookie.getDomain());
            }
            httpGet.setHeader("Referer", url);
            httpGet.setHeader("X-CSRFToken", csrfToken);
            SharedData.setCookie(csrfToken);
            SharedData.setSessionid(sessionid);
            new HttpAsyncGetOrgs().execute(SharedData.getAppUrl() + "organisations/");

        } catch (Exception e) {
            Log.d("Check InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private class HttpAsyncTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String u = sharedpreferences.getString(name, null);
            String p = sharedpreferences.getString(pass, null);

            return POST(urls[0],u,p);
        }
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            response(result);
        }
    }

    private class HttpAsyncRegisterTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            String regEmail = sharedpreferences.getString(emailregi, null);
            String regName = sharedpreferences.getString(namereg, null);
            String regPwd = sharedpreferences.getString(passreg, null);
            return postRegistrationData(urls[0], regName, regEmail, regPwd);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            responseReg(result);
        }
    }

    private void responseReg(String responsedata){

        JSONObject json;
        JSONArray arr;
        JSONObject jObj = null;
        String status="";
        Log.d("Check Response Reg = ",responsedata);
        try {
            // arr = new JSONArray(responsedata);
            jObj = new JSONObject(responsedata);
            status = jObj.getString("username");
            Log.d("Check username reg = ",status);

        } catch (JSONException e) {
            e.printStackTrace();
        }

        if (status.equalsIgnoreCase(sharedpreferences.getString(namereg,null))){
            Toast.makeText(getBaseContext(), "Registration Successful", Toast.LENGTH_SHORT).show();
            Editor editor = sharedpreferences.edit();
            editor.putString(name, sharedpreferences.getString(namereg,null));
            editor.putString(pass, sharedpreferences.getString(passreg,null));
            editor.commit();
            String url = SharedData.getAppUrl();
            url = url + "auth/";
            Toast.makeText(getBaseContext(), "Logging In .. ", Toast.LENGTH_SHORT).show();
            if(isConnected()) {
                new HttpAsyncTask()
                        .execute(url);
            }
            else {
                Toast.makeText(getBaseContext(), "No Network Connectivity", Toast.LENGTH_SHORT).show();
            }
        }
        else{
            Toast.makeText(getBaseContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_SHORT).show();
        }
    }


    public String postRegistrationData(String url, String regName, String regEmail, String regPwd){
        InputStream inputStream = null;
        String result = "";
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpPost httpPost = new HttpPost(url);
            String json = "";
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("username", regName);
            jsonObject.put("email", regEmail);
            jsonObject.put("password", regPwd);
            json = jsonObject.toString();
            StringEntity se = new StringEntity(json);
            httpPost.setEntity(se);
            httpPost.setHeader("Accept", "application/json");
            httpPost.setHeader("Content-type", "application/json");

            HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            HttpResponse httpResponse = httpclient.execute(httpPost);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
            //######################################################

        } catch (Exception e) {
            Log.d("Check InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    private void responseOrgList(String responsedata){
        if(!responsedata.equals("")) {
            Editor editor = sharedpreferences.edit();
            editor.putString("organisations", responsedata);
            editor.commit();
            Toast.makeText(getBaseContext(), "Updated Organisations ", Toast.LENGTH_SHORT).show();
        }
        else {
            //orgs available for offline search
        }
    }

    public class HttpAsyncGetOrgs extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return getOrgData(urls[0]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            responseOrgList(result);
        }
    }

    public String getOrgData(String url){
        InputStream inputStream = null;
        String result = "";
        Log.d("Check Url = ", url);
        try {
            DefaultHttpClient httpclient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            String csrfToken = SharedData.getCookie();
            String session = SharedData.getSessionid();
            httpGet.setHeader("X-CSRFToken", csrfToken);
            String urlauth = SharedData.getAppUrl();
            urlauth = urlauth + "auth/";
            httpGet.setHeader("Referer", urlauth);
            httpGet.setHeader("Accept", "application/json");
            httpGet.setHeader("Content-type", "application/json");
            final BasicCookieStore cookieStore =  new BasicCookieStore();
            BasicClientCookie csrf_cookie = new BasicClientCookie("csrftoken", csrfToken);
            BasicClientCookie csrf_cookie1 = new BasicClientCookie("sessionid", session);
            urlauth = SharedData.getAppUrl();
            csrf_cookie.setDomain(SharedData.getCookieDomain());
            csrf_cookie1.setDomain(SharedData.getCookieDomain());
            cookieStore.addCookie(csrf_cookie);
            cookieStore.addCookie(csrf_cookie1);
            HttpContext localContext = new BasicHttpContext();
            localContext.setAttribute(ClientContext.COOKIE_STORE, cookieStore);
            HttpHost proxy = new HttpHost("proxy.iiit.ac.in", 8080);
            httpclient.getParams().setParameter(ConnRoutePNames.DEFAULT_PROXY, proxy);
            HttpResponse httpResponse = httpclient.execute(httpGet, localContext);
            inputStream = httpResponse.getEntity().getContent();
            if(inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work!";
        } catch (Exception e) {
            Log.d("Check InputStream", e.getLocalizedMessage());
        }
        return result;
    }

    public boolean isConnected(){
        ConnectivityManager connMgr = (ConnectivityManager) getSystemService(Activity.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected())
            return true;
        else
            return false;
    }

    public void register(View view){

        String u = usernamereg.getText().toString();
        String e = emailreg.getText().toString();
        String p = passwordreg.getText().toString();
        Editor editor = sharedpreferences.edit();
        editor.putString(namereg, u);
        editor.putString(passreg, p);
        editor.putString(emailregi, e);
        editor.commit();
        String url = SharedData.getAppUrl();
        url = url + "users/";
        Toast.makeText(getBaseContext(), "Registering....", Toast.LENGTH_SHORT).show();

        // call AsynTask to perform network operation on separate thread
        if (isConnected()) {
            new HttpAsyncRegisterTask()
                    .execute(url);
        }
        else {
            Toast.makeText(getApplicationContext(),"No Network Connectivity", Toast.LENGTH_SHORT).show();
        }
    }


}
