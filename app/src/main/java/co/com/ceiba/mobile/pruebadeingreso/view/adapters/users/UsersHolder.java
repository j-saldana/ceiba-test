package co.com.ceiba.mobile.pruebadeingreso.view.adapters.users;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import co.com.ceiba.mobile.pruebadeingreso.R;

public class UsersHolder extends RecyclerView.ViewHolder{
    TextView nameView, phoneView, emailView;
    Button postsView;

    public UsersHolder(View itemView) {
        super(itemView);

        this.nameView = itemView.findViewById(R.id.name);
        this.phoneView = itemView.findViewById(R.id.phone);
        this.emailView = itemView.findViewById(R.id.email);
        this.postsView = itemView.findViewById(R.id.btn_view_post);
    }
}
