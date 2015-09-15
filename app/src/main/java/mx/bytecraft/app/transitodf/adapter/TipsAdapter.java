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

public class TipsAdapter extends HeaderAdapter {

    private List<Tip> mTips;
    private Context mContext;
    private boolean mShowHeader;
    private OnShareTipListener mOnShareTipListener;
    private OnHeaderClicListener mOnHeaderClicListener;

    public TipsAdapter(Context context, List<Tip> tips, boolean showHeader) {
        this.mContext = context;
        this.mTips = tips;
        this.mShowHeader = showHeader;
    }

    @Override
    public boolean useHeader() {
        return mShowHeader;
    }

    @Override
    public RecyclerView.ViewHolder onCreateHeaderViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_tips_auto, parent, false);
        return new HeaderViewHolder(view);
    }

    @Override
    public void onBindHeaderView(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public RecyclerView.ViewHolder onCreateBasicItemViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_tip, parent, false);
        return new TipViewHolder(view);
    }

    @Override
    public void onBindBasicItemView(RecyclerView.ViewHolder holder, int i) {
        TipViewHolder viewHolder = (TipViewHolder)holder;

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
    public int getBasicItemCount() {
        return mTips == null ? 0 : mTips.size();
    }

    @Override
    public int getBasicItemType(int position) {
        return 0;
    }

    class HeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public HeaderViewHolder(View itemView) {
            super(itemView);

            itemView.findViewById(R.id.infracciones_btn).setOnClickListener(this);
            itemView.findViewById(R.id.depositos_btn).setOnClickListener(this);
            itemView.findViewById(R.id.agentes_btn).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mOnHeaderClicListener.onHeaderClic(v.getId());
        }
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

    public interface OnHeaderClicListener {
        void onHeaderClic(int id);
    }

    public void setOnHeaderClicListener(final OnHeaderClicListener headerClicListener) {
        this.mOnHeaderClicListener = headerClicListener;
    }

    public void setOnShareTipListener(final OnShareTipListener shareTipListener) {
        this.mOnShareTipListener = shareTipListener;
    }

}