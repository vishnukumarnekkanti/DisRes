package shodhiiith.disres;

import android.app.Application;

import com.google.android.gms.common.api.GoogleApiClient;

import org.apache.http.client.CookieStore;

/**
 * Created by rajat on 4/13/15.
 */
public class SharedData extends Application {

        private static String app_url;
        private static String cookie;
        private static String sessionid;
        private static CookieStore cookiestore ;
        private static GoogleApiClient mGoogleApiClient;
        @Override
        public void onCreate() {
            super.onCreate();
            app_url="http://disres.pythonanywhere.com/";
            cookie="";
            sessionid="";
        }

        public static String getCookie() {
            return cookie;
        }

        public static void setCookie(String cookie) {
            SharedData.cookie = cookie;
        }

        public static String getSessionid() {
            return sessionid;
        }

        public static void setSessionid(String sessionid) {
        SharedData.sessionid = sessionid;
    }

        public  static CookieStore getcookiestore() {
            return cookiestore;
        }

        public static  void setcookiestore(CookieStore cookieStore) {
            SharedData.cookiestore = cookiestore;
        }

        public static String getAppUrl() { app_url="http://disres.pythonanywhere.com/";
            return app_url;
        }

        public static String getCookieDomain() {
        return getAppUrl();
        }

        public static void setGoogleApiClient(GoogleApiClient ApiClient){
            mGoogleApiClient = ApiClient;
        }

        public static GoogleApiClient getGoogleApiClient(){
            return mGoogleApiClient;
        }

}