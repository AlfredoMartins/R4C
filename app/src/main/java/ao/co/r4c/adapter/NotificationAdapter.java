package ao.co.r4c.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.model.NotificationItem;

public class NotificationAdapter extends RecyclerView.Adapter<NotificationAdapter.ViewHolder> {

    private final Context context;
    private final List<NotificationItem> userItemList;

    public NotificationAdapter(Context context, List<NotificationItem> userItemList) {
        this.context = context;
        this.userItemList = userItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.notification_item, viewGroup, false);
        return new NotificationAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final NotificationItem userItem = userItemList.get(i);

        viewHolder.txt_title.setText(userItem.getTitle());
        viewHolder.txt_text_notification.setText(userItem.getText());
        viewHolder.txt_date.setText(userItem.getDate());

    }


    @Override
    public int getItemCount() {
        return userItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_title, txt_text_notification, txt_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_title = itemView.findViewById(R.id.txt_title);
            txt_text_notification = itemView.findViewById(R.id.txt_text_notification);
            txt_date = itemView.findViewById(R.id.txt_date);
        }
    }
}
