package mx.bytecraft.app.transitodf.utils;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.text.DecimalFormat;

import mx.bytecraft.app.transitodf.R;

public class Util {

    public static MaterialDialog createCalcDialog(Context context) {
        final float ucdmx = Float.parseFloat(context.getString(R.string.ucdmx));
        final float sm = Float.parseFloat(context.getString(R.string.sm));

        MaterialDialog dialog = new MaterialDialog.Builder(context)
                .title(R.string.calc_title)
                .customView(R.layout.dialog_calc, true)
                .positiveText(R.string.dialog_close)
                .build();

        View view = dialog.getCustomView();
        final TextView multaText = (TextView)view.findViewById(R.id.calc_multa);
        final TextView multaDescText = (TextView)view.findViewById(R.id.calc_multa_desc);
        final RadioGroup tipoRadio = (RadioGroup)view.findViewById(R.id.calc_radio_tipo);
        final EditText calcText = ((EditText)view.findViewById(R.id.calc_amount));

        tipoRadio.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int id) {
                actualizaTextos(calcText.getText().toString(), id == R.id.calc_radio_ucdmx ? ucdmx : sm, multaText, multaDescText);
            }
        });

        calcText.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                actualizaTextos(s.toString(), tipoRadio.getCheckedRadioButtonId() == R.id.calc_radio_ucdmx ? ucdmx : sm, multaText, multaDescText);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        return dialog;
    }

    static private void actualizaTextos(String multaStr, float tipo, TextView multaText, TextView multaDescText){
        try {
            float multa = Integer.parseInt(multaStr) * tipo;
            multaText.setText(DecimalFormat.getCurrencyInstance().format(multa));
            multaDescText.setText(DecimalFormat.getCurrencyInstance().format(multa / 2));
        } catch (NumberFormatException ignored) {}
    }

}
