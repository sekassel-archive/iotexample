package uniks.de.iotexample.viewholder;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import uniks.de.iotexample.R;

public class ChartViewHolder extends RecyclerView.ViewHolder {
    public static final int VIEW_TYPE = 14 ;

    public ChartViewHolder(View view) {
        super(view);
    }

    public void onBind(JSONObject jsonObject, final JSONObject docu) {

        final String serviceurl = docu.optString("apiBase")
                                    + jsonObject.optString("endpoint");

        new ChartTask().execute(serviceurl);

    }

    private class ChartTask extends AsyncTask<String,Void,JSONArray>{

        @Override
        protected JSONArray doInBackground(String... strings) {

            final RequestQueue requestQueue = Volley.newRequestQueue(ChartViewHolder.this.itemView.getContext());

            RequestFuture<JSONArray> jsonArrayRequestFuture = RequestFuture.newFuture();


            JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(strings[0], jsonArrayRequestFuture, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(jsonArrayRequest);

            try {
                JSONArray jsonArray = jsonArrayRequestFuture.get();
                requestQueue.stop();
                return jsonArray;
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {

            LineChart chart = itemView.findViewById(R.id.chart);

            List<Entry> entries = new ArrayList<Entry>();

            for (int i = 0; i < jsonArray.length(); i++) {
                try {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    entries.add(new Entry(jsonObject.getLong("timestamp"), (float)jsonObject.getDouble("level")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            LineDataSet lineDataSet = new LineDataSet(entries,"water");
            LineData lineData = new LineData(lineDataSet);

            chart.setData(lineData);
            chart.invalidate();
        }
    }

}
