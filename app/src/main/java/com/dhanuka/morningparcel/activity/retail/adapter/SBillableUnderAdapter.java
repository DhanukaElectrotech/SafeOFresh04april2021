package com.dhanuka.morningparcel.activity.retail.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dhanuka.morningparcel.R;

import java.util.List;

import com.dhanuka.morningparcel.activity.retail.beans.DbBillAbleUnder;

public class SBillableUnderAdapter extends ArrayAdapter<DbBillAbleUnder> {

	private Activity activity;
	private List<DbBillAbleUnder> data;

	DbBillAbleUnder tempValues = null;
	LayoutInflater inflater;

	/************* CustomAdapter Constructor *****************/
	public SBillableUnderAdapter(Activity activitySpinner, List<DbBillAbleUnder> objects) {
		super(activitySpinner, R.layout.row_country, objects);

		/********** Take passed values **********/
		activity = activitySpinner;
		data = objects;

		/*********** Layout inflator to call external xml layout () **********************/
		inflater = (LayoutInflater) activity
				.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	}

	@Override
	public View getDropDownView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		return getCustomView(position, convertView, parent);
	}

	// This funtion called for each row ( Called data.size() times )
	public View getCustomView(int position, View convertView, ViewGroup parent) {

		/********** Inflate spinner_rows.xml file for each row ( Defined below ) ************/
		View row = inflater.inflate(R.layout.row_country, parent, false);

		/***** Get each Model object from Arraylist ********/
		tempValues = null;
		tempValues = (DbBillAbleUnder) data.get(position);

		TextView label = (TextView) row.findViewById(R.id.tv);
		ImageView icon = (ImageView) row.findViewById(R.id.ic_icon);
        icon.setVisibility(View.GONE);
/*
		if(position==data.size()-1){
			icon.setVisibility(View.VISIBLE);
			label.setTextColor(parent.getContext().getResources().getColor(R.color.work_order_theme_color));
		}*/

		// Set values for spinner each row
		label.setText(tempValues.getmPrintName());

		return row;
	}

	@Override
	public int getCount() {
		return data.size();
	}
}