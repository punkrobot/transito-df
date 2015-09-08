package mx.bytecraft.app.transitodf.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

import mx.bytecraft.app.transitodf.R;
import mx.bytecraft.app.transitodf.model.Tip;
import mx.bytecraft.app.transitodf.utils.ListTagHandler;

public class TipsAdapter extends RecyclerView.Adapter<TipsAdapter.TipViewHolder> {

    private List<Tip> mTips;
    private Context mContext;
    private OnShareTipListener mOnShareTipListener;

    public TipsAdapter(Context context, List<Tip> tips) {
        this.mContext = context;
        this.mTips = tips;
    }

    @Override
    public TipViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_tip, viewGroup, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindViewHolder(TipViewHolder viewHolder, int i) {
        viewHolder.titulo.setText(mTips.get(i).getTitulo());
        viewHolder.texto.setText(Html.fromHtml(mTips.get(i).getTexto(), null, new ListTagHandler()));

        switch (mTips.get(i).getTipo()){
            case DERECHO:
                viewHolder.tipo.setText(R.string.label_tipo_derecho);
                viewHolder.tipoIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_white_36dp));
                break;
            case OBLIGACION:
                viewHolder.tipo.setText(R.string.label_tipo_obligacion);
                viewHolder.tipoIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_grey_400_36dp));
                break;
            case CONSEJO:
                viewHolder.tipo.setText(R.string.label_tipo_consejo);
                viewHolder.tipoIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_info_outline_grey_400_36dp));
                break;
        }

        if(mTips.get(i).getArticulo() != 0){
            viewHolder.art.setText(mContext.getString(R.string.label_articulo) + " " + mTips.get(i).getArticulo());
        } else {
            viewHolder.art.setText("");
        }

        String imagen = mTips.get(i).getImagen();
        if(!TextUtils.isEmpty(imagen)){
            int id = mContext.getResources().getIdentifier(imagen, "drawable", mContext.getPackageName());
            viewHolder.tipImagen.setImageDrawable(mContext.getResources().getDrawable(id));
            viewHolder.tipImagen.setVisibility(View.VISIBLE);

        } else {
            viewHolder.tipImagen.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return mTips == null ? 0 : mTips.size();
    }

    class TipViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView titulo;
        TextView texto;
        TextView tipo;
        TextView art;
        ImageView tipoIcon;
        ImageView tipImagen;

        public TipViewHolder(View itemView) {
            super(itemView);

            titulo = (TextView) itemView.findViewById(R.id.tip_titulo);
            texto = (TextView) itemView.findViewById(R.id.tip_texto);
            tipo = (TextView) itemView.findViewById(R.id.tip_tipo);
            art = (TextView) itemView.findViewById(R.id.tip_art);
            tipoIcon = (ImageView) itemView.findViewById(R.id.tip_tipo_icon);
            tipImagen = (ImageView) itemView.findViewById(R.id.tip_imagen);

            itemView.findViewById(R.id.tip_share).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnShareTipListener.onShareTip(mTips.get(getPosition()));
        }
    }

    public interface OnShareTipListener {
        void onShareTip(Tip tip);
    }

    public void setOnShareTipListener(final OnShareTipListener shareTipListener) {
        this.mOnShareTipListener = shareTipListener;
    }

}