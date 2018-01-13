package com.ahmet.showmustgoon;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.Spinner;

import com.ahmet.showmustgoon.model.MapObjectContainer;
import com.ahmet.showmustgoon.model.MapObjectModel;
import com.ahmet.showmustgoon.trilateration.LinearLeastSquaresSolver;
import com.ahmet.showmustgoon.trilateration.NonLinearLeastSquaresSolver;
import com.ahmet.showmustgoon.trilateration.TrilaterationFunction;
import com.ls.widgets.map.MapWidget;

import com.ls.widgets.map.config.MapGraphicsConfig;
import com.ls.widgets.map.config.OfflineMapConfig;
import com.ls.widgets.map.events.MapScrolledEvent;
import com.ls.widgets.map.interfaces.Layer;
import com.ls.widgets.map.interfaces.MapEventsListener;
import com.ls.widgets.map.interfaces.OnLocationChangedListener;
import com.ls.widgets.map.interfaces.OnMapScrollListener;
import com.ls.widgets.map.model.MapObject;
import com.ls.widgets.map.utils.PivotFactory;
import com.ls.widgets.map.utils.PivotFactory.PivotPosition;

import org.apache.commons.math3.fitting.leastsquares.LeastSquaresOptimizer.Optimum;
import org.apache.commons.math3.fitting.leastsquares.LevenbergMarquardtOptimizer;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import static com.ahmet.showmustgoon.AccessPoints.accessPoints;


public class BrowseMapActivity extends Activity
        implements MapEventsListener {
    private static final String TAG = "BrowseMapActivity";

    private static final Integer LAYER1_ID = 0;
    private static final Integer LAYER2_ID = 1;
    private static final int MAP_ID = 23;

    private int nextObjectId;
    private int pinHeight;

    private MapObjectContainer model;
    private MapWidget map;
    private Location points[];
    private int currentPoint;
    private static String[][] dropdownList = new String[105][5];
    private static String[][] floorBedges = new String[50][4];
    private static String[][] floorAedges = new String[38][4];
    private static String[][] floor1edges = new String[18][4];
    private static String[][] floor2edges = new String[16][4];
    private static List<ScanResult> wList;
    private static List<double[]> coord = new ArrayList<>();
    private static List<double[]> finalcoord = new ArrayList<>();
    private static List<double[]> initial = new ArrayList<>();
    private static List<Double> distances = new ArrayList<>();
    private static List<String> levels = new ArrayList<>();
    private static List<String> levelsFirst = new ArrayList<>();
    private static String point;
    private static String floor;
    private static List<Double> closestPoint = new ArrayList<>();
    private static String firstNode;
    private static String lastNode;
    private static String FNodeFromList;
    private static List<String> a = new ArrayList<>();
    Graph g = new Graph();
    private static Spinner dropdownFirstNode;
    private static String firstNodeX = "";
    private static String firstNodeY = "";
    private static boolean scanOK = false;
    private static boolean offlineMod = true;
    private static String firstNodeKat;


    /**
     * Called when the activity is first created.
     */

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);

        nextObjectId = 0;

        model = new MapObjectContainer();

        //initialize methods
        initTestLocationPoints();
        initMap(savedInstanceState);
        readExcelForDropDown();

        //get the spinner from the xml.
        final Spinner dropdown = findViewById(R.id.spinner1);
        //create a list of items for the spinner.
        String[] items = new String[105];
        for (int i = 0; i < dropdownList.length; i++) {
            Log.e("Dropdown", dropdownList[i][0]);
            items[i] = dropdownList[i][0];
        }
        dropdownFirstNode = findViewById(R.id.spinner2);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdownFirstNode.setAdapter(adapter2);
        dropdownFirstNode.setEnabled(true);

        //initialize methods
        initMapListeners();
        readExcelBEdges();
        readExcelAEdges();
        readExcelOneEdges();
        readExcelTwoEdges();
        search();

        //GO Button
        Button goButton = findViewById(R.id.goButton);
        goButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int count = 0;
                String pathX;
                String pathY;

                while (count < a.size()) {
                    for (int j = 0; j < floorBedges.length; j++) {
                        if (a.get(count).equalsIgnoreCase(floorBedges[j][0])) {
                            pathX = (floorBedges[j][1]);
                            pathY = (floorBedges[j][2]);
                            initModel(Integer.parseInt(pathX), Integer.parseInt(pathY));
                        }
                    }
                    count++;
                }
                initMapObjects("Shortest");
                Log.e("Shortest Path Size:", String.valueOf(a.size()));
            }
        });
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        dropdown.setAdapter(adapter);
        dropdown.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                Log.e("Selected Item", (String) parent.getItemAtPosition(position));

                for (int i = 0; i < dropdownList.length; i++) {
                    if (((String) parent.getItemAtPosition(position)).equalsIgnoreCase(dropdownList[i][0])) {
                        point = dropdownList[i][4];
                        floor = dropdownList[i][3];
                    }
                }

                firstKat(firstNodeKat);
                shortestPathKat(floor);
                //Different or same floor control
                if (firstNodeKat.equalsIgnoreCase(floor)) {
                    a = g.getShortestPath((String) firstNode, (String) lastNode);
                } else {
                    if (firstNodeKat.equalsIgnoreCase("b")) {
                        a = g.getShortestPath((String) firstNode, "12");
                    } else if (firstNodeKat.equalsIgnoreCase("a")) {
                        a = g.getShortestPath((String) firstNode, "13");
                    } else if (firstNodeKat.equalsIgnoreCase("1")) {
                        a = g.getShortestPath((String) firstNode, "11");
                    } else if (firstNodeKat.equalsIgnoreCase("2")) {
                        a = g.getShortestPath((String) firstNode, "12");
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        map.centerMap();
        WifiReceiver.listOfProvider = new ArrayList<String>();
        WifiReceiver.wifiManager = wm;
        /*checking wifi connection
		 * if wifi is on searching available wifi provider*/
        if (WifiReceiver.wifiManager.isWifiEnabled() == false) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Open WI-FI and Restart app!").create().show();

        } else if (WifiReceiver.wifiManager.isWifiEnabled() == true) {
            scaning();
        }
        //Refresh Button
        Button refreshButton = findViewById(R.id.refreshButton);
        refreshButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                refresh();
            }
        });
    }

    private void shortestPathKat(String kat) {
        finalcoord.clear();
        lastNode = "";
        switch (kat) {
            case "b":
                //   shortestPathFloorB();
                for (int i = 0; i < floorBedges.length; i++) {
                    if (point.equalsIgnoreCase(floorBedges[i][0])) {
                        finalcoord.add(new double[]{Double.parseDouble(floorBedges[i][1]), Double.parseDouble(floorBedges[i][2])});
                        lastNode = floorBedges[i][0];
                    }
                }
                break;
            case "a":
                //   shortestPathFloorA();
                for (int i = 0; i < floorAedges.length; i++) {
                    if (point.equalsIgnoreCase(floorAedges[i][0])) {
                        finalcoord.add(new double[]{Double.parseDouble(floorAedges[i][1]), Double.parseDouble(floorAedges[i][2])});
                        lastNode = floorAedges[i][0];
                    }
                }
                break;
            case "1":
                //  shortestPathFloor1();
                for (int i = 0; i < floor1edges.length; i++) {
                    if (point.equalsIgnoreCase(floor1edges[i][0])) {
                        finalcoord.add(new double[]{Double.parseDouble(floor1edges[i][1]), Double.parseDouble(floor1edges[i][2])});
                        lastNode = floor1edges[i][0];
                    }
                }
                break;
            case "2":
                //   shortestPathFloor2();
                for (int i = 0; i < floor2edges.length; i++) {
                    if (point.equalsIgnoreCase(floor2edges[i][0])) {
                        finalcoord.add(new double[]{Double.parseDouble(floor2edges[i][1]), Double.parseDouble(floor2edges[i][2])});
                        lastNode = floor2edges[i][0];
                    }
                }
        }

    }

    private void firstKat(String kat) {
        switch (kat) {
            case "b":
                shortestPathFloorB();
                break;
            case "a":
                shortestPathFloorA();
                break;
            case "1":
                shortestPathFloor1();
                break;
            case "2":
                shortestPathFloor2();
                break;
        }
    }

    private void shortestPathFloorB() {
        g.addVertex("1", Arrays.asList(new Vertex("2", 2)));
        g.addVertex("2", Arrays.asList(new Vertex("1", 2), new Vertex("3", 2), new Vertex("50", 2)));
        g.addVertex("3", Arrays.asList(new Vertex("2", 2), new Vertex("4", 2)));
        g.addVertex("4", Arrays.asList(new Vertex("3", 2), new Vertex("5", 2)));
        g.addVertex("5", Arrays.asList(new Vertex("4", 2), new Vertex("6", 2)));
        g.addVertex("6", Arrays.asList(new Vertex("5", 2), new Vertex("7", 2)));
        g.addVertex("7", Arrays.asList(new Vertex("6", 2), new Vertex("8", 2)));
        g.addVertex("8", Arrays.asList(new Vertex("7", 2), new Vertex("9", 2)));
        g.addVertex("9", Arrays.asList(new Vertex("8", 2), new Vertex("10", 2)));
        g.addVertex("10", Arrays.asList(new Vertex("9", 2), new Vertex("11", 2), new Vertex("27", 2)));
        g.addVertex("11", Arrays.asList(new Vertex("10", 2), new Vertex("12", 2)));
        g.addVertex("12", Arrays.asList(new Vertex("11", 2), new Vertex("13", 2)));
        g.addVertex("13", Arrays.asList(new Vertex("12", 2), new Vertex("14", 2), new Vertex("29", 2)));
        g.addVertex("14", Arrays.asList(new Vertex("13", 2), new Vertex("15", 2)));
        g.addVertex("15", Arrays.asList(new Vertex("14", 2), new Vertex("16", 2)));
        g.addVertex("16", Arrays.asList(new Vertex("15", 2), new Vertex("17", 2)));
        g.addVertex("17", Arrays.asList(new Vertex("16", 2), new Vertex("18", 2)));
        g.addVertex("18", Arrays.asList(new Vertex("17", 2), new Vertex("19", 2)));
        g.addVertex("19", Arrays.asList(new Vertex("18", 2), new Vertex("20", 2)));
        g.addVertex("20", Arrays.asList(new Vertex("19", 2), new Vertex("21", 2), new Vertex("35", 2)));
        g.addVertex("21", Arrays.asList(new Vertex("20", 2), new Vertex("22", 2)));
        g.addVertex("22", Arrays.asList(new Vertex("21", 2), new Vertex("23", 2), new Vertex("32", 2)));
        g.addVertex("23", Arrays.asList(new Vertex("22", 2), new Vertex("24", 2)));
        g.addVertex("24", Arrays.asList(new Vertex("23", 2), new Vertex("25", 2)));
        g.addVertex("25", Arrays.asList(new Vertex("24", 2), new Vertex("26", 2)));
        g.addVertex("26", Arrays.asList(new Vertex("25", 2)));
        g.addVertex("27", Arrays.asList(new Vertex("10", 2), new Vertex("28", 2)));
        g.addVertex("28", Arrays.asList(new Vertex("27", 2)));
        g.addVertex("29", Arrays.asList(new Vertex("13", 2), new Vertex("30", 2)));
        g.addVertex("30", Arrays.asList(new Vertex("29", 2), new Vertex("31", 2)));
        g.addVertex("31", Arrays.asList(new Vertex("30", 2)));
        g.addVertex("32", Arrays.asList(new Vertex("22", 2), new Vertex("33", 2)));
        g.addVertex("33", Arrays.asList(new Vertex("32", 2), new Vertex("34", 2)));
        g.addVertex("34", Arrays.asList(new Vertex("33", 2)));
        g.addVertex("35", Arrays.asList(new Vertex("20", 2), new Vertex("36", 2)));
        g.addVertex("36", Arrays.asList(new Vertex("35", 2), new Vertex("37", 2)));
        g.addVertex("37", Arrays.asList(new Vertex("36", 2), new Vertex("38", 2)));
        g.addVertex("38", Arrays.asList(new Vertex("37", 2), new Vertex("39", 2)));
        g.addVertex("39", Arrays.asList(new Vertex("38", 2), new Vertex("40", 2)));
        g.addVertex("40", Arrays.asList(new Vertex("39", 2), new Vertex("41", 2)));
        g.addVertex("41", Arrays.asList(new Vertex("40", 2), new Vertex("42", 2)));
        g.addVertex("42", Arrays.asList(new Vertex("41", 2), new Vertex("43", 2)));
        g.addVertex("43", Arrays.asList(new Vertex("42", 2), new Vertex("44", 2), new Vertex("45", 2)));
        g.addVertex("44", Arrays.asList(new Vertex("43", 2)));
        g.addVertex("45", Arrays.asList(new Vertex("43", 2), new Vertex("46", 2)));
        g.addVertex("46", Arrays.asList(new Vertex("45", 2), new Vertex("47", 2)));
        g.addVertex("47", Arrays.asList(new Vertex("46", 2), new Vertex("48", 2)));
        g.addVertex("48", Arrays.asList(new Vertex("47", 2), new Vertex("49", 2)));
        g.addVertex("49", Arrays.asList(new Vertex("48", 2), new Vertex("50", 2)));
        g.addVertex("50", Arrays.asList(new Vertex("49", 2), new Vertex("2", 2)));
    }

    private void shortestPathFloorA() {
        g.addVertex("1", Arrays.asList(new Vertex("2", 2)));
        g.addVertex("2", Arrays.asList(new Vertex("1", 2), new Vertex("3", 2)));
        g.addVertex("3", Arrays.asList(new Vertex("2", 2), new Vertex("4", 2)));
        g.addVertex("4", Arrays.asList(new Vertex("3", 2), new Vertex("5", 2)));
        g.addVertex("5", Arrays.asList(new Vertex("4", 2), new Vertex("6", 2)));
        g.addVertex("6", Arrays.asList(new Vertex("5", 2), new Vertex("7", 2)));
        g.addVertex("7", Arrays.asList(new Vertex("6", 2), new Vertex("8", 2)));
        g.addVertex("8", Arrays.asList(new Vertex("7", 2), new Vertex("9", 2)));
        g.addVertex("9", Arrays.asList(new Vertex("8", 2), new Vertex("10", 2)));
        g.addVertex("10", Arrays.asList(new Vertex("9", 2), new Vertex("11", 2)));
        g.addVertex("11", Arrays.asList(new Vertex("10", 2), new Vertex("12", 2)));
        g.addVertex("12", Arrays.asList(new Vertex("11", 2), new Vertex("13", 2)));
        g.addVertex("13", Arrays.asList(new Vertex("12", 2), new Vertex("14", 2)));
        g.addVertex("14", Arrays.asList(new Vertex("13", 2), new Vertex("15", 2)));
        g.addVertex("15", Arrays.asList(new Vertex("14", 2), new Vertex("16", 2), new Vertex("25", 2)));
        g.addVertex("16", Arrays.asList(new Vertex("15", 2), new Vertex("17", 2)));
        g.addVertex("17", Arrays.asList(new Vertex("16", 2), new Vertex("18", 2)));
        g.addVertex("18", Arrays.asList(new Vertex("17", 2), new Vertex("19", 2)));
        g.addVertex("19", Arrays.asList(new Vertex("18", 2), new Vertex("20", 2)));
        g.addVertex("20", Arrays.asList(new Vertex("19", 2), new Vertex("21", 2)));
        g.addVertex("21", Arrays.asList(new Vertex("20", 2), new Vertex("22", 2)));
        g.addVertex("22", Arrays.asList(new Vertex("21", 2), new Vertex("23", 2), new Vertex("35", 2)));
        g.addVertex("23", Arrays.asList(new Vertex("22", 2), new Vertex("24", 2)));
        g.addVertex("24", Arrays.asList(new Vertex("23", 2), new Vertex("28", 2)));
        g.addVertex("25", Arrays.asList(new Vertex("15", 2), new Vertex("26", 2)));
        g.addVertex("26", Arrays.asList(new Vertex("25", 2), new Vertex("27", 2)));
        g.addVertex("27", Arrays.asList(new Vertex("26", 2)));
        g.addVertex("28", Arrays.asList(new Vertex("24", 2), new Vertex("29", 2)));
        g.addVertex("29", Arrays.asList(new Vertex("28", 2), new Vertex("30", 2)));
        g.addVertex("30", Arrays.asList(new Vertex("29", 2)));
        g.addVertex("31", Arrays.asList(new Vertex("22", 2), new Vertex("32", 2)));
        g.addVertex("32", Arrays.asList(new Vertex("31", 2), new Vertex("33", 2)));
        g.addVertex("33", Arrays.asList(new Vertex("32", 2), new Vertex("34", 2)));
        g.addVertex("34", Arrays.asList(new Vertex("33", 2), new Vertex("35", 2)));
        g.addVertex("35", Arrays.asList(new Vertex("34", 2), new Vertex("36", 2)));
        g.addVertex("36", Arrays.asList(new Vertex("35", 2), new Vertex("37", 2)));
        g.addVertex("37", Arrays.asList(new Vertex("36", 2), new Vertex("38", 2)));
        g.addVertex("38", Arrays.asList(new Vertex("37", 2)));
    }

    private void shortestPathFloor1() {
        g.addVertex("1", Arrays.asList(new Vertex("2", 2)));
        g.addVertex("2", Arrays.asList(new Vertex("1", 2), new Vertex("3", 2)));
        g.addVertex("3", Arrays.asList(new Vertex("2", 2), new Vertex("4", 2)));
        g.addVertex("4", Arrays.asList(new Vertex("3", 2), new Vertex("5", 2)));
        g.addVertex("5", Arrays.asList(new Vertex("4", 2), new Vertex("6", 2)));
        g.addVertex("6", Arrays.asList(new Vertex("5", 2), new Vertex("7", 2)));
        g.addVertex("7", Arrays.asList(new Vertex("6", 2), new Vertex("8", 2)));
        g.addVertex("8", Arrays.asList(new Vertex("7", 2), new Vertex("9", 2)));
        g.addVertex("9", Arrays.asList(new Vertex("8", 2), new Vertex("10", 2)));
        g.addVertex("10", Arrays.asList(new Vertex("9", 2), new Vertex("11", 2)));
        g.addVertex("11", Arrays.asList(new Vertex("10", 2), new Vertex("12", 2)));
        g.addVertex("12", Arrays.asList(new Vertex("11", 2), new Vertex("13", 2)));
        g.addVertex("13", Arrays.asList(new Vertex("12", 2), new Vertex("14", 2)));
        g.addVertex("14", Arrays.asList(new Vertex("13", 2)));
        g.addVertex("15", Arrays.asList(new Vertex("16", 2), new Vertex("18", 2)));
        g.addVertex("16", Arrays.asList(new Vertex("15", 2), new Vertex("17", 2)));
        g.addVertex("17", Arrays.asList(new Vertex("16", 2)));
        g.addVertex("18", Arrays.asList(new Vertex("15", 2)));
    }

    private void shortestPathFloor2() {
        g.addVertex("1", Arrays.asList(new Vertex("2", 2)));
        g.addVertex("2", Arrays.asList(new Vertex("1", 2), new Vertex("3", 2)));
        g.addVertex("3", Arrays.asList(new Vertex("2", 2), new Vertex("4", 2)));
        g.addVertex("4", Arrays.asList(new Vertex("3", 2), new Vertex("5", 2)));
        g.addVertex("5", Arrays.asList(new Vertex("4", 2), new Vertex("6", 2)));
        g.addVertex("6", Arrays.asList(new Vertex("5", 2), new Vertex("7", 2)));
        g.addVertex("7", Arrays.asList(new Vertex("6", 2), new Vertex("8", 2)));
        g.addVertex("8", Arrays.asList(new Vertex("7", 2), new Vertex("9", 2)));
        g.addVertex("9", Arrays.asList(new Vertex("8", 2), new Vertex("10", 2)));
        g.addVertex("10", Arrays.asList(new Vertex("9", 2), new Vertex("11", 2)));
        g.addVertex("11", Arrays.asList(new Vertex("10", 2), new Vertex("12", 2)));
        g.addVertex("12", Arrays.asList(new Vertex("11", 2), new Vertex("13", 2)));
        g.addVertex("13", Arrays.asList(new Vertex("12", 2), new Vertex("14", 2)));
        g.addVertex("14", Arrays.asList(new Vertex("13", 2), new Vertex("15", 2)));
        g.addVertex("15", Arrays.asList(new Vertex("14", 2), new Vertex("16", 2)));
        g.addVertex("16", Arrays.asList(new Vertex("15", 2)));
    }


    private void search() {

        double[] result = new double[2];
        readExcel();
        readExcelForDropDown();
        WifiReceiver wr = new WifiReceiver();
        Intent intent = new Intent();
        wr.onReceive(getApplicationContext(), intent);
        wList = wr.wifiList;
        boolean threeFull = false;
        for (int k = 0; k < wList.size(); k++) {
            Log.e("BSSID", wList.get(k).BSSID);
            Log.e("AP Length", String.valueOf(accessPoints.length));
            for (int j = 0; j < accessPoints.length; j++) {
                Log.e("accessPoints[j]", accessPoints[j][0]);
                if (accessPoints[j][0].equals(wList.get(k).BSSID) && !threeFull) {
                    coord.add(new double[]{Double.parseDouble(accessPoints[j][1]), Double.parseDouble(accessPoints[j][2])});
                    for (double[] d : coord) {
                        Log.e("Coord ", String.valueOf(d[0]) + "+" + String.valueOf(d[1]));
                    }
                    distances.add(wr.calculateDistance(wList.get(k).level, wList.get(k).frequency));
                    for (double x : distances) {
                        Log.e("Dist", String.valueOf(x));
                    }

                    Log.e("Check:", "Check is successful" + String.valueOf(scanOK));
                    Log.e("Check:", String.valueOf(coord.size()));

                    if (coord.size() > 2) {
                        scanOK = true;
                        threeFull = true;
                    }
                }
            }
        }
        Log.e("STATUS", "ScanOk: " + scanOK + " / threeFull: " + threeFull);

        if (scanOK) {
            result = Locate(coord, distances);
            initModel((int) result[0], (int) result[1]);
            initMapObjects("mapObject");
            dropdownFirstNode.setEnabled(false);
        }
        for (int k = 0; k < wList.size(); k++) {
            for (int j = 0; j < accessPoints.length; j++) {
                Log.e("apString[j]", accessPoints[j][0]);

                if (accessPoints[j][0].equals(wList.get(k).BSSID)) {
                    closestPoint.clear();
                    levelsFirst.add(accessPoints[j][3]);
                    firstNode = "";
                    initial.clear();
                    double min;
                    if (levelsFirst.get(0).equalsIgnoreCase("A")) {
                        for (int i = 0; i < floorAedges.length; i++) {
                            closestPoint.add(Math.sqrt(Math.pow((result[0]) - Double.parseDouble(floorAedges[i][1]), 2)
                                    + Math.pow((result[1]) - Double.parseDouble(floorAedges[i][2]), 2)));
                        }
                        min = closestPoint.get(0);
                        for (int i = 0; i < closestPoint.size(); i++) {
                            if (closestPoint.get(i) < min) {
                                min = closestPoint.get(i);
                                firstNode = floorAedges[i][0];
                                firstNodeKat = "a";
                            }
                        }
                    }
                    else if (levelsFirst.get(0).equalsIgnoreCase("B")) {
                        for (int i = 0; i < floorBedges.length; i++) {
                            closestPoint.add(Math.sqrt(Math.pow((result[0]) - Double.parseDouble(floorBedges[i][1]), 2)
                                    + Math.pow((result[1]) - Double.parseDouble(floorBedges[i][2]), 2)));
                        }
                        min = closestPoint.get(0);
                        for (int i = 0; i < closestPoint.size(); i++) {
                            if (closestPoint.get(i) < min) {
                                min = closestPoint.get(i);
                                firstNode = floorBedges[i][0];
                                firstNodeKat = "b";
                            }
                        }
                    }
                    else if (levelsFirst.get(0).equalsIgnoreCase("1")) {
                        for (int i = 0; i < floor1edges.length; i++) {
                            closestPoint.add(Math.sqrt(Math.pow((result[0]) - Double.parseDouble(floor1edges[i][1]), 2)
                                    + Math.pow((result[1]) - Double.parseDouble(floor1edges[i][2]), 2)));
                        }
                        min = closestPoint.get(0);
                        for (int i = 0; i < closestPoint.size(); i++) {
                            if (closestPoint.get(i) < min) {
                                min = closestPoint.get(i);
                                firstNode = floor1edges[i][0];
                                firstNodeKat = "1";
                            }
                        }
                    }
                    else if (levelsFirst.get(0).equalsIgnoreCase("2")) {
                        for (int i = 0; i < floor2edges.length; i++) {

                            closestPoint.add(Math.sqrt(Math.pow((result[0]) - Double.parseDouble(floor2edges[i][1]), 2)
                                    + Math.pow((result[1]) - Double.parseDouble(floor2edges[i][2]), 2)));
                        }
                        min = closestPoint.get(0);
                        for (int i = 0; i < closestPoint.size(); i++) {
                            if (closestPoint.get(i) < min) {
                                min = closestPoint.get(i);
                                firstNode = floor2edges[i][0];
                                firstNodeKat = "2";
                            }
                        }
                    }
                }
                else {

                    Log.e("Else condition", "ELSE OK");

                    dropdownFirstNode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> parent, View view,
                                                   int position, long id) {

                            Log.e("FirstNode item", (String) parent.getItemAtPosition(position));
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {
                        }
                    });

                    FNodeFromList = dropdownFirstNode.getSelectedItem().toString().trim();
                    Log.e("FNodeFromList", FNodeFromList);
                    for (int i = 0; i < dropdownList.length; i++) {
                        if (FNodeFromList.equals(dropdownList[i][0])) {
                            Log.e("ddL4", dropdownList[i][4]);
                            firstNode = dropdownList[i][4];
                            floor = dropdownList[i][3];
                            firstNodeX = dropdownList[i][1];
                            firstNodeY = dropdownList[i][2];
                            firstNodeKat = "b";
                        }
                    }
                }
            }
        }
    }

    private void scaning() {
        WifiReceiver.receiverWifi = new WifiReceiver();
        registerReceiver(WifiReceiver.receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));
        WifiReceiver.wifiManager.startScan();
        registerReceiver(WifiReceiver.receiverWifi, new IntentFilter(
                WifiManager.SCAN_RESULTS_AVAILABLE_ACTION));

        unregisterReceiver(WifiReceiver.receiverWifi);
    }

    public double[] Locate(List<double[]> accessPointPos, List<Double> distance) {
        double[][] pos = new double[accessPointPos.size()][1];
        pos = accessPointPos.toArray(pos);

        double[] dist = new double[distance.size()];
        for (int q = 0; q < dist.length; q++) {
            dist[q] = distance.get(q).doubleValue();
        }

        TrilaterationFunction trilaterationFunction = new TrilaterationFunction(pos, dist);
        LinearLeastSquaresSolver lSolver = new LinearLeastSquaresSolver(trilaterationFunction);
        NonLinearLeastSquaresSolver nlSolver = new NonLinearLeastSquaresSolver(trilaterationFunction, new LevenbergMarquardtOptimizer());

        Optimum nonLinearOptimum = nlSolver.solve();
        double[] result = nonLinearOptimum.getPoint().toArray();
        Log.e("RESULT", String.valueOf(result));
        return result;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        map.saveState(outState);
    }

    private void initTestLocationPoints() {
        points = new Location[5];
        for (int i = 0; i < points.length; ++i) {
            points[i] = new Location("test");
        }
        currentPoint = 0;
    }

    public String[][] readExcel() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("accesspoints.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell hucre = sheet.getCell(j, i);
                    accessPoints[i][j] = hucre.getContents();
                }
            }
            return accessPoints;
        } catch (Exception e) {
            String[][] errorStr = {{"Error except", "a", "b"}};
            Log.e("EXCELEXC", e.toString());
            return errorStr;
        }
    }           //Accesspoints

    public String[][] readExcelForDropDown() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("classrooms.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell hucre = sheet.getCell(j, i);
                    dropdownList[i][j] = hucre.getContents();
                }
            }
            return dropdownList;
        } catch (Exception e) {
            String[][] errorStr = {{"Error except", "a", "b"}};
            Log.e("EXCELEXC", e.toString());
            return errorStr;
        }
    }       //Classrooms names for final position

    public String[][] readExcelBEdges() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("floorBedges.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell hucre = sheet.getCell(j, i);
                    floorBedges[i][j] = hucre.getContents();
                }
            }
            return floorBedges;
        } catch (Exception e) {
            String[][] errorStr = {{"Error except", "a", "b"}};
            Log.e("EXCELEXC", e.toString());
            return errorStr;
        }
    }     //Floor B edges

    public String[][] readExcelAEdges() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("floorAedges.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell hucre = sheet.getCell(j, i);
                    floorAedges[i][j] = hucre.getContents();
                }
            }
            return floorAedges;
        } catch (Exception e) {
            String[][] errorStr = {{"Error except", "a", "b"}};
            Log.e("EXCELEXC", e.toString());
            return errorStr;
        }
    }      //Floor A edges

    public String[][] readExcelOneEdges() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("floor1edges.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell hucre = sheet.getCell(j, i);
                    floor1edges[i][j] = hucre.getContents();
                }
            }
            return floor1edges;
        } catch (Exception e) {
            String[][] errorStr = {{"Error except", "a", "b"}};
            Log.e("EXCELEXC", e.toString());
            return errorStr;
        }
    }   //Floor 1 edges

    public String[][] readExcelTwoEdges() {
        try {
            AssetManager assetManager = getAssets();
            InputStream inputStream = assetManager.open("floor2edges.xls");
            Workbook workbook = Workbook.getWorkbook(inputStream);
            Sheet sheet = workbook.getSheet(0);
            for (int i = 0; i < sheet.getRows(); i++) {
                for (int j = 0; j < sheet.getColumns(); j++) {
                    Cell hucre = sheet.getCell(j, i);
                    floor2edges[i][j] = hucre.getContents();
                }
            }
            return floor2edges;
        } catch (Exception e) {
            String[][] errorStr = {{"Error except", "a", "b"}};
            Log.e("EXCELEXC", e.toString());
            return errorStr;
        }
    }   //Floor 2 edges

    private Location getNextLocationPoint() {
        if (currentPoint < points.length - 1) {
            currentPoint += 1;
        } else {
            currentPoint = 0;
        }

        return points[currentPoint];
    }


    private void initMap(Bundle savedInstanceState) {

        readExcel();
        WifiReceiver wr = new WifiReceiver();
        Intent intent = new Intent();
        wr.onReceive(getApplicationContext(), intent);
        wList = wr.wifiList;
        if (!wList.isEmpty()) {
            for (int k = 0; k < wList.size(); k++) {
                Log.e("srBSSD", wList.get(k).BSSID);
                Log.e("apstr.len", String.valueOf(accessPoints.length));
                for (int j = 0; j < accessPoints.length; j++) {
                    Log.e("apString[j]", accessPoints[j][0]);

                    if (accessPoints[j][0].equals(wList.get(k).BSSID)) {
                        levels.add(accessPoints[j][3]);
                        if (levels.get(0).equalsIgnoreCase("A")) {
                            map = new MapWidget(savedInstanceState, this, "mapa", 11);
                        } else if (levels.get(0).equalsIgnoreCase("B")) {
                            map = new MapWidget(savedInstanceState, this, "mapb", 11);
                        } else if (levels.get(0).equalsIgnoreCase("1")) {
                            map = new MapWidget(savedInstanceState, this, "map1", 11);
                            shortestPathFloor1();
                        } else if (levels.get(0).equalsIgnoreCase("2")) {
                            map = new MapWidget(savedInstanceState, this, "map2", 11);
                        }
                    }
                /*else{
                    map = new MapWidget(savedInstanceState, this, "mapb", 11);
                }*/
                }
            }
        }
        else {
            map = new MapWidget(savedInstanceState, this, "mapb", 11);
        }

        map.setId(MAP_ID);

        OfflineMapConfig config = map.getConfig();
        config.setPinchZoomEnabled(true); // Sets pinch gesture to zoom
        config.setFlingEnabled(true);    // Sets inertial scrolling of the map
        config.setMaxZoomLevelLimit(20);
        config.setZoomBtnsVisible(false); // Sets embedded zoom buttons visible

        // Configuration of position marker
        MapGraphicsConfig graphicsConfig = config.getGraphicsConfig();
        graphicsConfig.setAccuracyAreaColor(0x550000FF); // Blue with transparency
        graphicsConfig.setAccuracyAreaBorderColor(Color.BLUE); // Blue without transparency

        RelativeLayout layout = findViewById(R.id.rootLayout);
        // Adding the map to the layout
        layout.addView(map, 0);
        layout.setBackgroundColor(Color.parseColor("#eaeaea"));

        // Adding layers in order to put there some map objects
        map.createLayer(LAYER1_ID);
        /*map.createLayer(LAYER2_ID);*/
    }


    private void initModel(int x, int y) {
        MapObjectModel objectModel = new MapObjectModel(0, x, y, "You are here!");
        model.addObject(objectModel);
    }

    private void initMapObjects(String type) {

        Layer layer1 = map.getLayerById(LAYER1_ID);

        for (int i = 0; i < model.size(); ++i) {
            addNotScalableMapObject(model.getObject(i), layer1, type);
        }
    }


    private void addNotScalableMapObject(int x, int y, Layer layer, String type) {
        Drawable drawable = getResources().getDrawable(R.drawable.map_object);

        switch (type) {
            case "Shortest":
                drawable = getResources().getDrawable(R.drawable.shortest);
                break;
            case "mapObject":
                drawable = getResources().getDrawable(R.drawable.map_object);
                break;
            case "Destination":
                drawable = getResources().getDrawable(R.drawable.map_dest);
                break;
        }

        pinHeight = drawable.getIntrinsicHeight();
        // Creating the map object
        MapObject object1 = new MapObject(Integer.valueOf(nextObjectId),
                drawable,
                new Point(x, y), // coordinates in original map coordinate system.
                PivotFactory.createPivotPoint(drawable, PivotPosition.PIVOT_CENTER),
                true, // This object will be passed to the listener
                false); // is not scalable. It will have the same size on each zoom level

        // Adding object to layer
        layer.addMapObject(object1);
        nextObjectId += 1;
    }

    private void addNotScalableMapObject(MapObjectModel objectModel, Layer layer, String type) {
        if (objectModel.getLocation() != null) {
            addNotScalableMapObject(objectModel.getLocation(), layer, type);
        } else {
            addNotScalableMapObject(objectModel.getX(), objectModel.getY(), layer, type);
        }
    }


    private void addNotScalableMapObject(Location location, Layer layer, String type) {
        if (location == null)
            return;

        // Getting the drawable of the map object
        Drawable drawable = getResources().getDrawable(R.drawable.map_object);
        // Creating the map object
        MapObject object1 = new MapObject(Integer.valueOf(nextObjectId),
                drawable,
                new Point(0, 0), // coordinates in original map coordinate system.
                PivotFactory.createPivotPoint(drawable, PivotPosition.PIVOT_CENTER),
                true, // This object will be passed to the listener
                true); // is not scalable. It will have the same size on each zoom level
        layer.addMapObject(object1);

        object1.moveTo(location);
        nextObjectId += 1;
    }


    private void initMapListeners() {

        map.addMapEventsListener(this);
        map.setOnMapScrolledListener(new OnMapScrollListener() {
            public void onScrolledEvent(MapWidget v, MapScrolledEvent event) {
                handleOnMapScroll(v, event);
            }
        });

        map.setOnLocationChangedListener(new OnLocationChangedListener() {
            @Override
            public void onLocationChanged(MapWidget v, Location location) {
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = new MenuInflater(this);
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void refresh() {
        Layer layer = map.getLayer(LAYER1_ID);
        for (int s = layer.getMapObjectCount(); s >= 0; s--) {
            Log.e("Remove:", String.valueOf(s));
            layer.removeMapObject(s);
        }
        Log.e("Refresh", "Success");
        search();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.zoomIn:
                map.zoomIn();
                return true;
            case R.id.zoomOut:
                map.zoomOut();
                return true;
            case R.id.hideLayer2: {
                Layer layer = map.getLayerById(LAYER2_ID);
                if (layer != null) {
                    layer.setVisible(false);
                    map.invalidate(); // Need to repaint the layer. This is a bug and will be fixed in next version.
                }
                return true;
            }
            case R.id.showLayer2: {
                Layer layer = map.getLayerById(LAYER2_ID);
                if (layer != null) {
                    layer.setVisible(true);
                    map.invalidate(); // Need to repaint the layer. This is a bug and will be fixed in next version.
                }
                return true;
            }
            case R.id.scroll_next:
                map.scrollMapTo(getNextLocationPoint());
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void handleOnMapScroll(MapWidget v, MapScrolledEvent event) {

        int dx = event.getDX();
        int dy = event.getDY();
    }


    @Override
    public void onPostZoomIn() {
        Log.i(TAG, "onPostZoomIn()");
    }

    @Override
    public void onPostZoomOut() {
        Log.i(TAG, "onPostZoomOut()");
    }

    @Override
    public void onPreZoomIn() {
        Log.i(TAG, "onPreZoomIn()");

    }

    @Override
    public void onPreZoomOut() {
        Log.i(TAG, "onPreZoomOut()");

    }


}


