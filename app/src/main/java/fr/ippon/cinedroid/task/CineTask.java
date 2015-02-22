package fr.ippon.cinedroid.task;

/**
 * Created by sfoubert on 21/09/2014.
 */

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import fr.ippon.cinedroid.CineActivity;
import fr.ippon.cinedroid.R;

/**
 * Async Task
 */
public class CineTask extends AsyncTask<String, String, JSONArray> {

    public static final String CINEJS_URL = "http://cinejs.herokuapp.com";
    public static final String CINEJS_LAST_RECOMMANDATIONS_JSON = CINEJS_URL + "/entry/listLastRecommandationsJSON";

    private CineActivity cineActivity;

    private ProgressDialog progressDialog;

    public CineTask(CineActivity cineActivity) {
        this.cineActivity = cineActivity;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();

        progressDialog = new ProgressDialog(cineActivity);
        progressDialog.setMessage("En attente...");
        progressDialog.setIndeterminate(false);
        progressDialog.setCancelable(true);
        progressDialog.show();
    }

    @Override
    protected JSONArray doInBackground(String[] params) {
        return listLastRecommandationsJSON();
    }

    public JSONArray listLastRecommandationsJSON() {
        JSONArray recommandationsArray = null;
        String json = "";
        try {

            // make web service connection
            HttpGet request = new HttpGet(CINEJS_LAST_RECOMMANDATIONS_JSON);
            request.setHeader("Accept", "application/json");
            request.setHeader("Content-type", "application/json");

            // Send request to service
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpResponse response = httpClient.execute(request);

            Log.d("WebInvoke", "Saving: " + response.getStatusLine().toString());
            // Get the status of web service
            BufferedReader rd = new BufferedReader(new InputStreamReader(
                    response.getEntity().getContent()));
            // print status in log
            String line = "";
            while ((line = rd.readLine()) != null) {
                json += line;
            }
            Log.d("JSON", "JSON : " + json);

            // try parse the string to a JSON object
            try {
                recommandationsArray = new JSONArray(json);
            } catch (JSONException e) {
                Log.e("JSON Parser", "Error parsing data " + e.toString());
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return recommandationsArray;
    }

    @Override
    protected void onPostExecute(JSONArray recommandationsArray) {
        progressDialog.dismiss();
        try {
            // Getting JSON Array
            Log.d("recommandationsArray", recommandationsArray.toString());


            List<String> itemList = new ArrayList<String>();

            for(int i = 0 ; i < recommandationsArray.length() ; i++){
                JSONObject recommandation = recommandationsArray.getJSONObject(i);
                JSONObject movie = recommandation.getJSONObject("movie");
                JSONObject user = recommandation.getJSONObject("user");

                StringBuilder strItem = new StringBuilder().append(user.getString("firstname")).append(" ").append(user.getString("name"))
                        .append(" recommande ").append(movie.getString("title")).append("\n");

                itemList.add(strItem.toString());
            }

            ArrayAdapter<String> adapter = new ArrayAdapter<String>(cineActivity, android.R.layout.simple_list_item_1, itemList) {
                /* (non-Javadoc)
                 * @see android.widget.ArrayAdapter#getView(int, android.view.View, android.view.ViewGroup)
                 */
                @Override
                public View getView(int position, View convertView, ViewGroup parent) {
                    // TODO Auto-generated method stub
                    TextView txt = new TextView(this.getContext());
                    //just change textview properties
                    txt.setTextColor(Color.BLACK);
                    txt.setTextSize(12);
                    txt.setText(this.getItem(position));
                    return txt;
                }
            };

            ListView listView = (ListView)cineActivity.findViewById(R.id.listView);
            listView.setAdapter(adapter);

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

}
