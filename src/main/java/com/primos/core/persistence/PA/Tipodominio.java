package com.primos.core.persistence.PA;
// Generated 14/10/2018 08:08:00 AM by Hibernate Tools 4.3.1


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * Tipodominio generated by hbm2java
 */
@Entity
@Table(name="tipodominio"
)
public class Tipodominio  implements java.io.Serializable {


     private long idtipodominio;
     private String strdescripcion;

    public Tipodominio() {
    }

    public Tipodominio(long idtipodominio, String strdescripcion) {
       this.idtipodominio = idtipodominio;
       this.strdescripcion = strdescripcion;
    }
   
     @Id 

    
    @Column(name="idtipodominio", nullable=false)
    public long getIdtipodominio() {
        return this.idtipodominio;
    }
    
    public void setIdtipodominio(long idtipodominio) {
        this.idtipodominio = idtipodominio;
    }

    
    @Column(name="strdescripcion", nullable=false, length=30)
    public String getStrdescripcion() {
        return this.strdescripcion;
    }
    
    public void setStrdescripcion(String strdescripcion) {
        this.strdescripcion = strdescripcion;
    }




}

