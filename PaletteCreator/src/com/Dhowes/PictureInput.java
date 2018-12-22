package com.Dhowes;

import java.awt.*;
import java.io.File;

import edu.princeton.cs.algs4.Picture;

public class PictureInput {
    Picture pic;


    public PictureInput(int width, int height) {
        this.pic = new Picture(width,height);

    }
    public PictureInput(String fileName) {
        this.pic = new Picture(fileName);
    }
    public PictureInput(Picture pic) {
        this.pic = pic;
    }
    public PictureInput(File file) {
        this.pic = new Picture(file);
    }
    public void show() {
        pic.show();
    }
    public void save(File file) {
        this.pic.save(file);
    }
    public void save(String fileName) {
        this.pic.save(fileName);
    }
}
