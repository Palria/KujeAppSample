package com.govance.businessprojectconfiguration;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class SystemBootDetector extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        context.startService(new Intent(context,NotificationHandlerService.class));

    }
}