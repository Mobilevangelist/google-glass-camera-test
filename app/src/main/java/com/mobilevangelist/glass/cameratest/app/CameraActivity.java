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
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.os.Bundle;
import android.view.KeyEvent;

//import com.google.android.glass.touchpad.GestureDetector;

/**
 * Camera preview
 */
public class CameraActivity extends Activity {
  private CameraPreview _cameraPreview;
  private Camera _camera;

  //private GestureDetector _gestureDetector;

  @Override
  public void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);

    //setContentView(R.layout.camera_preview);

    //_cameraPreview = (CameraPreview) findViewById(R.id.camerapreview);

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

  class SavePicture implements Camera.PictureCallback {
    @Override
    public void onPictureTaken(byte[] bytes, Camera camera) {
      android.util.Log.d("CameraActivity", "In onPictureTaken().");
    }
  }
}
