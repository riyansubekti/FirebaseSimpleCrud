package subekti.riyan.firebasesimplecrud;

import android.app.ProgressDialog;
import android.content.ContentProvider;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import subekti.riyan.firebasesimplecrud.model.Requests;

public class MainActivity extends AppCompatActivity {

    private static final int CHOOSE_IMAGE = 101;
    private DatabaseReference database, mDatabaseRef;
    private StorageReference mStorageRef;

    private TextView tv_judul;
    private EditText etNama, etEmail, etDesk;
    private Button btnSimpan, btnHapus;
    private ProgressDialog loading;
    ImageView ivGambar;
    Uri mImageUri;

    private String sPid, sPnama, sPemail, sPdesk;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        database = FirebaseDatabase.getInstance().getReference();
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("simple-crud");
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

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
        ivGambar = findViewById(R.id.iv_gambar);

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

                if (btnSimpan.getText().equals("Simpan")) {
                    // perintah save

                    loading = ProgressDialog.show(MainActivity.this, null, "Pleas Wait",
                            true, false);

                    submitUser();

                }else {
                    // perintah edit
                    loading = ProgressDialog.show(MainActivity.this,
                            null,
                            "Please wait...",
                            true,
                            false);

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

        ivGambar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showImageChooser();
            }
        });

    }

    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }

    private void saveImage() {

    }

    private void showImageChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), CHOOSE_IMAGE);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHOOSE_IMAGE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            mImageUri = data.getData();

            Picasso.get().load(mImageUri).into(ivGambar);
        }
    }

    private void submitUser() {
        final String nama = etNama.getText().toString();
        final String email = etEmail.getText().toString();
        final String desk = etDesk.getText().toString();

        if (mImageUri != null) {
            final StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

//                    Requests request = new Requests(nama,email,desk,fileReference.getDownloadUrl().toString());
//                    String uploadId = mDatabaseRef.push().getKey();
//                    mDatabaseRef.child(uploadId).setValue(request);

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Requests request = new Requests(nama,email,desk,uri.toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(request);
                        }
                    });

                    Toast.makeText(MainActivity.this, "Data Berhasil Ditambahkan", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(MainActivity.this, ListActivity.class));
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(MainActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });

        }else{
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }

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
