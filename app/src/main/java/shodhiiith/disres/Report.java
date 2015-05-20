package shodhiiith.disres;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.android.gms.maps.model.LatLng;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Entity;
import android.location.Criteria;
import android.location.LocationManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;


import org.apache.http.HttpEntity;
import org.apache.http.HttpHost;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.conn.params.ConnRoutePNames;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MIME;
import org.apache.http.entity.mime.MultipartEntityBuilder;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.impl.cookie.BasicClientCookie;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HTTP;
import org.apache.http.protocol.HttpContext;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import shodhiiith.disres.R;

public class Report extends Fragment {

    int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Button btnSelect, btnsubmit;
    ImageView ivImage;
    Bitmap bm;
    Spinner spinner;
    File f;
    String tempPath="";
    public Report() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.report, container, false);

        super.onCreate(savedInstanceState);

        spinner = (Spinner) rootView.findViewById(R.id.spinner);
// Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this.getActivity(),
                R.array.disasters, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        spinner.setAdapter(adapter);


        btnSelect = (Button) rootView.findViewById(R.id.btnSelectPhoto);
        btnSelect.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        btnsubmit = (Button) rootView.findViewById(R.id.btnreport);
        btnsubmit.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                submitReport();
            }
        });
        ivImage = (ImageView) rootView.findViewById(R.id.ivImage);
		return rootView;
	}

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
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

    public static String POST(String url, String type,String imageurl, String description, String lati, String longi){
        InputStream inputStream = null;
        String result = "";
        Log.d("Check Url = ", url);
        try {

            // 1. create HttpClient
            DefaultHttpClient httpclient = new DefaultHttpClient();

            // 2. make POST request to the given URL
            HttpPost httpPost = new HttpPost(url);

            MultipartEntityBuilder builder = MultipartEntityBuilder.create();
            builder.setCharset(MIME.UTF8_CHARSET);
           // builder.setMode(HttpMultipartMode.BROWSER_COMPATIBLE);
            builder.addTextBody("dis_type", type, ContentType.create("text/plain", MIME.UTF8_CHARSET));
            builder.addTextBody("description", description, ContentType.create("text/plain", MIME.UTF8_CHARSET));
            builder.addTextBody("latitude", lati, ContentType.create("text/plain", MIME.UTF8_CHARSET));
            builder.addTextBody("longitude", longi, ContentType.create("text/plain", MIME.UTF8_CHARSET));
            File imagefile = new File(imageurl);
            Log.d("Check imagefilename", imagefile.getName());
            builder.addBinaryBody("image", imagefile, ContentType.create("image/jpeg"), imagefile.getName());
            Log.d("fuck","1");
            HttpEntity temp = builder.build();
            Log.d("fuck","2");
            httpPost.setEntity(temp);
            Log.d("fuck","3");
            String csrfToken = SharedData.getCookie();
            String session = SharedData.getSessionid();
            // 7. Set some headers to inform server about the type of the content
            httpPost.setHeader("X-CSRFToken", csrfToken);
            String urlauth = SharedData.getAppUrl();
            urlauth = urlauth + "auth/";
            httpPost.setHeader("Referer", urlauth);
            httpPost.setHeader("Content-type", "multipart/form-data");
            httpPost.addHeader("Accept", "application/json");

            Log.d("fuck","4");

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

    private class HttpAsyncTaskReport extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {
            return POST(urls[0],urls[1], urls[2], urls[3], urls[4], urls[5]);
        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);
            responseReport(result);
        }
    }

    private void responseReport(String responsedata){

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
            Toast.makeText(getActivity().getApplicationContext(), "SOS successfully Sent", Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getActivity().getApplicationContext(), "Something went wrong , Check your network connectivity", Toast.LENGTH_LONG).show();
        }

    }


    public void submitReport(){
        String selected = spinner.getSelectedItem().toString();
        EditText obsdata = (EditText) getActivity().findViewById(R.id.obs);
        String observation = obsdata.getText().toString();
        String imgurl = "";
        if(!tempPath.equals("")){
            imgurl = tempPath;
            if(isConnected()){
                Toast.makeText(getActivity(), "Sending Report . . . " , Toast.LENGTH_LONG).show();
                String url = SharedData.getAppUrl();
                url = url + "observations/";
                String distype = disType(selected);
                Location location = getCurrentPosition();
                String lati = String.format("%.6f",location.getLatitude());
                String longi = String.format("%.6f",location.getLongitude());
                Toast.makeText(getActivity(), "Report Sent " , Toast.LENGTH_SHORT).show();
                // new HttpAsyncTaskReport().execute(url,distype,imgurl,observation, lati, longi);
            }
            else{
                Toast.makeText(getActivity(), "Check your Network connectivity and Try Again" , Toast.LENGTH_SHORT).show();

            }
        }
        else{
            Toast.makeText(getActivity(), "Please select Photo" , Toast.LENGTH_SHORT).show();
        }
        Log.v("check selected = ", selected);
        Log.v("check observation = ", observation);
        Log.v("check imgurl = ", imgurl);
    }

    private String disType(String inp){
        switch (inp){
            case "Earthquake": return "EQ";
            case "Flood": return "FL";
            case "LandSlide": return "LS";
            case "Fire": return "FI";
            case "Cyclone": return "CYC";
            case "Tsunami": return "TSU";
        }
        return "";
    }

    private void selectImage() {
        final CharSequence[] items = { "Take Photo", "Choose from Gallery",
                "Cancel" };

        AlertDialog.Builder builder = new AlertDialog.Builder(this.getActivity());
        builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                if (items[item].equals("Take Photo")) {
                    Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                    File f = new File(android.os.Environment
                            .getExternalStorageDirectory(), "temp.jpg");
                    intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(f));
                    startActivityForResult(intent, REQUEST_CAMERA);
                } else if (items[item].equals("Choose from Gallery")) {
                    Intent intent = new Intent(
                            Intent.ACTION_PICK,
                            android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                    intent.setType("image/*");
                    startActivityForResult(
                            Intent.createChooser(intent, "Select File"),
                            SELECT_FILE);
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == REQUEST_CAMERA) {
                f = new File(Environment.getExternalStorageDirectory()
                        .toString());
                for (File temp : f.listFiles()) {
                    if (temp.getName().equals("temp.jpg")) {
                        f = temp;
                        break;
                    }
                }
                try {

                    BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                    tempPath = f.getAbsolutePath();
                    bm = BitmapFactory.decodeFile(f.getAbsolutePath(),
                            btmapOptions);
                    Bitmap d = new BitmapDrawable(getActivity().getResources() , f.getAbsolutePath()).getBitmap();
                   // int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
                    double xFactor = 0;
                    double width = Double.valueOf(d.getWidth());
                    Log.v("check WIDTH", String.valueOf(width));
                    double height = Double.valueOf(d.getHeight());
                    Log.v("check height", String.valueOf(height));
                    if(width>height){
                        xFactor = 841/width;
                    }
                    else{
                        xFactor = 595/width;
                    }
                    int Nheight = (int) ((xFactor*height));
                    int NWidth =(int) (xFactor * width) ;
                    bm = Bitmap.createScaledBitmap( bm,NWidth, Nheight, true);
                    ivImage.setImageBitmap(bm);

                   /* String path = android.os.Environment
                            .getExternalStorageDirectory()
                            + File.separator
                            + "Phoenix" + File.separator + "default";
                    f.delete();
                    OutputStream fOut = null;
                    File file = new File(path, String.valueOf(System
                            .currentTimeMillis()) + ".jpg");
                    try {
                        fOut = new FileOutputStream(file);
                        bm.compress(Bitmap.CompressFormat.JPEG, 70, fOut);
                        fOut.flush();
                        fOut.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }*/
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else if (requestCode == SELECT_FILE) {
                Uri selectedImageUri = data.getData();

                tempPath = getPath(selectedImageUri, this.getActivity());
                Bitmap bm;
                BitmapFactory.Options btmapOptions = new BitmapFactory.Options();
                bm = BitmapFactory.decodeFile(tempPath, btmapOptions);
                Bitmap d = new BitmapDrawable(getActivity().getResources() , tempPath).getBitmap();
                // int nh = (int) ( d.getHeight() * (512.0 / d.getWidth()) );
                f = new File(selectedImageUri.getPath());
                double xFactor = 0;
                double width = Double.valueOf(d.getWidth());
                Log.v("check WIDTH", String.valueOf(width));
                double height = Double.valueOf(d.getHeight());
                Log.v("check height", String.valueOf(height));
                if(width>height){
                    xFactor = 841/width;
                }
                else{
                    xFactor = 595/width;
                }
                int Nheight = (int) ((xFactor*height));
                int NWidth =(int) (xFactor * width) ;
                bm = Bitmap.createScaledBitmap( bm,NWidth, Nheight, true);
                ivImage.setImageBitmap(bm);
            }
        }
    }

    public String getPath(Uri uri, Activity activity) {
        String[] projection = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity
                .managedQuery(uri, projection, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }
    private Location getCurrentPosition(){
        // location details
        LocationManager locationManager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
        return location;
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
