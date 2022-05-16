package ao.co.r4c.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.model.UserCallItem;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserCallAdapter extends RecyclerView.Adapter<UserCallAdapter.ViewHolder> {

    private final Context context;
    private final List<UserCallItem> userItemList;

    public UserCallAdapter(Context context, List<UserCallItem> userItemList) {
        this.context = context;
        this.userItemList = userItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_call_item, viewGroup, false);
        return new UserCallAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final UserCallItem userItem = userItemList.get(i);

        viewHolder.txt_user_name.setText(userItem.getUser_name());
        viewHolder.txt_date.setText(userItem.getData());

        if (viewHolder.img_user_image == null)
            viewHolder.img_user_image.setImageResource(R.drawable.img_alfredo);
        else
            viewHolder.img_user_image.setImageDrawable(userItem.getUser_image());

        if (viewHolder.img_call_1 == null)
            viewHolder.img_call_1.setImageResource(R.drawable.ic_call_missed);
        else
            viewHolder.img_call_1.setImageDrawable(userItem.getCancelado_image());

        if (viewHolder.img_call_2 == null)
            viewHolder.img_call_2.setImageResource(R.drawable.ic_call_received);
        else
            viewHolder.img_call_2.setImageDrawable(userItem.getAtentido_image());

    }


    @Override
    public int getItemCount() {
        return userItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user_image;
        TextView txt_user_name, txt_date;
        ImageView img_call_1, img_call_2;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user_image = itemView.findViewById(R.id.img_user_image);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_date = itemView.findViewById(R.id.txt_date);
            img_call_1 = itemView.findViewById(R.id.img_call_1);
            img_call_2 = itemView.findViewById(R.id.img_call_2);

        }
    }
}
