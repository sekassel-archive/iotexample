package uniks.de.iotexample.viewholder;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import uniks.de.iotexample.R;

public class TextViewHolder extends RecyclerView.ViewHolder {
    public static final int VIEW_TYPE = 12 ;

    public TextViewHolder(View view) {
        super(view);
    }

    public void setText(String s) {
        TextView text = itemView.findViewById(R.id.textView);
        text.setText(s);
    }
}
