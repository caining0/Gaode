package com.oslanka.caining;

import android.app.Activity;
import android.graphics.Color;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.amap.api.maps.AMap;
import com.amap.api.maps.CameraUpdateFactory;
import com.amap.api.maps.model.BitmapDescriptor;
import com.amap.api.maps.model.BitmapDescriptorFactory;
import com.amap.api.maps.model.LatLng;
import com.amap.api.maps.model.LatLngBounds;
import com.amap.api.maps.model.LatLngBounds.Builder;
import com.amap.api.maps.model.Marker;
import com.amap.api.maps.model.MarkerOptions;
import com.amap.api.services.core.PoiItem;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public  class PoiOverlay {
    private List<PoiItem> poiItems;
    private AMap aMap;
    private ArrayList<Marker> markers = new ArrayList();
    private List<View> views = new ArrayList<>();
    private int[] srcs;

    public ArrayList<Marker> getMark() {
        return markers;
    }

    public PoiOverlay(AMap var1, List<PoiItem> var2,Activity context,int[] srcs) {
        this.aMap = var1;
        this.poiItems = var2;
        for (int i = 0; i < var2.size(); i++) {
            views.add(context.getLayoutInflater().inflate(
                    R.layout.custom_info_contents, null));
        }
        this.srcs = srcs;
    }

    public void addToMap() {
        try {
            for(int var1 = 0; var1 < this.poiItems.size(); ++var1) {
                MarkerOptions mo = this.getMO(var1);
                Marker var2 = this.aMap.addMarker(mo);

                var2.setObject(Integer.valueOf(var1));
                this.markers.add(var2);
            }
        } catch (Throwable var3) {
            var3.printStackTrace();
        }

    }

    public void removeFromMap() {
        Iterator var1 = this.markers.iterator();

        while(var1.hasNext()) {
            Marker var2 = (Marker)var1.next();
            var2.remove();
        }

    }

    public void zoomToSpan() {
        try {
            if(this.poiItems != null && this.poiItems.size() > 0) {
                if(this.aMap == null) {
                    return;
                }

                if(this.poiItems.size() == 1) {
                    this.aMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(((PoiItem)this.poiItems.get(0)).getLatLonPoint().getLatitude(), ((PoiItem)this.poiItems.get(0)).getLatLonPoint().getLongitude()), 18.0F));
                } else {
                    LatLngBounds var1 = this.getMO();
                    this.aMap.moveCamera(CameraUpdateFactory.newLatLngBounds(var1, 5));
                }
            }
        } catch (Throwable var2) {
            var2.printStackTrace();
        }

    }

    private LatLngBounds getMO() {
        Builder var1 = LatLngBounds.builder();

        for(int var2 = 0; var2 < this.poiItems.size(); ++var2) {
            var1.include(new LatLng(((PoiItem)this.poiItems.get(var2)).getLatLonPoint().getLatitude(), ((PoiItem)this.poiItems.get(var2)).getLatLonPoint().getLongitude()));
        }

        return var1.build();
    }

    private MarkerOptions getMO(int var1) {
        return (new MarkerOptions()).position(new LatLng(((PoiItem)this.poiItems.get(var1)).getLatLonPoint().getLatitude(), ((PoiItem)this.poiItems.get(var1)).getLatLonPoint().getLongitude())).icon(this.getBitmapDescriptor(var1,this.getTitle(var1),this.getSnippet(var1)));
    }


    public BitmapDescriptor getBitmapDescriptor(int var1,String title,String s) {
        View getres = views.get(var1);
        render(title,s,getres,var1);
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromBitmap(BitmapUtils.getInstance().getBitmapFromView(getres));
        return bitmapDescriptor;
    }


    private  void render(String title,String snippet, View view,int position) {
        ((ImageView) view.findViewById(R.id.badge))
                .setImageResource(R.mipmap.badge_sa);
        ((ImageView) view.findViewById(R.id.littlereddian))
                .setImageResource(srcs[position]);
//        String title = marker.getTitle();
        TextView titleUi = ((TextView) view.findViewById(R.id.title));
        if (title != null) {
            SpannableString titleText = new SpannableString(title);
            titleText.setSpan(new ForegroundColorSpan(Color.RED), 0,
                    titleText.length(), 0);
            titleUi.setTextSize(15);
            titleUi.setText(titleText);

        } else {
            titleUi.setText("");
        }
//        String snippet = marker.getSnippet();
        TextView snippetUi = ((TextView) view.findViewById(R.id.snippet));
        if (snippet != null) {
            SpannableString snippetText = new SpannableString(snippet);
            snippetText.setSpan(new ForegroundColorSpan(Color.GREEN), 0,
                    snippetText.length(), 0);
            snippetUi.setTextSize(20);
            snippetUi.setText(snippetText);
        } else {
            snippetUi.setText("");
        }
    }

    protected String getTitle(int var1) {
        return ((PoiItem)this.poiItems.get(var1)).getTitle();
    }

    protected String getSnippet(int var1) {
        return ((PoiItem)this.poiItems.get(var1)).getSnippet();
    }

    public int getPoiIndex(Marker var1) {
        for(int var2 = 0; var2 < this.markers.size(); ++var2) {
            if(((Marker)this.markers.get(var2)).equals(var1)) {
                return var2;
            }
        }

        return -1;
    }

    public PoiItem getPoiItem(int var1) {
        return var1 >= 0 && var1 < this.poiItems.size()?(PoiItem)this.poiItems.get(var1):null;
    }
}