package com.hing.simplelauncher.activities;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.hing.simplelauncher.R;
import com.hing.simplelauncher.adapters.AppLauncherAdapter;
import com.hing.simplelauncher.interfaces.IOnRecyclerViewItemClickListener;

import java.util.ArrayList;
import java.util.List;

public class SimpleLauncherActivity extends AppCompatActivity {
    private String TAG="SimpleLauncherActivity";
    private RecyclerView appLauncherGridView;
    private ProgressBar progressBar;
    private AppLauncherAdapter appLauncherAdapter;
    private List<ApplicationInfo> applicationList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_simple_launcher);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        appLauncherGridView = findViewById(R.id.appLauncherGridView);
        progressBar = findViewById(R.id.progressBar);

        initAppLauncherGridView();
        new LoadApplications().execute();
    }

    private void initAppLauncherGridView(){
        appLauncherAdapter= new AppLauncherAdapter(this, applicationList, new IOnRecyclerViewItemClickListener() {
            @Override
            public void onItemClick(int position) {
                ApplicationInfo applicationInfo = applicationList.get(position);
                try{
                    Intent intent = getPackageManager().getLaunchIntentForPackage(applicationInfo.packageName);
                    if(intent!=null){
                        startActivity(intent);
                    }else{
                        Toast.makeText(SimpleLauncherActivity.this, R.string.activity_not_found_message, Toast.LENGTH_SHORT).show();
                    }
                }catch (ActivityNotFoundException e){
                    Toast.makeText(SimpleLauncherActivity.this, R.string.activity_not_found_message, Toast.LENGTH_SHORT).show();
                }catch (Exception e){
                    Toast.makeText(SimpleLauncherActivity.this,"",Toast.LENGTH_SHORT).show();
                }
            }
        });
        appLauncherGridView.setHasFixedSize(true);
        appLauncherGridView.setLayoutManager(new GridLayoutManager(this, 4));
    }

    private List<ApplicationInfo> checkForLaunchIntent(List<ApplicationInfo> applicationList){
        ArrayList<ApplicationInfo> result = new ArrayList<>();
        for(ApplicationInfo item :applicationList){
            try{
               if(getPackageManager().getLaunchIntentForPackage(item.packageName)!=null){
                   result.add(item);
               }
            }catch (Exception e){
                e.printStackTrace();
                Log.e(TAG,e.getMessage());
            }
        }
        return result;
    }

    private class LoadApplications extends AsyncTask<Void, Void, Void>{
        @Override
        protected void onPreExecute() {
            progressBar.setVisibility(View.VISIBLE);
            appLauncherGridView.setVisibility(View.GONE);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            applicationList = checkForLaunchIntent(getPackageManager().getInstalledApplications(PackageManager.GET_META_DATA));
            appLauncherAdapter.setApplicationList(applicationList);

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            appLauncherGridView.setAdapter(appLauncherAdapter);
            progressBar.setVisibility(View.GONE);
            appLauncherGridView.setVisibility(View.VISIBLE);
            super.onPostExecute(aVoid);
        }
    }
}
