package com.example.martinvieth.easyitemregistration;

import android.app.Activity;
import android.content.Context;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


/**
 * Created by Kenneth on 07/01/16.
 */

public class LocalSave extends Activity {
    String FILENAME = "hello_file";
    String string = "test";
    public void save() throws IOException {
        FileOutputStream fos = null;
        try {
            fos = openFileOutput(FILENAME, Context.MODE_PRIVATE);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }
        fos.write(string.getBytes());
        fos.close();
    }

}
