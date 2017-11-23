package receiver.service.android.vincent.com.mitchellint;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.view.LayoutInflater;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import ListViewAdapter.VehicleListAdapter;
import Models.EntityFilter;
import Models.Vehicle;
import Models.VehicleManager;

public class MainActivity extends AppCompatActivity {

    VehicleManager vincent;

    EntityFilter entityFilter;
    ArrayList<Vehicle> vehicleList;
    Map<Integer, Vehicle> vehicleMap;

    ListView vehicleListView;
    VehicleListAdapter vehicleListAdapter;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_layout);

        vincent = new VehicleManager(
                "Intern: Software Engineer",
                "Chen Chen",
                "402-739-7780",
                "vannesschancc@live.com");

        //All CRUD operation is relied on 4 buttons
        Button getVehiclesButton = findViewById(R.id.getVehiclesButton);
        getVehiclesButton.setOnClickListener( new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                vehicleMap = vincent.getAllVehicles();
                getVehiclesView(vehicleMap);
            }
        });

        Button createButton = findViewById(R.id.create);
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createVehiclePrompt();
            }
        });

        Button filterButton = findViewById(R.id.filter);
        filterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                filterVehiclePrompt();
            }
        });

        Button getButton = findViewById(R.id.get);
        getButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                getVehicleByIdPrompt();
            }
        });

        Button deleteButton = findViewById(R.id.delete);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteVehiclePrompt();
            }
        });



        TextView title = findViewById(R.id.title);
        TextView name = findViewById(R.id.name);
        TextView phone = findViewById(R.id.phone);
        TextView email = findViewById(R.id.email);

        title.setText(vincent.title);
        name.setText(vincent.name);
        phone.setText(vincent.phone);
        email.setText(vincent.email);

        vehicleMap = new HashMap<>();
        vehicleList = new ArrayList<>();
        vehicleListView = findViewById(R.id.vehicleListView);
        vehicleListAdapter = new VehicleListAdapter(this, vehicleList);
        vehicleListView.setAdapter(vehicleListAdapter);

        vehicleListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                updateVehiclePrompt(position);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void createVehiclePrompt() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.create_vehicle_promt_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);
        final EditText idEditText = (EditText)promptsView.findViewById(R.id.editTextId);
        final EditText makeEditText = (EditText)promptsView.findViewById(R.id.editTextMake);
        final EditText yearEditText = (EditText)promptsView.findViewById(R.id.editTextYear);
        final EditText modelEditText = (EditText)promptsView.findViewById(R.id.editTextModel);

        if (TextUtils.isEmpty(idEditText.getText().toString())) {
            idEditText.setError("Please enter an id");
            idEditText.requestFocus();
        }

        if (TextUtils.isEmpty(yearEditText.getText().toString())) {
            yearEditText.setError("Please enter the year");
            yearEditText.requestFocus();
        }

        if (TextUtils.isEmpty(makeEditText.getText().toString())) {
            makeEditText.setError("Please enter a manufacture");
            makeEditText.requestFocus();
        }

        if (TextUtils.isEmpty(modelEditText.getText().toString())) {
            modelEditText.setError("Please enter a model");
            modelEditText.requestFocus();
        }

        alertDialogBuilder
                .setTitle("Create A New Vehicle:")
                .setCancelable(false)
                .setMessage("Please enter the following information")
                .setPositiveButton("Create",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                int year;
                                int vim;

                                if (!TextUtils.isEmpty(idEditText.getText().toString())) {
                                    vim = Integer.parseInt(idEditText.getText().toString());
                                } else {
                                    vim = -1;
                                }

                                if (!TextUtils.isEmpty(yearEditText.getText().toString())) {
                                    year = Integer.parseInt(yearEditText.getText().toString());
                                }
                                else {
                                    year = -1;
                                }

                                String make = makeEditText.getText().toString();
                                String model = modelEditText.getText().toString();

                                boolean status = vincent.createVehicle(vim, year, make, model);
                                if (!status) {
                                    Toast.makeText(context, vincent.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    vehicleMap = vincent.getAllVehicles();
                                    vehicleList.add(vehicleMap.get(vim));
                                    updateVehicleListView();
                                    dialog.cancel();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public void filterVehiclePrompt() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.filter_vehicle_promt_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);


        final EditText startYearEditText = promptsView.findViewById(R.id.editTextStartYear);
        final EditText makeEditText = promptsView.findViewById(R.id.editTextMake);
        final EditText endYearEditText = promptsView.findViewById(R.id.editTextEndYear);
        final EditText modelEditText = promptsView.findViewById(R.id.editTextModel);

        alertDialogBuilder
                .setTitle("Filter vehicles:")
                .setCancelable(false)
                .setPositiveButton("Apply Filter",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                EntityFilter entityFilter = new EntityFilter();

                                int startYear;
                                int endYear;

                                if (!TextUtils.isEmpty(startYearEditText.getText().toString())) {
                                    startYear = Integer.parseInt(startYearEditText.getText().toString());
                                } else {
                                    startYear = entityFilter.startYear;
                                }

                                if (!TextUtils.isEmpty(endYearEditText.getText().toString())) {
                                    endYear = Integer.parseInt(endYearEditText.getText().toString());
                                }
                                else {
                                    endYear = entityFilter.endYear;
                                }

                                String make = makeEditText.getText().toString();
                                String model = modelEditText.getText().toString();

                                boolean status = entityFilter.setStartYear(startYear) &
                                                 entityFilter.setEndYear(endYear)&
                                                 entityFilter.setMake(make)&
                                                 entityFilter.setModel(model);

                                if (!status) {
                                    Toast.makeText(context, entityFilter.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Map<Integer, Vehicle> filterMap = vincent.applyFilter(entityFilter);
                                    getVehiclesView(filterMap);
                                    dialog.cancel();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void getVehicleByIdPrompt() {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.get_vehicle_prompy_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);

        alertDialogBuilder.setView(promptsView);

        final EditText idEditText = (EditText)promptsView.findViewById(R.id.editTextId);

        if (TextUtils.isEmpty(idEditText.getText().toString())) {
            idEditText.setError("Please enter an id");
            idEditText.requestFocus();
        }


        alertDialogBuilder
                .setTitle("Search for a Vehicle:")
                .setCancelable(false)
                .setMessage("Please enter the id you want to search")
                .setPositiveButton("Search",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                int vehicleId;

                                if (!TextUtils.isEmpty(idEditText.getText().toString())) {
                                    vehicleId = Integer.parseInt(idEditText.getText().toString());
                                } else {
                                    vehicleId = -1;
                                }

                                Vehicle targetVehicle = vincent.getVehicleById(vehicleId);
                                if (targetVehicle == null) {
                                    Toast.makeText(context, vincent.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    vehicleList.clear();
                                    vehicleList.add(targetVehicle);
                                    updateVehicleListView();
                                    dialog.cancel();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

    }

    public void updateVehiclePrompt(int position) {
        // get prompts.xml view
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.update_vehicle_prompt_layout, null);

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);

        final Vehicle selectedVehicle = vehicleList.get(position);
        final TextView idTextView = promptsView.findViewById(R.id.textViewId);
        idTextView.setText(Integer.toString(selectedVehicle.getId()));
        final EditText makeEditText = promptsView.findViewById(R.id.editTextMake);
        makeEditText.setHint(selectedVehicle.getMake());
        final EditText yearEditText = promptsView.findViewById(R.id.editTextYear);
        yearEditText.setHint(Integer.toString(selectedVehicle.getYear()));
        final EditText modelEditText = promptsView.findViewById(R.id.editTextModel);
        modelEditText.setHint(selectedVehicle.getModel());

        alertDialogBuilder
                .setTitle("Updating vehicle:")
                .setMessage("Please update the information")
                .setCancelable(false)
                .setPositiveButton("Update",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {

                                String model;
                                String make;
                                int year;
                                int vehicleId = selectedVehicle.getId();

                                if (!TextUtils.isEmpty(yearEditText.getText().toString())) {
                                    year = Integer.parseInt(yearEditText.getText().toString());
                                } else {
                                    year = selectedVehicle.getYear();
                                }

                                if (!TextUtils.isEmpty(makeEditText.getText().toString())) {
                                    make = makeEditText.getText().toString();
                                } else {
                                    make = selectedVehicle.getMake();
                                }

                                if (!TextUtils.isEmpty(modelEditText.getText().toString())) {
                                    model = modelEditText.getText().toString();
                                } else {
                                    model = selectedVehicle.getModel();
                                }

                                boolean status = vincent.updateVehicle(vehicleId,year,make,model);

                                if (!status) {
                                    Toast.makeText(context, vincent.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    selectedVehicle.setMake(make);
                                    selectedVehicle.setYear(year);
                                    selectedVehicle.setModel(model);
                                    updateVehicleListView();
                                    dialog.cancel();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void deleteVehiclePrompt() {
        LayoutInflater li = LayoutInflater.from(context);
        View promptsView = li.inflate(R.layout.delete_vehicle_prompt_layout, null);


        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(
                context);
        alertDialogBuilder.setView(promptsView);


        final EditText idEditText = promptsView.findViewById(R.id.editTextId);

        if (TextUtils.isEmpty(idEditText.getText().toString())) {
            idEditText.setError("Please enter an id");
            idEditText.requestFocus();
        }

        alertDialogBuilder
                .setTitle("Deleting vehicle:")
                .setCancelable(false)
                .setMessage("Please enter the id of the vehicle you want to delete")
                .setPositiveButton("Delete",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                String model;
                                String make;
                                int year;
                                int vehicleId;

                                if (!TextUtils.isEmpty(idEditText.getText().toString())) {
                                    id = Integer.parseInt(idEditText.getText().toString());
                                } else {
                                    id = -1;
                                }

                                boolean status = vincent.deleteVehicleById(id);

                                if (!status) {
                                    Toast.makeText(context, vincent.getErrorMessage(), Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(context, "Successfully Deleted Selected Vehicle", Toast.LENGTH_SHORT).show();
                                    vehicleMap = vincent.getAllVehicles();
                                    getVehiclesView(vehicleMap);
                                    updateVehicleListView();
                                }
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });

        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();
    }

    public void getVehiclesView(Map<Integer, Vehicle> map) {
        if (vehicleList.isEmpty()) {
            Toast.makeText(context, "The fleet is currently empty", Toast.LENGTH_SHORT).show();
        }

        vehicleList.clear();

        for (Map.Entry<Integer, Vehicle> entry : map.entrySet()) {
            Vehicle eachVehicle = entry.getValue();
            vehicleList.add(eachVehicle);
        }

        updateVehicleListView();
    }

    public void updateVehicleListView() {
        vehicleListAdapter.updateVehicleList(vehicleList);
    }

}
