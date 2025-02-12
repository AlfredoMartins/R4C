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
import ao.co.r4c.model.TripHistoryItem;
import ao.co.r4c.service.ApiClient;
import de.hdodenhof.circleimageview.CircleImageView;

public class TripHistoryAdapter extends RecyclerView.Adapter<TripHistoryAdapter.ViewHolder> {

    private final Context context;
    private final List<TripHistoryItem> tripHistoryItemList;

    public TripHistoryAdapter(Context context, List<TripHistoryItem> tripHistoryItemList) {
        this.context = context;
        this.tripHistoryItemList = tripHistoryItemList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.trip_history_item, viewGroup, false);
        return new TripHistoryAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        TripHistoryItem item = tripHistoryItemList.get(i);

        viewHolder.txt_nome_motorista.setText(item.getUser_name());
        viewHolder.txt_origem.setText("Origem: " + item.getOrigem());
        viewHolder.txt_destino.setText("Destino: " + item.getDestino());
        viewHolder.txt_valor_pago.setText("Valor pago: " + item.getValor_pago() + " Kzs");
        viewHolder.txt_avaliacao.setText("Avaliação: " + item.getAvaliacao());
        viewHolder.txt_duracao.setText("Duração: " + item.getDuracao() + " minutos");
        viewHolder.txt_data.setText("Data: " + item.getData());


        if (item.getAvaliacao() == null) {
            for (int j = 0; j < viewHolder.imageViewList.size(); j++) {
                viewHolder.imageViewList.get(j).setImageResource(R.drawable.ic_star_border_yellow);
            }
        } else {
            int j = Integer.parseInt(item.getAvaliacao());
            int x = 0;

            for (; x < j; x++) {
                viewHolder.imageViewList.get(x).setImageResource(R.drawable.ic_star_yellow);
            }

            for (; j < viewHolder.imageViewList.size(); j++) {
                viewHolder.imageViewList.get(j).setImageResource(R.drawable.ic_star_border_yellow);
            }
        }


        try {
            Glide.with(context).load(ApiClient.getBaseUrl() + "r4c/api/objects/usuarios/upload_images/" + item.getUser_id() + ".jpg").diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(viewHolder.img_foto_usuario);
        } catch (Exception e) {
            viewHolder.img_foto_usuario.setImageResource(R.drawable.img_user_default);
        }

    }


    @Override
    public int getItemCount() {
        return tripHistoryItemList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_origem, txt_destino, txt_valor_pago, txt_avaliacao, txt_data, txt_nome_motorista, txt_duracao;
        Integer num_estrelas;
        CircleImageView img_foto_usuario;
        ImageView img_star_1, img_star_2, img_star_3, img_star_4, img_star_5;
        List<ImageView> imageViewList;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_avaliacao = itemView.findViewById(R.id.txt_avaliacao);
            txt_origem = itemView.findViewById(R.id.txt_origem);
            txt_destino = itemView.findViewById(R.id.txt_destino);
            txt_nome_motorista = itemView.findViewById(R.id.txt_user_name);
            txt_data = itemView.findViewById(R.id.txt_date);
            txt_valor_pago = itemView.findViewById(R.id.txt_valor_pago);
            txt_duracao = itemView.findViewById(R.id.txt_duracao);
            img_foto_usuario = itemView.findViewById(R.id.img_user_image);


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