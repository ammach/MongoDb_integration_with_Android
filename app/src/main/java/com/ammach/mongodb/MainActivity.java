package com.ammach.mongodb;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import bean.Compte;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import services.CompteService;
import services.OperationService;

public class MainActivity extends AppCompatActivity {


    @BindView(R.id.editIdCompte)
    EditText editIdCompte;
    @BindView(R.id.editSoldeCompte)
    EditText editSoldeCompte;
    @BindView(R.id.spinnerCompte)
    Spinner spinnerCompte;
    @BindView(R.id.spinnerTypeOperation)
    Spinner spinnerTypeOperation;
    @BindView(R.id.editMontantOp)
    EditText editMontantOp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setDataToSpinners();
    }

    private void setDataToSpinners() {
        List<String> listCompte = new ArrayList<>();
        for (Compte compte : new CompteService(this).getComptes()) {
            listCompte.add(compte.getIdCompte());
        }
        ArrayAdapter<String> dataAdapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listCompte);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCompte.setAdapter(dataAdapter);

        List<String> listOperation = new ArrayList<>();
        listOperation.add("Débit");
        listOperation.add("Crédit");
        ArrayAdapter<String> dataAdapter1 = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, listOperation);
        dataAdapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerTypeOperation.setAdapter(dataAdapter1);
    }


    private void createOperation() {
        OperationService operationService = new OperationService(this);
        Boolean create=operationService.createOperation(spinnerCompte.getSelectedItem().toString(),spinnerTypeOperation.getSelectedItem().toString(), Double.valueOf(editMontantOp.getText().toString()));
        if(create){
            Toast.makeText(MainActivity.this, "opération crée pour le compte " + spinnerCompte.getSelectedItem().toString(), Toast.LENGTH_SHORT).show();
            editMontantOp.setText("");
        }else{
            Toast.makeText(MainActivity.this, "opération n'est pas permise", Toast.LENGTH_SHORT).show();
        }
    }

    private void createCompte() {
        CompteService compteService = new CompteService(this);
        Boolean create = compteService.createCompte(editIdCompte.getText().toString(), Double.valueOf(editSoldeCompte.getText().toString()));
        if (create == true) {
            Toast.makeText(MainActivity.this, "compte crée", Toast.LENGTH_SHORT).show();
            setDataToSpinners();
            editIdCompte.setText("");
            editSoldeCompte.setText("");
        } else {
            Toast.makeText(MainActivity.this, "compte n'est pas crée", Toast.LENGTH_SHORT).show();
        }
    }



    @OnClick({R.id.createCompte, R.id.listeComptes, R.id.createOp})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createCompte:
                createCompte();
                break;
            case R.id.listeComptes:
                startActivity(new Intent(this, ComptesActivity.class));
                break;
            case R.id.createOp:
                createOperation();
                break;
        }
    }
}
