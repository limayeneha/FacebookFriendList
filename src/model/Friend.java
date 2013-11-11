package model;

import android.content.Context;
import android.util.Log;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: rahulnd
 * Date: 10/30/13
 * Time: 7:03 PM
 * To change this template use File | Settings | File Templates.
 */
public class Friend {

    public Long id;

    public String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public static List<Friend> getFriends(final Context context, final String access_token) {

        String result = null;
        try {
            // Create http cliient object to send request to server
            HttpClient Client = new DefaultHttpClient();

            // Create URL string
            String URL = "https://graph.facebook.com/me/friends?" + "access_token=" + access_token;

            try {
                // Create Request to server and get response
                HttpGet httpget = new HttpGet(URL);
                ResponseHandler<String> responseHandler = new BasicResponseHandler();
                result = Client.execute(httpget, responseHandler);
            } catch (Exception ex) {
                Log.e("Friend", "HttpGetError: " + ex);
            }

            if (result != null && result.length() > 0) ;
            return friendsFromJSON(new JSONObject(result), "data");
        } catch (Exception e) {
            Log.e("FFL", "HttpClientError: " + e);
        }
        return null;
    }

    public static ArrayList<Friend> friendsFromJSON(final JSONObject jo, final String nodeName) throws JSONException {
        final ArrayList<Friend> friends = new ArrayList<Friend>();

        if (jo.has(nodeName)) {
            final JSONArray appArray = jo.getJSONArray("data");
            final int rlen = appArray.length();
            for (int i = 0; i < rlen; i++) {
                Friend friend = new Friend();
                final JSONObject obj = appArray.getJSONObject(i);
                if (obj.has("name")) friend.setName(obj.getString("name"));
                if (obj.has("id")) friend.setId(obj.getLong("id"));
                friends.add(friend);
            }
        }
        return friends;
    }
}
