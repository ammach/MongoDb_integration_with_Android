package services;

import android.content.Context;
import android.os.AsyncTask;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.MongoClient;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

import bean.Compte;
import bean.Operation;
import config.Config;

/**
 * Created by ammach on 10/13/2016.
 */
public class OperationService {


    class UpdateCompteTask extends AsyncTask<HashMap<String,Object>,Void,Void>{

        DBCollection comptesCollection;
        @Override
        protected Void doInBackground(HashMap<String,Object>... params) {
            Compte compte= (Compte) params[0].get("compte");
            String typeOp= (String) params[0].get("operation");
            Double montant= (Double) params[0].get("montant");
            MongoClient client = new MongoClient(Config.HOST, 27017);
            DB db = client.getDB("local");
            comptesCollection = db.getCollection("comptes");
            if(typeOp.equals("debit")){
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.put("solde", compte.getSolde()-montant );
                newDocument.put("idCompte", compte.getIdCompte() );
                BasicDBObject searchQuery = new BasicDBObject().append("idCompte", compte.getIdCompte());
                comptesCollection.update(searchQuery, newDocument);
            }else{
                BasicDBObject newDocument = new BasicDBObject();
                newDocument.put("solde", compte.getSolde()+montant);
                newDocument.put("idCompte", compte.getIdCompte() );
                BasicDBObject searchQuery = new BasicDBObject().append("idCompte", compte.getIdCompte());
                comptesCollection.update(searchQuery, newDocument);
            }
            client.close();
            return null;
        }
    }

    class SelectTask extends AsyncTask<String, Void, List<Operation>> {
        DBCollection operationsCollection;

        @Override
        protected List<Operation> doInBackground(String... params) {
            List<Operation> operations = new ArrayList<>();
            MongoClient client = new MongoClient(Config.HOST, 27017);
            DB db = client.getDB("local");
            operationsCollection = db.getCollection("operations");
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("idCompte", params[0]);
            DBCursor cursor = operationsCollection.find(basicDBObject);
            while (cursor.hasNext()) {
                String result = cursor.next().toString();
                try {
                    JSONObject jsonObject = new JSONObject(result);
                    operations.add(new Operation(jsonObject.getString("typeOperation"), jsonObject.getDouble("montant"), jsonObject.getString("idCompte")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            client.close();
            return operations;
        }
    }


    class InsertTask extends AsyncTask<BasicDBObject, Void, Void> {
        DBCollection operationsCollection;

        @Override
        protected Void doInBackground(BasicDBObject... params) {
            MongoClient client = new MongoClient(Config.HOST, 27017);
            DB db = client.getDB("local");
            operationsCollection = db.getCollection("operations");
            operationsCollection.insert(params[0]);
            client.close();
            return null;
        }
    }

    Context context;

    public OperationService(Context context) {
        this.context = context;
    }


    public Boolean createOperation(String idCompte, String typeOp, Double montant) {


        //get solde compte
        Compte compte = new Compte();
        for (Compte compte1 : new CompteService(context).getComptes()) {
            if (compte1.getIdCompte().equals(idCompte)) {
                compte = compte1;
                break;
            }
        }
        if (typeOp == "Débit") {

            //test
            if (compte.getSolde() >= montant) {
                //create operation
                BasicDBObject basicDBObject = new BasicDBObject();
                basicDBObject.put("idCompte", idCompte);
                basicDBObject.put("typeOperation", typeOp);
                basicDBObject.put("montant", montant);
                new InsertTask().execute(basicDBObject);
                //update solde compte
                HashMap<String,Object>hashMap=new HashMap<>();
                hashMap.put("compte",compte);
                hashMap.put("operation","debit");
                hashMap.put("montant",montant);
                new UpdateCompteTask().execute(hashMap);
                return true;
            } else {
                return false;
            }
        } else {
            //create operation
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put("idCompte", idCompte);
            basicDBObject.put("typeOperation", typeOp);
            basicDBObject.put("montant", montant);
            new InsertTask().execute(basicDBObject);
            //update solde compte
            HashMap<String,Object>hashMap=new HashMap<>();
            hashMap.put("compte",compte);
            hashMap.put("operation","credit");
            hashMap.put("montant",montant);
            new UpdateCompteTask().execute(hashMap);
            return true;
        }

    }

    public List<Operation> getOperationsByCompte(String idCompte) {
        try {
            return new SelectTask().execute(idCompte).get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }
}
