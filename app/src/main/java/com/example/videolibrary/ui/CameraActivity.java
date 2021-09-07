package com.example.videolibrary.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.TextureView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.camera.core.AspectRatio;
import androidx.camera.core.CameraSelector;
import androidx.camera.core.ImageAnalysis;
import androidx.camera.core.ImageProxy;
import androidx.camera.core.Preview;
import androidx.camera.lifecycle.ProcessCameraProvider;
import androidx.camera.view.PreviewView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.videolibrary.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CameraActivity extends AppCompatActivity{
    private int REQUEST_CODE_PERMISSIONS = 101;
    private final String[] REQUIRED_PERMISSIONS = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final String[] CAMERA_PERMISSION = new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"};
    private static final int CAMERA_REQUEST_CODE = 10;
    private Preview imagePreview;
    TextureView textureView;
    PreviewView previewView;
    ListenableFuture<ProcessCameraProvider> cameraProviderListenableFuture;
    ProcessCameraProvider cameraProviderFuture;

    private  ImageAnalysis imageAnalysis;
    private ExecutorService cameraExecutor;
    private String video_to_play;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_camera);
        Intent intent = getIntent();
        video_to_play = intent.getStringExtra("video_path");
        cameraExecutor = Executors.newSingleThreadExecutor();

        previewView=findViewById(R.id.preview_view);
        if (hasCameraPermission()) {
            Log.d("ENaBLED", "Camera Enabled");
            enableCamera();
            //startCamera();
        } else {
            Log.d("NOT ENaBLED", "Camera not Enabled");
            requestPermission();
            //enableCamera();
        }

    }

    public void enableCamera(){
        cameraProviderListenableFuture = ProcessCameraProvider.getInstance(this);
        CameraSelector cameraSelector = new CameraSelector.Builder().requireLensFacing(CameraSelector.LENS_FACING_FRONT).build();
        Log.d("TAG", cameraSelector.toString());
        cameraProviderListenableFuture.addListener(() -> {
            try {
                imageAnalysis = new ImageAnalysis.Builder().setImageQueueDepth(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST).build();
                Image_Analyzer ia = new Image_Analyzer();
                imageAnalysis.setAnalyzer(cameraExecutor, ia);
                Log.d("IMAGE_ANALYZER", ia.toString());
                imagePreview = new Preview.Builder().setTargetAspectRatio(AspectRatio.RATIO_16_9)
                        .setTargetRotation(previewView.getDisplay().getRotation()).build();
                ProcessCameraProvider cameraProvider = cameraProviderListenableFuture.get();
                cameraProvider.bindToLifecycle(this,cameraSelector, imagePreview, imageAnalysis);
                previewView.setImplementationMode(PreviewView.ImplementationMode.COMPATIBLE);
                //previewView.setPreferredImplementationMode(PreviewView.ImplementationMode.SURFACE_VIEW);
                imagePreview.setSurfaceProvider(previewView.getSurfaceProvider());
            }catch (Exception e){
                e.printStackTrace();
            }
        }, ContextCompat.getMainExecutor(this));

    }

    private class Image_Analyzer implements ImageAnalysis.Analyzer{

        @Override
        public void analyze(@NonNull ImageProxy imageProxy) {
            @SuppressLint("UnsafeOptInUsageError") Image mediaImage = imageProxy.getImage();
            if(mediaImage!=null){
                InputImage image =
                        InputImage.fromMediaImage(mediaImage, imageProxy.getImageInfo().getRotationDegrees());

                detectFaces(image);
                imageProxy.close();
            }

        }
        public void detectFaces(InputImage image){
            FaceDetectorOptions options =
                    new FaceDetectorOptions.Builder()
                            .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_ACCURATE)
                            .setLandmarkMode(FaceDetectorOptions.LANDMARK_MODE_ALL)
                            .setClassificationMode(FaceDetectorOptions.CLASSIFICATION_MODE_ALL)
                            .setMinFaceSize(0.15f)
                            .enableTracking()
                            .build();
            FaceDetector detector = FaceDetection.getClient(options);
            Task<List<Face>> result =
                    detector.process(image)
                            .addOnSuccessListener(
                                    faces -> {
                                        Log.d("FACES", faces.toString());
                                        for (Face face : faces) {
                                            Log.d("FACES", face.toString());

                                            if (face.getRightEyeOpenProbability() > 0.4 || face.getLeftEyeOpenProbability() > 0.4) {
                                                Intent intent  = new Intent(getApplicationContext(), SingleVideoView.class);
                                                intent.putExtra("video_path", video_to_play);
                                                startActivity(intent);
                                                finish();
                                            }

                                        }
                                        // [END get_face_info]
                                        // [END_EXCLUDE]

                                    })
                            .addOnFailureListener(
                                    new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            // Task failed with an exception
                                            // ...
                                        }
                                    });

        }

    }

    private boolean hasCameraPermission() {
        return ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
        ) == PackageManager.PERMISSION_GRANTED;
    }
    private void requestPermission() {
        ActivityCompat.requestPermissions(
                this,
                CAMERA_PERMISSION,
                CAMERA_REQUEST_CODE
        );
    }
}