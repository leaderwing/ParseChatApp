package com.chatt.demo;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.starter.R;

/**
 * The Class UserList is the Activity class. It shows a list of all users of
 * this app. It also shows the Offline/Online status of users.
 */
public class UserList extends CustomActivity
{

	/** The Chat list. */
	private ArrayList<ParseUser> uList;

	/** The user. */
	public static ParseUser user;
	EditText editSearch;
	UserAdapter userAdapter = new UserAdapter();
	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState)
	{
		super.onCreate(savedInstanceState);
		setContentView(R.layout.user_list);

		//getActionBar().setDisplayHomeAsUpEnabled(false);
		ListView list = (ListView) findViewById(R.id.list);
		editSearch = (EditText) findViewById(R.id.editsearch);

		updateUserStatus(true);
		registerForContextMenu(list);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onDestroy()
	 */
	@Override
	protected void onDestroy()
	{
		super.onDestroy();
		updateUserStatus(false);
	}

	/* (non-Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onResume()
	 */
	@Override
	protected void onResume()
	{
		super.onResume();
		loadUserList();

	}

	/**
	 * Update user status.
	 * 
	 * @param online
	 *            true if user is online
	 */
	private void updateUserStatus(boolean online)
	{
		user.put("online", online);
		user.saveEventually();
	}

	/**
	 * Load list of users.
	 */
	private void loadUserList()
	{
		final ProgressDialog dia = ProgressDialog.show(this, null,
				getString(R.string.alert_loading));
		ParseUser.getQuery().whereNotEqualTo("username", user.getUsername())
				.findInBackground(new FindCallback<ParseUser>() {

					@Override
					public void done(List<ParseUser> li, ParseException e) {
						dia.dismiss();
						if (li != null) {
							if (li.size() == 0)
								Toast.makeText(UserList.this,
										R.string.msg_no_user_found,
										Toast.LENGTH_SHORT).show();

							uList = new ArrayList<ParseUser>(li);
							ListView list = (ListView) findViewById(R.id.list);

							list.setAdapter(userAdapter);
							list.setTextFilterEnabled(true);
							list.setOnItemClickListener(new OnItemClickListener() {

								@Override
								public void onItemClick(AdapterView<?> arg0,
														View arg1, int pos, long arg3) {
									startActivity(new Intent(UserList.this,
											Chat.class).putExtra(
											Const.EXTRA_DATA, uList.get(pos)
													.getUsername()));
								}
							});
							list.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
								@Override
								public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
									Bundle bundle=new Bundle();
									bundle.putString("User" , uList.get(position).getUsername());
									bundle.putString("Email" , uList.get(position).getEmail());
									startActivityForResult(new Intent(UserList.this, UserProfile.class).putExtra(Const.EXTRA_DATA, bundle), 100);
									//return onLongListItemClick(view, position, id);
									return  true;
								}
							});
						} else {
							Utils.showDialog(
									UserList.this,
									getString(R.string.err_users) + " "
											+ e.getMessage());
							e.printStackTrace();
						}
					}
				});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	if(requestCode == 100 && resultCode==RESULT_OK)
		finish();
	}
	//	private boolean onLongListItemClick(View view, int position, long id) {
//
//
//	}

	/**
	 * The Class UserAdapter is the adapter class for User ListView. This
	 * adapter shows the user name and it's only online status for each item.
	 */

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		//return super.onCreateOptionsMenu(menu);
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu_userlist,menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		//return super.onOptionsItemSelected(item);
		switch (item.getItemId())
		{
			case  R.id.search1:
				//Toast.makeText(this , "is already added to your friends" , Toast.LENGTH_LONG ).show();
				editSearch.setVisibility(View.VISIBLE);
				editSearch.addTextChangedListener(new TextWatcher() {
					@Override
					public void beforeTextChanged(CharSequence s, int start, int count, int after) {

					}

					@Override
					public void onTextChanged(CharSequence s, int start, int before, int count) {
						//UserList.this.userAdapter
						UserList.this.userAdapter.getFilter().filter(s);
					}

					@Override
					public void afterTextChanged(Editable s) {
						//String text = editSearch.getText().toString().toLowerCase(Locale.getDefault());


					}
				});
				Toast.makeText(this , "Search a name in list friend" , Toast.LENGTH_SHORT).show();
				break;
			case  R.id.edit:
				Toast.makeText(this , "Edit my profile" , Toast.LENGTH_SHORT ).show();
//				Bundle bundle=new Bundle();
//				bundle.putString("User", ParseUser.getCurrentUser().getUsername());
//				bundle.putString("Email", ParseUser.getCurrentUser().getEmail());
				startActivity(new Intent(UserList.this , EditProfile.class));
				break;
		}
		return true;
	}

	private class UserAdapter extends BaseAdapter implements Filterable
	{
			ListView lv;
		/* (non-Javadoc)
		 * @see android.widget.Adapter#getCount()
		 */
		@Override
		public int getCount()
		{
			return uList.size();
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItem(int)
		 */
		@Override
		public ParseUser getItem(int arg0)
		{
			return uList.get(arg0);
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getItemId(int)
		 */
		@Override
		public long getItemId(int arg0)
		{
			return arg0;
		}

		/* (non-Javadoc)
		 * @see android.widget.Adapter#getView(int, android.view.View, android.view.ViewGroup)
		 */
		@Override
		public View getView(int pos, View v, ViewGroup arg2)
		{
			if (v == null)
				v = getLayoutInflater().inflate(R.layout.chat_item, null);

			ParseUser c = getItem(pos);
			final TextView lbl = (TextView) v;
			//LinearLayout item = (LinearLayout) lbl.findViewById(R.id.item);
			lbl.setText(c.getUsername());

			if( Chat.hasMsg == true ) {
				//item.setBackgroundColor(Color.RED);
				//lbl.setBackgroundColor(Color.RED);
				//runnable.run();
				Chat.hasMsg = false;
			}
			else
			lbl.setBackgroundColor(Color.WHITE);
			lbl.setCompoundDrawablesWithIntrinsicBounds(
					c.getBoolean("online") ? R.drawable.ic_online
							: R.drawable.ic_offline, 0, R.drawable.arrow, 0);

			return v;

		}



		@Override
		public Filter getFilter() {
			Filter filter = new Filter() {

				@SuppressWarnings("unchecked")
				@Override
				protected void publishResults(CharSequence constraint, FilterResults results) {

					List arrayListNames = (List<String>) results.values;
					notifyDataSetChanged();
				}

				@Override
				protected FilterResults performFiltering(CharSequence constraint) {

					FilterResults results = new FilterResults();
					ArrayList<ParseUser> FilteredArrayNames = new ArrayList<>();

					// perform your search here using the searchConstraint String.

					constraint = constraint.toString().toLowerCase();
					for (int i = 0; i < uList.size(); i++) {
						ParseUser dataNames = uList.get(i);
						if (dataNames.getUsername().toLowerCase().startsWith(constraint.toString()))  {
							FilteredArrayNames.add(dataNames);
						}
					}

					results.count = FilteredArrayNames.size();
					results.values = FilteredArrayNames;
					Log.e("VALUES", results.values.toString());

					return results;
				}
			};

			return filter;
		}
	}


}
