package ieee.donn.Main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONException;
import org.json.JSONObject;

import ieee.donn.R;

public class ConnectUsers extends AppCompatActivity {

    Toolbar toolbar;
    TextView data, patient;
    Button call, message, facebookThem;
    String blood, city, email, phone, facebook, name;
    FirebaseAuth mFirebaseAuth;
    Button logout;
    //TextView name, email, phone, blood, facebook, country;
String phone_number;


    String mUserId;
    FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_connect_users);

        toolbar = (Toolbar) findViewById(R.id.toolbar);


        if (getIntent().getExtras() != null) {

            for (String key : getIntent().getExtras().keySet()) {

                String value = getIntent().getExtras().getString(key);

                Log.e("dhg" , value);

               //  if (key.equals("AnotherActivity")) {

                    try {

                        JSONObject json_data = new JSONObject(value);


                        city = json_data.getString("country");
                       email = json_data.getString("email");
//                      //  facebook = json_data.getString("facebook");
                        name = json_data.getString("name");
                        phone = json_data.getString("phone");
                      //  Log.d(value, "This is my message");
                    //    mUserId = value;


                    } catch (JSONException e) {

                        e.printStackTrace();

                    }

                //}
            }

        }
     //   mFirebaseAuth = FirebaseAuth.getInstance();
   //     mFirebaseUser = mFirebaseAuth.getCurrentUser();
      //  mUserId = mFirebaseUser.getUid();

        data = (TextView) findViewById(R.id.data);
        patient = (TextView) findViewById(R.id.patient);
        call = (Button) findViewById(R.id.call);
     //   facebookThem = (Button) findViewById(R.id.facebook);
        message = (Button) findViewById(R.id.message);


       data.setText("Phone :  " + phone + "\nEmail:  " + email );
       patient.setText("Name :  " + name + "\nBlood Type :  " + blood);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
//        mDatabase.child("users").child(mUserId).child("data").addListenerForSingleValueEvent(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
////                name.setText("" + dataSnapshot.child("name").getValue());
////                email.setText("Email : " + dataSnapshot.child("email").getValue());
////                //     facebook.setText("Facebook : " + dataSnapshot.child("facebook").getValue());
////                country.setText("City : " + dataSnapshot.child("city").getValue());
////                phone.setText("Phone : " + dataSnapshot.child("phone").getValue());
////                blood.setText("Blood Type : " + dataSnapshot.child("blood_group").getValue());
//                data.setText("Phone :  " +  dataSnapshot.child("phone").getValue() + "\nEmail:  " + dataSnapshot.child("email").getValue() );
//                patient.setText("Name :  " + dataSnapshot.child("name").getValue() + "\nBlood Type :  " + dataSnapshot.child("blood_group").getValue());
//         //   phone_number =(dataSnapshot.child("phone").getValue()).toString();
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//






        ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent callIntent = new Intent(Intent.ACTION_CALL); //use ACTION_CALL class
                callIntent.setData(Uri.parse("tel:" + phone));    //this is the phone number calling
                //check permission
                //If the device is running Android 6.0 (API level 23) and the app's targetSdkVersion is 23 or higher,
                //the system asks the user to grant approval.
                if (ActivityCompat.checkSelfPermission(ConnectUsers.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    //request permission from user if the app hasn't got the required permission
                    ActivityCompat.requestPermissions(ConnectUsers.this,
                            new String[]{Manifest.permission.CALL_PHONE},   //request specific permission from user
                            10);
                    return;
                } else {     //have got permission
                    try {
                        startActivity(callIntent);  //call activity and make phone call
                    } catch (android.content.ActivityNotFoundException ex) {
                        Toast.makeText(getApplicationContext(), "yourActivity is not found", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });


        message.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Uri sms_uri = Uri.parse("smsto:" + phone);
                Intent sms_intent = new Intent(Intent.ACTION_SENDTO, sms_uri);
                sms_intent.putExtra("sms_body", "Hello, I have requested blood type.");
                startActivity(sms_intent);

            }
        });

    }
}