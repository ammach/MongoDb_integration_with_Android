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
import java.util.List;
import java.util.concurrent.ExecutionException;

import bean.Compte;
import config.Config;

/**
 * Created by ammach on 10/13/2016.
 */
public class CompteService {



class DeleteTask extends AsyncTask<String,Void,Void>{
        DBCollection comptesCollection;
        DBCollection operationsCollection;
        @Override
        protected Void doInBackground(String... params) {
            MongoClient client = new MongoClient(Config.HOST,27017);
            DB db=client.getDB("local");
            comptesCollection=db.getCollection("comptes");
            operationsCollection=db.getCollection("operations");
            //delete operations for compte
            BasicDBObject basicDBObject=new BasicDBObject();
            basicDBObject.put("idCompte",params[0]);
            DBCursor cursorOperation = operationsCollection.find(basicDBObject);
            while (cursorOperation.hasNext()) {
                operationsCollection.remove(cursorOperation.next());
            }
            //delete compte
            BasicDBObject basicDBObject1=new BasicDBObject();
            basicDBObject1.put("idCompte",params[0]);
            comptesCollection.remove(basicDBObject1);
            client.close();
            return null;
        }
    }

    class SelectTask extends AsyncTask<Void,Void,List<Compte>>{
        DBCollection comptesCollection;
        @Override
        protected List<Compte> doInBackground(Void... params) {
            List<Compte> comptes=new ArrayList<>();
            MongoClient client = new MongoClient(Config.HOST,27017);
            DB db=client.getDB("local");
            comptesCollection=db.getCollection("comptes");
            DBCursor cursor = comptesCollection.find();
            while(cursor.hasNext()) {
                String result=cursor.next().toString();
                try {
                    JSONObject jsonObject=new JSONObject(result);
                    comptes.add(new Compte(jsonObject.getString("idCompte"),jsonObject.getDouble("solde")));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            client.close();
            return comptes;
        }
    }


    class InsertTask extends AsyncTask<BasicDBObject,Void,Void>{
        DBCollection comptesCollection;
        @Override
        protected Void doInBackground(BasicDBObject... params) {
            MongoClient client = new MongoClient(Config.HOST,27017);
            DB db=client.getDB("local");
            comptesCollection=db.getCollection("comptes");
            comptesCollection.insert(params[0]);
            client.close();
            return null;
        }
    }

    Context context;

    public CompteService(Context context) {
        this.context=context;
    }


    public Boolean createCompte(String id, Double solde) {
        BasicDBObject basicDBObject=new BasicDBObject();
        basicDBObject.put("idCompte",id);
        basicDBObject.put("solde",solde);
        new InsertTask().execute(basicDBObject);
       return true;
    }

    public List<Compte> getComptes() {
        try {
            return  new SelectTask().execute().get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        return null;
    }

    public void deleteCompte(String idCompte) {
        new DeleteTask().execute(idCompte);
    }
}
