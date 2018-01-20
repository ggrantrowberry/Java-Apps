/**
 * Created by GrantRowberry on 1/12/17.
 */

public class Image {

    private int [][][] pixels;
    private int width;
    private int height;
    private int blurLength;

    Image (int newPixels [][][], int newWidth, int newHeight){
        pixels = newPixels;
        width = newWidth;
        height = newHeight;

    }

    Image (int newPixels [][][], int newWidth, int newHeight, int newBlurLength){
        pixels = newPixels;
        width = newWidth;
        height = newHeight;
        blurLength = newBlurLength;
    }

    int [][][] invert(){
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                for(int z = 0; z < 3; ++z){
                    pixels[x][y][z] = 255 -pixels[x][y][z];
                }
            }
        }

        return pixels;

    }

    int [][][] grayscale(){
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){

                int grayscaleConverter = 0;

                for(int z = 0; z < 3; ++z){

                    grayscaleConverter += pixels[x][y][z];
                }
                int newGrayscaleValue = grayscaleConverter/3;
                for(int z = 0; z < 3; ++z){
                    pixels[x][y][z] = newGrayscaleValue;
                }
            }
        }
        return pixels;
    }

    int [][][] emboss(){
        int [][][] pixelsCopy = pixels.clone();

        for(int y = height-1; y > -1; --y){
            for(int x = width-1; x > -1; --x) {
                if (x - 1 < 0 || y - 1 < 0) {
                    pixels[x][y][0] = 128;
                    pixels[x][y][1] = 128;
                    pixels[x][y][2] = 128;
                } else {

                    int redDiff = pixels[x][y][0] - pixelsCopy[x - 1][y - 1][0];
                    int greenDiff = pixels[x][y][1] - pixelsCopy[x - 1][y - 1][1];
                    int blueDiff = pixels[x][y][2] - pixelsCopy[x - 1][y - 1][2];
                    int[] diffArray = {redDiff, greenDiff, blueDiff};


                    int maxDifference = 0;
                    int[] absArray = {Math.abs(redDiff), Math.abs(greenDiff), Math.abs(blueDiff)};
                    int maxIndex = 0;
                    for (int i = 0; i < 3; ++i) {
                        if (Math.abs(diffArray[i]) > maxDifference) {
                            maxDifference = absArray[i];
                            maxIndex = i;
                        }
                    }


                    int v = 128 + diffArray[maxIndex];
                    if (v < 0) {
                        v = 0;
                    } else if (v > 255) {
                        v = 255;
                    }
                    pixels[x][y][0] = v;
                    pixels[x][y][1] = v;
                    pixels[x][y][2] = v;


                }
            }
        }
        return pixels;
    }
    int [][][] motionBlur(int blurLength) throws IllegalArgumentException {
        int [][][]pixelsCopy = pixels;

        if(blurLength <= 0){
            throw new IllegalArgumentException("Blur Length must be greater than 0");
        }
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                for(int z = 0; z < 3; ++z){
                    int forAverage = 0;
                    int blurLengthCopy = blurLength;
                    if(x+ blurLengthCopy > width) {
                        //forAverage += pixelsCopy[x][y][z];
                        blurLengthCopy = width - x;
                    }

                    for(int n = 0; n < blurLengthCopy; ++n){

                                forAverage += pixelsCopy[x+n][y][z];


                    }

                        forAverage = forAverage/blurLengthCopy;

                    pixels[x][y][z] = forAverage;
                }
            }
        }

        return pixels;

    }
}
