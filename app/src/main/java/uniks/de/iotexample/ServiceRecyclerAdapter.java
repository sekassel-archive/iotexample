package uniks.de.iotexample;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import uniks.de.iotexample.viewholder.ActionViewHolder;
import uniks.de.iotexample.viewholder.ChartViewHolder;
import uniks.de.iotexample.viewholder.TextViewHolder;

public class ServiceRecyclerAdapter extends RecyclerView.Adapter {
    private String mServiceUrl;
    private JSONObject docu;
    private ArrayList<JSONObject> mServices =  new ArrayList<>();

    public ServiceRecyclerAdapter(String serviceUrl) {
        this.mServiceUrl = serviceUrl;
    }


    @Override
    public int getItemViewType(int position) {

        switch (position){
            case 0:
            case 1:
                return TextViewHolder.VIEW_TYPE;
            default:
            {

                JSONObject jsonObject = mServices.get(position - 2);

                try {
                    String kind = jsonObject.getString("kind");

                    if ("action".equals(kind)){
                        return ActionViewHolder.VIEW_TYPE;
                    }else if ("timeseries".equals(kind)){
                        return ChartViewHolder.VIEW_TYPE;
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        }

        return TextViewHolder.VIEW_TYPE;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(viewGroup.getContext());

        RecyclerView.ViewHolder viewHolder = null;

        switch (viewType){
            case TextViewHolder.VIEW_TYPE:
                View textView = layoutInflater.inflate(R.layout.text_card_view, viewGroup,false);
                viewHolder =  new TextViewHolder(textView);
                break;
            case ActionViewHolder.VIEW_TYPE:
                View actionView = layoutInflater.inflate(R.layout.action_card_view, viewGroup, false);
                viewHolder = new ActionViewHolder(actionView);
                break;
            case ChartViewHolder.VIEW_TYPE:
                View chartView = layoutInflater.inflate(R.layout.chart_card_view, viewGroup, false);
                viewHolder = new ChartViewHolder(chartView);
                break;
            default: new RuntimeException();
        }

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, int position) {

        if (viewHolder instanceof TextViewHolder) {
            TextViewHolder textViewHolder = (TextViewHolder) viewHolder;

            switch (position){
                case 0:
                    textViewHolder.setText("Name: " + docu.optString("name"));
                break;
                case 1:
                    textViewHolder.setText("Description: \n" + docu.optString("describtion"));
                break;
                default:

            }

        }else if (viewHolder instanceof ActionViewHolder) {
            ActionViewHolder actionViewHolder = (ActionViewHolder) viewHolder;
            actionViewHolder.onBind(mServices.get(position-2),docu);
        }else if (viewHolder instanceof ChartViewHolder) {
            ChartViewHolder chartViewHolder = (ChartViewHolder) viewHolder;
            chartViewHolder.onBind(mServices.get(position-2),docu);
        }

    }

    @Override
    public int getItemCount() {
        return (docu != null)? 2 + mServices.size():0;
    }

    public void update(JSONObject docu) {
        this.docu = docu;

        try {
            JSONArray services = docu.getJSONArray("services");

            for (int i = 0; i < services.length(); i++) {
                JSONObject jsonObject = services.getJSONObject(i);
                if (jsonObject.has("kind")){
                    mServices.add(services.getJSONObject(i));
                }
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
        notifyDataSetChanged();
    }
}
