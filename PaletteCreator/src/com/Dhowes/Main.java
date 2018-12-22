package com.Dhowes;

import edu.princeton.cs.algs4.Picture;

import java.awt.*;
import java.math.*;

public class Main {
    public static void mimicPalette(int[][] firstPalette, int[][] secondPalette, int[][] secondPhotoSortedColors, Picture pic) {
        //    Compare Sorted first 16th of sorted list to tile of medianCut palette
//    Find Difference and record RGB differences in array
//    Make Current colors the same difference from original palette color
//    Redraw picture with index spaces in sorted array[x][4] & array[x][5]
        int counter = 0;
        for (int i = 0; i <16; i++) {
            for (int j = 0; j <(pic.width()*pic.height())/16 ; j++) {
                int redChange = secondPhotoSortedColors[counter][0] - secondPalette[i][0];
                int greenChange = secondPhotoSortedColors[counter][1] - secondPalette[i][1];
                int blueChange = secondPhotoSortedColors[counter][2] - secondPalette[i][2];

                if(firstPalette[i][0]+redChange>255){
                    int correctionVariable = firstPalette[i][0]+redChange - 255;
                    redChange =- correctionVariable;
                }
                if(firstPalette[i][1]+greenChange>255){
                    int correctionVariable = firstPalette[i][1]+greenChange - 255;
                    greenChange =- correctionVariable;
                }
                if(firstPalette[i][2]+blueChange>255){
                    int correctionVariable = firstPalette[i][2]+blueChange - 255;
                    blueChange =- correctionVariable;
                }


                if(firstPalette[i][0]+redChange<0){
                    int correctionVariable = firstPalette[i][0]+redChange ;
                    redChange += Math.abs(correctionVariable);
                }
                if(firstPalette[i][1]+greenChange<0){
                    int correctionVariable = firstPalette[i][1]+greenChange;
                    greenChange += Math.abs(correctionVariable);
                }
                if(firstPalette[i][2]+blueChange<0){
                    int correctionVariable = firstPalette[i][2]+blueChange;
                    blueChange += Math.abs(correctionVariable);
                }

                Color color = new Color(firstPalette[i][0]+redChange,firstPalette[i][1]+greenChange,firstPalette[i][2]+blueChange);
                pic.set(secondPhotoSortedColors[counter][3],secondPhotoSortedColors[counter][4],color); //each 2d array has its original position
                counter ++;

            }
        }
        pic.show();
    }

    public static void main(String[] args) {
        MedianCut pic = new MedianCut("Photos/FallsWinter.PNG");
        pic.show();
        int[][] sortedColors = pic.sortSetup();
        int[][] paletteColors = pic.cut(sortedColors);

        MedianCut secondPic = new MedianCut("Photos/FallsSummer.PNG");
        secondPic.show();
        int[][] secondSortedColors = secondPic.sortSetup();
        int[][] secondPaletteColors = secondPic.cut(secondSortedColors);

        mimicPalette(paletteColors,secondPaletteColors,secondSortedColors,secondPic.pic);
    }

}
