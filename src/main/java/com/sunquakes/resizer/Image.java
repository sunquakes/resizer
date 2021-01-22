package com.sunquakes.resizer;

import net.coobird.thumbnailator.Thumbnails;

import java.io.*;

/**
 * Image
 */
public class Image {

    private static void checkForNull(byte[] b, String message) {
        if (b == null) {
            throw new NullPointerException(message);
        }
    }

    private static void checkForEmpty(byte[] b, String message) {
        if (b.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    private static void checkForSize(long minSize, long maxSize) {
        if (minSize > maxSize) {
            throw new IllegalArgumentException("The minimum value should be less than the maximum value.");
        }
    }

    /**
     * scaleRange
     *
     * @param sourceBytes Source image bytes
     * @param minSize     Minimum value after conversion
     * @param maxSize     Maximum value after conversion
     * @return Output image bytes
     * @throws IOException Throw exception
     */
    public static byte[] scaleRange(byte[] sourceBytes, long minSize, long maxSize) throws IOException {
        checkForNull(sourceBytes, "Cannot specify null for sourceBytes.");
        checkForEmpty(sourceBytes, "Cannot specify an empty array for sourceBytes.");
        checkForSize(minSize, maxSize);

        byte[] desImageBytes = new byte[0];
        double startAccuracy = 0;
        double endAccuracy = 0;
        double currentAccuracy = 1;

        if (sourceBytes.length > maxSize) { // The source image is larger than the maximum size
            startAccuracy = 0;
            endAccuracy = 1;
        } else { // The source image is smaller than the minimum size
            while (desImageBytes.length == 0) {
                currentAccuracy *= 2;

                ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceBytes);
                ByteArrayOutputStream outputStream = new ByteArrayOutputStream(sourceBytes.length);
                Thumbnails.of(inputStream)
                        .scale(currentAccuracy)
                        .toOutputStream(outputStream);
                byte[] currentImageBytes = outputStream.toByteArray();

                if (currentImageBytes.length <= maxSize && currentImageBytes.length >= minSize) { // By coincidence, the size is in range
                    desImageBytes = currentImageBytes;
                } else if (currentImageBytes.length > maxSize) {
                    startAccuracy = currentAccuracy / 2;
                    endAccuracy = currentAccuracy;
                    break;
                }
                currentImageBytes = null;
            }
        }

//        Use dichotomy to find the first point that happens to be in the range
        while (desImageBytes.length == 0) {
            currentAccuracy = (startAccuracy + endAccuracy) / 2;

            ByteArrayInputStream inputStream = new ByteArrayInputStream(sourceBytes);
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream(sourceBytes.length);
            Thumbnails.of(inputStream)
                    .scale(currentAccuracy)
                    .toOutputStream(outputStream);
            byte[] currentImageBytes = outputStream.toByteArray();

            if (currentImageBytes.length > maxSize) {
                endAccuracy = currentAccuracy;
            } else if (currentImageBytes.length < minSize) {
                startAccuracy = currentAccuracy;
            } else {
                desImageBytes = currentImageBytes;
            }
            currentImageBytes = null;
        }
        return desImageBytes;
    }

    /**
     * scaleRange
     *
     * @param pathname Source image path
     * @param minSize  Minimum value after conversion
     * @param maxSize  Maximum value after conversion
     * @return Output image bytes
     * @throws IOException Throw exception
     */
    public static byte[] scaleRange(String pathname, long minSize, long maxSize) throws IOException {
        File sourceFile = new File(pathname);
        if (!sourceFile.exists()) {
            throw new FileNotFoundException(pathname);
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream((int) sourceFile.length());
        BufferedInputStream in = null;
        in = new BufferedInputStream(new FileInputStream(sourceFile));
        int buf_size = 1024;
        byte[] buffer = new byte[buf_size];
        int len = 0;
        while (-1 != (len = in.read(buffer, 0, buf_size))) {
            bos.write(buffer, 0, len);
        }
        return scaleRange(bos.toByteArray(), minSize, maxSize);
    }
}
