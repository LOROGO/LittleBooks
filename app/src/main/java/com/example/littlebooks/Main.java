package com.example.littlebooks;

public class Main {

    String obrazok;
    String nazov;

    public Main() {

    }

    public Main(String nazov, String obrazok) {
        this.nazov = nazov;
        this.obrazok = obrazok;
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
}
