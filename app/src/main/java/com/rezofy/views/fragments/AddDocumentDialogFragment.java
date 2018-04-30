package com.rezofy.views.fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.facebook.internal.Utility;
import com.rezofy.R;
import com.rezofy.utils.RealPathUtil;
import com.rezofy.utils.Utils;
import com.rezofy.views.activities.AddDocumentActivity;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileDescriptor;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.Date;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by linchpin on 7/2/17.
 */

public class AddDocumentDialogFragment extends DialogFragment {


    final int REQUEST_CAMERA = 101;
    final int SELECT_FILE = 102;
    final int REQUEST_ADD_DOCUMENT = 103;
    int userChoosenTask = 0;
    String imagePath;
    String pictureImagePath = "";
    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder;
        final AlertDialog alertDialog;


        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(getActivity().LAYOUT_INFLATER_SERVICE);
        View layout = inflater.inflate(R.layout.dialog_add_my_document,null);
        initViews(layout);
        builder = new AlertDialog.Builder(getActivity());
        builder.setView(layout);
        alertDialog = builder.create();
        alertDialog.getWindow().setBackgroundDrawableResource(R.drawable.rounded_dialog);
        return alertDialog;

    }

    public void initViews(View view)
    {
        RelativeLayout rlCamera = (RelativeLayout)view.findViewById(R.id.rlCamera);
        rlCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = REQUEST_CAMERA;
                boolean result= Utils.checkPermission(getActivity());
                if(result)
                {
                    cameraIntent();

                }
                else
                {

                }

            }
        });

        RelativeLayout rlGallery = (RelativeLayout)view.findViewById(R.id.rlGallery);
        rlGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userChoosenTask = SELECT_FILE;
                boolean result= Utils.checkPermission(getActivity());
                if(result)
                {
                    galleryIntent();
                }
                else
                {

                }
            }
        });

    }
    private void cameraIntent()
    {
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
    /*    String imageFileName = timeStamp + ".jpg";*/
        File directory =  new File(Environment.getExternalStorageDirectory(),
                "tripbox");
        if(!directory.exists())
            directory.mkdir();
        File storageDir = new File(Environment.getExternalStorageDirectory(),
                "tripbox/"+timeStamp + ".jpg");

        imagePath = storageDir.getAbsolutePath();

        Uri outputFileUri = Uri.fromFile(storageDir);
        Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
        startActivityForResult(cameraIntent, REQUEST_CAMERA);
    }

    private void galleryIntent()
    {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"),SELECT_FILE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utils.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if(userChoosenTask == REQUEST_CAMERA)
                        cameraIntent();
                    else if(userChoosenTask == SELECT_FILE)
                        galleryIntent();
                } else {
//code for deny
                }
                break;
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE) {
                if( onSelectFromGalleryResult(data))
                    moveToAddDocumentActivity();
                else
                    Toast.makeText(getActivity(),"Failed to get image from gallery",Toast.LENGTH_LONG).show();
            }
            else if (requestCode == REQUEST_CAMERA) {
                //onCaptureImageResult(data);
                if(imagePath!=null&&!imagePath.equals("")) {
                    File imgFile = new File(imagePath);
                    if (imgFile.exists()) {
                        Bitmap bm = decodeSampledBitmapFromFile(imagePath);
                        imgFile.delete();
                        saveFileToSdcard(bm);

                        moveToAddDocumentActivity();
                    }
                }
                else
                {
                    Toast.makeText(getActivity(),"Failed to save image", Toast.LENGTH_SHORT).show();
                }

            }
            else if(requestCode == REQUEST_ADD_DOCUMENT)
            {
                Log.d("Document added", "Document Added");
                if(getDialog().isShowing())
                    getDialog().dismiss();
            }
        }
    }

    public static Bitmap decodeSampledBitmapFromFile(String path) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, options.outWidth/2, options.outHeight/2);
        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }

    public static Bitmap decodeSampledBitmapFromFile(String path,
                                                         int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path,options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeFile(path,options);
    }
    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) >= reqHeight
                    && (halfWidth / inSampleSize) >= reqWidth) {
                inSampleSize *= 2;
            }
        }

        return inSampleSize;
    }

    public void moveToAddDocumentActivity()
    {
        Intent intent = new Intent(getActivity(), AddDocumentActivity.class);
        intent.putExtra("path", imagePath);
        startActivityForResult(intent, REQUEST_ADD_DOCUMENT);
    }

    @SuppressWarnings("deprecation")
    private boolean onSelectFromGalleryResult(Intent data) {

        //Bitmap bm=null;
        if (data != null) {
            try {

                String realPath = null;




                try {
                    // SDK < API11
                    if (Build.VERSION.SDK_INT < 11)
                        realPath = RealPathUtil.getRealPathFromURI_BelowAPI11(getActivity(), data.getData());

                        // SDK >= 11 && SDK < 19
                    else if (Build.VERSION.SDK_INT < 19)
                        realPath = RealPathUtil.getRealPathFromURI_API11to18(getActivity(), data.getData());

                        // SDK > 19 (Android 4.4)
                    else
                        realPath = RealPathUtil.getRealPathFromURI_API19(getActivity(), data.getData());
                }catch (Exception e)
                {
                    e.printStackTrace();
                }

                if(realPath==null || realPath.equals(""))
                    realPath = getRealPathFromURI(data.getData());

                if(realPath==null|| realPath.equals(""))
                    return false;

                Bitmap bm = decodeSampledBitmapFromFile(realPath);

                saveFileToSdcard(bm);




            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return  true;

        //ivImage.setImageBitmap(bm);
    }

    private String getRealPathFromURI(Uri contentURI) {
        String result;
        Cursor cursor = getActivity().getContentResolver().query(contentURI, null, null, null, null);
        if (cursor == null) { // Source is Dropbox or other similar local file path
            result = contentURI.getPath();
        } else {
            cursor.moveToFirst();
            int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
            result = cursor.getString(idx);
            cursor.close();
        }
        return result;
    }




    private void saveFileToSdcard(Bitmap bitmap)
    {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File directory =  new File(Environment.getExternalStorageDirectory(),
                "tripbox");
        if(!directory.exists())
            directory.mkdir();

        File destination = new File(Environment.getExternalStorageDirectory(),
                "tripbox/"+System.currentTimeMillis() + ".jpg");

        imagePath = destination.getAbsolutePath();
        Log.d("image path", destination.getAbsolutePath());
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


}
