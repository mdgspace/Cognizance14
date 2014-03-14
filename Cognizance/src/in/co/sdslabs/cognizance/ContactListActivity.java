package in.co.sdslabs.cognizance;

import java.util.ArrayList;
import java.util.List;

import android.app.ListActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
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

	int position;

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		lv = getListView();
		lv.setOnItemClickListener(this);

		List<ContactClass> contactList = new ArrayList<ContactClass>();

		ContactClass addContact = new ContactClass();
		addContact.setName(name);
		addContact.setNumber(number);
		addContact.setEmail(email);
		addContact.setPost(post);
		contactList.add(addContact);

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

	}

}
