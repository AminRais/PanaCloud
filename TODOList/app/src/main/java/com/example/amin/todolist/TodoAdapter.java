package com.example.amin.todolist;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

import com.firebase.client.Firebase;

import java.util.ArrayList;

/**
 * Created by Amin on 6/18/2015.
 */
public class TodoAdapter extends BaseAdapter implements  View.OnLongClickListener{
    private Activity activity;
    private ArrayList<Todo> myTodos;
    private LayoutInflater inflater;

    @Override
    public boolean onLongClick(View v) { return false; }

    public TodoAdapter(){}


    public TodoAdapter(Activity activity, ArrayList<Todo> todos){

        this.activity = activity;
        this.myTodos = todos;
        inflater = activity.getLayoutInflater();

    }


    @Override
    public View getView(int position, final View convertView, ViewGroup parent) {
        View myTodoView = convertView;
        final int myPosition = position;
        if(myTodoView == null){
            myTodoView = inflater.inflate(R.layout.todoitem,null);
        }
        final CheckBox check = (CheckBox) myTodoView.findViewById(R.id.isCompleteCheckBox);
        TextView view = (TextView) myTodoView.findViewById(R.id.todoTitleTextView);
        view.setText(((Todo) myTodos.get(position)).getTitle().toString());

        myTodoView.setOnLongClickListener(this);
        myTodoView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Firebase Ref = new Firebase("https://todoli.firebaseio.com/List/"+MainActivity.myKeys.get(myPosition)+"/complete/");
                if (check.isChecked()) {
                    Ref.setValue(false);

                    ((Todo)MainActivity.myTodos.get(myPosition)).setComplete(false);
                    check.setChecked(((Todo) myTodos.get(myPosition)).isComplete());
                    Log.d("if CON", String.valueOf(((Todo) myTodos.get(myPosition)).isComplete()));

                } else {
                    Ref.setValue(true);
                    ((Todo)MainActivity.myTodos.get(myPosition)).setComplete(true);
                    Log.d("else CON",String.valueOf(((Todo) myTodos.get(myPosition)).isComplete()));
                    check.setChecked(((Todo) myTodos.get(myPosition)).isComplete());

                }

            }
        });

        return myTodoView;
    }

    @Override
    public Object getItem(int position) {   return myTodos.get(position);   }

    @Override
    public int getCount() { return myTodos.size();  }

    @Override
    public long getItemId(int position) {   return 0;   }

}
