package com.Dhowes;

import java.awt.*;

public class MedianCut extends PictureInput{

    public MedianCut(String fileName) {
        super(fileName);
    }
    public MedianCut(int width, int height){
        super(width, height);
    }

    private int findRange(){
        int greenLow=((pic.getRGB(0,0)>> 8) & 0xff);
        int greenHigh=((pic.getRGB(0,0)>> 8) & 0xff);
        int redLow=((pic.getRGB(0,0)>> 16) & 0xff);
        int redHigh=((pic.getRGB(0,0)>> 16) & 0xff);
        int blueLow=(pic.getRGB(0,0) & 0xff);
        int blueHigh=(pic.getRGB(0,0) & 0xff);
        for (int i = 0; i <pic.height() ; i++) {
            for (int j = 0; j <pic.width() ; j++) {   // Find max and min for all colors
                if (((pic.getRGB(j,i)>> 16) & 0xff)> redHigh){
                    redHigh = ((pic.getRGB(j,i)>> 16) & 0xff);
                }
                if (((pic.getRGB(j,i)>> 8) & 0xff)> greenHigh){
                    greenHigh = ((pic.getRGB(j,i)>> 16) & 0xff);
                }
                if ((pic.getRGB(j,i) & 0xff)> blueHigh){
                    blueHigh = ((pic.getRGB(j,i)>> 16) & 0xff);
                }
                if (((pic.getRGB(j,i)>> 16) & 0xff)< redLow){
                    redLow = ((pic.getRGB(j,i)>> 16) & 0xff);
                }
                if (((pic.getRGB(j,i)>> 8) & 0xff)< greenLow){
                    greenLow = ((pic.getRGB(j,i)>> 16) & 0xff);
                }
                if ((pic.getRGB(j,i) & 0xff)< blueLow){
                    blueLow = ((pic.getRGB(j,i)>> 16) & 0xff);
                }

            }

        }
        int redRange = redHigh-redLow;
        int greenRange = greenHigh-greenLow;
        int blueRange = blueHigh-blueLow;

        if(greenRange>=redRange && greenRange>=blueRange){
            return 2;
        }
        else if (redRange>=greenRange && redRange>=blueRange){
            return 1;
        }
        else{
            return 3;
        }
    }

    private int[][] mergeSplit(int[][] listToSort, int rangeColor){

        int halfLength = listToSort.length/2;
        int a[][] = new int [halfLength][5];
        int b[][] = new int [listToSort.length - halfLength][5];
        for (int i = 0; i <halfLength ; i++) {
            for (int j = 0; j < 5; j++) {
                a[i][j] = listToSort[i][j];
            }
        }
        for (int i = 0; i <listToSort.length - halfLength ; i++) {
            for (int j = 0; j < 5; j++) {
                b[i][j] = listToSort[i + halfLength][j];
            }

        }
        if (a.length >= 2){
            int[][] sortedA = mergeSplit(a, rangeColor);
        }
        if (b.length >= 2){
            int[][] sortedB = mergeSplit(b, rangeColor);
            //return mergeMerge(sortedA,sortedB, rangeColor);
        }

        return mergeMerge(a,b, rangeColor);

    }

    private int [][] mergeMerge(int[][] partA, int[][] partB, int rangeColor){
        int returnArray[][] =  new int[partA.length+partB.length][5];
        int counterA = 0;
        int counterB = 0;
        for (int i = 0; i < partA.length+partB.length-1; i++) {
            if(counterA == partA.length-1){
                for (int j = 0; j < 5; j++) {
                    returnArray[i][j] = partB[counterB][j];
                }
                counterB++;
            }
            else if(counterB == partB.length-1){
                for (int j = 0; j < 5; j++) {
                    returnArray[i][j] = partA[counterA][j];
                }
                counterA++;
            }
            else{
                if(partA[counterA][rangeColor]>=partB[counterB][rangeColor]){
                    for (int j = 0; j < 5; j++) {
                        returnArray[i][j] = partA[counterA][j];
                    }
                    counterA++;
                }
                else if(partB[counterB][rangeColor]>partA[counterA][rangeColor]){
                    for (int j = 0; j < 5; j++) {
                        returnArray[i][j] = partB[counterB][j];
                    }
                    counterB++;
                }
            }

        }
        return returnArray;
    }

    public int[][] sortSetup(){
        int counter =0;
        int colorLists[][] = new int[pic.width()*pic.height()][5];
        for (int i = 0; i < pic.width()-1; i++) {
            for (int j = 0; j < pic.height()-1; j++) {
                    colorLists[counter][0]=(pic.getRGB(i,j)>>16) & 0xff; //Red
                    colorLists[counter][1]=(pic.getRGB(i,j)>>8) & 0xff; //Green
                    colorLists[counter][2]=pic.getRGB(i,j) & 0xff; //Blue
                colorLists[counter][3]= i;
                colorLists[counter][4]= j;  //Records Pixel Placement for palette mimic
                counter++;
            }
        }
        int rangeColor = findRange();
        colorLists = mergeSplit(colorLists, rangeColor);
        return colorLists;

    }

    public int [][] cut(int[][] listToCut){
        int returnArray[][] = new int [16][3];
        int counter = 0;
        for (int i = 0; i < 16; i++) {
            int totalRed=0;
            int totalGreen=0;
            int totalBlue=0;
            for (int j = 0; j < listToCut.length/16; j++) {
                totalRed = totalRed+listToCut[counter][0];
                totalBlue = totalBlue+listToCut[counter][1];  //Finding total color counts to find average for palette
                totalGreen = totalGreen+listToCut[counter][2];
                counter++;
            }
            int avgRed = totalRed/(listToCut.length/16);
            int avgBlue = totalBlue/(listToCut.length/16);
            int avgGreen = totalGreen/(listToCut.length/16);
            returnArray[i][0] = avgRed;
            returnArray[i][1] = avgBlue;  //average colors found in each segment
            returnArray[i][2] = avgGreen;
        }
        return returnArray;
    }

    public void drawPalette(int[][] Colors){
        PictureInput Palette = new PictureInput(640,40);
        for (int i = 0; i < 16; i++) {
            for (int col = 0; col < 40; col++) {
                for (int row = 0; row <40 ; row++) {
                    Color color = new Color(Colors[i][0],Colors[i][1],Colors[i][2]);
                    Palette.pic.set(row+i*40, col,color);
                }
            }
        }
        Palette.pic.show();
    }

}


