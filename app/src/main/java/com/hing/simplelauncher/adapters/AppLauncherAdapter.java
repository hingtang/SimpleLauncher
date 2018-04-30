package com.hing.simplelauncher.adapters;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.hing.simplelauncher.R;
import com.hing.simplelauncher.interfaces.IOnRecyclerViewItemClickListener;

import java.util.List;

/**
 * Created by HingTang on 4/29/18.
 */
public class AppLauncherAdapter extends RecyclerView.Adapter<AppLauncherAdapter.AppLauncherViewHolder> {
    private List<ApplicationInfo> applicationList;
    private PackageManager packageManager;
    private IOnRecyclerViewItemClickListener onRecyclerViewItemClickListener;

    public AppLauncherAdapter(Context context, List<ApplicationInfo> applicationList,
                              IOnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
        this.applicationList = applicationList;
        this.onRecyclerViewItemClickListener= onRecyclerViewItemClickListener;
        packageManager= context.getPackageManager();
    }

    public void setApplicationList(List<ApplicationInfo> applicationList){
        this.applicationList = applicationList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AppLauncherViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_app_launcher, parent, false);
        return new AppLauncherViewHolder(itemView, onRecyclerViewItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull AppLauncherViewHolder holder, int position) {
        ApplicationInfo applicationInfo = applicationList.get(position);
        holder.imgvAppLauncher.setImageDrawable(applicationInfo.loadIcon(packageManager));
        holder.tvAppName.setText(applicationInfo.loadLabel(packageManager));
    }

    @Override
    public int getItemCount() {
        return applicationList.size();
    }

    public class AppLauncherViewHolder extends RecyclerView.ViewHolder {
        private ImageView imgvAppLauncher;
        private TextView tvAppName;

        public AppLauncherViewHolder(View itemView, final IOnRecyclerViewItemClickListener onRecyclerViewItemClickListener) {
            super(itemView);
            imgvAppLauncher = itemView.findViewById(R.id.imgvAppLauncher);
            tvAppName = itemView.findViewById(R.id.tvAppName);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    onRecyclerViewItemClickListener.onItemClick(getAdapterPosition());
                }
            });
        }
    }
}
