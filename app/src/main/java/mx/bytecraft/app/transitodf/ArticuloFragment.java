package mx.bytecraft.app.transitodf;

import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SearchView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import java.lang.reflect.Method;

import mx.bytecraft.app.transitodf.model.Articulo;
import mx.bytecraft.app.transitodf.model.Reglamento;

public class ArticuloFragment extends Fragment implements SearchView.OnQueryTextListener, SearchView.OnCloseListener {

    public static final String ARG_ITEM_ID = "itemId";
    public static final String ARG_QUERY_ID = "queryId";
    public static final String ARG_TOTAL_ID = "total";

    private Articulo mArticulo;

    public ArticuloFragment() {}

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_ITEM_ID)) {
            Reglamento reglamento = new Reglamento(getContext());
            mArticulo = reglamento.getArticulo(getArguments().getInt(ARG_ITEM_ID));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_articulo, container, false);

        if (mArticulo != null) {
            setTitle(mArticulo.getTitulo());

            String head = "<html><head><style type='text/css'>" +
                    "body { padding: .7em; }" +
                    "ol { list-style-type: upper-roman; }" +
                    "ol li { margin-bottom: .3em }" +
                    "ol ol { list-style-type: lower-latin; }" +
                    "table { width:100%; } " +
                    "table th{ font-weight:normal; color: #FFF; background-color: #0277BD; padding: 10px; }" +
                    "table td{ padding: 10px; }" +
                    "</style></head><body>";

            String html = head + mArticulo.getTexto() +
                    (mArticulo.getSanciones() != null ? mArticulo.getSanciones() : "") +
                    "</body></html>";

            WebView webView = (WebView)rootView.findViewById(R.id.articulo_webview);
            webView.loadDataWithBaseURL(null, html, "text/html", "utf-8", null);

            webView.setWebViewClient(new WebViewClient() {
                @Override
                public void onPageFinished(WebView view, String url) {
                    super.onPageFinished(view, url);

                    String query = getArguments().getString(ARG_QUERY_ID);
                    if(!TextUtils.isEmpty(query)){
                        highlightText(view, query);
                    }
                }
            });
        }

        return rootView;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        if(getActivity() != null) {
            if (TextUtils.isEmpty(newText)) {
                WebView webView = (WebView) getActivity().findViewById(R.id.articulo_webview);
                webView.clearMatches();

            } else if (newText.length() > 2) {
                onQueryTextSubmit(newText);
            }
        }
        return false;
    }

    @Override
    public boolean onQueryTextSubmit(String query){
        if(getActivity() != null) {
            highlightText((WebView) getActivity().findViewById(R.id.articulo_webview), query);
        }
        return true;
    }

    @Override
    public boolean onClose() {
        if(getActivity() != null) {
            WebView webView = (WebView) getActivity().findViewById(R.id.articulo_webview);
            webView.clearMatches();
        }
        return false;
    }

    private void highlightText(WebView webView, String query){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
            webView.findAllAsync(query);
        } else {
            webView.findAll(query);
        }

        try {
            Method m = WebView.class.getMethod("setFindIsUp", Boolean.TYPE);
            m.invoke(webView, true);
        } catch (Throwable ignored) {}
    }

    private void setTitle(String title) {
        if(!getResources().getBoolean(R.bool.is_tablet)){
            (getActivity().findViewById(R.id.subtitle_text)).setVisibility(View.GONE);
            TextView titleTextView = (TextView)getActivity().findViewById(R.id.title_text);
            titleTextView.setText(title);
        }
    }
}
