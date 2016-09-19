package com.appartoo.utils.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.PorterDuff;
import android.graphics.drawable.LayerDrawable;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.utils.model.CommentModel;
import com.appartoo.utils.model.GarantorModel;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

/**
 * Created by alexandre on 16-07-05.
 */
public class CommentsAdapter extends BaseAdapter {

    private ArrayList<CommentModel> commentModels;
    private LayoutInflater layoutInflater;
    private Context context;
    private SimpleDateFormat dateFormat;

    public CommentsAdapter(Context context, ArrayList<CommentModel> cm) {
        this.commentModels = cm;
        this.context = context;
        this.layoutInflater = LayoutInflater.from(context);
        this.dateFormat = new SimpleDateFormat("dd/MM/yy HH:mm", Locale.getDefault());
    }

    @Override
    public int getCount() {
        return commentModels.size();
    }

    @Override
    public Object getItem(int position) {
        return position;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder holder;
        if (convertView == null) {
            convertView = layoutInflater.inflate(R.layout.list_item_comments, null);
            holder = new ViewHolder();
            holder.givenName = (TextView) convertView.findViewById(R.id.commentName);
            holder.noteBar = (RatingBar) convertView.findViewById(R.id.commentNoteStars);
            holder.date = (TextView) convertView.findViewById(R.id.commentDate);
            holder.description = (TextView) convertView.findViewById(R.id.commentDescription);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CommentModel commentModel = commentModels.get(position);
        holder.givenName.setText(commentModel.getGivenName());
        holder.noteBar.setRating(commentModel.getNote());
        holder.description.setText(commentModel.getDescription());
        holder.date.setText(dateFormat.format(new Date(commentModel.getCreatedTime()*1000)));

        return convertView;
    }

    private static class ViewHolder {
        TextView givenName;
        RatingBar noteBar;
        TextView date;
        TextView description;
    }
}