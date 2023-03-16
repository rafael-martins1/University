/**
 *
 * @author rafaelmartins
 */

import java.io.Serializable;
import java.util.Date;

//Torna-se a classe abstrata uma vez que não é permitido criar Product
abstract class Product implements Serializable{

    public String name; //Nome do produto
    public double cost; //Custo do produto

    Product(String a,double c){
        name = a;
        cost = c;
    }

    //Retorna o nome
    public String getName(){
        return name;
    }

    //Retorna o custo
    public double getCost(){
        return cost;
    }
}


class NonPerishable extends Product{
    public double volume; //Volume do produto

    public NonPerishable(String n, double c, double v){
        super(n,c);
        this.volume = v;
    }

    double getVolume(){
        return volume;
    }

}


class Perishable extends Product implements Freshness{
    public Date limitDate; //Data

    public Perishable(String n, double c, Date d){
        super(n,c);
        limitDate = d;
    }


    public boolean isOutDated(){

        //Metodo Date().after verifica se a data atual ultrapassa a data especificada.
        return  new Date().after(limitDate);
    }


    public boolean isFromToday(){
        Date today = new Date();

        //Metodo compareTo() retorna 0 se a datas forem iguais.
        if(limitDate.compareTo(today) == 0) return true;

        else return false;
    }
}