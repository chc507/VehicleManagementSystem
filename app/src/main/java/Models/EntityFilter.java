package Models;

import java.util.Calendar;

public class EntityFilter {
    public int startYear = 1950;
    public int endYear = Calendar.getInstance().get(Calendar.YEAR);
    public String make = null;
    public String model = null;

    public String errorMessage;

    public EntityFilter() {

    }

    public boolean setStartYear(int startYear) {
        if (!validateYear(startYear)) {
            return false;
        }
        this.startYear = startYear;
        return true;
    }

    public boolean setEndYear(int endYear) {
        if (!validateYear(endYear) && endYear >= startYear) {
            return false;
        }
        this.endYear = endYear;
        return true;
    }

    public boolean setMake(String make) {
        this.make = make;
        return true;
    }

    public boolean setModel(String model) {
        this.model = model;
        return true;
    }

    private boolean validateYear(int year){
        if (year < 1950 || year > 2050) {
            System.out.println("Failed to create vehicle: We only support vehicles from 1950 to 2050, please reenter");
            errorMessage = "We only support vehicles from 1950 to 2050, please reenter";
            return false;
        }
        return true;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}