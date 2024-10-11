package fr.esisar.exercice1;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class CopyFile {

    public static void main(String[] args) throws IOException {
        String fromFile = "esisar.bmp";
        String toFile = "esisar2.bmp";

        System.out.println("Debut de la copie de " + fromFile + " vers "+ toFile);
        FileInputStream fis = new FileInputStream(fromFile);

        byte[] bs = new byte[64*1024];
        int len = 0;
        //String str = "";

        FileOutputStream fos = new FileOutputStream(toFile); //TODO Solution

        while (len != -1) {
            //str = str + new String(bs, 0, len);
            fos.write(bs, 0, len); //TODO Solution
            len = fis.read(bs);
        }
        fis.close();

        //FileOutputStream fos = new FileOutputStream(toFile);
        //fos.write(str.getBytes());
        fos.close();
        System.out.println("Fin de la copie");
    }
}
