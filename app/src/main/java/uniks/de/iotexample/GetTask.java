package uniks.de.iotexample;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.RequestFuture;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.util.concurrent.ExecutionException;

public class GetTask  extends AsyncTask<String,Void,JSONObject> {

    private Context context;
    private ServiceRecyclerAdapter serviceRecyclerAdapter;

    public GetTask(Context context, ServiceRecyclerAdapter serviceRecyclerAdapter){
        this.context = context;
        this.serviceRecyclerAdapter = serviceRecyclerAdapter;
    }
    @Override
    protected JSONObject doInBackground(String... strings) {


        RequestFuture<JSONObject> future =  RequestFuture.newFuture();

        RequestQueue requestQueue = Volley.newRequestQueue(context);

        JsonObjectRequest jsonArrayRequest =  new JsonObjectRequest(strings[0],null, future, new Response.ErrorListener() {
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

        return new JSONObject();
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {

        serviceRecyclerAdapter.update(jsonObject);

    }
}