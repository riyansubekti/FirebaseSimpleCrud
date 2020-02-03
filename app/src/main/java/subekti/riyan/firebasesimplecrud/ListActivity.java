package subekti.riyan.firebasesimplecrud;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import subekti.riyan.firebasesimplecrud.adapter.RequestAdapter;
import subekti.riyan.firebasesimplecrud.model.Requests;

public class ListActivity extends AppCompatActivity {

    private DatabaseReference database;

    private ArrayList<Requests> daftarReq;
    private RequestAdapter requestAdapter;

    private RecyclerView rc_list_request;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        database = FirebaseDatabase.getInstance().getReference();

        rc_list_request = findViewById(R.id.rc_list_request);

        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
        rc_list_request.setLayoutManager(mLayoutManager);
        rc_list_request.setItemAnimator(new DefaultItemAnimator());

        loading = ProgressDialog.show(ListActivity.this, null, "Pleas Wait...",
                true, false);

        database.child("simple-crud").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

//                Saat ada data baru, masukan datanya ke ArrayList
                daftarReq = new ArrayList<>();
                for (DataSnapshot noteDataSnapshot : dataSnapshot.getChildren()) {

//                Mapping data pada DataSnapshot ke dalam object Wisata dan juga menyimpan primary key pada object Wisata
//                untuk keperluan Edit dan Delete data

                    Requests requests = noteDataSnapshot.getValue(Requests.class);
                    requests.setKey(noteDataSnapshot.getKey());

//                    Menambahkan object Wisata yang sudah dimapping ke dalam ArrayList

                    daftarReq.add(requests);
                }

//                Inisialisasi adapter dan data hotel dalam bentuk ArrayList dan mengeset Adapter ke dalam RecyclerView

                requestAdapter = new RequestAdapter(daftarReq);
                rc_list_request.setAdapter(requestAdapter);
                loading.dismiss();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
//                Kode ini akan dipanggil ketika ada error dan pengambilan data gagal dan memprint errornya ke logcat
                System.out.println(databaseError.getDetails()+ " " +databaseError.getMessage());
                loading.dismiss();
            }
        });
    }
}
