package com.example.littlebooks;

public class ModelMainData2 {
    private String uid;
    private String popis;
    private String hodnotenie;
    private String id_kniha;
    //private String id_recenzie;
    private String obrazok;
    private String meno;

    public ModelMainData2(String uid, String popis, String hodnotenie, String id_kniha, String obrazok, String meno, String priezvisko) {
        this.uid = uid;
        this.popis = popis;
        this.hodnotenie = hodnotenie;
        this.id_kniha = id_kniha;
        //this.id_recenzie = id_recenzie;
        this.obrazok = obrazok;
        this.meno = meno+" "+priezvisko;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPopis() {
        return popis;
    }

    public void setPopis(String popis) {
        this.popis = popis;
    }

    public String getHodnotenie() {
        return hodnotenie;
    }

    public void setHodnotenie(String hodnotenie) {
        this.hodnotenie = hodnotenie;
    }

    public String getId_kniha() {
        return id_kniha;
    }

    public void setId_kniha(String id_kniha) {
        this.id_kniha = id_kniha;
    }

   /* public String getId_recenzie() {
        return id_recenzie;
    }

    public void setId_recenzie(String id_recenzie) {
        this.id_recenzie = id_recenzie;
    }*/

    public String getObrazok() {
        return obrazok;
    }

    public void setObrazok(String obrazok) {
        this.obrazok = obrazok;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }
}
