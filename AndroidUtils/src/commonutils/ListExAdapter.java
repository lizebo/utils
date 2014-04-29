package commonutils;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

public class ListExAdapter extends BaseAdapter {

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public View getView(int arg0, View arg1, ViewGroup arg2) {
		// TODO Auto-generated method stub
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {

		if (convertView == null) {
		convertView = LayoutInflater.from(context)
		.inflate(R.layout.banana_phone, parent, false);
		}

		bananaView = ViewHolder.get(convertView, R.id.banana);
		phoneView = ViewHolder.get(convertView, R.id.phone);

		BananaPhone bananaPhone = getItem(position);
		phoneView.setText(bananaPhone.getPhone());
		bananaView.setImageResource(bananaPhone.getBanana());

		return convertView;
		}
	}

}

