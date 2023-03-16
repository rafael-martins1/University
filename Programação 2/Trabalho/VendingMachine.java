/**
 *
 * @author rafaelmartins
 */


import java.io.*;

public class VendingMachine implements Serializable{
    private ProductMachine productMachine;
    private MoneyMachine moneyMachine;
    
    
    VendingMachine(ProductMachine pm, MoneyMachine mm){
        this.productMachine = pm;
        this.moneyMachine = mm;
    }
    
    public static void saveMachine(VendingMachine m, String nomeFicheiro){
        
         try {
            FileOutputStream fileOutStream = new FileOutputStream(nomeFicheiro);
            ObjectOutputStream outObjectStream = new ObjectOutputStream(fileOutStream);
            outObjectStream.writeObject(m);
            outObjectStream.close();
            fileOutStream.close();
            } 
         
         catch (IOException e) {
             
        }
    }
    
    public static VendingMachine restoreMachine(String nomeFicheiro) throws ClassNotFoundException, IOException {
    try (FileInputStream fileInStream = new FileInputStream(nomeFicheiro);
         ObjectInputStream inObjectStream = new ObjectInputStream(fileInStream)) {
        return (VendingMachine) inObjectStream.readObject();
    } catch (IOException | ClassNotFoundException e) {
        e.printStackTrace();
    }
    return null;
}
    public ProductMachine getProductMachine(){
        return productMachine;
       
    }
    
    public MoneyMachine getMoneyMachine(){
        return moneyMachine;
    }
    
    public void setProductMachine(ProductMachine m){
        this.productMachine = m;
    }
 
}
