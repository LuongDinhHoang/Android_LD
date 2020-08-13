package com.example.listnangcao;

public class TraiCay {
    private String traicay;
    private String mota;
    private  int Image;

    public TraiCay(String traicay, String mota, int image) {
        this.traicay = traicay;
        this.mota = mota;
        Image = image;
    }

    public String getTraicay() {
        return traicay;
    }

    public void setTraicay(String traicay) {
        this.traicay = traicay;
    }

    public String getMota() {
        return mota;
    }

    public void setMota(String mota) {
        this.mota = mota;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
