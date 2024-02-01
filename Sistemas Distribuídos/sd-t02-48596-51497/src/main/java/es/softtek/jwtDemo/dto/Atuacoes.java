package es.softtek.jwtDemo.dto;

import java.time.LocalDate;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "atuacao")
public class Atuacoes {

    @XmlElement(required = true)
    private int id;

    @XmlElement(required = true)
    private LocalDate data;

    @XmlElement(required = true)
    private boolean atuar;

    public Atuacoes(int id, LocalDate data, boolean atuar) {
        this.id = id;
        this.data = data;
        this.atuar = atuar;
    }


    public int getId() {
        return id;
    }
    public LocalDate getData() {
        return data;
    }

}
