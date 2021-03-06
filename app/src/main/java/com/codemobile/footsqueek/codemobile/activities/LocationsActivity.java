package com.codemobile.footsqueek.codemobile.activities;

import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.codemobile.footsqueek.codemobile.AppDelegate;
import com.codemobile.footsqueek.codemobile.R;
import com.codemobile.footsqueek.codemobile.adapters.LocationRecyclerAdapter;
import com.codemobile.footsqueek.codemobile.database.Location;
import com.codemobile.footsqueek.codemobile.database.LocationRowType;
import com.codemobile.footsqueek.codemobile.database.LocationWithHeaders;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;

/**
 * Created by greg on 06/02/2017.
 */

public class LocationsActivity extends BaseActivity {

    LocationRecyclerAdapter adapter;
    RecyclerView recyclerView;
    List<LocationWithHeaders> locationsWithHeaders = new ArrayList<>();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);
       // fetchSchedule();
        if(getResources().getBoolean(R.bool.portrait_only)){
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        }
        recyclerView = (RecyclerView)findViewById(R.id.recycler);

        recyclerView.setHasFixedSize(true);



        addHeaders();
        GridLayoutManager glm = new GridLayoutManager(getApplicationContext(),2);
        glm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if(locationsWithHeaders.get(position).getRowType() == LocationRowType.HEADER){
                    return 2;
                }else{
                    return 1;
                }

            }
        });
        recyclerView.setLayoutManager(glm);
        adapter = new LocationRecyclerAdapter(locationsWithHeaders,getApplicationContext());
        recyclerView.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        setupActionBar();
        getSupportActionBar().setTitle("Locations");
        navigationViewItemPosition = 3;

    }

    public void addHeaders(){

        List<Location> locations = getLocations();
        for (int i = 0; i < locations.size(); i++) {

        }

        String lastType = "";

        for (int i = 0; i < locations.size(); i++) {

            if(i == 0){

                LocationWithHeaders header = new LocationWithHeaders(
                        null,
                        LocationRowType.HEADER,
                        locations.get(i).getType()
                );
                LocationWithHeaders row = new LocationWithHeaders(
                        locations.get(i),
                        LocationRowType.ROW,
                        null
                );
                locationsWithHeaders.add(header);
                locationsWithHeaders.add(row);
            }else if(locations.get(i).getType().equals(lastType)){
                LocationWithHeaders row = new LocationWithHeaders(
                        locations.get(i),
                        LocationRowType.ROW,
                        null
                );
                locationsWithHeaders.add(row);
            }else if(!locations.get(i).getType().equals(lastType)){
                LocationWithHeaders header = new LocationWithHeaders(
                        null,
                        LocationRowType.HEADER,
                        locations.get(i).getType()
                );
                LocationWithHeaders row = new LocationWithHeaders(
                        locations.get(i),
                        LocationRowType.ROW,
                        null
                );
                locationsWithHeaders.add(header);
                locationsWithHeaders.add(row);
            }

            lastType = locations.get(i).getType();
        }



    }

    public List<Location> getLocations(){
        Realm realm = AppDelegate.getRealmInstance();

        return realm.where(Location.class).findAllSorted("type");
    }

    @Override
    protected void onResume() {
        super.onResume();
        navigationViewItemPosition = 3;
    }



    /*@Override
    public void updateUi() {
        super.updateUi();
        addHeaders();
        adapter.notifyDataSetChanged();
        Log.d("Realmstuff", "locations adapter updated");
    }*/
}
