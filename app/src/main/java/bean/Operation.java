package bean;

/**
 * Created by ammach on 10/13/2016.
 */
public class Operation {

    String typeOperation;
    Double montant;
    String idCompte;

    public Operation() {
    }

    public Operation(String typeOperation, Double montant, String idCompte) {
        this.typeOperation = typeOperation;
        this.montant = montant;
        this.idCompte = idCompte;
    }

    public String getTypeOperation() {
        return typeOperation;
    }

    public void setTypeOperation(String typeOperation) {
        this.typeOperation = typeOperation;
    }

    public Double getMontant() {
        return montant;
    }

    public void setMontant(Double montant) {
        this.montant = montant;
    }

    public String getIdCompte() {
        return idCompte;
    }

    public void setIdCompte(String idCompte) {
        this.idCompte = idCompte;
    }
}
