package com.example.mateuszzaporowski.wotd.support;

import android.app.Activity;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mateuszzaporowski.wotd.R;

/**
 * Created by mateuszzaporowski on 05.05.18.
 */

public class TextRowAdapter extends ArrayAdapter<String> {
    private Activity _context;
    private String[] _text;
    private String[] _alternativeText;

    public TextRowAdapter(Activity context, String[] text) {
        super(context, R.layout.menu_view, text);
        this._context = context;
        this._text = text;
        this._alternativeText = new String[0];
    }

    public void set_alternativeText(String[] _alternativeText) {
        this._alternativeText = _alternativeText;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, @NonNull ViewGroup parent) {

        LayoutInflater inflater = _context.getLayoutInflater();
        View rowView = inflater.inflate(R.layout.list_row, null, true);
        TextView txtTitle = rowView.findViewById(R.id.text);
        txtTitle.setText(_text[position]);
        if (_alternativeText.length == _text.length) {
            txtTitle = rowView.findViewById(R.id.addition_text);
            txtTitle.setText(_alternativeText[position]);
        }

        Typeface tfArial = Typeface.createFromAsset(_context.getAssets(), "fonts/Arial.ttf");
        txtTitle.setTypeface(tfArial);

        return rowView;
    }
}
