package com.stone.network;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.Locale;

import com.stone.app.App;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;

public class NetworkUtil {
	public static String getIPAddress() {
		Context context = App.getInstance().getContext();
		ConnectivityManager cm = (ConnectivityManager) context.getSystemService(
			Context.CONNECTIVITY_SERVICE);
		NetworkInfo info = cm.getActiveNetworkInfo();

		if (info != null && info.isConnected()) {
			if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
				try {
					for (Enumeration<NetworkInterface> en = NetworkInterface
						.getNetworkInterfaces(); en.hasMoreElements();) {

						NetworkInterface intf = en.nextElement();

						for (Enumeration<InetAddress> enumIpAddr = intf
							.getInetAddresses(); enumIpAddr.hasMoreElements();) {

							InetAddress inetAddress = enumIpAddr.nextElement();
							if (!inetAddress.isLoopbackAddress()
								&& inetAddress instanceof Inet4Address) {

								return inetAddress.getHostAddress();
							}
						}
					}
				} catch (SocketException e) {
					e.printStackTrace();
				}

			} else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
				WifiManager wifiManager = (WifiManager) context.getSystemService(
					Context.WIFI_SERVICE);
				WifiInfo wifiInfo = wifiManager.getConnectionInfo();
				String ipAddress = intIP2StringIP(wifiInfo.getIpAddress());
				return ipAddress;
			}
		} else {

		}

		return null;
	}

	public static String intIP2StringIP(int ip) {
		return String.format(Locale.ENGLISH, "%d.%d.%d.%d", ip & 0xFF, (ip >> 8) & 0xFF,
			(ip >> 16) & 0xFF, (ip >> 24) & 0xFF);
	}
}
