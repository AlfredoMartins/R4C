package ao.co.r4c.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.model.UserEarnItem;
import ao.co.r4c.service.ApiClient;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserEarnAdapter extends RecyclerView.Adapter<UserEarnAdapter.ViewHolder> {

    private final Context context;
    private final List<UserEarnItem> userItemList;

    public UserEarnAdapter(Context context, List<UserEarnItem> userItemList) {
        this.context = context;
        this.userItemList = userItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_earn_item_driver, viewGroup, false);
        return new UserEarnAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final UserEarnItem userItem = userItemList.get(i);

        viewHolder.txt_user_name.setText(userItem.getUser_name());
        viewHolder.txt_date.setText(userItem.getData());
        viewHolder.txt_money.setText(userItem.getMoney() + "Kzs");

        try {
            Glide.with(context).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + userItem.getUser_id() + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(viewHolder.img_user_image);
        } catch (Exception e) {
            viewHolder.img_user_image.setImageResource(R.drawable.img_user_default);
        }

    }


    @Override
    public int getItemCount() {
        return userItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user_image;
        TextView txt_user_name, txt_money, txt_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user_image = itemView.findViewById(R.id.img_user_image);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_money = itemView.findViewById(R.id.txt_money);
            txt_date = itemView.findViewById(R.id.txt_date);

        }
    }
}
