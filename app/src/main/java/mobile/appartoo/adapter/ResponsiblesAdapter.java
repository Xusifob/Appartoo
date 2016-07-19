package mobile.appartoo.adapter;

import android.content.Context;
import android.content.DialogInterface;
import android.support.v4.content.res.ResourcesCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;

import mobile.appartoo.R;
import mobile.appartoo.model.ResponsibleModel;
import mobile.appartoo.model.UserModel;

/**
 * Created by alexandre on 16-07-05.
 */
public class ResponsiblesAdapter extends BaseAdapter {

    private ArrayList<ResponsibleModel> responsibleModels;
    private LayoutInflater layoutInflater;
    private Boolean acceptAnimals;
    private Context context;

    public ResponsiblesAdapter(Context context, ArrayList<ResponsibleModel> rm) {
        this.context = context;
        this.responsibleModels = rm;
        this.layoutInflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return responsibleModels.size();
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
            convertView = layoutInflater.inflate(R.layout.list_item_responsible, null);
            holder = new ViewHolder();
            holder.fullname = (TextView) convertView.findViewById(R.id.responsibleFullName);
            holder.income = (TextView) convertView.findViewById(R.id.responsibleIncome);
            holder.mail = (TextView) convertView.findViewById(R.id.responsibleMail);
            holder.delete = (ImageView) convertView.findViewById(R.id.deleteResponsible);
            holder.modify = (ImageView) convertView.findViewById(R.id.modifyResponsible);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        final ResponsibleModel responsibleModel = responsibleModels.get(position);
        holder.fullname.setText(responsibleModel.getFirstname() + " " + responsibleModel.getLastname());
        holder.income.setText(Float.toString(responsibleModel.getIncome()) +  " " + "€/mois");
        holder.mail.setText(responsibleModel.getMail());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                responsibleModels.remove(position);
                notifyDataSetChanged();
            }
        });

        holder.modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final View dialogLayout = layoutInflater.inflate(R.layout.alert_dialog_responsible, null);
                android.app.AlertDialog.Builder selectContractDialog = new android.app.AlertDialog.Builder(context);

                ((EditText) dialogLayout.findViewById(R.id.responsibleRecordFirstName)).setText(responsibleModel.getFirstname());
                ((EditText) dialogLayout.findViewById(R.id.responsibleRecordLastName)).setText(responsibleModel.getLastname());
                ((EditText) dialogLayout.findViewById(R.id.responsibleRecordMail)).setText(responsibleModel.getMail());
                ((EditText) dialogLayout.findViewById(R.id.responsibleRecordIncome)).setText(Float.toString(responsibleModel.getIncome()));

                selectContractDialog.setTitle("Ajouter un garant");
                selectContractDialog.setView(dialogLayout);
                selectContractDialog.setPositiveButton("Ok", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        responsibleModel.setFirstname(((EditText) dialogLayout.findViewById(R.id.responsibleRecordFirstName)).getText().toString());
                        responsibleModel.setLastname(((EditText) dialogLayout.findViewById(R.id.responsibleRecordLastName)).getText().toString());
                        responsibleModel.setMail(((EditText) dialogLayout.findViewById(R.id.responsibleRecordMail)).getText().toString());
                        responsibleModel.setIncome(Float.valueOf(((EditText) dialogLayout.findViewById(R.id.responsibleRecordIncome)).getText().toString()));

                        notifyDataSetChanged();
                    }
                });

                selectContractDialog.setNegativeButton("Annuler", null);
                selectContractDialog.show();
            }
        });

        return convertView;
    }

    static class ViewHolder {
        TextView fullname;
        TextView income;
        TextView mail;
        ImageView modify;
        ImageView delete;
    }
}