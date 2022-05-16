package ao.co.r4c.adapter;

import android.content.Context;
import android.content.Intent;
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
import ao.co.r4c.activity.main.ChatMesssageActivity;
import ao.co.r4c.model.UserItem;
import ao.co.r4c.service.ApiClient;
import de.hdodenhof.circleimageview.CircleImageView;

public class UsersChatListAdapter extends RecyclerView.Adapter<UsersChatListAdapter.ViewHolder> {

    private final Context context;
    private final List<UserItem> userItemList;

    public UsersChatListAdapter(Context context, List<UserItem> userItemList) {
        this.context = context;
        this.userItemList = userItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.user_chat_item, viewGroup, false);
        return new UsersChatListAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final UserItem userItem = userItemList.get(i);

        viewHolder.txt_user_name.setText(userItem.getUser_name());
        viewHolder.txt_date.setText(userItem.getTime_sent_last_message());
        viewHolder.txt_message_text.setText(userItem.getLast_message());

        //Try to load images

        try {
            if (userItem.getFoto_url().isEmpty())
                viewHolder.img_user_image.setImageResource(R.drawable.img_user_default);
            else
                Glide.with(context).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + userItem.getUser_id() + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(viewHolder.img_user_image);
        } catch (Exception e) {
            //Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ChatMesssageActivity.class);
                intent.putExtra("id", userItem.getUser_id());
                intent.putExtra("username", userItem.getUser_name());
                intent.putExtra("telefone", userItem.getTelefone());

                context.startActivity(intent);
            }
        });
    }


    @Override
    public int getItemCount() {
        return userItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        CircleImageView img_user_image;
        TextView txt_user_name, txt_message_text, txt_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            img_user_image = itemView.findViewById(R.id.img_user_image);
            txt_user_name = itemView.findViewById(R.id.txt_user_name);
            txt_message_text = itemView.findViewById(R.id.txt_message_text);
            txt_date = itemView.findViewById(R.id.txt_date);

        }
    }
}
