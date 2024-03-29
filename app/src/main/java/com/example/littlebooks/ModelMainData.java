package com.example.littlebooks;

public class ModelMainData {
    private String nazov;
    private String obrazok;
    private String id;
    private static int last;

    public ModelMainData(String id, String nazov, String obrazok) {
        this.nazov = nazov;
        this.obrazok = obrazok;
        this.id = id;
    }

    public ModelMainData() {
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    @Override
    public String toString() {
        return "ModelMainData{" +
                "nazov='" + nazov + '\'' +
                ", obrazok='" + obrazok + '\'' +
                ", id='" + id + '\'' +
                '}';
    }
}
