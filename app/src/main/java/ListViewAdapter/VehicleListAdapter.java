package ListViewAdapter;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;
import android.widget.ImageView;
import android.widget.TextView;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

import Models.Vehicle;
import receiver.service.android.vincent.com.mitchellint.R;

/**
 * Created by vanne on 10/29/2017.
 */

public class VehicleListAdapter extends ArrayAdapter {
    private Activity context;
    private ArrayList<Vehicle> vehicles;
    public VehicleListAdapter(Activity context, ArrayList<Vehicle> vehicles){
        super(context, R.layout.vehicle_list_layout,vehicles);
        this.context = context;
        this.vehicles = vehicles;
    }
    String MenuAdapterTag = "In Menu Adapter";

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View r = convertView;
        ViewHolder viewHolder = null;
        if(r == null){
            LayoutInflater layoutInflater = context.getLayoutInflater();
            r = layoutInflater.inflate(R.layout.vehicle_list_layout, null, true);
            viewHolder = new ViewHolder(r);
            r.setTag(viewHolder);
        }else{
            viewHolder = (ViewHolder)r.getTag();
        }

        viewHolder.vehicleImage.setImageResource(R.drawable.vehicle);

        String vimView = Integer.toString(vehicles.get(position).getId());
        viewHolder.vim.setText(vimView);

        viewHolder.make.setText(vehicles.get(position).getMake().toUpperCase());
        String yearView = Integer.toString(vehicles.get(position).getYear());

        viewHolder.year.setText(yearView);
        viewHolder.model.setText(vehicles.get(position).getModel().toUpperCase());
        return r;
    }

    class ViewHolder {
        ImageView vehicleImage;
        TextView vim;
        TextView make;
        TextView year;
        TextView model;

        ViewHolder(View view){
            vehicleImage = view.findViewById(R.id.vehicleImage);
            vim = view.findViewById(R.id.vim);
            make = view.findViewById(R.id.make);
            year = view.findViewById(R.id.year);
            model = view.findViewById(R.id.model);
        }
    }

    public void updateVehicleList(ArrayList<Vehicle> vehicles) {
        this.vehicles = vehicles;
        notifyDataSetChanged();
    }

}
