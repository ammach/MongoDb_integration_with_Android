package bean;

/**
 * Created by ammach on 10/13/2016.
 */
public class Compte {

    String idCompte;
    Double solde;




    public Compte() {
    }



    public Compte(String idCompte, double solde) {
        this.solde = solde;
        this.idCompte = idCompte;
    }



    public String getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }

    public Double getSolde() {
        return solde;
    }

    public void setSolde(Double solde) {
        this.solde = solde;
    }
}
