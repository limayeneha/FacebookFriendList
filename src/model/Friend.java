package model;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: nehalimaye
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
