package Models;

/**
 * Created by vanne on 10/28/2017.
 */

public class Vehicle {
    private int id;
    private int year;
    private String make;
    private String model;

    public Vehicle(int id, int year, String make, String model) {
        this.id = id;
        this.year = year;
        this.make = make;
        this.model = model;
    }

    public void setId(int id){
        this.id = id;
    }

    public int getId() {
        return this.id;
    }
    public void setYear(int year) {
        this.year = year;
    }

    public int getYear() {
        return this.year;
    }


    public void setMake(String make){

        this.make = make;
    }

    public String getMake(){
        return this.make;
    }

    public void  setModel(String model){
        this.model = model;
    }

    public String getModel()
    {
        return this.model;
    }
}