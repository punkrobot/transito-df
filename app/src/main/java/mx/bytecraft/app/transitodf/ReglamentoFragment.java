package mx.bytecraft.app.transitodf;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

import mx.bytecraft.app.transitodf.adapter.GroupedAdapter;
import mx.bytecraft.app.transitodf.adapter.ReglamentoAdapter;
import mx.bytecraft.app.transitodf.model.Articulo;
import mx.bytecraft.app.transitodf.model.Capitulo;
import mx.bytecraft.app.transitodf.model.Reglamento;

public class ReglamentoFragment extends Fragment implements ReglamentoAdapter.OnItemClickListener{

    private OnArticuloSelected mOnArticuloSelected;
    private ReglamentoAdapter mReglamentoAdapter;
    private RecyclerView mRecyclerView;
    private Reglamento mReglamento;

    public ReglamentoFragment() {}

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_reglamento, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.reglamento_recycler_view);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mReglamento = new Reglamento(getContext());
        setAdapter(true);

        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof OnArticuloSelected)) {
            throw new IllegalStateException("Activity must implement fragment's callbacks.");
        }
        mOnArticuloSelected = (OnArticuloSelected) activity;
    }

    public void searchArticulo(String query){
        setAdapter(false);
        mReglamentoAdapter.setFilter(query);
    }

    public void resetSearchFilter(){
        mReglamentoAdapter.resetFilter();
        setAdapter(true);
    }

    public void setAdapter(boolean grouped){
        int index = 0;
        List<Articulo> articulos = new ArrayList<>();
        List<GroupedAdapter.Section> sections = new ArrayList<>();
        for(Capitulo capitulo : mReglamento.getCapitulos()){
            sections.add(new GroupedAdapter.Section(index,capitulo.getCapitulo()));
            for(Articulo articulo : capitulo.getArticulos()){
                articulo.setExtracto("");
                articulos.add(articulo);
                index++;
            }
        }

        mReglamentoAdapter = new ReglamentoAdapter(getActivity(), articulos);
        mReglamentoAdapter.setOnItemClickListener(this);

        if(grouped) {
            GroupedAdapter groupedAdapter = new GroupedAdapter(getActivity(), R.layout.item_capitulo, R.id.section_text, mReglamentoAdapter);
            groupedAdapter.setSections(sections.toArray(new GroupedAdapter.Section[sections.size()]));
            mRecyclerView.setAdapter(groupedAdapter);

        } else {
            mRecyclerView.setAdapter(mReglamentoAdapter);
        }
    }

    @Override
    public void onItemClick(View view, int id) {
        mOnArticuloSelected.onArticuloSelected(id, mReglamento.getArticulos());
    }

    public interface OnArticuloSelected {
        void onArticuloSelected(int id, int total);
    }

}
