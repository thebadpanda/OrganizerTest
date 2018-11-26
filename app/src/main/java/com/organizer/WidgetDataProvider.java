package com.organizer;

import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.RemoteViews;
import android.widget.RemoteViewsService.RemoteViewsFactory;

import static com.organizer.WidgetProvider.ACTION_CHECK;

public class WidgetDataProvider implements RemoteViewsFactory {

    List<String> mCollections = new ArrayList<>();

    Context mContext = null;
    private SharedPreferences prefs;

    WidgetDataProvider(Context context, Intent intent) {
        mContext = context;
    }

    @Override
    public void onCreate() {
        initData();
    }

    @Override
    public void onDataSetChanged() {
        initData();
    }

    @Override
    public int getCount() {
        return mCollections.size();
    }

    @Override
    public RemoteViews getLoadingView() {
        return null;
    }


    @Override
    public int getViewTypeCount() {
        return 1;
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public RemoteViews getViewAt(int position) {
        RemoteViews mView = new RemoteViews(mContext.getPackageName(), R.layout.widget_item);
        mView.setTextViewText(R.id.tvItem, mCollections.get(position));
        Intent checkedIntent = new Intent();
        checkedIntent.setAction(ACTION_CHECK);
        Bundle bundleCheck = new Bundle();
        bundleCheck.putInt(WidgetProvider.EXTRA_INT, position);
        bundleCheck.putString(WidgetProvider.EXTRA_STRING, mCollections.get(position));
        checkedIntent.putExtras(bundleCheck);
        mView.setOnClickFillInIntent(R.id.checkBtn, checkedIntent);

        return mView;
    }

    private void initData() {
        mCollections.clear();
        prefs = mContext.getSharedPreferences("sPref", Context.MODE_PRIVATE);
        if (prefs.getInt("counter", 0) > 0) {
            for (int i = 0; i < prefs.getInt("counter", 0); i++) {
                String myValue = prefs.getString("value_" + i, "");
                mCollections.add(myValue);
            }
        } else {
            System.out.println("Список пустий");
        }
    }

    @Override
    public void onDestroy() {

    }
}
