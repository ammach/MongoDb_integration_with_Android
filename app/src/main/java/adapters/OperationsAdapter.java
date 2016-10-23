package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ammach.mongodb.R;

import java.util.List;

import bean.Operation;

/**
 * Created by ammach on 10/13/2016.
 */
public class OperationsAdapter extends BaseAdapter {

    List<Operation> operations;
    Context context;

    public OperationsAdapter(List<Operation> operations, Context context) {
        this.operations = operations;
        this.context = context;
    }

    @Override
    public int getCount() {
        return operations.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {

        LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        convertView=inflater.inflate(R.layout.operation_item,null);

        TextView txtIdCompte= (TextView) convertView.findViewById(R.id.idCompte);
        TextView txtTypeOp= (TextView) convertView.findViewById(R.id.typeOp);
        TextView txtMontantOp= (TextView) convertView.findViewById(R.id.montantOp);

        Operation operation=operations.get(position);

        txtIdCompte.setText(operation.getIdCompte());
        txtTypeOp.setText(operation.getTypeOperation());
        txtMontantOp.setText("Montant: "+operation.getMontant());

        return convertView;
    }
}
