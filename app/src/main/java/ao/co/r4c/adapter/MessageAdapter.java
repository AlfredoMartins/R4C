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
import ao.co.r4c.model.ChatMessage;
import ao.co.r4c.storage.SharedPreferenceManager;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.ViewHolder> {

    public static final int MSG_TYPE_LEFT = 0;
    public static final int MSG_TYPE_RIGHT = 1;

    private final Context context;
    private final List<ChatMessage> chatMessageList;

    public MessageAdapter(Context context, List<ChatMessage> chatMessageList) {
        this.context = context;
        this.chatMessageList = chatMessageList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        if (i == MSG_TYPE_RIGHT) {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_right, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.chat_item_left, viewGroup, false);
            return new MessageAdapter.ViewHolder(view);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final ChatMessage chatMessageItem = chatMessageList.get(i);

        viewHolder.txt_message.setText(chatMessageItem.getMessage());
        viewHolder.txt_date.setText(chatMessageItem.getData());

    }

    public ChatMessage getMessageAt(int position) {
        return chatMessageList.get(position);
    }


    @Override
    public int getItemCount() {
        return chatMessageList.size();
    }

    @Override
    public int getItemViewType(int position) {
        return
                chatMessageList.get(position).getId_sender() == SharedPreferenceManager.getInstance(context).getUsuario().getId() ? MSG_TYPE_RIGHT : MSG_TYPE_LEFT;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView txt_message, txt_date;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_message = itemView.findViewById(R.id.txt_text_message);
            txt_date = itemView.findViewById(R.id.txt_data);
        }
    }
}