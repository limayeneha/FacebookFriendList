package com.example.FacebookFriendList;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.AsyncTaskLoader;
import android.support.v4.content.Loader;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.facebook.widget.ProfilePictureView;
import model.Friend;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: nehalimaye
 * Date: 10/28/13
 * Time: 11:22 PM
 * To change this template use File | Settings | File Templates.
 */
public class SelectionFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<Friend>> {

    private static final String TAG = "SelectionFragment";
    String accessToken = "";
    ListView listView;

    @Override
    public View onCreateView(LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        setRetainInstance(true);
        View view = inflater.inflate(R.layout.selection,
                container, false);
        listView = (ListView) view.findViewById(R.id.list);
        return view;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    //To be called to update friends in the list
    public void loadFriends() {
        if (accessToken != null && accessToken.length() > 0) {
            Bundle params = new Bundle();
            params.putString("access_token", accessToken);
            getLoaderManager().initLoader(0, params, this).forceLoad();
        }

    }

    public Loader<List<Friend>> onCreateLoader(int id, Bundle args) {
        FriendsListLoader friendsListLoader = new FriendsListLoader(getActivity(), args.getString("access_token"));
        return friendsListLoader;

    }

    public void onLoadFinished(Loader<List<Friend>> loader, List<Friend> data) {
        // Swap the new cursor in.  (The framework will take care of closing the
        // old cursor once we return.)
        FBFriendsArrayAdapter fbFriendsArrayAdapter = new FBFriendsArrayAdapter(getActivity(), data);
        listView.setAdapter(fbFriendsArrayAdapter);
    }

    public void onLoaderReset(Loader<List<Friend>> loader) {
        // This is called when the last Cursor provided to onLoadFinished()
        // above is about to be closed.  We need to make sure we are no
        // longer using it.
        if (listView != null) listView.setAdapter(null);
    }

    private static class FriendsListLoader extends AsyncTaskLoader<List<Friend>> {
        String access_token;

        public FriendsListLoader(Context context, String accessToken) {
            super(context);
            this.access_token = accessToken;
        }

        @Override
        public List<Friend> loadInBackground() {

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
                return Friend.friendsFromJSON(new JSONObject(result), "data");
            } catch (Exception e) {
                Log.e("FFL", "HttpClientError: " + e);
            }
            return null;
        }

    }

    public class FBFriendsArrayAdapter extends ArrayAdapter<Friend> {
        private final Context context;
        private final List<Friend> values;

        public FBFriendsArrayAdapter(Context context, List<Friend> values) {
            super(context, R.layout.list_row_layout, values);
            this.context = context;
            this.values = values;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            LayoutInflater inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View rowView = inflater.inflate(R.layout.list_row_layout, parent, false);
            TextView textView = (TextView) rowView.findViewById(R.id.name);
            ProfilePictureView imageView = (ProfilePictureView) rowView.findViewById(R.id.home_profile_img);
            textView.setText(values.get(position).getName());
            imageView.setProfileId(String.valueOf(values.get(position).getId()));

            return rowView;
        }
    }
}

