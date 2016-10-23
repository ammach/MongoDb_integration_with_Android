package adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ammach.mongodb.R;

import java.util.List;

import bean.Compte;

/**
 * Created by ammach on 10/13/2016.
 */
public class ComptesAdapter extends BaseAdapter {

    List<Compte> comptes;
    Context context;

    public ComptesAdapter(List<Compte> comptes, Context context) {
        this.comptes = comptes;
        this.context = context;
    }

    @Override
    public int getCount() {
        return comptes.size();
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
        convertView=inflater.inflate(R.layout.compte_item,null);
        TextView txtIdCompte= (TextView) convertView.findViewById(R.id.idCompte);
        TextView txtSoldeCompte= (TextView) convertView.findViewById(R.id.soldeCompte);

        Compte compte=comptes.get(position);

        txtIdCompte.setText(compte.getIdCompte());
        txtSoldeCompte.setText("solde: "+compte.getSolde());

        return convertView;
    }
}
