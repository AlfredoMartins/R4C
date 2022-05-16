package ao.co.r4c.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

import java.util.ArrayList;
import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.model.UserCommentItem;
import ao.co.r4c.service.ApiClient;
import de.hdodenhof.circleimageview.CircleImageView;

public class UserCommentsAdapter extends RecyclerView.Adapter<UserCommentsAdapter.ViewHolder> {

    private final Context context;
    private final List<UserCommentItem> userItemList;

    public UserCommentsAdapter(Context context, List<UserCommentItem> userItemList) {
        this.context = context;
        this.userItemList = userItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_comments_item_driver, viewGroup, false);

        return new UserCommentsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final UserCommentItem userItem = userItemList.get(i);

        viewHolder.txt_user_name.setText(userItem.getUser_name());
        viewHolder.txt_date.setText(userItem.getData());
        viewHolder.txt_comment.setText(userItem.getComment());

        int j = userItem.getNum_stars();
        int x = 0;

        for (; x < j; x++) {
            viewHolder.imageViewList.get(x).setImageResource(R.drawable.ic_star_yellow);
        }

        for (; j < viewHolder.imageViewList.size(); j++) {
            viewHolder.imageViewList.get(j).setImageResource(R.drawable.ic_star_border_yellow);
        }

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
        TextView txt_user_name, txt_comment, txt_date;
        ImageView img_star_1, img_star_2, img_star_3, img_star_4, img_star_5;
        List<ImageView> imageViewList;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user_image = itemView.findViewById(R.id.img_user_image);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_date = itemView.findViewById(R.id.txt_date);
            txt_comment = itemView.findViewById(R.id.txt_comment);

            img_star_1 = itemView.findViewById(R.id.star_1);
            img_star_2 = itemView.findViewById(R.id.star_2);
            img_star_3 = itemView.findViewById(R.id.star_3);
            img_star_4 = itemView.findViewById(R.id.star_4);
            img_star_5 = itemView.findViewById(R.id.star_5);

            imageViewList = new ArrayList<>();
            imageViewList.add(img_star_1);
            imageViewList.add(img_star_2);
            imageViewList.add(img_star_3);
            imageViewList.add(img_star_4);
            imageViewList.add(img_star_5);
        }


    }
}
