
import java.io.Serializable;

/**
 *
 * @author rafaelmartins
 */

public class MoneyMachine  extends ElementarMachine<Float> implements Serializable{
    private float valorTotal = 0;
    
    public float getTotalValue(){
      for (Element<Float> element : elements) {
            valorTotal += element.getCount() * element.getThing();
        }
      return valorTotal;
    }
    
    public void addMoney(int n, float m){
       addThings(n,m);
    }
}
