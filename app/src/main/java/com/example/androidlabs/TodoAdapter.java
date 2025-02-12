package com.example.androidlabs;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class TodoAdapter extends BaseAdapter {
    private Context context;
    private List<TodoItem> list;

    public TodoAdapter(Context context, List<TodoItem> list) {
        this.context = context;
        this.list = list;
    }

    @Override
    public int getCount() { return list.size(); }

    @Override
    public Object getItem(int position) { return list.get(position); }

    @Override
    public long getItemId(int position) { return position; }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context)
                    .inflate(R.layout.todo_item, parent, false);
        }

        TextView textView = convertView.findViewById(R.id.todoTextView);
        TodoItem item = list.get(position);

        textView.setText(item.getText());

        if (item.isUrgent()) {
            textView.setBackgroundColor(Color.RED);
            textView.setTextColor(Color.WHITE);
        } else {
            textView.setBackgroundColor(Color.TRANSPARENT);
            textView.setTextColor(Color.BLACK);
        }

        return convertView;
    }
}
