package com.ahmet.showmustgoon;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import static com.ahmet.showmustgoon.AccessPoints.accessPoints;

class WifiReceiver extends BroadcastReceiver {

    public static WifiManager wifiManager;
    public static WifiReceiver receiverWifi;
    public static List<ScanResult> wifiList;
    public static List<String> listOfProvider = new ArrayList<>();
    public static List<String> threeWifi = new ArrayList<>();

    public void onReceive(Context c, Intent intent) {
        //Waiting for the Wi-Fi list to fill
        try{
            wifiManager = (WifiManager) c.getSystemService(Context.WIFI_SERVICE);
            wifiManager.setWifiEnabled(true);
            Thread.sleep(2000L);
        }
        catch (InterruptedException ie)
        {
            return;
        }

        wifiManager.startScan();
        wifiList = wifiManager.getScanResults();
        Collections.sort(wifiList, new Comparator<ScanResult>() {
            @Override
            public int compare(ScanResult lhs, ScanResult rhs) {
                return (lhs.level > rhs.level ? -1
                        : (lhs.level == rhs.level ? 0 : 1));
            }
        });
        listOfProvider.clear();
        String providerName;

        for (int i = 0; i < wifiList.size(); i++) {
            for(int j=0; j<accessPoints.length; j++) {
                if ((wifiList.get(i).BSSID).toString().equalsIgnoreCase(accessPoints[j][0])) {
                    providerName = (wifiList.get(i).SSID).toString()
                            + "\n" + (wifiList.get(i).BSSID).toString()
                            + "\n" + (wifiList.get(i).frequency)
                            + "\n" + "Distance: " + calculateDistance(wifiList.get(i).level, wifiList.get(i).frequency) + " (m)";
                    listOfProvider.add(providerName);
                }}}
    }
    //Translate dBm and Mhz to Meter
    public double calculateDistance(double signalLevelInDb, double freqInMHz) {
        double exp = (27.55 - (20 * Math.log10(freqInMHz)) + Math.abs(signalLevelInDb)) / 20.0;
        return Math.pow(10.0, exp);
    }
}
