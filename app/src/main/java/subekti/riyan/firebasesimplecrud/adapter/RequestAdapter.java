package subekti.riyan.firebasesimplecrud.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

import subekti.riyan.firebasesimplecrud.R;
import subekti.riyan.firebasesimplecrud.model.Requests;

public class RequestAdapter extends RecyclerView.Adapter<RequestAdapter.MyViewHolder> {

    private List<Requests> moviesList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public LinearLayout l_layout;
        public TextView tv_nama, tv_email;

        public MyViewHolder(View view) {
            super(view);
            l_layout = view.findViewById(R.id.l_layout);
            tv_nama = view.findViewById(R.id.tv_nama);
            tv_email = view.findViewById(R.id.tv_email);
        }
    }

    public RequestAdapter(List<Requests> moviesList) {
        this.moviesList = moviesList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_request, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        final Requests movie = moviesList.get(position);

        holder.tv_nama.setText(movie.getNama());
        holder.tv_email.setText(movie.getEmail());

    }

    @Override
    public int getItemCount() {
        return moviesList.size();
    }

}
