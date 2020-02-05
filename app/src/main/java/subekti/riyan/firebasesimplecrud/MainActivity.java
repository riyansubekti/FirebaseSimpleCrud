package subekti.riyan.firebasesimplecrud;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import subekti.riyan.firebasesimplecrud.model.Requests;

public class MainActivity extends AppCompatActivity {

    private DatabaseReference database;

    private TextView tv_judul;
    private EditText etNama, etEmail, etDesk;
    private Button btnSimpan, btnHapus;
    private ProgressDialog loading;

    private String sPid, sPnama, sPemail, sPdesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();

        sPid = getIntent().getStringExtra("id");
        sPnama = getIntent().getStringExtra("nama");
        sPemail = getIntent().getStringExtra("email");
        sPdesk = getIntent().getStringExtra("desk");

        tv_judul = findViewById(R.id.tv_judul);
        etNama = findViewById(R.id.et_nama);
        etEmail = findViewById(R.id.et_email);
        etDesk = findViewById(R.id.et_desk);
        btnSimpan = findViewById(R.id.btn_simpan);
        btnHapus = findViewById(R.id.btn_hapus);

        etNama.setText(sPnama);
        etEmail.setText(sPemail);
        etDesk.setText(sPdesk);

        if (sPid.equals("")){
            btnSimpan.setText("Simpan");
            btnHapus.setText("NULL");
            tv_judul.setText("Tambah Data");
            btnHapus.setVisibility(View.INVISIBLE);

        } else {
            btnSimpan.setText("Ubah");
            btnHapus.setText("Hapus");
            tv_judul.setText("Edit Data");
            btnHapus.setVisibility(View.VISIBLE);
        }

        btnSimpan.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                String nama = etNama.getText().toString();
                String email = etEmail.getText().toString();
                String desk = etDesk.getText().toString();

                if (btnSimpan.getText().equals("Simpan")) {
                    // perintah save

                    loading = ProgressDialog.show(MainActivity.this, null, "Pleas Wait",
                            true, false);

                    submitUser(new Requests(nama.toLowerCase(), email.toLowerCase(), desk.toLowerCase()));

                }else {
                    // perintah edit
                    loading = ProgressDialog.show(MainActivity.this,
                            null,
                            "Please wait...",
                            true,
                            false);

                    editUser(new Requests(
                            nama.toLowerCase(),
                            email.toLowerCase(),
                            desk.toLowerCase()), sPid);
                }

            }
        });

        btnHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (btnHapus.getText().equals("Hapus")) {
                    loading = ProgressDialog.show(MainActivity.this, null, "Pleas Wait",
                            true, false);
                    deleteUser(new Requests());
                }
            }
        });

    }

    private void deleteUser(Requests requests) {
        database.child("simple-crud")
                .child(sPid)
                .removeValue()
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        loading.dismiss();
                        Toast.makeText(MainActivity.this, "Data Berhasil Dihapus", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
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
                        startActivity(new Intent(MainActivity.this, ListActivity.class));
                    }
                });
    }

    private void editUser(Requests requests, String id) {
        database.child("simple-crud")
                .child(id)
                .setValue(requests)
                .addOnSuccessListener(this, new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                        loading.dismiss();

                        etNama.setText("");
                        etEmail.setText("");
                        etDesk.setText("");

                        Toast.makeText(MainActivity.this,
                                "Data Berhasil diedit",
                                Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(MainActivity.this, ListActivity.class));

                    }

                });
    }
}
