package uniks.de.iotexample.viewholder;

import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import uniks.de.iotexample.R;

public class ActionViewHolder extends RecyclerView.ViewHolder {
    public static final int VIEW_TYPE = 13 ;

    public ActionViewHolder(View view) {
        super(view);
    }

    public void onBind(JSONObject jsonObject) {

        final String serviceurl = jsonObject.keys().next();

        Button button = itemView.findViewById(R.id.button);
        button.setText(serviceurl);

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new ActionTask().execute("http://avocado.uniks.de:13345/api"+ serviceurl);
            }
        });

    }

    private class ActionTask extends AsyncTask<String,Void,String>{

        @Override
        protected String doInBackground(String... strings) {

            final RequestQueue requestQueue = Volley.newRequestQueue(ActionViewHolder.this.itemView.getContext());

            StringRequest stringRequest =  new StringRequest(strings[0], new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    requestQueue.stop();
                }
            }, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    error.printStackTrace();
                }
            });

            requestQueue.add(stringRequest);

            return null;
        }
    }

}
