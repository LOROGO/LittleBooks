package com.example.littlebooks;

public class FavoriteBook {

    private String meno;
    private String nazov;
    private String obrazok;
    private String pocetStran;

    public FavoriteBook() {

    }

    public FavoriteBook(String meno, String nazov, String obrazok, String pocetStran) {
        this.meno = meno;
        this.nazov = nazov;
        this.obrazok = obrazok;
        this.pocetStran = pocetStran;
    }

    public String getMeno() {
        return meno;
    }

    public void setMeno(String meno) {
        this.meno = meno;
    }

    public String getNazov() {
        return nazov;
    }

    public void setNazov(String nazov) {
        this.nazov = nazov;
    }

    public String getObrazok() {
        return obrazok;
    }

    public void setObrazok(String obrazok) {
        this.obrazok = obrazok;
    }

    public String getPocetStran() {
        return pocetStran;
    }

    public void setPocetStran(String pocetStran) {
        this.pocetStran = pocetStran;
    }

}



