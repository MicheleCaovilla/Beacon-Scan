package it.unipd.mich.beaconscan;

import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by Mich on 13/03/15.
 */
public class TxtWriter {

    File log = new File(Environment.getExternalStorageDirectory(), "log.txt");

    public void saveOnSD(MyBeacon[] myBeacon, Position pos, String config) {

        try {
            File root = new File(Environment.getExternalStorageDirectory(), "Beacons Data");
            if (!root.exists()) {
                root.mkdirs();
            }

            Calendar c = Calendar.getInstance();
            String giorno = String.valueOf(c.get(Calendar.DATE));
            String mese = String.valueOf(c.get(Calendar.MONTH) + 1);  //+1 because java stupid
            String anno = String.valueOf(c.get(Calendar.YEAR));

            String ore = String.valueOf(c.get(Calendar.HOUR_OF_DAY));
            String minuti = String.valueOf(c.get(Calendar.MINUTE));
            String secondi = String.valueOf(c.get(Calendar.SECOND));

            String nomeCartella = new String(anno + "-" + mese + "-" + giorno + "T" + ore + "." + minuti + "." + secondi + "_x" + String.valueOf(pos.getX()) + "_y" + String.valueOf(pos.getY()) + "_" + config);


            File cartella = new File(root, nomeCartella);
            if (!cartella.exists()) {
                cartella.mkdirs();
            }

            File beacons = new File(cartella, "beacons.txt"); //property of beacons
            FileWriter writer = new FileWriter(beacons);
            writer.append("X:  Y:  Z:  α:\n");
            for (MyBeacon b : myBeacon) {
                writer.append(b.getX() + " " + b.getY() + " " + b.getZ() + " " + b.getAlpha() + " " + "\n");
            }
            writer.flush();
            writer.close();

            File position = new File(cartella, "position.txt");
            writer = new FileWriter(position);
            writer.append("X:  Y:  Configuration:\n");
            writer.append(String.valueOf(pos.getX()) + " " + String.valueOf(pos.getY()) + " " + config);
            writer.flush();
            writer.close();


            log = new File(cartella, "log.txt"); //1 row = calculated position (x,y,z) & distances of beacons
            writer = new FileWriter(log);
            writer.append(String.format("%10S %10S %10S   %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s %4s\n",
                    "X:", "Y:", "Z:", "B0:", "B1:", "B2:", "B3:", "B4:", "B5:", "B6:", "B7:", "B8:", "B9:", "B10:", "B11:"));
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FILE", e.getMessage());
        }
    }


    public void updateLogTxt(MyBeacon[] myBeacon, Position result) {
        try {
            FileWriter writer = new FileWriter(log, true);
            writer.append(String.format(Locale.US, "%10.6f %10.6f %10.6f   ", result.getX(), result.getY(), result.getZ()));
            for (MyBeacon b : myBeacon) {
                writer.append(String.format(Locale.US, "%4s ", b.getDistance()));
            }
            writer.append("\n");
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
            Log.e("FILE", e.getMessage());
        }

    }

}
