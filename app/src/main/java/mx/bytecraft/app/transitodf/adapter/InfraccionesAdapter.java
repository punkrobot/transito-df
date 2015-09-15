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

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

import mx.bytecraft.app.transitodf.R;
import mx.bytecraft.app.transitodf.model.Infraccion;

public class InfraccionesAdapter extends RecyclerView.Adapter<InfraccionesAdapter.InfraccionViewHolder> {

    private List<Infraccion> mInfracciones;
    private Context mContext;

    public InfraccionesAdapter(Context context, List<Infraccion> infracciones) {
        this.mContext = context;
        this.mInfracciones = infracciones;
    }

    @Override
    public InfraccionViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.item_infraccion, viewGroup, false);
        return new InfraccionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(InfraccionViewHolder viewHolder, int i) {
        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault());
        viewHolder.fecha.setText(format.format(mInfracciones.get(i).getFecha()));

        viewHolder.folio.setText("Folio: " + mInfracciones.get(i).getFolio());
        viewHolder.status.setText(mInfracciones.get(i).getSituacion());
        viewHolder.motivo.setText(Html.fromHtml(mInfracciones.get(i).getMotivo()));
        viewHolder.sancion.setText(Html.fromHtml(mInfracciones.get(i).getSancion()));
        viewHolder.fundamento.setText(Html.fromHtml(mInfracciones.get(i).getFundamento()));

        switch (mInfracciones.get(i).getSituacion()){
            case "Pagada":
                viewHolder.statusIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_check_circle_white_36dp));
                break;
            case "Adeudo":
                viewHolder.statusIcon.setImageDrawable(mContext.getResources().getDrawable(R.drawable.ic_error_grey_400_36dp));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mInfracciones == null ? 0 : mInfracciones.size();
    }

    class InfraccionViewHolder extends RecyclerView.ViewHolder {
        TextView fecha;
        TextView folio;
        TextView status;
        TextView motivo;
        TextView fundamento;
        TextView sancion;
        ImageView statusIcon;

        public InfraccionViewHolder(View itemView) {
            super(itemView);

            fecha = (TextView) itemView.findViewById(R.id.infraccion_fecha);
            folio = (TextView) itemView.findViewById(R.id.infraccion_folio);
            status = (TextView) itemView.findViewById(R.id.infraccion_status);
            motivo = (TextView) itemView.findViewById(R.id.infraccion_motivo);
            fundamento = (TextView) itemView.findViewById(R.id.infraccion_fundamento);
            sancion = (TextView) itemView.findViewById(R.id.infraccion_sancion);
            statusIcon = (ImageView) itemView.findViewById(R.id.infraccion_status_icon);
        }
    }

}