package in.co.sdslabs.cognizance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

public class AlarmReciever extends BroadcastReceiver {

	NotificationManager nm;
	
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		

		String eventname = intent.getExtras().getString("event");
		
		Intent myIntent = new Intent(context, in.co.sdslabs.cognizance.EventActivity.class);
		Bundle data = new Bundle();
		data.putString("event", eventname);
		data.putBoolean("dept", false);
		myIntent.putExtras(data);
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence from = "Cognizance 2014";
		
		CharSequence message = eventname;
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				myIntent, PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_ONE_SHOT);
				
		
		Notification notif = new Notification(R.drawable.launcher_icon,
				eventname, System.currentTimeMillis());
		notif.flags = Notification.FLAG_INSISTENT;
		notif.setLatestEventInfo(context, from, message, contentIntent);
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		
		nm.notify(1, notif);
		
	}
};