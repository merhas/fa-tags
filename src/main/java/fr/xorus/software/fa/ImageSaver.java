package fr.xorus.software.fa;

import org.jsoup.Connection;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by Xorus on 26/10/2015.
 */
public class ImageSaver {
    public static final String OUTPUT_FOLDER = "download";

    public void saveFromResponse(Connection.Response response, String filename) throws IOException {
        File folder = new File(OUTPUT_FOLDER);
        if(!folder.exists()) {
            if(!folder.mkdir()) {
                throw new IOException("Could not mkdir : " + folder);
            }
        }

        FileOutputStream out = (new FileOutputStream(new java.io.File(OUTPUT_FOLDER + filename)));
        out.write(response.bodyAsBytes());  // resultImageResponse.body() is where the image's contents are.
        out.close();
    }
}
