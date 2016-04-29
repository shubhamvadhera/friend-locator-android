package com.svadhera.friendlocator.friendlocator;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by svadhera on 4/5/2016.
 */
public class HttpHelper {
    static final String SERVER_URL = "http://cmpe-277-shubhamvadhera.c9users.io";
    static final String SERVER_PORT = ":8080";
    static final String SERVER_URL_EXT = "/flserver";

    protected static String getLocation (String userId) {
        String resp = "error";
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(SERVER_URL + SERVER_PORT + SERVER_URL_EXT + "/" + userId);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(3000);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                resp =  sb.toString();
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED !");
            e.printStackTrace();
            //resp = "Exception: " + e.getMessage() + e.getCause();
        }
        finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    System.out.println("EXCEPTION OCCURRED !");
                    System.out.println(ex.getMessage() + ex.getCause());
                }
            }
            return resp;
        }
    }

    //@Deprecated
    protected static boolean checkKey (String key) {
        boolean resp = false;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(SERVER_URL + SERVER_PORT + SERVER_URL_EXT + "/" + key);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setConnectTimeout(3000);
            urlConnection.setRequestMethod("GET");
            urlConnection.setReadTimeout(3000);
            urlConnection.connect();
            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) resp = true;
        } catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED !");
            e.printStackTrace();
        }
        finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    System.out.println("EXCEPTION OCCURRED !");
                    System.out.println(ex.getMessage() + ex.getCause());
                }
            }
            return resp;
        }
    }

    protected static String postLocation (double lat, double lng) {
        String resp = "Blank";
        HttpURLConnection urlConnection = null;
        try {
            JSONObject coordinate = new JSONObject();
            JSONObject latlng = new JSONObject();
            latlng.put("lat",lat);
            latlng.put("lng",lng);
            coordinate.put("coordinate",latlng);

            URL url = new URL(SERVER_URL + SERVER_PORT + SERVER_URL_EXT);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("POST");
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.connect();

            OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
            os.write(coordinate.toString().getBytes());
            os.flush();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                resp =  sb.toString();
            } else {
                resp = "Response Code: " + responseCode;
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED !");
            e.printStackTrace();
            resp = "Exception: " + e.getMessage() + e.getCause();
        }
        finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    System.out.println("EXCEPTION OCCURRED !");
                    System.out.println(ex.getMessage() + ex.getCause());
                }
            }
            return resp;
        }
    }

    protected static String updateLocation (String userKey, double lat, double lng) {
        String resp = "error";
        HttpURLConnection urlConnection = null;
        try {
            JSONObject coordinate = new JSONObject();
            JSONObject latlng = new JSONObject();
            latlng.put("lat",lat);
            latlng.put("lng",lng);
            coordinate.put ("id", userKey);
            coordinate.put("coordinate",latlng);

            URL url = new URL(SERVER_URL + SERVER_PORT + SERVER_URL_EXT + "/" + userKey);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.setRequestMethod("PUT");
            urlConnection.setChunkedStreamingMode(0);
            urlConnection.setConnectTimeout(3000);
            urlConnection.setReadTimeout(3000);
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=utf-8");
            urlConnection.connect();

            OutputStream os = new BufferedOutputStream(urlConnection.getOutputStream());
            os.write(coordinate.toString().getBytes());
            os.flush();

            int responseCode = urlConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_CREATED) {
                BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                StringBuilder sb = new StringBuilder();
                String line;
                while ((line = br.readLine()) != null) {
                    sb.append(line+"\n");
                }
                br.close();
                resp =  sb.toString();
            }
        } catch (Exception e) {
            System.out.println("EXCEPTION OCCURRED !");
            e.printStackTrace();
            //resp = "Exception: " + e.getMessage() + e.getCause();
        }
        finally {
            if (urlConnection != null) {
                try {
                    urlConnection.disconnect();
                } catch (Exception ex) {
                    System.out.println("EXCEPTION OCCURRED !");
                    System.out.println(ex.getMessage() + ex.getCause());
                }
            }
            return resp;
        }
    }
}
