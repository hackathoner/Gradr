package me.anuraag.grader;

import java.text.DecimalFormat;

/**
 * Created by Anuraag on 12/25/14.
 */
public class SingleGrade {
    private double percentage;
    private double pointsRight;
    private String name;
    private double totalPoints;
    public SingleGrade(){

    }
    public double getPercentage(){
        return Math.floor((this.pointsRight/this.totalPoints)*10000)/100;
    }
    public double getPointsRight(){
        return this.pointsRight;
    }
    public double getTotalPoints(){
        return this.totalPoints;
    }
    public void setPercentage(double percentage){
        this.percentage = percentage;
    }
    public void setPointsRight(int pointsRight){
        this.pointsRight = pointsRight;
    }
    public void setName(String name){
        this.name = name;
    }
    public void setTotalPoints(int totalPoints){
        this.totalPoints = totalPoints;
    }
    public String toString(){
        return this.percentage + " " + this.pointsRight + " " + this.totalPoints;
    }
    public String getName(){
        return this.name;
    }
}
