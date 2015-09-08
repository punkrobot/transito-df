package mx.bytecraft.app.transitodf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import mx.bytecraft.app.transitodf.R;
import mx.bytecraft.app.transitodf.model.Articulo;

public class ReglamentoAdapter extends RecyclerView.Adapter<ReglamentoAdapter.ArticuloViewHolder> {

    private Context mContext;
    private List<Articulo> mReglamento;
    private List<Articulo> mVisibleArticulos;
    private OnItemClickListener mOnClickListener;
    private int mSelectedId = -1;

    public ReglamentoAdapter(Context context, List<Articulo> reglamento) {
        this.mContext = context;
        this.mReglamento= reglamento;
        this.mVisibleArticulos = new ArrayList<>(reglamento);
    }

    @Override
    public ArticuloViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_articulo, viewGroup, false);
        return new ArticuloViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ArticuloViewHolder articuloViewHolder, int position) {
        articuloViewHolder.title.setText(mVisibleArticulos.get(position).getTitulo());
        articuloViewHolder.title.setTag(mVisibleArticulos.get(position).getId());

        String extract = mVisibleArticulos.get(position).getExtracto();
        if(!TextUtils.isEmpty(extract)) {
            articuloViewHolder.extract.setVisibility(View.VISIBLE);
            articuloViewHolder.extract.setText(Html.fromHtml(mVisibleArticulos.get(position).getExtracto()));
        } else {
            articuloViewHolder.extract.setVisibility(View.GONE);
        }

        if(mContext.getResources().getBoolean(R.bool.is_tablet)) {
            articuloViewHolder.view.setSelected(mSelectedId == mVisibleArticulos.get(position).getId());
        }
    }

    @Override
    public int getItemCount() {
        return mVisibleArticulos == null ? 0 : mVisibleArticulos.size();
    }

    public void setFilter(String queryText) {
        mVisibleArticulos = new ArrayList<>();
        for (Articulo articulo : mReglamento) {
            if (articulo.getTexto().toLowerCase().contains(queryText.toLowerCase())) {
                articulo.setExtracto(getExtract(articulo.getTexto(), queryText));
                mVisibleArticulos.add(articulo);
            }
        }
        notifyDataSetChanged();
    }

    public void resetFilter(){
        mVisibleArticulos = new ArrayList<>();
        mVisibleArticulos.addAll(mReglamento);
        notifyDataSetChanged();
    }

    class ArticuloViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        LinearLayout view;
        TextView title;
        TextView extract;

        public ArticuloViewHolder(View itemView) {
            super(itemView);

            view = (LinearLayout) itemView.findViewById(R.id.item_articulo);
            title = (TextView) itemView.findViewById(R.id.item_articulo_text);
            extract= (TextView) itemView.findViewById(R.id.item_articulo_extract);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int id = (int)view.findViewById(R.id.item_articulo_text).getTag();
            mOnClickListener.onItemClick(view, id);

            if(mContext.getResources().getBoolean(R.bool.is_tablet)) {
                mSelectedId = id;
                notifyDataSetChanged();
            }
        }
    }

    public String getExtract(String text, String query){
        String[] htmlTags = {"<strong>", "</strong>", "<li>", "</li>", "<ol>", "</ol>", "<br>"};
        for(String tag : htmlTags) {
            text = text.replaceAll(tag, " ");
        }

        int index = text.toLowerCase().indexOf(query.toLowerCase());
        int inicio   = index - 70;
        if(inicio < 0) inicio = 0;
        int fin = index + query.length() + 70;
        if(fin > text.length()) fin = text.length();

        String extract = text.substring(inicio, fin);
        index = extract.toLowerCase().indexOf(query.toLowerCase());
        extract = extract.substring(0, index) + "<strong>" + query + "</strong>" + extract.substring(index + query.length(), extract.length());

        return extract;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int id);
    }

    public void setOnItemClickListener(final OnItemClickListener itemClickListener) {
        this.mOnClickListener = itemClickListener;
    }
}
