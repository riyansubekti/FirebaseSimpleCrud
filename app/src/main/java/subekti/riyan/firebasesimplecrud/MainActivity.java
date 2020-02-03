package subekti.riyan.firebasesimplecrud;

import android.app.ProgressDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {
    private DatabaseReference database;

    private EditText etNama, etEmail, etDesk;
    private Button btnSimpan;
    private ProgressDialog loading;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();
        
        etNama = findViewById(R.id.et_nama);
        etEmail = findViewById(R.id.et_email);
        etDesk = findViewById(R.id.et_desk);
        btnSimpan = findViewById(R.id.btn_simpan);

        btnSimpan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String nama = etNama.getText().toString();
                String email = etEmail.getText().toString();
                String desk = etDesk.getText().toString();

                loading = ProgressDialog.show(MainActivity.this, null, "Pleas Wait",
                        true, false);

                submitUser(new Requests(nama.toLowerCase(),email.toLowerCase(),desk.toLowerCase()));

            }
        });
    }

    private void submitUser(Requests requests) {
        database.child("simple-crud")
//                child adalah membuat cabang baru didalam database simple-crud-b171b
//               .child("Table_Manual")
//                Push adalah membuat child dengan nama acak
                .push()
                .setValue(requests)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();

                        etNama.setText("");
                        etEmail.setText("");
                        etDesk.setText("");

                        Toast.makeText(MainActivity.this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}
