/*
 * Copyright (C) 2013 Mobilevangelist.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

package com.mobilevangelist.glass.cameratest.app;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.KeyEvent;

import com.google.android.glass.app.Card;
import com.google.android.glass.timeline.TimelineManager;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Camera preview
 */
public class CameraActivity extends Activity {
  private CameraPreview _cameraPreview;
  private Camera _camera;

  private Context _context = this;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    _cameraPreview = new CameraPreview(this);
    setContentView(_cameraPreview);
  }

  @Override
  public boolean onKeyDown(int keyCode, KeyEvent event) {
    switch (keyCode) {
      // Handle tap events.
      case KeyEvent.KEYCODE_CAMERA:
        android.util.Log.d("CameraActivity", "Camera button pressed.");
        _cameraPreview.getCamera().takePicture(null, null, new SavePicture());
        android.util.Log.d("CameraActivity", "Picture taken.");

        return true;
      case KeyEvent.KEYCODE_DPAD_CENTER:
      case KeyEvent.KEYCODE_ENTER:
        android.util.Log.d("CameraActivity", "Tap.");
        _cameraPreview.getCamera().takePicture(null, null, new SavePicture());

        return true;
      default:
        return super.onKeyDown(keyCode, event);
    }
  }

  public String savePicture(Bitmap image) throws IOException {
    android.util.Log.d("CameraActivity", "Saving picture...");
    SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd_HHmmss_SSS");
    String imageFilename = sdf.format(new Date()) + ".jpg";
    String imageFullPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM) + File.separator + "Camera" + File.separator + imageFilename;
    //android.util.Log.d("CameraActivity", "external dcim dir: " + Environment.getExternalStoragePublicDirectory(Environment.));

    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(imageFullPath);

      image.compress(Bitmap.CompressFormat.JPEG, 100, fos);

      android.util.Log.d("CameraActivity", "Picture saved.");
      return imageFullPath;
    }
    catch (IOException e) {
      e.printStackTrace();

      throw(e);
    }
    finally {
      if (null != fos) {
        try {
          fos.close();
        }
        catch (IOException e) {
          e.printStackTrace();
        }
      }
    }
  }

  class SavePicture implements Camera.PictureCallback {
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
      android.util.Log.d("CameraActivity", "In onPictureTaken().");
      Bitmap image = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);

      try {
        // Save the image
        String imageFilename = savePicture(image);

        Uri uri = Uri.fromFile(new File(imageFilename));
        android.util.Log.d("CameraActivity", "imageFilename: " + imageFilename);
        android.util.Log.d("CameraActivity", "uri: " + uri);
        Card photoCard = new Card(_context);
        photoCard.setImageLayout(Card.ImageLayout.FULL);
        photoCard.addImage(uri);
        android.util.Log.d("CameraActivity", "Inserting into timeline.");
        TimelineManager.from(_context).insert(photoCard);
      }
      catch (IOException e) {
        e.printStackTrace();
      }
    }
  }
}
