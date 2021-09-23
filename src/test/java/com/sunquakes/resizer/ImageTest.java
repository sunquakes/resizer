package com.sunquakes.resizer;

import com.sunquakes.resizer.web.ResourceWebServer;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.io.IOException;
import java.net.URL;
import java.util.Base64;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class ImageTest {

    protected String sourceImageBase64 = "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";

    protected String enlargeImageBase64 = "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";

    protected String zoomImageBase64 = "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";

    @Test
    public void scaleRangeBytes() {
        byte[] sourceImage = Base64.getDecoder().decode(sourceImageBase64);

        try {
//            enlarge
            byte[] image = Image.scaleRange(sourceImage, 5 * 1024, 8 * 1024);
            String imageBase64Test = Base64.getEncoder().encodeToString(image);
            assertEquals(imageBase64Test, enlargeImageBase64);
            assertTrue(image.length >= 5 * 1024);
            assertTrue(image.length <= 8 * 1024);

//            zoom
            image = Image.scaleRange(sourceImage, 1 * 1024, 2 * 1024);
            imageBase64Test = Base64.getEncoder().encodeToString(image);
            assertEquals(imageBase64Test, zoomImageBase64);
            assertTrue(image.length >= 1 * 1024);
            assertTrue(image.length <= 2 * 1024);
        } catch (IOException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Test
    public void scaleRangePathname() {
        String sep = java.io.File.separator;
        String pathname = String.format("%s%ssrc%stest%sresources%simage.jpg", System.getProperty("user.dir"), sep, sep, sep, sep);
        try {
            byte[] image = Image.scaleRange(pathname, 1 * 1024, 2 * 1024);
            String imageBase64Test = Base64.getEncoder().encodeToString(image);
            assertEquals(imageBase64Test, zoomImageBase64);
            assertTrue(image.length >= 1 * 1024);
            assertTrue(image.length <= 2 * 1024);
        } catch (IOException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }

    @Rule
    public ExpectedException thrown=ExpectedException.none();

    @Test
    public void scaleRangeCheckForSize() {
        byte[] sourceImage = Base64.getDecoder().decode(sourceImageBase64);
        try {
            Image.scaleRange(sourceImage, 8 * 1024, 5 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("The minimum value should be less than the maximum value."));
        }
    }

    @Test
    public void scaleRangeCheckForNull() {
        try {
            byte[] b = null;
            Image.scaleRange(b, 8 * 1024, 5 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            assertThat(e.getMessage(), is("Cannot specify null for sourceBytes."));
        }
    }

    @Test
    public void scaleRangeCheckForEmpty() {
        try {
            byte[] b = new byte[0];
            Image.scaleRange(b, 8 * 1024, 5 * 1024);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            assertThat(e.getMessage(), is("Cannot specify an empty array for sourceBytes."));
        }
    }

    @Test
    public void scaleRangeFileNotFound() {
        try {
            String path = "./image.jpg";
            Image.scaleRange(path, 1 * 1024, 2 * 1024);
        } catch (IOException e) {
            assertThat(e.getMessage(), is("./image.jpg"));
        }
    }

    @Test
    public void scaleRangeUrl() {
        RWSThread rwsThread = new RWSThread();
        rwsThread.start();

        try {
            byte[] image = Image.scaleRange(new URL("http://localhost:3200/image.jpg"), 1 * 1024, 2 * 1024);
            String imageBase64Test = Base64.getEncoder().encodeToString(image);
            assertEquals(imageBase64Test, zoomImageBase64);
            assertTrue(image.length >= 1 * 1024);
            assertTrue(image.length <= 2 * 1024);
        } catch (IOException e) {
            assertTrue(false);
            e.printStackTrace();
        }
    }
}

// 3707602317
class RWSThread extends Thread {
    @Override
    public void run() {
        ResourceWebServer server = new ResourceWebServer();
        try {
            server.startServer();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
