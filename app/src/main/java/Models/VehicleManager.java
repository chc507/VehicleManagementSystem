package Models;


import java.util.HashMap;
import java.util.Map;

public class VehicleManager {
    public String title;
    public String name;
    public String phone;
    public String email;


    private static Map<Integer, Vehicle> map = new HashMap<>();; //All the managers will share the same map.

    Map<Integer, Vehicle> filteredMap;

    public String errorMessage;


    public VehicleManager() {

    }

    public VehicleManager(String title, String name, String phone, String email) {
        this.title = title;
        this.name = name;
        this.phone = phone;
        this.email = email;
    }

    //Perform CRUD
    //Read All
    public Map<Integer, Vehicle> getAllVehicles() {
        return map;
    }

    //Read By ID
    public Vehicle getVehicleById(int id) {
        if (map.containsKey(id)) {
            System.out.println("Successfully retrieve vehicle with id: " + id + ".");
            return map.get(id);
        } else {
            System.out.println("The vehicle with id: " + id + " did not exist in the system.");
            errorMessage = "The vehicle with id: " + id + " did not exist in the system.";
            return null;
        }
    }

    //Create
    public boolean createVehicle(int id, int year, String make, String model){

        if (!(validateId(id) && validateYear(year) && validateMake(make) && validateModel(model))) {
            return false;
        }

        make = make.trim();
        model = model.trim();
        Vehicle newVehicle = new Vehicle(id,year,make,model);
        map.put(id, newVehicle);
        return true;
    }


    //Update
    public boolean updateVehicle(int id, int year, String make, String model){
        if (!map.containsKey(id)) {
            System.out.println("This vehicle with id " + id + " is not in the system.");
            errorMessage = "This vehicle with id " + id + " is not in the system.";
            return false;
        }

        if (!(validateYear(year) && validateMake(make) && validateModel(model))) {
            return false;
        }

        make = make.trim();
        model = model.trim();
        map.get(id).setYear(year);
        map.get(id).setModel(model);
        map.get(id).setMake(make);

        return true;
    }

    //Delete by ID
    public boolean deleteVehicleById(int id){

        if (!map.containsKey(id)) {
            System.out.println("This vehicle is not in the system");
            errorMessage = "This vehicle is not in the system";
            return false;
        }


        map.remove(id);
        System.out.println("This vehicle with id: " + id + " is successfully removed");
        return true;
    }

    //Filter
    public Map<Integer, Vehicle> applyFilter(EntityFilter filter) {
        String make = filter.make;
        String model = filter.model;
        int startYear = filter.startYear;
        int endYear = filter.endYear;

        filteredMap = new HashMap<>();

        for (Map.Entry<Integer, Vehicle> entry : map.entrySet()) {
            if (entry.getValue().getYear() < startYear) {
                continue;
            }

            if (entry.getValue().getYear() > endYear) {
                continue;
            }

            if (make != null && make.length() != 0 && !entry.getValue().getMake().equalsIgnoreCase(make)) {
                continue;
            }

            if (model != null && model.length() != 0 && !entry.getValue().getModel().equalsIgnoreCase(model)) {
                continue;
            }

            filteredMap.put(entry.getKey(), entry.getValue());
        }

        return filteredMap;
    }


    private boolean validateYear(int year){
        if (year < 1950 || year > 2050) {
            System.out.println("Failed to create vehicle: We only support vehicles from 1950 to 2050, please reenter");
            errorMessage = "We only support vehicles from 1950 to 2050, please reenter";
            return false;
        }
        return true;
    }

    private boolean validateMake(String make){
        if (make == null || make.trim().length() == 0) {

            System.out.println("Failed to create vehicle: You did not enter manufature for this vehicle, please reenter");
            errorMessage = "You did not enter manufature for this vehicle, please reenter";
            return false;
        }
        return true;
    }

    private boolean validateModel(String model){
        if (model == null || model.trim().length() == 0) {
            System.out.println("Failed to create vehicle: You did not enter model for this vehicle, please reenter");
            errorMessage = "You did not enter model for this vehicle, please reenter";
            return false;
        }
        return true;
    }

    private boolean validateId(int id){
        if (id < 0 ) {
            System.out.println("Failed to create vehicle: You did not enter model for this vehicle, please reenter");
            errorMessage = "You did not enter model for this vehicle, please reenter";
            return false;
        }

        if (map.containsKey(id)) {
            System.out.println("This id already exist, please reenter, please reenter");
            errorMessage="This id already exist, please reenter";
            return false;
        }
        return true;
    }

    public boolean deleteAllVehicles(){
        map.clear();
        return true;
    }

    public String getErrorMessage() {
        return this.errorMessage;
    }
}