package com.example.littlebooks;

public class ModelMainDataFavourite {
    private String nazov;
    private String autor;
    private String obrazok;
    private String id;
    private static int last;

    public ModelMainDataFavourite(String id, String nazov, String obrazok, String autor) {
        this.nazov = nazov;
        this.obrazok = obrazok;
        this.id = id;
        this.autor = autor;
    }

    public ModelMainDataFavourite() {
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

    public String getAutor() {
        return autor;
    }

    public void setAutor(String autor) {
        this.autor = autor;
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
