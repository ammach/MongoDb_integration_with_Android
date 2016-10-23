package com.ammach.mongodb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.List;

import adapters.ComptesAdapter;
import bean.Compte;
import butterknife.BindView;
import butterknife.ButterKnife;
import services.CompteService;

public class ComptesActivity extends AppCompatActivity {

    @BindView(R.id.listeComptes)
    ListView listeComptes;

    ComptesAdapter comptesAdapter;
    List<Compte> comptes;
    Compte actualCompte=new Compte();
    CompteService compteService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comptes);
        ButterKnife.bind(this);

        getData();
        comptesAdapter=new ComptesAdapter(comptes,this);
        listeComptes.setAdapter(comptesAdapter);

        registerForContextMenu(listeComptes);
    }


    // get list Comptes
    private void getData() {
        compteService=new CompteService(this);
        comptes=compteService.getComptes();
    }


    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo)menuInfo;
        actualCompte=comptes.get(info.position);
        menu.add(0, v.getId(), 0, "Delete");
        menu.add(0, v.getId(), 0, "Liste des Operations");
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {
        if (item.getTitle() == "Delete"){
            compteService=new CompteService(this);
            compteService.deleteCompte(actualCompte.getIdCompte());
            comptes.remove(actualCompte);
            comptesAdapter.notifyDataSetChanged();
            Toast.makeText(this, "compte supprimé", Toast.LENGTH_SHORT).show();
        }
        else if(item.getTitle() == "Liste des Operations"){
            Intent intent=new Intent(this,OperationsActivity.class);
            intent.putExtra("idCompte",actualCompte.getIdCompte());
            startActivity(intent);
        }
        return super.onContextItemSelected(item);
    }


}
