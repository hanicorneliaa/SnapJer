package com.example.snapjer1;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.graphics.Bitmap;
import android.util.Log;

import org.tensorflow.lite.Interpreter;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.List;

public class Classifier {

    //private static final String LOG_TAG = Classifier.class.getSimpleName();
    private static final String MODEL_PATH = "model.tflite";
    private static final String LABEL_PATH = "labels.txt";

    private Interpreter interpreter;
    private ByteBuffer inputImage;
    private List<String> labelList;

    private static final int DIM_BATCH_SIZE = 1;
    public static final int DIM_IMG_SIZE_X = 100; // height
    public static final int DIM_IMG_SIZE_Y = 100; // width
    private static final int DIM_PIXEL_SIZE = 3; //rgb

    private final int[] imagePixels = new int[DIM_IMG_SIZE_X * DIM_IMG_SIZE_Y];
    private static final int DIGITS = 28;
    private float[][] outputArray = new float[DIM_BATCH_SIZE][DIGITS];

    public Classifier(Activity activity) throws IOException {
        Interpreter.Options options = new Interpreter.Options();
        interpreter = new Interpreter(loadModelFile(activity), options);
        labelList = loadLabelList(activity);
        inputImage =
                ByteBuffer.allocateDirect(4
                        * DIM_BATCH_SIZE
                        * DIM_IMG_SIZE_X
                        * DIM_IMG_SIZE_Y
                        * DIM_PIXEL_SIZE);
        inputImage.order(ByteOrder.nativeOrder());
    }

    private MappedByteBuffer loadModelFile(Activity activity) throws IOException {
        AssetFileDescriptor fileDescriptor = activity.getAssets().openFd(MODEL_PATH);
        FileInputStream inputStream = new FileInputStream(fileDescriptor.getFileDescriptor());
        FileChannel fileChannel = inputStream.getChannel();
        long startOffset = fileDescriptor.getStartOffset();
        long declaredLength = fileDescriptor.getDeclaredLength();
        return fileChannel.map(FileChannel.MapMode.READ_ONLY, startOffset, declaredLength);
    }

    public String classify(Bitmap bitmap) {
        preprocess(bitmap);
        runInference();
        return postprocess();
    }

    private void preprocess(Bitmap bitmap) {
        convertBitmapToByteBuffer(bitmap);
        Log.d("Classify", "Preprocess Done");
    }

    private void convertBitmapToByteBuffer(Bitmap bitmap) {
        if (inputImage == null) {
            return;
        }
        inputImage.rewind();

        bitmap.getPixels(imagePixels, 0, bitmap.getWidth(), 0, 0,
                bitmap.getWidth(), bitmap.getHeight());

        int pixel = 0;
        for (int i = 0; i < DIM_IMG_SIZE_X; ++i) {
            for (int j = 0; j < DIM_IMG_SIZE_Y; ++j) {
                final int val = imagePixels[pixel++];
                inputImage.putFloat((((val >> 16) & 0xFF))/255.0f);
                inputImage.putFloat((((val >> 8) & 0xFF))/255.0f);
                inputImage.putFloat((((val) & 0xFF))/255.0f);
            }
        }


    }

    private void runInference() {
        interpreter.run(inputImage, outputArray);
        Log.d("Classify", "Inference Done");
    }

    private String postprocess() {
        // Index with highest probability
        int maxIndex = -1;
        float maxProb = 0.0f;
        for (int i = 0; i < outputArray[0].length; i++) {
            if (outputArray[0][i] > maxProb) {
                maxProb = outputArray[0][i];
                maxIndex = i;
            }
        }
        Log.d("Classify", "Postprocess Done");
        String result = labelList.get(maxIndex);

        return result;

    }

    private List<String> loadLabelList(Activity activity) throws IOException {
        labelList = new ArrayList<String>();
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(activity.getAssets().open(LABEL_PATH)));
        String line;
        while ((line = reader.readLine()) != null) {
            labelList.add(line);
        }
        reader.close();
        return labelList;
    }




}
