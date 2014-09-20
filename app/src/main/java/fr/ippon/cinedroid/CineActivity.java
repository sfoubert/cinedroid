package fr.ippon.cinedroid;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;


public class CineActivity extends Activity {

    private ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_cine);

        // Call Async webService
        new CineTask().execute();

//        ListView listView = (ListView) findViewById(R.id.listView);
//
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, itemList);
//        listView.setAdapter(adapter);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.cine, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Async Task
     */
    private class CineTask extends AsyncTask<String, String, JSONArray> {

        public static final String CINEJS_URL = "http://cinejs.herokuapp.com";
        public static final String CINEJS_LAST_RECOMMANDATIONS_JSON = CINEJS_URL + "/entry/listLastRecommandationsJSON";

        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            listView = (ListView)findViewById(R.id.listView);
            progressDialog = new ProgressDialog(CineActivity.this);
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

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(CineActivity.this, android.R.layout.simple_list_item_1, itemList);
                listView.setAdapter(adapter);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

}
