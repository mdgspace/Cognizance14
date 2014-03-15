package in.co.sdslabs.cognizance;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.database.SQLException;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class ContactListActivity extends ListActivity implements
		OnItemClickListener {

	// private ListView mContactList;
	private String name = "";
	private String number = "";
	private String email = "";
	private String post = "";
	ListView lv;
	ArrayList<String> contacts_name;
	ArrayList<String> contacts_number;
	ArrayList<String> contacts_email;
	ArrayList<String> contacts_post;

	int position;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lv = getListView();
		lv.setOnItemClickListener(this);

		List<ContactClass> contactList = new ArrayList<ContactClass>();
		int i = 0;
		DatabaseHelper myDbHelper = new DatabaseHelper(ContactListActivity.this);
		try {
			myDbHelper.createDataBase();
		} catch (IOException ioe) {
			throw new Error("Unable to create database");
		}

		try {
			myDbHelper.openDataBase();
		} catch (SQLException sqle) {
			throw sqle;
		}
		contacts_name = myDbHelper.getcontactsname();
		contacts_number = myDbHelper.getcontactsnumber();
		contacts_email = myDbHelper.getcontactsemail();
		contacts_post = myDbHelper.getcontactspost();
		while (i < contacts_name.size()) {
			ContactClass addContact = new ContactClass();
			addContact.setName(contacts_name.get(i));
			addContact.setNumber(contacts_number.get(i));
			addContact.setEmail(contacts_email.get(i));
			addContact.setPost(contacts_post.get(i));
			contactList.add(addContact);
			i++;
		}
		setListAdapter(new ContactAdapter(this, contactList));
	}

	@Override
	public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
		ContactClass contact_item = (ContactClass) getListAdapter().getItem(
				arg2);
		name = contact_item.getName();
		number = contact_item.getNumber();
		email = contact_item.getEmail();
		post = contact_item.getPost();
		final Dialog dialog = new Dialog(ContactListActivity.this);
		dialog.setContentView(R.layout.dialogbox);
		dialog.setTitle(name);
		ImageButton call = (ImageButton) dialog.findViewById(R.id.call);

		call.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (number.length() == 14) {
					Intent callIntent = new Intent(Intent.ACTION_CALL);
					callIntent.setData(Uri.parse("tel:" + number));
					startActivity(callIntent);
				}
			}
		});
		ImageButton message = (ImageButton) dialog.findViewById(R.id.msg);
		message.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				// TODO Auto-generated method stub
				try {
					if (number.length() == 14) {
						Intent sendIntent = new Intent(Intent.ACTION_VIEW);
						sendIntent.putExtra("sms_body", "");
						sendIntent.putExtra("address", number);
						sendIntent.setType("vnd.android-dir/mms-sms");
						startActivity(sendIntent);
					}
				} catch (Exception e) {
					Toast.makeText(getApplicationContext(),
							"SMS faild, please try again later!",
							Toast.LENGTH_LONG).show();
					e.printStackTrace();
				}

			}
		});
		ImageButton mail = (ImageButton) dialog.findViewById(R.id.email);

		mail.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				if (email.contains("@")) {
					Intent massemail = new Intent(Intent.ACTION_SEND);

					massemail.putExtra(Intent.EXTRA_EMAIL,
							new String[] { email });
					massemail.setType("message/rfc822");
					startActivity(Intent.createChooser(massemail,
							"Choose an Email client :"));
				}
			}
		});

		dialog.show();
	}
}
