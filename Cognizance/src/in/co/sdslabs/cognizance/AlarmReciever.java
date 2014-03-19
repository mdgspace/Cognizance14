package in.co.sdslabs.cognizance;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

public class AlarmReciever extends BroadcastReceiver {

	NotificationManager nm;
	
	@SuppressWarnings("deprecation")
	@Override
	public void onReceive(Context context, Intent intent) {
		

		String eventname = intent.getStringExtra("event");
		Intent myIntent = new Intent(context, EventActivity.class);
		Bundle data = new Bundle();
		data.putString("event", eventname);
		myIntent.putExtras(data);
		nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		CharSequence from = "Cognizance 2014";
		
		CharSequence message = eventname;
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				myIntent, 0);
		Notification notif = new Notification(R.drawable.launcher_icon,
				"enter name of event that needs to be notified about", System.currentTimeMillis());
		notif.flags = Notification.FLAG_INSISTENT;
		notif.setLatestEventInfo(context, from, message, contentIntent);
		notif.flags |= Notification.FLAG_AUTO_CANCEL;
		nm.notify(1, notif);
		
	}
};