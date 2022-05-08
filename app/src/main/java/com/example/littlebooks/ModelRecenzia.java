package com.example.littlebooks;

public class ModelRecenzia {
    private String uid;
    private String popis;
    private String hodnotenie;
    private String id_kniha;
    private String id_recenzie;
    private String obrazok;
    private String meno;
    private char up;

    public ModelRecenzia(String uid, String popis, String hodnotenie, String id_kniha, String id_recenzie, String obrazok, String meno, String priezvisko, char up) {
        this.uid = uid;
        this.popis = popis;
        this.hodnotenie = hodnotenie;
        this.id_kniha = id_kniha;
        this.id_recenzie = id_recenzie;
        this.obrazok = obrazok;
        this.meno = meno+" "+priezvisko;
        this.up = up;
    }

    public char getUp() {
        return up;
    }

    public void setUp(char up) {
        this.up = up;
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

    public String getId_recenzie() {
        return id_recenzie;
    }

    public void setId_recenzie(String id_recenzie) {
        this.id_recenzie = id_recenzie;
    }

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

    @Override
    public String  toString() {
        return "ModelRecenzia{" +
                "uid='" + uid + '\'' +
                ", popis='" + popis + '\'' +
                ", hodnotenie='" + hodnotenie + '\'' +
                ", id_kniha='" + id_kniha + '\'' +
                ", id_recenzie='" + id_recenzie + '\'' +
                ", obrazok='" + obrazok + '\'' +
                ", meno='" + meno + '\'' +
                ", up=" + up +
                '}';
    }
}
