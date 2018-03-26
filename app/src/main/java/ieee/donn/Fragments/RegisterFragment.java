package ieee.donn.Fragments;

import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.ArrayList;
import java.util.Locale;

import cn.pedant.SweetAlert.SweetAlertDialog;
import dmax.dialog.SpotsDialog;
import ieee.donn.Main.MainActivity;
import ieee.donn.R;


public class RegisterFragment extends Fragment {

    View root;
    Toolbar toolbar;
    Locale[] locale;
    Spinner spinner , blood_type;
    Button register;
    String country, blood_group;
    SharedPreferences spf;
    SharedPreferences.Editor edit;
    ArrayList<String> cities , blood_group_list;
    ArrayAdapter<String> adapter;
    EditText name, email, phone, blood, facebook, password;
    String nameStr, emailStr, phoneStr, bloodStr, facebookStr, passwordStr;

    String mUserId;
    FirebaseAuth mFirebaseAuth;
    DatabaseReference mDatabase;
    FirebaseUser mFirebaseUser;


    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        root = inflater.inflate(R.layout.register, container, false);
        register = (Button) root.findViewById(R.id.reg);
        name = (EditText) root.findViewById(R.id.editText);
        email = (EditText) root.findViewById(R.id.editText2);
        password = (EditText) root.findViewById(R.id.editText7);
        phone = (EditText) root.findViewById(R.id.editText3);
       // facebook = (EditText) root.findViewById(R.id.editText5);
      //  blood = (EditText) root.findViewById(R.id.editText4);
        spinner = (Spinner) root.findViewById(R.id.spinner);
        blood_type = (Spinner) root.findViewById(R.id.blood_type);

        setupSpinner();
       setupSpinner1();
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                int position = spinner.getSelectedItemPosition();

                if (!(position == 0) || !(position == 1)) {

                    country = cities.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });
        blood_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {

                int position = blood_type.getSelectedItemPosition();

                if (!(position == 0) || !(position == 1)) {

                    blood_group = blood_group_list.get(position);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> arg0) {
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                nameStr = name.getText().toString();
                emailStr = email.getText().toString();
                phoneStr = phone.getText().toString();
//                facebookStr = facebook.getText().toString();
                passwordStr = password.getText().toString();


                if (nameStr.isEmpty() || emailStr.isEmpty() ||
                        phoneStr.isEmpty()  ||
                        passwordStr.isEmpty()) {

                    Toast.makeText(getActivity(), "Fill all data please..", Toast.LENGTH_LONG).show();


                } else {
                    final AlertDialog dialog = new SpotsDialog(getActivity(), "Loginn' in..");
                    dialog.show();

                    mFirebaseAuth = FirebaseAuth.getInstance();

                    mFirebaseAuth.createUserWithEmailAndPassword(emailStr, passwordStr)
                            .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {

                                    if (task.isSuccessful()) {

                                        mFirebaseAuth = FirebaseAuth.getInstance();
                                        mFirebaseUser = mFirebaseAuth.getCurrentUser();
                                        mUserId = mFirebaseUser.getUid();

                                        mDatabase = FirebaseDatabase.getInstance().getReference();

                                        mDatabase.child("users").child(mUserId).child("data").child("email").setValue((email.getText().toString()));
                                        mDatabase.child("users").child(mUserId).child("data").child("name").setValue((name.getText().toString()));
                                        mDatabase.child("users").child(mUserId).child("data").child("phone").setValue((phone.getText().toString()));
                                        mDatabase.child("users").child(mUserId).child("data").child("password").setValue((password.getText().toString()));
                                    //    mDatabase.child("users").child(mUserId).child("data").child("facebook").setValue((facebook.getText().toString()));
                                     //   mDatabase.child("users").child(mUserId).child("data").child("blood").setValue((blood.getText().toString().toUpperCase()));
                                        mDatabase.child("users").child(mUserId).child("data").child("city").setValue((country));
                                       mDatabase.child("users").child(mUserId).child("data").child("blood_group").setValue(blood_group);


                                        save("email" , emailStr);
                                        save("phone", phoneStr);
                                        save("facebook", facebookStr);
                                        save("name", nameStr);


                                        mFirebaseAuth.signInWithEmailAndPassword(emailStr, passwordStr)
                                                .addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<AuthResult> task) {
                                                        if (task.isSuccessful()) {

                                                            FirebaseMessaging.getInstance().subscribeToTopic("Ap");
                                                            FirebaseMessaging.getInstance().subscribeToTopic("Am");
                                                            FirebaseMessaging.getInstance().subscribeToTopic("Bp");
                                                            FirebaseMessaging.getInstance().subscribeToTopic("Bm");
                                                            FirebaseMessaging.getInstance().subscribeToTopic("Op");
                                                            FirebaseMessaging.getInstance().subscribeToTopic("Om");
                                                            FirebaseMessaging.getInstance().subscribeToTopic("ABp");
                                                            FirebaseMessaging.getInstance().subscribeToTopic("ABm");


                                                            Log.d("AndroidBash", "Subscribed");
                                                            Toast.makeText(getActivity(), "Subscribed", Toast.LENGTH_SHORT).show();
                                                            String token = FirebaseInstanceId.getInstance().getToken();
                                                            Log.d("AndroidBash", token);


                                                            dialog.dismiss();
                                                            startActivity(new Intent(getActivity(), MainActivity.class));
                                                            getActivity().finish();
                                                            save("code", "1");

                                                        } else {

                                                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                                    .setTitleText("Error")
                                                                    .setContentText(task.getException().getMessage())
                                                                    .setConfirmText("Retry")
                                                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                                        @Override
                                                                        public void onClick(SweetAlertDialog sDialog) {
                                                                            sDialog.dismissWithAnimation();
                                                                        }
                                                                    })
                                                                    .show();

                                                        }
                                                    }
                                                });


                                    } else {

                                        new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                                .setTitleText("Error")
                                                .setContentText(task.getException().getMessage())
                                                .setConfirmText("Retry")
                                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                    @Override
                                                    public void onClick(SweetAlertDialog sDialog) {
                                                        sDialog.dismissWithAnimation();
                                                    }
                                                })
                                                .show();
                                    }

                                }
                            });

                }
            }
        });


        return root;


    }


    ////////Methods/////////    ////////Methods/////////    ////////Methods////////

    public void setupSpinner() {

        setupCountriesList();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, cities);
        spinner.setAdapter(adapter);


    }

    public void setupCountriesList() {

        locale = Locale.getAvailableLocales();
        cities = new ArrayList<String>();

        cities.add("Select City");
        cities.add("Mumbai");
        cities.add("Ahmednagar");
        cities.add("Gandhinagar");
        cities.add("Hyderabad");
        cities.add("Belgaum");
        cities.add("Chennai");
        cities.add("Bangalore");
        cities.add("Latur");
        cities.add("Allahabad");
        cities.add("Gorakhpur");
        cities.add("Aligarh");
        cities.add("Itanagar");
        cities.add("Delhi");
        cities.add("Agra");
        cities.add("Pondicherry");
        cities.add("Kolkata");
        cities.add("Chandigarh");
        cities.add("Amritsar");
        cities.add("Jaipur");
        cities.add("Ranchi");
        cities.add("Tehri");
        cities.add("Rudraprayag");
        cities.add("Srinagar");
        cities.add("Shimla");
        cities.add("Patna");

    }
    public void setupSpinner1() {

        setupCountriesList1();

        adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, blood_group_list);
        blood_type.setAdapter(adapter);


    }

    public void setupCountriesList1() {

        locale = Locale.getAvailableLocales();
        blood_group_list = new ArrayList<String>();

        blood_group_list.add("Select Blood Group");
        blood_group_list.add("A+");
        blood_group_list.add("A-");
        blood_group_list.add("B+");
        blood_group_list.add("B-");
        blood_group_list.add("O+");
        blood_group_list.add("O-");
        blood_group_list.add("AB+");
        blood_group_list.add("AB-");


    }

    public void save(String key, String value) {

        spf = PreferenceManager.getDefaultSharedPreferences(getActivity());
        edit = spf.edit();
        edit.putString(key, value);
        edit.commit();

    }

}



