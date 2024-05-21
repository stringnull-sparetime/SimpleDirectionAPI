package com.example.simpledirectionapi;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.android.PolyUtil;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.function.BiFunction;

public class MainActivity extends FragmentActivity implements OnMapReadyCallback {
    private String DESTINATION_API_URL = "https://maps.googleapis.com/maps/api/directions/json?";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.id_map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        LatLng destLoc = new LatLng(15.129803272456604, 120.5652024846851);
        LatLng originLoc = new LatLng(14.284016725977539, 121.1488988669392);

        MarkerOptions destination = new MarkerOptions().position(destLoc).title("Destination");
        googleMap.addMarker(destination);

        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(originLoc,8));
        googleMap.getUiSettings().setZoomControlsEnabled(true);
        googleMap.getUiSettings().setMapToolbarEnabled(true);

        BiFunction<LatLng, LatLng, String> getParameters = (o, d) -> {

            return "origin=" + o.latitude + "," + o.longitude+
                    "&destination=" + d.latitude + "," + d.longitude+
                    "&key="+ getString(R.string.api_key);
        };

        RequestQueue queue = Volley.newRequestQueue(this);
        String url = DESTINATION_API_URL + getParameters.apply(originLoc, destLoc);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        // Handle the response
                        JSONObject jsonObject = null;
                        try {
                            jsonObject = new JSONObject(response);

                        // Extract polyline string from JSON
                        JSONObject routes = jsonObject.getJSONArray("routes").getJSONObject(0);
                        JSONObject overviewPolyline = routes.getJSONObject("overview_polyline");
                        String encodedPolyline = overviewPolyline.getString("points");

                        //DETAILS
                        JSONObject legs = routes.getJSONArray("legs").getJSONObject(0);
                        JSONObject duration = legs.getJSONObject("duration");
                        String details = duration.getString("text");

                        MarkerOptions origin = new MarkerOptions().position(originLoc).title("Origin - EST (" + details + ")").icon(getMarkerIcon(R.drawable.parcel));
                        googleMap.addMarker(origin);

                        googleMap.addPolyline(new PolylineOptions().addAll(PolyUtil.decode(encodedPolyline)).color(Color.argb(255, 126, 132, 247)).width(10));

                        } catch (JSONException e) {
                            throw new RuntimeException(e);
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Handle errors
                Log.e("MainActivity", "Error: " + error.toString());
            }
        });

        // Add the request to the RequestQueue
        queue.add(stringRequest);
    }


    private BitmapDescriptor getMarkerIcon(int resource) {
        @SuppressLint("UseCompatLoadingForDrawables") BitmapDrawable drawable = (BitmapDrawable) getResources().getDrawable(resource);
        Bitmap bitmap = drawable.getBitmap();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        int newWidth = 100;
        int newHeight = 100;
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return BitmapDescriptorFactory.fromBitmap(resizedBitmap);
    }
}