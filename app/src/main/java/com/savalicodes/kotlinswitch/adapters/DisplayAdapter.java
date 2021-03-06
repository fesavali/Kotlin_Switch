package com.savalicodes.kotlinswitch.adapters;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.sriyank.javatokotlindemo.R;
import com.sriyank.javatokotlindemo.app.Util;
import com.sriyank.javatokotlindemo.models.Repository;

import java.util.List;



public class DisplayAdapter extends RecyclerView.Adapter<DisplayAdapter.MyViewHolder> {

	private static final String TAG = DisplayAdapter.class.getSimpleName();

	private List<Repository> mData;
	private LayoutInflater inflater;
	private Context mContexts;

	public DisplayAdapter(Context context, List<Repository> items) {
		inflater = LayoutInflater.from(context);
		this.mData = items;
		this.mContexts = context;
	}

	@Override
	public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
		View view = inflater.inflate(R.layout.list_item, parent, false);
		MyViewHolder holder = new MyViewHolder(view);
		return holder;
	}

	@Override
	public void onBindViewHolder(MyViewHolder holder, int position) {
		Repository current = mData.get(position);
		holder.setData(current, position);
	}

	@Override
	public int getItemCount() {
		return mData.size();
	}

	public void swap(List<Repository> data)
	{
		if (data.size() == 0)
			Util.showMessage(mContexts, "No Items Found");
		mData = data;
		notifyDataSetChanged();
	}

	class MyViewHolder extends RecyclerView.ViewHolder {

		private TextView name, language, stars, watchers, forks;
		private int position;
		private ImageView imgBookmark;
		private Repository current;

		public MyViewHolder(View itemView) {
			super(itemView);

			name = itemView.findViewById(R.id.txvName);
			language = itemView.findViewById(R.id.txvLanguage);
			stars = itemView.findViewById(R.id.txvStars);
			watchers = itemView.findViewById(R.id.txvWatchers);
			forks = itemView.findViewById(R.id.txvForks);

			imgBookmark = itemView.findViewById(R.id.img_bookmark);
			imgBookmark.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					bookmarkRepository(current);
				}
			});

			itemView.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					String url = current.getHtmlUrl();
					Uri webpage = Uri.parse(url);
					Intent intent = new Intent(Intent.ACTION_VIEW, webpage);
					if (intent.resolveActivity(mContexts.getPackageManager()) != null) {
						mContexts.startActivity(intent);
					}
				}
			});
		}

		public void setData(Repository current, int position) {

			this.name.setText(current.getName());
			this.language.setText(String.valueOf(current.getLanguage()));
			this.forks.setText(String.valueOf(current.getForks()));
			this.watchers.setText(String.valueOf(current.getWatchers()));
			this.stars.setText(String.valueOf(current.getStars()));
			this.position = position;
			this.current = current;
		}


	}
}
