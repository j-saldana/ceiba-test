package co.com.ceiba.mobile.pruebadeingreso.view.adapters.users;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import java.util.ArrayList;
import java.util.List;

import co.com.ceiba.mobile.pruebadeingreso.R;
import co.com.ceiba.mobile.pruebadeingreso.model.User;
import co.com.ceiba.mobile.pruebadeingreso.view.PostActivity;

public class UsersAdapter extends RecyclerView.Adapter<UsersHolder> implements Filterable {
    Context context;
    List<User> userList;
    List<User> userListFull;


    public UsersAdapter(Context context, List<User> userList) {
        this.context = context;
        this.userList = userList;
        userListFull = new ArrayList<>(userList);
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

    @Override
    public Filter getFilter() {
        return usersFilter;
    }

    private final Filter usersFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence charSequence) {
            List<User> filteredList = new ArrayList<>();
            if (charSequence == null || charSequence.length() == 0){
                filteredList.addAll(userListFull);
            }else {
                String filterPattern = charSequence.toString().toLowerCase().trim();
                for (User user: userListFull){
                    if (user.getName().toLowerCase().contains(filterPattern)){
                        filteredList.add(user);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;
            return results;
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
            userList.clear();
            userList.addAll((List) filterResults.values);
            notifyDataSetChanged();
        }
    };
}
