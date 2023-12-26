/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package domain;

import java.io.Serializable;
import java.util.Date;

/**
 *
 * @author vojislav
 */
public class Poruka implements Serializable {
    String tekst;
    Date datum;
    User posaljilac;
    User primalac;
    
    // ovo mislim da su neophodni za funkcionisanje poruka, bitno je da je serializable

    public Poruka(String tekst, Date datum, User posaljilac, User primalac) {
        this.tekst = tekst;
        this.datum = datum;
        this.posaljilac = posaljilac;
        this.primalac = primalac;
    }

    public Poruka(String tekst, Date datum, User posaljilac) {
        this.tekst = tekst;
        this.datum = datum;
        this.posaljilac = posaljilac;
    }

    public String getTekst() {
        return tekst;
    }

    public void setTekst(String tekst) {
        this.tekst = tekst;
    }

    public Date getDatum() {
        return datum;
    }

    public void setDatum(Date datum) {
        this.datum = datum;
    }

    public User getPosaljilac() {
        return posaljilac;
    }

    public void setPosaljilac(User posaljilac) {
        this.posaljilac = posaljilac;
    }

    public User getPrimalac() {
        return primalac;
    }

    public void setPrimalac(User primalac) {
        this.primalac = primalac;
    }

    @Override
    public String toString() {
        if(primalac==null){
            return datum+" [ALL] "+posaljilac.getEmail()+":"+tekst;
        }else{
            return datum+" ["+primalac.getEmail()+"] "+posaljilac.getEmail()+":"+tekst;
        }
    }
    
    
}
