import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;


/**
 * Created by GrantRowberry on 1/12/17.
 */

public class ImageEditor {
    public static void main(String[] args) throws FileNotFoundException {
        ImageEditor ie = new ImageEditor();
        ie.run(args);

    }

    public void run(String[] args) throws FileNotFoundException {
        File file = new File(args[0]);
        Scanner s = new Scanner(file).useDelimiter("((#[^\\n]*\\n)|(\\s+))+");
        String mag_num = s.next();
        int width = s.nextInt();
        int height = s.nextInt();
        int maxColorValue = s.nextInt();
        int [][][] pixels;
        pixels = new int[width][height][3];

        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                for(int z = 0; z < 3; ++z){
                    pixels[x][y][z] = s.nextInt();
                }
            }
        }
        int[][][] newPixels = {{{0, 0, 0}, {0, 0, 0}}};
        if(args[2].equals("invert")){
            Image img = new Image(pixels,width,height);
            newPixels = img.invert();
        } else if(args[2].equals("grayscale")){
            Image img = new Image(pixels,width,height);
            newPixels = img.grayscale();
        } else if(args[2].equals("emboss")){
            Image img = new Image(pixels,width,height);
            newPixels = img.emboss();
        } else if(args.length == 4){
            int motionBlurValue = Integer.parseInt(args[3]);
            if(args[2].equals("motionblur")){
                Image img = new Image(pixels,width,height,motionBlurValue);
                newPixels = img.motionBlur(motionBlurValue);
            }
        } else {
            System.out.println("Not a valid argument");
            return;
        }
        File newFile = new File(args[1]);
        PrintWriter pw = new PrintWriter(newFile);
        pw.println(mag_num);
        pw.println(width);
        pw.println(height);
        pw.println(maxColorValue);
        for(int y = 0; y < height; ++y){
            for(int x = 0; x < width; ++x){
                for(int z = 0; z < 3; ++z){
                    pw.println(newPixels[x][y][z]);
                }
            }
        }
        pw.close();
    }



}
