package com.ammach.mongodb;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

import adapters.OperationsAdapter;
import bean.Operation;
import butterknife.BindView;
import butterknife.ButterKnife;
import services.OperationService;

public class OperationsActivity extends AppCompatActivity {

    @BindView(R.id.listeOp)
    ListView listeOp;

    OperationsAdapter operationsAdapter;
    List<Operation> operations;
    Operation actualOperation = new Operation();
    @BindView(R.id.txtOps)
    TextView txtOps;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_operations);
        ButterKnife.bind(this);


        getData();
        operationsAdapter = new OperationsAdapter(operations, this);
        listeOp.setAdapter(operationsAdapter);

    }


    //get operations for compte
    private void getData() {
        String idCompte = getIntent().getExtras().getString("idCompte");
        OperationService operationService = new OperationService(this);
        operations = operationService.getOperationsByCompte(idCompte);
        if (operations.size() == 0) {
            txtOps.setVisibility(View.VISIBLE);
        }
    }






}
