package me.anuraag.grader;

import java.io.Serializable;

/**
 * Created by Anuraag on 12/25/14.
 */
public class GradesObject implements Serializable{
    private String gradesJson;
    private String className;
    private boolean weighted;
    private SingleGrade[] gradesList;
    private String totalGradeAverage;

    public GradesObject(String name){
        this.className = name;
    }
    public String getGrades(){
        return this.gradesJson;
    }
    public String getClassName(){
        return this.className;
    }
    public String getAverage(){
        return this.totalGradeAverage;
    }
    public void setGradesList(SingleGrade[] newgrades){
        this.gradesList = newgrades;
    }
    public boolean isWeighted(){
        return this.weighted;
    }
    public void setWeighted(boolean weightd){
        this.weighted = weightd;
    }
    public void setGradesJson(String gradesJson){
        this.gradesJson = gradesJson;
    }
    public void setGradeAverage(String gradeAverage){
        this.totalGradeAverage = gradeAverage;
    }
    public void setClassName(String name){
        this.className = name;
    }
    public SingleGrade[] getGradesList(){
        return this.gradesList;
    }
    public String toString(){
        return this.gradesJson + " " +  this.totalGradeAverage;
    }
}
