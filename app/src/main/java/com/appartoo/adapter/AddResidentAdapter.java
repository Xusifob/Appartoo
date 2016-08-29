package com.appartoo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.appartoo.R;
import com.appartoo.model.UserModel;

import java.util.ArrayList;

/**
 * Created by alexandre on 16-07-05.
 */
public class AddResidentAdapter extends BaseAdapter {

    private ArrayList<UserModel> residentModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public AddResidentAdapter(Context context, ArrayList<UserModel> rm) {
        this.context = context;
        this.residentModels = rm;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return residentModels.size();
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
            convertView = layoutInflater.inflate(R.layout.list_item_resident, null);
            holder = new ViewHolder();
            holder.fullname = (TextView) convertView.findViewById(R.id.residentFullName);
            holder.delete = (ImageView) convertView.findViewById(R.id.deleteResident);
            holder.modify = (ImageView) convertView.findViewById(R.id.modifyResident);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final UserModel residentModel = residentModels.get(position);
        holder.fullname.setText(residentModel.getGivenName() + " " + residentModel.getFamilyName());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                residentModels.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_resident, null);
                android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(context);

                ((EditText) dialogLayout.findViewById(R.id.residentRecordFirstName)).setText(residentModel.getGivenName());
                ((EditText) dialogLayout.findViewById(R.id.residentRecordLastName)).setText(residentModel.getFamilyName());

                selectContractDialog.setTitle("Ajouter un garant");
                selectContractDialog.setView(dialogLayout);
                selectContractDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        residentModel.setGivenName(((EditText) dialogLayout.findViewById(R.id.residentRecordFirstName)).getText().toString());
                        residentModel.setFamilyName(((EditText) dialogLayout.findViewById(R.id.residentRecordLastName)).getText().toString());
                        notifyDataSetChanged();
                    }
                });

                selectContractDialog.setNegativeButton(R.string.cancel, null);
                selectContractDialog.show();
            }
        });

        return convertView;
    }

    private static class ViewHolder {
        TextView fullname;
        TextView mail;
        ImageView modify;
        ImageView delete;
    }
}