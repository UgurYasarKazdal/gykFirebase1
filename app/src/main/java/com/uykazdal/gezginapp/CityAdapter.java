package com.uykazdal.gezginapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.List;

/**
 * {@link RecyclerView.Adapter} that can display a {@link CityModel} and makes a call to the
 * TODO: Replace the implementation with code for your data type.
 */
public class CityAdapter extends RecyclerView.Adapter<CityAdapter.ViewHolder> {

    private final List<CityModel> mValues;

    public CityAdapter(List<CityModel> items) {
        mValues = items;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_home_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        holder.mItem = mValues.get(position);
        holder.mIdView.setText(mValues.get(position).getName());
        holder.mContentView.setText(mValues.get(position).getContent());
        holder.mImageView.setImageResource(mValues.get(position).getPhoto());


        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dialog = new AlertDialog.Builder(holder.mView.getContext())
                        .setMessage(holder.mItem.getContent())
                        .setTitle(holder.mItem.getName())
                        .setIcon(holder.mItem.getPhoto())
                        .setPositiveButton("Tamam", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });

                dialog.show();


       /*         if (null != mListener) {
                    // Notify the active callbacks interface (the activity, if the
                    // fragment is attached to one) that an item has been selected.
                    mListener.onListFragmentInteraction(holder.mItem);
                }*/
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mIdView;
        public final TextView mContentView;
        public final ImageView mImageView;
        public CityModel mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mIdView = (TextView) view.findViewById(R.id.tv_homeRow_city);
            mContentView = (TextView) view.findViewById(R.id.tv_homeRow_content);
            mImageView = view.findViewById(R.id.imageView);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + mContentView.getText() + "'";
        }
    }
}
