package model;

public class student {
private String Name;
private  String Title;
private  int Image;

    public student()
    {

    }
    public student(String name, String title, int image) {
        Name = name;
        Title = title;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
