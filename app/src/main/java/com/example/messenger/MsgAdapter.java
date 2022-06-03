package com.example.messenger;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

public class MsgAdapter extends ArrayAdapter<Message> {

    public MsgAdapter(Context context, Message[] arr) {
        super(context, R.layout.adapter_item, arr);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        final Message message = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.adapter_item, null);
        }

        ((TextView) convertView.findViewById(R.id.nameTextView)).setText(message.name);
        ((TextView) convertView.findViewById(R.id.msgTextView)).setText(String.valueOf(message.message));

        return convertView;
    }
}
