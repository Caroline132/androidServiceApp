package com.group15.seg2105finalproject.customer;

import android.content.Context;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.TextView;

import com.group15.seg2105finalproject.R;

import java.util.HashMap;
import java.util.List;

public class FormListviewAdapter extends BaseAdapter {
    private Context context;
    private List list;
    private HashMap<String, Object> form;
    LayoutInflater infl;

    FormListviewAdapter(Context c, List l){
        context = c;
        form = new HashMap<String, Object>();
//        Log.d("l size", String.valueOf(l.size()));
//        for (int i = 0; i < l.size(); i++){
//            Log.d("formfield", l.get(i).toString());
//            form.put(l.get(i).toString(), "");
//        }
//        Log.d("FORMRMMRM", form.toString());
        list = l;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public HashMap<String, Object> getForm(){ return form; };

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder h;
        convertView = null;

        if (convertView==null){
            h = new ViewHolder();
            infl = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            convertView = infl.inflate(R.layout.activity_form_listview_adapter,null);
            h.c = (EditText)convertView.findViewById(R.id.formField);
            h.formAttribute = convertView.findViewById(R.id.formAttribute);
            String key = list.get(position).toString();
            Object value = form.get(key);
            String text = "";
            if (value != null){
                text = value.toString();
            }else{
                form.put(list.get(position).toString(), "");
            }
            if (h.formAttribute.getText().toString().equals("")){
                h.formAttribute.setText(list.get(position).toString());
            }
            h.c.setTag(position);
            h.c.setText(form.get(list.get(position).toString()).toString());
            convertView.setTag(h);
        }else{
            h = (ViewHolder) convertView.getTag();
        }
        int tag_index = (Integer) h.c.getTag();
        h.c.setId(tag_index);
        h.c.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                final int position = h.c.getId();
                final EditText Caption = (EditText) h.c;
                if(Caption.getText().toString().length()>0){
                    form.put(list.get(position).toString(),Caption.getText().toString());
                }else{
                    form.put(list.get(position).toString(), "");
                    Toast.makeText(context, "Please enter form field", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        return convertView;
    }
}

class ViewHolder{
    EditText c;
    TextView formAttribute;
}