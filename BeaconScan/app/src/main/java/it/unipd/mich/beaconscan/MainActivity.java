package it.unipd.mich.beaconscan;

import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Vibrator;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;


public class MainActivity extends Activity implements IBeaconListener {


    private static final int REQUEST_BLUETOOTH_ENABLE = 1;

    private static ArrayList<IBeacon> ibeacons;
    private static IBeaconProtocol ibprot;

    MyBeacon[] myBeacons = new MyBeacon[12]; //I've got 12 beacons
    Position pos;
    String config;
    TxtWriter writer = new TxtWriter();

    private ViewSwitcher switcher;
    private static final int REFRESH_SCREEN = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        switcher = (ViewSwitcher) findViewById(R.id.profileSwitcher);
        /*https://inphamousdevelopment.wordpress.com/2010/10/11/using-a-viewswitcher-in-your-android-xml-layouts/
        switcher.showNext();  // Switches to the next view
        switcher.showPrevious();  // Switches to the previous view*/

        final Button bStart = (Button) findViewById(R.id.buttonStart);
        final Button bStop = (Button) findViewById(R.id.buttonStop2);

        final Timer[] timer = {new Timer()};

        if (ibeacons == null)
            ibeacons = new ArrayList<>();

        IBeaconProtocol.SCANNING_PERIOD = 5000;

        ibprot = IBeaconProtocol.getInstance(this);
        ibprot.setListener(this);

        //Log.e("TEST", "Press start");

        //Press Start button
        bStart.setOnClickListener(new View.OnClickListener() { //do not click more than one time the button! Press stop or wait the end
            public void onClick(View v) { //if less than 3 beacon, can't do nlateration,app continue infinitely to scan

                switcher.showNext();  //Don't know why I must put it here but works

                final MapView map = (MapView) findViewById(R.id.mapView);

                //get values from UI and set them
                //1 set position and configuration (hand, pocket, land, etc) of mobile phone
                TextView phoneX = (TextView) findViewById(R.id.PhoneX);
                double xPhone = Double.valueOf(phoneX.getText().toString());

                TextView phoneY = (TextView) findViewById(R.id.PhoneY);
                double yPhone = Double.valueOf(phoneY.getText().toString());

                pos = new Position(xPhone, yPhone);

                TextView phoneConfig = (TextView) findViewById(R.id.PhoneConfig);
                config = phoneConfig.getText().toString();

                //2 set configuration of map dimensions

                TextView MapX = (TextView) findViewById(R.id.MapX);
                double xMap = Double.valueOf(MapX.getText().toString());

                TextView MapY = (TextView) findViewById(R.id.MapY);
                double yMap = Double.valueOf(MapY.getText().toString());

                //3 set initial values for beacons, distances are still unknown

                //beacon #0
                TextView beacon0X = (TextView) findViewById(R.id.beacon0X);
                double x0 = Double.valueOf(beacon0X.getText().toString());

                TextView beacon0Y = (TextView) findViewById(R.id.beacon0Y);
                double y0 = Double.valueOf(beacon0Y.getText().toString());

                TextView alpha0 = (TextView) findViewById(R.id.alpha0);
                double a0 = Double.valueOf(alpha0.getText().toString());

                myBeacons[0] = new MyBeacon(x0, y0, "nil", a0, 0);


                //beacon #1
                TextView beacon1X = (TextView) findViewById(R.id.beacon1X);
                double x1 = Double.valueOf(beacon1X.getText().toString());

                TextView beacon1Y = (TextView) findViewById(R.id.beacon1Y);
                double y1 = Double.valueOf(beacon1Y.getText().toString());

                TextView alpha1 = (TextView) findViewById(R.id.alpha1);
                double a1 = Double.valueOf(alpha1.getText().toString());

                myBeacons[1] = new MyBeacon(x1, y1, "nil", a1, 1);


                //beacon #2
                TextView beacon2X = (TextView) findViewById(R.id.beacon2X);
                double x2 = Double.valueOf(beacon2X.getText().toString());

                TextView beacon2Y = (TextView) findViewById(R.id.beacon2Y);
                double y2 = Double.valueOf(beacon2Y.getText().toString());

                TextView alpha2 = (TextView) findViewById(R.id.alpha2);
                double a2 = Double.valueOf(alpha2.getText().toString());

                myBeacons[2] = new MyBeacon(x2, y2, "nil", a2, 2);


                //beacon #3
                TextView beacon3X = (TextView) findViewById(R.id.beacon3X);
                double x3 = Double.valueOf(beacon3X.getText().toString());

                TextView beacon3Y = (TextView) findViewById(R.id.beacon3Y);
                double y3 = Double.valueOf(beacon3Y.getText().toString());

                TextView alpha3 = (TextView) findViewById(R.id.alpha3);
                double a3 = Double.valueOf(alpha3.getText().toString());

                myBeacons[3] = new MyBeacon(x3, y3, "nil", a3, 3);


                //beacon #4
                TextView beacon4X = (TextView) findViewById(R.id.beacon4X);
                double x4 = Double.valueOf(beacon4X.getText().toString());

                TextView beacon4Y = (TextView) findViewById(R.id.beacon4Y);
                double y4 = Double.valueOf(beacon4Y.getText().toString());

                TextView alpha4 = (TextView) findViewById(R.id.alpha4);
                double a4 = Double.valueOf(alpha4.getText().toString());

                myBeacons[4] = new MyBeacon(x4, y4, "nil", a4, 4);


                //beacon #5
                TextView beacon5X = (TextView) findViewById(R.id.beacon5X);
                double x5 = Double.valueOf(beacon5X.getText().toString());

                TextView beacon5Y = (TextView) findViewById(R.id.beacon5Y);
                double y5 = Double.valueOf(beacon5Y.getText().toString());

                TextView alpha5 = (TextView) findViewById(R.id.alpha5);
                double a5 = Double.valueOf(alpha5.getText().toString());

                myBeacons[5] = new MyBeacon(x5, y5, "nil", a5, 5);


                //beacon #6
                TextView beacon6X = (TextView) findViewById(R.id.beacon6X);
                double x6 = Double.valueOf(beacon6X.getText().toString());

                TextView beacon6Y = (TextView) findViewById(R.id.beacon6Y);
                double y6 = Double.valueOf(beacon6Y.getText().toString());

                TextView alpha6 = (TextView) findViewById(R.id.alpha6);
                double a6 = Double.valueOf(alpha6.getText().toString());

                myBeacons[6] = new MyBeacon(x6, y6, "nil", a6, 6);


                //beacon #7
                TextView beacon7X = (TextView) findViewById(R.id.beacon7X);
                double x7 = Double.valueOf(beacon7X.getText().toString());

                TextView beacon7Y = (TextView) findViewById(R.id.beacon7Y);
                double y7 = Double.valueOf(beacon7Y.getText().toString());

                TextView alpha7 = (TextView) findViewById(R.id.alpha7);
                double a7 = Double.valueOf(alpha7.getText().toString());

                myBeacons[7] = new MyBeacon(x7, y7, "nil", a7, 7);


                //beacon #8
                TextView beacon8X = (TextView) findViewById(R.id.beacon8X);
                double x8 = Double.valueOf(beacon8X.getText().toString());

                TextView beacon8Y = (TextView) findViewById(R.id.beacon8Y);
                double y8 = Double.valueOf(beacon8Y.getText().toString());

                TextView alpha8 = (TextView) findViewById(R.id.alpha8);
                double a8 = Double.valueOf(alpha8.getText().toString());

                myBeacons[8] = new MyBeacon(x8, y8, "nil", a8, 8);


                //beacon #9
                TextView beacon9X = (TextView) findViewById(R.id.beacon9X);
                double x9 = Double.valueOf(beacon9X.getText().toString());

                TextView beacon9Y = (TextView) findViewById(R.id.beacon9Y);
                double y9 = Double.valueOf(beacon9Y.getText().toString());

                TextView alpha9 = (TextView) findViewById(R.id.alpha9);
                double a9 = Double.valueOf(alpha9.getText().toString());

                myBeacons[9] = new MyBeacon(x9, y9, "nil", a9, 9);


                //beacon #10
                TextView beacon10X = (TextView) findViewById(R.id.beacon10X);
                double x10 = Double.valueOf(beacon10X.getText().toString());

                TextView beacon10Y = (TextView) findViewById(R.id.beacon10Y);
                double y10 = Double.valueOf(beacon10Y.getText().toString());

                TextView alpha10 = (TextView) findViewById(R.id.alpha10);
                double a10 = Double.valueOf(alpha10.getText().toString());

                myBeacons[10] = new MyBeacon(x10, y10, "nil", a10, 10);


                //beacon #11
                TextView beacon11X = (TextView) findViewById(R.id.beacon11X);
                double x11 = Double.valueOf(beacon11X.getText().toString());

                TextView beacon11Y = (TextView) findViewById(R.id.beacon11Y);
                double y11 = Double.valueOf(beacon11Y.getText().toString());

                TextView alpha11 = (TextView) findViewById(R.id.alpha11);
                double a11 = Double.valueOf(alpha11.getText().toString());

                myBeacons[11] = new MyBeacon(x11, y11, "nil", a11, 11);


                //create file to SD (measurement 0 of 30)
                String state = Environment.getExternalStorageState();
                if (!state.equals(Environment.MEDIA_MOUNTED)) {
                    Toast.makeText(getApplicationContext(), "NO EXTERNAL STORAGE FOUND", Toast.LENGTH_LONG).show();
                } else {
                    writer.saveOnSD(myBeacons, pos, config);
                }

                //scan, update values of distance, calculate lateration and save new data to SD

                timer[0] = new Timer();

                TimerTask searchIbeaconTask = new TimerTask() {

                    private int i = 0;

                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {

                                //phone is in scanning

                                //check number of beacons, calculate nlateration, save data, reset, scan

                                //TEST Log.e("ITERATION #", String.valueOf(i));

                                for (IBeacon b : ibeacons) {
                                    myBeacons[b.getMinor()].setDistance(b.getProximity()); //update distances of mybeacon
                                }

                                //check there are at least 3 beacons to calculate nlateration
                                int count = 0;
                                for (int j = 0; j < 12; j++) {
                                    if (!(myBeacons[j].getDistance().equals("nil"))) {
                                        count++;
                                    }
                                }

                                //TEST Log.e("COuNT", Integer.valueOf(c).toString());

                                if (count < 3) {
                                    scanBeacons();
                                    map.bNlat = new MyBeacon(-10, -10, 0, 1, "R"); //created but not visible, show only when we got result
                                    return; //if less then 3 beacon, do not count the measurement and repeat
                                }


                                //at least 3 beacon -> calculate nlateration

                                final Position nlaterationResult = calculate();

                                writer.updateLogTxt(myBeacons, nlaterationResult);

                                map.bNlat = new MyBeacon(nlaterationResult.getX(), nlaterationResult.getY(), 0, 1, "R");

                                ibeacons.clear();


                            /*  I want to use not updated beacon distances, else use the following code
                                to reset mybeacons:

                                for (int j = 0; j < 12; j++) { //distances reset on my beacon
                                    myBeacons[j].resetDistance();
                                }
                            */


                                i++;
                                if (i >= 30) { //30 measurement then exit
                                    timer[0].cancel();
                                    //notification of end
                                    Vibrator v = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
                                    v.vibrate(1000);
                                    Toast.makeText(getApplicationContext(), "Finished", Toast.LENGTH_SHORT).show();
                                    switcher.showPrevious();  // Back to input view
                                } else {
                                    scanBeacons();
                                }
                            }
                        });

                    }
                };

                switcher.showNext();  // Show map layout

                //send data to mapview

                map.num_col = (int) xMap + 4;
                map.num_row = (int) yMap + 4;

                map.bPhone = new MyBeacon(xPhone, yPhone, 0, 1, "P"); //fake beacon, it is the real position of the mobile phone

                map.bNlat = new MyBeacon(-10, -10, 0, 1, "R"); //created but not visible, show only when we got result

                map.beacons = myBeacons;


                scanBeacons(); //first scan

                Toast.makeText(getApplicationContext(), "Started", Toast.LENGTH_SHORT).show();

                timer[0].scheduleAtFixedRate(searchIbeaconTask, 5000, 6000);

                switcher.showPrevious();  // Back to input view

            }
        });


        bStop.setOnClickListener(new View.OnClickListener() { //stop and reset
            public void onClick(View v) {
                timer[0].cancel();
                //timer[0] = new Timer();
                ibprot.stopScan();
                myBeacons = new MyBeacon[12]; //reset beacon to reset view
                switcher.showPrevious();  // Back to input view
            }
        });

    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_BLUETOOTH_ENABLE) {
            if (resultCode == Activity.RESULT_OK) {
                scanBeacons();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private Position calculate() {

        double start[] = new double[3];

        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;

        //max min X
        for (int i = 0; i < 12; ++i) {
            if (myBeacons[i].getX() < min) {
                min = myBeacons[i].getX();
            } else if (myBeacons[i].getX() > max) {
                max = myBeacons[i].getX();
            }
        }

        start[0] = min + (max - min) / 2;

        //max min Y
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;

        for (int i = 0; i < 12; ++i) {
            if (myBeacons[i].getY() < min) {
                min = myBeacons[i].getY();
            } else if (myBeacons[i].getY() > max) {
                max = myBeacons[i].getY();
            }
        }

        start[1] = min + (max - min) / 2;


        //max min Z
        min = Double.MAX_VALUE;
        max = -Double.MAX_VALUE;

        for (int i = 0; i < 12; ++i) {
            if (myBeacons[i].getZ() < min) {
                min = myBeacons[i].getZ();
            } else if (myBeacons[i].getZ() > max) {
                max = myBeacons[i].getZ();
            }
        }

        start[2] = min + (max - min) / 2;


        int dim = 3;
        double eps = 1.0e-10;
        double scale = 0.1;

        Objfun objf = new Objfunc(myBeacons);

        Constraints c = new Constraint();

        NMSimplex simplex = new NMSimplex(start, dim, eps, scale, objf, c);

        return new Position(start[0], start[1], start[2]);
    }


    @Override
    protected void onStop() {
        ibprot.stopScan();
        super.onStop();
    }


    private void scanBeacons() {
        // Check Bluetooth every time
        Log.i(Utils.LOG_TAG, "Scanning");

        // Filter based on default easiBeacon UUID, remove if not required
        // ibprot.setScanUUID(UUID here);

        if (!IBeaconProtocol.configureBluetoothAdapter(this)) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_BLUETOOTH_ENABLE);
        } else {
            if (ibprot.isScanning())
                ibprot.stopScan();
            ibprot.reset();
            ibprot.startScan();
        }
    }


// The following methods implement the IBeaconListener interface

    @Override
    public void beaconFound(IBeacon ibeacon) {
        ibeacons.add(ibeacon);
        //Log.e("FOUND", String.valueOf(ibeacon.getMinor()) + "  " + String.valueOf(ibeacon.getProximity()) + "m");
    }

    @Override
    public void enterRegion(IBeacon ibeacon) {
        // TODO Auto-generated method stub

    }

    @Override
    public void exitRegion(IBeacon ibeacon) {
        // TODO Auto-generated method stub

    }

    @Override
    public void operationError(int status) {
        Log.i(Utils.LOG_TAG, "Bluetooth error: " + status);
    }


    @Override
    public void searchState(int state) {
        // TODO Auto-generated method stub
    }
}