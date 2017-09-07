package ui.service;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.widget.Toast;

/**
 * Created by Tyhj on 2017/9/6.
 */

public class BootCompleteReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        Toast.makeText(context,"哈哈",Toast.LENGTH_SHORT).show();
        context.startService(new Intent(context,MyService.class));
    }
}
