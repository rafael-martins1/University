import java.util.ArrayList;
import java.util.List;

public class ElementarMachine<T> {

    protected List<Element<T>> elements;

    public ElementarMachine() {
        elements = new ArrayList<>();
    }

    //Método para encontrar um elemento que tenha esse tipo de coisa
    protected Element<T> getElement(T thing){

        //Percorrer a ArrayList
        for(Element<T> element : elements){
            if(element.getThing().equals(thing)){
                return element;
            }
        }

        return null;
    }

    public void addThings(int n, T thing){
        Element<T> el = getElement(thing);

        //Verifica se já existe algum elemento com o mesmo tipo de coisa
        if(el == null){
            elements.add(new Element<>(n,thing));
        }

        //Atualiza a quantidade de coisas no elemento
        else el.setCount(el.getCount()+ n);
    }

    public boolean removeOneThing(T thing){

        Element<T> el = getElement(thing);

        //Verifica se ainda existem coisas dentro do elemento
        if(el.getCount() == 0 || el == null){
            return false;
        }

        else {
            el.setCount(el.getCount() -1 );
            return true;
        }

    }

    public void listAll(){
        for(Element<T> element : elements){
            System.out.println(element);
        }
    }

}