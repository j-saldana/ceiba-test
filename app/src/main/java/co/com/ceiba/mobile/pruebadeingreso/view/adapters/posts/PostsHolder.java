package co.com.ceiba.mobile.pruebadeingreso.view.adapters.posts;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import co.com.ceiba.mobile.pruebadeingreso.R;

public class PostsHolder extends RecyclerView.ViewHolder {
    TextView titleView, bodyView;

    public PostsHolder(View itemView) {
        super(itemView);

        this.titleView = itemView.findViewById(R.id.title);
        this.bodyView = itemView.findViewById(R.id.body);
    }
}
