package receiver.service.android.vincent.com.mitchellint;

/**
 * Created by vanne on 10/28/2017.
 */

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import java.util.Map;
import java.util.HashMap;

import Models.EntityFilter;
import Models.Vehicle;
import Models.VehicleManager;

public class VehicleManagerTest {

    static VehicleManager vincent = new VehicleManager();
    static Map<Integer, Vehicle> vehicleMap = new HashMap<>(); //All the managers will share the same vehicleMap.

    @Before
    //Precondition
    public void setMap() {
        vehicleMap.clear();
        Vehicle firstCar = new Vehicle(0, 2012, "Toyota", "Camry");
        Vehicle secondCar = new Vehicle(1, 2009, "Ford", "Mustang");
        Vehicle thirdCar = new Vehicle(2, 2001, "Honda", "S2000");
        Vehicle forthCar = new Vehicle(3, 2011, "Chevy", "Colorado");
        Vehicle fifthCar = new Vehicle(4, 1991, "Mazda", "Miata");


        vehicleMap.put(0, firstCar);
        vehicleMap.put(1, secondCar);
        vehicleMap.put(2, thirdCar);
        vehicleMap.put(3, forthCar);
        vehicleMap.put(4, fifthCar);
    }

    @Before
    //Precondition
    public void setTestMap() {
        vincent.deleteAllVehicles();
        vincent.createVehicle(0,2012, "Toyota", "Camry");
        vincent.createVehicle(1,2009, "Ford", "Mustang");
        vincent.createVehicle(2,2001, "Honda", "S2000");
        vincent.createVehicle(3,2011, "Chevy", "Colorado");
        vincent.createVehicle(4,1991, "Mazda", "Miata");
    }

    private void compareTwoVehicles(Vehicle v1, Vehicle v2) {
        assertEquals(v1.getId(), v2.getId());
        assertEquals(v1.getYear(), v2.getYear());
        assertEquals(v1.getMake(), v2.getMake());
        assertEquals(v1.getModel(), v2.getModel());
    }

    @Test
    public void getAllVehicles() throws Exception { ;
        Map<Integer, Vehicle> mapForTesting = vincent.getAllVehicles();
        assertNotNull(mapForTesting);

        for (Map.Entry<Integer, Vehicle> entry : vehicleMap.entrySet()) {
            int key = entry.getKey();
            Vehicle vehicleInSystem = entry.getValue();
            Vehicle vehicleForTesting = mapForTesting.get(key);
            compareTwoVehicles(vehicleInSystem, vehicleForTesting);
        }
    }

    @Test
    public void getVehicleById() throws Exception {

        int id = 0;
        Vehicle vehicleForTesting = vincent.getVehicleById(id);
        assertNotNull(vehicleForTesting);

        Vehicle vehicleInSystem = VehicleManagerTest.vehicleMap.get(id);

        compareTwoVehicles(vehicleInSystem, vehicleForTesting);
    }


    @Test
    public void createInvalidVehicle() throws Exception {
        assertFalse(vincent.createVehicle(5,1933, "Ford", "Bronco"));
        assertFalse(vincent.createVehicle(6,1977, "", "Bronco"));
        assertFalse(vincent.createVehicle(7,1992, "Cadilac", ""));
        assertFalse(vincent.createVehicle(8,1992, null, ""));
        assertFalse(vincent.createVehicle(9,2012, "Dodge", null));
        assertFalse(vincent.createVehicle(10,2012, "", null));
        assertFalse(vincent.createVehicle(11,2012, null, null));
        assertFalse(vincent.createVehicle(0,1933, "Ford", "Bronco"));
    }

    @Test
    public void createVehicle() throws Exception {
        assertTrue(vincent.createVehicle(5,2012, "Chevy", "Silverado"));
        assertTrue(vincent.createVehicle(6,1950, "Chevy", "Silverado"));
        assertTrue(vincent.createVehicle(7,2050, "Ford", "F150"));
        assertTrue(vincent.createVehicle(8,2012, "Ram", "500"));
        assertFalse(vincent.updateVehicle(9, 2012, "     ", "Accord"));
    }

    @Test
    public void updateInvalidVehicle() throws Exception {
        int id = vehicleMap.size() + 1;
        // Invalid id: id not found
        assertFalse(vincent.updateVehicle(id, 2016, "Ford", "Mustang"));
        //Invalid make
        assertFalse(vincent.updateVehicle(id, 2016, "", "Mustang"));
        //Invalid model
        assertFalse(vincent.updateVehicle(id, 2016, "Ford", null));
    }

    @Test
    public void updateVehicle() throws Exception {
        assertTrue(vincent.updateVehicle(0,2016, "Ford", "Focus"));
        assertTrue(vincent.updateVehicle(1,2008, "Ford", "Edge"));
        assertTrue(vincent.updateVehicle(2, 2016, "Honda", "Civic"));
        assertTrue(vincent.updateVehicle(3, 2012, "Honda", "Accord"));
        assertTrue(vincent.updateVehicle(3, 2012, "Chevy", "       Monte Carlo     "));
    }

    @Test
    public void deleteInvalidVehicleById() throws Exception {
        int id = vehicleMap.size() + 1;
        //Invalid id
        assertFalse(vincent.deleteVehicleById(id));
    }

    @Test
    public void deleteVehicleById() throws Exception {

        assertTrue(vincent.deleteVehicleById(0));
        assertNull(vincent.getVehicleById(0));

        assertTrue(vincent.deleteVehicleById(1));
        assertNull(vincent.getVehicleById(1));

        assertTrue(vincent.deleteVehicleById(2));
        assertNull(vincent.getVehicleById(2));

        assertTrue(vincent.deleteVehicleById(3));
        assertNull(vincent.getVehicleById(3));

        assertTrue(vincent.deleteVehicleById(4));
        assertNull(vincent.getVehicleById(4));

        assertEquals(0, vincent.getAllVehicles().size());

    }

    @Test
    public void deleteAllVehicles() throws Exception {
        vincent.deleteAllVehicles();
        assertTrue(vincent.getAllVehicles().size() == 0);
    }

    @Test
    public void filterMap() throws Exception {
        EntityFilter filter = new EntityFilter();
        filter.setStartYear(2001);
        filter.setEndYear(2011);

        //Vehicle from 1991 to 2011 are:
        //Id1, 2009, "Ford", "Mustang"
        //Id2, 2001, "Honda", "S2000"
        //Id3, 2011, "Chevy", "Colorado"

        Map<Integer, Vehicle> filteredMap = vincent.applyFilter(filter);
        assertEquals(3,filteredMap.size());

        Vehicle vehicleForTesting = vincent.getVehicleById(1);
        Vehicle vehicleInSystem = vehicleMap.get(1);
        compareTwoVehicles(vehicleForTesting, vehicleInSystem);

        vehicleForTesting = vincent.getVehicleById(2);
        vehicleInSystem = vehicleMap.get(2);
        compareTwoVehicles(vehicleForTesting, vehicleInSystem);

        vehicleForTesting = vincent.getVehicleById(3);
        vehicleInSystem = vehicleMap.get(3);
        compareTwoVehicles(vehicleForTesting, vehicleInSystem);

    }

}