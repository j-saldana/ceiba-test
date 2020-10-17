package co.com.ceiba.mobile.pruebadeingreso.view.adapters.users;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.io.Serializable;
import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.model.User;
import co.com.ceiba.mobile.pruebadeingreso.view.PostActivity;

public class UsersAdapter extends RecyclerView.Adapter<UsersHolder> {
    Context context;
    List<User> userList;

    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
    }

    @NonNull
    @Override
    public UsersHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        @SuppressLint("InflateParams") View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list_item, parent, false);
        return new UsersHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersHolder holder, int position) {
        holder.nameView.setText(userList.get(position).getName());
        holder.phoneView.setText(userList.get(position).getPhone());
        holder.emailView.setText(userList.get(position).getEmail());

        final int userPos = position;

        holder.postsView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                User user = userList.get(userPos);

                Intent intent = new Intent(context, PostActivity.class);
                intent.putExtra("id", user.getId());
                intent.putExtra("name", user.getName());
                intent.putExtra("phone", user.getPhone());
                intent.putExtra("email", user.getEmail());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }
}
