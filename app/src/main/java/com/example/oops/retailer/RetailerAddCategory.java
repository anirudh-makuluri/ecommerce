package com.example.oops.retailer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.DocumentsContract;
import android.sax.RootElement;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.oops.Prevalent.Prevalent;
import com.example.oops.R;
import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class RetailerAddCategory extends AppCompatActivity {
    private String Categoryname,Desc,price,pname,saveCurrentDate,saveCurrentTime,productRandomKey,downloadImageUrl;
    private Button AddProduct;
    private EditText Productname,Productdesc,Productprice;
    private ImageView ProductImage;
    private static int GalleryPic=1;
    private ProgressDialog loadingbar;
    private Uri ImageUri;
    private StorageReference productImagesRef;
    private DatabaseReference productRef;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_retailer_add_category);
        AddProduct=findViewById(R.id.add_product);
        Productname=findViewById(R.id.product_name);
        Productprice=findViewById(R.id.product_price);
        Productdesc=findViewById(R.id.product_desc);
        ProductImage=findViewById(R.id.product_image);
        loadingbar=new ProgressDialog(this);

        Categoryname=getIntent().getExtras().get("category").toString();
        productImagesRef= FirebaseStorage.getInstance().getReference().child("ProductImages");
        productRef=FirebaseDatabase.getInstance().getReference().child("Products");

        Toast.makeText(this, Categoryname, Toast.LENGTH_SHORT).show();
        ProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               OpenGallery();
            }
        });

        AddProduct.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });
    }

    private void ValidateProductData() {
        Desc=Productdesc.getText().toString();
        price=Productprice.getText().toString();
        pname=Productname.getText().toString().toLowerCase();

        if(ImageUri==null)
        {
            Toast.makeText(this, "product image is imp", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Desc)) Toast.makeText(this, "write desc", Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(price)) Toast.makeText(this, "write price", Toast.LENGTH_SHORT).show();
        else if(TextUtils.isEmpty(pname)) Toast.makeText(this, "write name", Toast.LENGTH_SHORT).show();
        else
        {
            StoreProductInformation();
        }
    }

    private void StoreProductInformation() {
        loadingbar.setTitle("Adding new product");
        loadingbar.setMessage("wait ");
        loadingbar.setCanceledOnTouchOutside(false);
        loadingbar.show();
        Calendar calendar=Calendar.getInstance();
        SimpleDateFormat currentDate= new SimpleDateFormat("MMM,dd,yyyy");
        saveCurrentDate=currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime= new SimpleDateFormat("HH:mm:ss a");
        saveCurrentTime=currentTime.format(calendar.getTime());

        productRandomKey =saveCurrentDate+saveCurrentTime;
        StorageReference filePath=productImagesRef.child(ImageUri.getLastPathSegment()+productRandomKey+".jpg");
        final UploadTask uploadTask=filePath.putFile(ImageUri);
        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                String message = e.toString();
                Toast.makeText(RetailerAddCategory.this, "122 "+message, Toast.LENGTH_SHORT).show();
                loadingbar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                Toast.makeText(RetailerAddCategory.this, "image uploaded successfully", Toast.LENGTH_SHORT).show();
                Task<Uri> uriTask=uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                         throw task.getException();

                        }
                        downloadImageUrl=filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();
                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl=task.getResult().toString();
                            Toast.makeText(RetailerAddCategory.this, "product image url saved to database successfully", Toast.LENGTH_SHORT).show();
                            SaveProductInfoToDB();
                        }

                    }
                });
            }
        });

    }

    private void SaveProductInfoToDB() {
        HashMap<String,Object> productmap=new HashMap<>();
        productmap.put("pid",productRandomKey);
        productmap.put("date",saveCurrentDate);
        productmap.put("time",saveCurrentTime);
        productmap.put("desc",Desc);
        productmap.put("image",downloadImageUrl);
        productmap.put("category",Categoryname);
        productmap.put("price",price);
        productmap.put("pname",pname);
        productmap.put("stock","in stock");
        productmap.put("retailername",Prevalent.currentonlineUser.getName());
        productRef.child(productRandomKey).updateChildren(productmap).
                addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent=new Intent(getApplicationContext(),RetailerHomeActivity.class);
                            startActivity(intent);

                            loadingbar.dismiss();
                            Toast.makeText(RetailerAddCategory.this, "product added successfully", Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            loadingbar.dismiss();
                            String message=task.getException().toString();
                            Toast.makeText(RetailerAddCategory.this,"140 "+ message, Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void OpenGallery() {
        Intent galleryIntent=new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");
        startActivityForResult(galleryIntent,GalleryPic);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPic && resultCode==RESULT_OK && data!=null)
        {
            ImageUri=data.getData();
            ProductImage.setImageURI(ImageUri);
        }
    }
}