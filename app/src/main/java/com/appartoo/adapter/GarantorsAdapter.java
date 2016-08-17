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

import java.util.ArrayList;

import com.appartoo.R;
import com.appartoo.model.GarantorModel;

/**
 * Created by alexandre on 16-07-05.
 */
public class GarantorsAdapter extends BaseAdapter {

    private ArrayList<GarantorModel> garantorModels;
    private LayoutInflater layoutInflater;
    private Context context;

    public GarantorsAdapter(Context context, ArrayList<GarantorModel> rm) {
        this.context = context;
        this.garantorModels = rm;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return garantorModels.size();
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
            convertView = layoutInflater.inflate(R.layout.list_item_guarantor, null);
            holder = new ViewHolder();
            holder.fullname = (TextView) convertView.findViewById(R.id.garantorFullName);
            holder.income = (TextView) convertView.findViewById(R.id.garantorIncome);
            holder.mail = (TextView) convertView.findViewById(R.id.garantorMail);
            holder.delete = (ImageView) convertView.findViewById(R.id.deleteGarantor);
            holder.modify = (ImageView) convertView.findViewById(R.id.modifyGarantor);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final GarantorModel garantorModel = garantorModels.get(position);
        holder.fullname.setText(garantorModel.getFirstname() + " " + garantorModel.getLastname());
        holder.income.setText(Float.toString(garantorModel.getIncome()) +  " " + context.getString(R.string.euro_per_month));
        holder.mail.setText(garantorModel.getMail());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                garantorModels.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_garantor, null);
                android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(context);

                ((EditText) dialogLayout.findViewById(R.id.garantorRecordFirstName)).setText(garantorModel.getFirstname());
                ((EditText) dialogLayout.findViewById(R.id.garantorRecordLastName)).setText(garantorModel.getLastname());
                ((EditText) dialogLayout.findViewById(R.id.garantorRecordMail)).setText(garantorModel.getMail());
                ((EditText) dialogLayout.findViewById(R.id.garantorRecordIncome)).setText(Float.toString(garantorModel.getIncome()));

                selectContractDialog.setTitle("Ajouter un garant");
                selectContractDialog.setView(dialogLayout);
                selectContractDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        garantorModel.setFirstname(((EditText) dialogLayout.findViewById(R.id.garantorRecordFirstName)).getText().toString());
                        garantorModel.setLastname(((EditText) dialogLayout.findViewById(R.id.garantorRecordLastName)).getText().toString());
                        garantorModel.setMail(((EditText) dialogLayout.findViewById(R.id.garantorRecordMail)).getText().toString());
                        garantorModel.setIncome(Float.valueOf(((EditText) dialogLayout.findViewById(R.id.garantorRecordIncome)).getText().toString()));

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
        TextView income;
        TextView mail;
        ImageView modify;
        ImageView delete;
    }
}