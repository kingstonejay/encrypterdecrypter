package com.jay.encrypterdecrypter;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;

public class MainActivity extends AppCompatActivity {

    private static final int STORAGE_PERMISSION_CODE = 112;
    Button encrypt,decrypt;
    TextView msg,password;
    String passwordstring="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        encrypt = findViewById(R.id.encrypt);
        decrypt = findViewById(R.id.decrypt);
        msg = findViewById(R.id.msg);
        password = findViewById(R.id.password);



        createDirs();

        if(isReadStorageAllowed())
        {            requestStoragePermission();       }


        encrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passwordstring = password.getText().toString();

                EncryptFiles();
            }
        });


        decrypt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passwordstring = password.getText().toString();
                DecryptFile();
            }
        });

    }

    private void EncryptFiles() {

        StringBuilder stringBuilder = new StringBuilder();

        String path = Environment.getExternalStorageDirectory() + "/AESENC";

        Log.d("Files", "Path: " + path);

        File directory = new File(path);

        File[] files = directory.listFiles();

      for(File file:files)
      {

          try {
              InputStream is = new FileInputStream(file);
              int size = is.available();
              byte[] buffer = new byte[size];
              is.read(buffer);
              is.close();
              String bufferedString = new String(buffer, "UTF-8");
              bufferedString = AESCrypt.encrypt(passwordstring, bufferedString);

                File sdcard = Environment.getExternalStorageDirectory();
// to this path add a new directory path
                File dir = new File(path);
// create this directory if not already created
                dir.mkdir();
// create the file in which we will write the contents
                File encrypted_file = new File(dir,"encrypted_"+ file.getName());
                FileOutputStream outStream = new FileOutputStream(encrypted_file);
                outStream.write(bufferedString.getBytes());
                outStream.close();

                stringBuilder.append("Encrypted File : "+encrypted_file.getAbsolutePath() + "\n");



          } catch (Exception ex) {
              Log.d("file open jason","fsddfsd" );

              ex.printStackTrace();


          }

          msg.setText(stringBuilder);
                         }

   }


    private void DecryptFile() {

        StringBuilder stringBuilder = new StringBuilder();

        String path = Environment.getExternalStorageDirectory() + "/AESENC_DECRYPTED";

        Log.d("Files", "Path: " + path);

        File directory = new File(path);

        File[] files = directory.listFiles();

        for(File file:files)
        {

            try {
                InputStream is = new FileInputStream(file);
                int size = is.available();
                byte[] buffer = new byte[size];
                is.read(buffer);
                is.close();
                String bufferedString = new String(buffer, "UTF-8");
                bufferedString = AESCrypt.decrypt(passwordstring, bufferedString);
                File dir = new File(path);
                dir.mkdir();

                File decryptfile = new File(dir, "decrypted_" +file.getName());
                FileOutputStream outStream = new FileOutputStream(decryptfile);
                outStream.write(bufferedString.getBytes());
                outStream.close();

                stringBuilder.append("Decrypted File : "+decryptfile.getAbsolutePath() + "\n");

            } catch (Exception ex) {
                Log.d("file open jason","fsddfsd" );

                ex.printStackTrace();


            }

            msg.setText(stringBuilder);
        }

    }





    private void createDirs() {

        File f = new File(Environment.getExternalStorageDirectory(),"AESENC");
        File n = new File(Environment.getExternalStorageDirectory(),"AESENC_DECRYPTED");
        f.mkdirs();
        n.mkdirs();

    }

    private boolean isReadStorageAllowed() {

        int result = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE);

        return result == PackageManager.PERMISSION_GRANTED;

    }


    private void requestStoragePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)){

        }
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)){

        }


        ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE},STORAGE_PERMISSION_CODE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {


        if(requestCode == STORAGE_PERMISSION_CODE){


            if(grantResults.length >0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){



            }else{


            }
        }
    }



}
