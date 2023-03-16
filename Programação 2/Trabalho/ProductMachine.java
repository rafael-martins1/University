
import java.io.Serializable;

/**
 *
 * @author rafaelmartins
 */

public class ProductMachine extends ElementarMachine<Product> implements Serializable{

    public void addProduct(int n, Product p){
        addThings(n,p);
    }

    public boolean hasProduct(Product p ){

        if(getElement(p) == null) return false;
        return true;
    }

    public void listAllOrdered( ){
       elements.sort((p1, p2) -> (int)(p1.getThing().getCost() - p2.getThing().getCost()));
       listAll();
       
    }

}
