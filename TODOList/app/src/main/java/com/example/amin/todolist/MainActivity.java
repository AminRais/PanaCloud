package com.example.amin.todolist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends ActionBarActivity {
    private ListView myListView;
    private TodoAdapter myAdapter;
    public static ArrayList<Todo> myTodos;
    public static ArrayList<String> myKeys;
    private final String FIREBASE_REF_FOR_DATA_RETRIEVAL = "https://todoli.firebaseio.com/List/";
    Firebase REF_FOR_RETRIEVAL;
    TodoAdapter che = new TodoAdapter();

    Map<String,Object> myNewTodos = new HashMap<String,Object>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Firebase.setAndroidContext(this);
        myTodos = new ArrayList<Todo>();
        myKeys = new ArrayList<String>();
        myListView = (ListView) findViewById(R.id.myListView);

        REF_FOR_RETRIEVAL = new Firebase(FIREBASE_REF_FOR_DATA_RETRIEVAL);

        REF_FOR_RETRIEVAL.addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
//                Log.d("mainAct onChildAdded",dataSnapshot.getKey());
//                myKeys.add(dataSnapshot.getKey());
                String gettingInsideChildToGetRef = "https://todoli.firebaseio.com/List/"+"/"+dataSnapshot.getKey()+"/";

                Firebase REF_FOR_RETRIEVAL_AGAIN = new Firebase(gettingInsideChildToGetRef);
                REF_FOR_RETRIEVAL_AGAIN.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        if (dataSnapshot.getValue() != null) {
                            if(doComparision(myKeys,dataSnapshot.getKey())) {
                                myKeys.add(dataSnapshot.getKey());
                                myTodos.add(dataSnapshot.getValue(Todo.class));
                            }
                            else {  }
                            myAdapter = new TodoAdapter(MainActivity.this, myTodos);
                            myListView.setAdapter(myAdapter);


                        }
                    }
                    @Override
                    public void onCancelled(FirebaseError firebaseError) {

                    }
                });



            }

            private boolean doComparision(ArrayList<String> keys,String timeStamp){
                int ptr = 0;

                for(int i=0; i<keys.size() ; i++){
                    if(keys.get(i).equals(timeStamp)) {
                        return  false;
                    }
                    else;

                }
                return true;
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {


            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

                Log.d("MainActivity when dlt", dataSnapshot.getValue().toString());
                Log.d("MainActivity when dkt", dataSnapshot.getKey().toString());
                for (int i = 0; i < myTodos.size(); i++) {
                    if (dataSnapshot.getKey().equals(myKeys.get(i))) {
                        myTodos.remove(i);
                        myKeys.remove(i);
                        myAdapter.notifyDataSetChanged();
                    }
                }
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }

        });


        myListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, final int position, long id) {
                Firebase.setAndroidContext(MainActivity.this);

                final String FIREBASE_REF_FOR_DATA_RETRIEVAL1 = "https://todoli.firebaseio.com/List/" + myKeys.get(position)+ "/";
                final Firebase RL1 = new Firebase(FIREBASE_REF_FOR_DATA_RETRIEVAL1);

                RL1.removeValue(new Firebase.CompletionListener() {
                    @Override
                    public void onComplete(FirebaseError firebaseError, Firebase firebase) {

                    }
                });

                return false;
            }
        });

        myListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int myPosition = position;
                Toast.makeText(MainActivity.this,position,Toast.LENGTH_SHORT).show();
                View v = view;
                Firebase Ref = new Firebase("https://todoli.firebaseio.com/List/"+MainActivity.myKeys.get(myPosition)+"/complete/");
                final CheckBox che = (CheckBox) v.findViewById(R.id.isCompleteCheckBox);
                if(che.isChecked()){
                    Ref.setValue(false);
                    Log.d("if CON Main",String.valueOf(((Todo) myTodos.get(myPosition)).isComplete()));
                    che.setChecked(((Todo) myTodos.get(myPosition)).isComplete());
                }
                else {
                    Ref.setValue(true);
                    Log.d("else CON Main",String.valueOf(((Todo) myTodos.get(myPosition)).isComplete()));
                    che.setChecked(((Todo) myTodos.get(myPosition)).isComplete());
                }
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_add_task:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Add a task");
                builder.setMessage("What do you want to do?");
                final EditText inputField = new EditText(this);
                builder.setView(inputField);
                builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Firebase myFirebaseRef = new Firebase("https://todoli.firebaseio.com/");
                        if (!inputField.getText().toString().equals("")) {
                            myFirebaseRef.child("List").push().setValue(new Todo(inputField.getText().toString(),false));
                        }
                        else{
                            Toast.makeText(MainActivity.this,
                                    "Add text so that it can be added to the list",Toast.LENGTH_SHORT).show();
                        }
                    }
                });

                myAdapter = new TodoAdapter(MainActivity.this,myTodos);
                myListView.setAdapter(myAdapter);

                builder.setNegativeButton("Cancel", null);
                builder.create().show();
                return true;

            default:
                return false;
        }
    }
}