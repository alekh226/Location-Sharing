package project226.a000webhostapp.com.demo;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class MainActivity extends AppCompatActivity{
    public static String  TAG ="Main";
    public  Button showButton;
    public  int counter=55;
    public static String username = "Alekh";
    public static String key ="XYZ";
    private static  String addKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize Firebase
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference();


        //On click button is clicked show map
        showButton = (Button)findViewById(R.id.showgraph);

        //configure initial path
        myRef.child(key);
        myRef = database.getReference().child(key);
        myRef.child("userName").setValue(username);
        myRef.child("accessList");
        //Read from firebase
       /* myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                //String value = dataSnapshot.getValue().toString();
                Object latitude =  dataSnapshot.getValue();
                Log.d("firebaseStatus", "Value is: " + latitude);
            }

            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w(TAG, "Failed to read value.", error.toException());
            }
        });*/

        //starting mapActivity on click
        showButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent =new Intent(MainActivity.this,MapsActivity.class);
                Bundle bundle =new Bundle();
                bundle.putString("username",username);
                //Log.d("TTTTTT","in main:"+username);
                bundle.putString("key",key);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });


       // final GetAddress getAddress=new GetAddress();



    }
    public void startSharingClicked(View view){
        SelectUser selectUser =new SelectUser();

        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.activityMain,selectUser);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
       // manager.beginTransaction().replace(R.id.activityMain,selectUser).commit();
    }
    public void addNewUserClicked(View view){
        AddUser newUser =AddUser.newInstance(key,"prams2");
        SelectUser selectUser =new SelectUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(selectUser);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.activityMain,newUser);
        fragmentTransaction.commit();

    }
    public void existingUserClicked(View view){
        UserList userList=new UserList();
        SelectUser selectUser =new SelectUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.popBackStackImmediate();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.remove(selectUser);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.add(R.id.activityMain,userList);
        fragmentTransaction.commit();
    }

    public void shareKeyClicked(View view){
        String whatsAppMessage = "This Is my Key :XYZ";

        Intent sendIntent = new Intent();
        sendIntent.setAction(Intent.ACTION_SEND);
        String title = getResources().getString(R.string.chooser_title);
        sendIntent.putExtra(Intent.EXTRA_TEXT, whatsAppMessage);
        sendIntent.setType("text/plain");
        Intent chooser = Intent.createChooser(sendIntent, title);
        // Do not forget to add this to open whatsApp App specifically
        /*sendIntent.setPackage("com.whatsapp");
        sendIntent.setPackage("com.bsb.hike");
        startActivity(sendIntent);*/
        if (sendIntent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

   /* public  void addNewKeyClicked (View view){
        AddUser addUser =new AddUser();
        FragmentManager fragmentManager = getSupportFragmentManager();
        AddUser fragment =(AddUser) fragmentManager.findFragmentById(R.id.addUserFragement);
        EditText addKey1 = (EditText) fragment.enterUserKey.getText();
        GetUsers getUsers =new GetUsers(key);
        //String temp = getUsers.addUser(addKey1);
        Toast.makeText(this,addKey1+"Entered",Toast.LENGTH_SHORT).show();
    }*/


}
