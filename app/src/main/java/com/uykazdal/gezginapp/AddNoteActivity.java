package com.uykazdal.gezginapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static br.com.zbra.androidlinq.Linq.stream;

public class AddNoteActivity extends Activity {
    EditText edtNote;
    Button btnAddNote;
    Button btnBack;
    String firstKey = "";
    String marmaraKey = "";
    String kuzeyMarmaraKey = "";
    String egeKey = "";
    String digerKey = "";
    private final int BURSA_PLAKA = 16;
    private final int BALIKESIR_PLAKA = 10;
    private final int ISTANBUL_PLAKA = 34;
    private final int IZMIR_PLAKA = 35;
    private final String MARMARA = "Marmara";
    private final String KUZEY_MARMARA = "Kuzey Marmara";
    private final String EGE = "Ege";
    private final String KARADENIZ = "Karadeniz";
    DatabaseReference myRef;

    RecyclerView recNote;
    boolean ekliMi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

        edtNote = findViewById(R.id.edt_note);
        btnAddNote = findViewById(R.id.btn_note_add);

        btnAddNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addNote();
            }
        });
        recNote = findViewById(R.id.rec_note);
        recNote.setLayoutManager(new LinearLayoutManager(AddNoteActivity.this));

        FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();

        myRef = firebaseDatabase.getReference().child("GezdigimYerler2");

        firstKey = getNewKey(myRef);
        marmaraKey = getNewKey(myRef);
        kuzeyMarmaraKey = getNewKey(myRef);
        egeKey = getNewKey(myRef);
        digerKey = getNewKey(myRef);


        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                String bolge = getBolge(getPlakaNo(edtNote.getText().toString()));
                List<CityModel> cityModelList = new ArrayList<>();

                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                    final String city = Objects.requireNonNull(ds.child("Karadeniz").child("sehir").child("bursa").child("ilce").getKey());



                   boolean ekliMi = stream(cityModelList).where(i -> i.getName().equals(city)).any();

                    if (!ekliMi) {
                        cityModelList.add(getNewCity(city));

                        CityModel sehir = stream(cityModelList).where(i -> i.getName().equals(city)).firstOrNull();

                        if (sehir != null) {
                            Toast.makeText(AddNoteActivity.this, sehir.getContent(), Toast.LENGTH_SHORT).show();
                        }
                    }
                }

                recNote.setAdapter(new CityAdapter(cityModelList));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


    }

    private CityModel getNewCity(String _string) {
        return new CityModel(_string, 0, "Content" + _string);
    }

    private void addNote() {

        String receivedUserNote = edtNote.getText().toString();


        if (receivedUserNote.length() > 0) {

            String bolge = getBolge(getPlakaNo(receivedUserNote));

            myRef.child(getNewKey(myRef)).child(bolge).child(getNewKey(myRef)).child("sehir").child(receivedUserNote).child(getNewKey(myRef)).child("ilce").setValue("Ilce" + receivedUserNote);
            //myRef.child(notesId2).child(notesId3).getDatabase().getReference().child("nufus").setValue(receivedUserNote);

            Toast.makeText(this, "Başarılı", Toast.LENGTH_SHORT);

        } else {
            Toast.makeText(this, "İşlem Başarısız", Toast.LENGTH_SHORT);
        }

        edtNote.setText("");
    }

    private String getBolge(int plakaNo) {
        switch (plakaNo) {
            case BURSA_PLAKA:
                return MARMARA;
            case BALIKESIR_PLAKA:
                return MARMARA;
            case ISTANBUL_PLAKA:
                return KUZEY_MARMARA;
            case IZMIR_PLAKA:
                return EGE;
            default:
                return KARADENIZ;
        }
    }

    private int getPlakaNo(String city) {
        switch (city) {
            case "Bursa":
                return BURSA_PLAKA;
            case "İstanbul":
                return ISTANBUL_PLAKA;
            case "İzmir":
                return IZMIR_PLAKA;
            default:
                return 53;
        }
    }

    private String getBolgeKey(String bolge) {
        switch (bolge) {
            case MARMARA:
                return marmaraKey;
            case KUZEY_MARMARA:
                return kuzeyMarmaraKey;
            case EGE:
                return egeKey;
            default:
                return digerKey;
        }
    }


    private String getNewKey(DatabaseReference reference) {
        return reference.push().getKey();
    }
}
