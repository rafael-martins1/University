package es.softtek.jwtDemo.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import javax.xml.bind.annotation.*;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "artista")
public class Artista {

    @XmlElement(required = true)
    private int id;

    @JsonProperty("tipo_arte")
    @XmlElement(required = true)
    private String nome;

    @XmlElement(required = true)
    private String tipo_arte;

    @XmlElement(required = true)
    private String descricao;

    @XmlElement(required = true)
    private String imagem;

    @XmlElement(required = true)
    private float rating;

    @XmlElement(required = true)
    private boolean estado;

    @XmlElement(required = true)
    private String localizacao;


    public Artista(int id, String nome, String tipoArte, boolean estado, String descricao, String imagem, float rating) {
        this.id = id;
        this.nome = nome;
        this.tipo_arte = tipoArte;
        this.descricao = descricao;
        this.imagem = imagem;
        this.rating = rating;
    }


    public Artista(String nome, String tipoArte, String localizacao) {
        this.nome = nome;
        this.tipo_arte = tipoArte;
        this.localizacao = localizacao;
    }

    public String getLocalizacao() {return localizacao;}

    public int getId() {
        return this.id;
    }

    public String getNome() {
        return this.nome;
    }

    public String getTipoArte() {
        return this.tipo_arte;
    }

    public float getRating() {
        return this.rating;
    }

    @Override
    public String toString() {
        return "Nome = " + this.nome + ", Tipo de Arte = " + this.tipo_arte + ";";
    }

}
