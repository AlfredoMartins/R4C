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

import java.util.List;

import ao.co.r4c.R;
import ao.co.r4c.model.Noticia;
import ao.co.r4c.service.ApiClient;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private final Context context;
    private final List<Noticia> noticiaList;

    public NewsAdapter(Context context, List<Noticia> noticiaList) {
        this.context = context;
        this.noticiaList = noticiaList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, viewGroup, false);

        return new NewsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, int i) {
        final Noticia noticia = noticiaList.get(i);

        viewHolder.txt_titulo.setText(noticia.getTitulo());
        viewHolder.txt_fonte.setText(noticia.getAutor());
        viewHolder.txt_data.setText(noticia.getData());
        viewHolder.txt_descricao.setText(noticia.getDescricao());
        viewHolder.txt_tempo.setText(noticia.getLocal());

        try {
            Glide.with(context).load(ApiClient.getBaseUrl() + "r4c/api/objects/noticias/upload_images/" + noticia.getFoto_url()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(viewHolder.img_news);
        } catch (Exception e) {
            viewHolder.img_news.setImageResource(R.drawable.img_user_default);
        }

    }

    @Override
    public int getItemCount() {
        return noticiaList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView txt_titulo, txt_descricao, txt_tempo, txt_fonte, txt_data;
        ImageView img_news;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            txt_titulo = itemView.findViewById(R.id.txt_titulo);
            txt_data = itemView.findViewById(R.id.publishedAt);
            txt_descricao = itemView.findViewById(R.id.txt_descricao);
            txt_tempo = itemView.findViewById(R.id.txt_tempo);
            txt_fonte = itemView.findViewById(R.id.txt_fonte);

            img_news = itemView.findViewById(R.id.img_news);
        }

    }
}
