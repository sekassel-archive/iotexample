package uniks.de.iotexample;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ServiceFragment extends Fragment {

    private String mServiceUrl;

    TextView mText;
    LineChart mChart;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.service_fragment, container,false);
        mText = view.findViewById(R.id.textView);
        mText.setText(mServiceUrl);

        mChart = (LineChart) view.findViewById(R.id.chart);

        mChart.getXAxis().setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return new Date((long) value).toLocaleString();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();

        new GetData().execute(mServiceUrl);

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mServiceUrl = getArguments().getString("serviceurl");
        super.onCreate(savedInstanceState);
    }

    private class GetData extends AsyncTask<String,Void,JSONArray>{

        @Override
        protected JSONArray doInBackground(String... strings) {


            RequestFuture<JSONArray> future =  RequestFuture.newFuture();

            RequestQueue requestQueue = Volley.newRequestQueue(getContext());

            JsonArrayRequest jsonArrayRequest =  new JsonArrayRequest(strings[0], future, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Log.e(getClass().getSimpleName(), error.toString());
                }
            });

            requestQueue.add(jsonArrayRequest);

            try {
                return future.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }finally {
                requestQueue.stop();
            }

            return new JSONArray();
        }

        @Override
        protected void onPostExecute(JSONArray jsonArray) {
            List<Entry> entries = new ArrayList<Entry>();

            try {
                for (int i = 0; i< jsonArray.length();i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    entries.add(new Entry(jsonObject.getLong("timestamp"),(float) jsonObject.getDouble("level")));
                }
            }catch (JSONException e){
                e.printStackTrace();
            }

            LineDataSet dataSet = new LineDataSet(entries, "water fill");

            LineData lineData =  new LineData(dataSet);



            mChart.setData(lineData);

            mChart.invalidate();


        }
    }

}
