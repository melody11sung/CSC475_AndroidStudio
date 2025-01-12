package com.example.unitconverter;

import static android.widget.Toast.LENGTH_SHORT;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;


public class MainActivity extends AppCompatActivity {


    private Spinner spinner_menu, spinner_before, spinner_after;
    private ArrayAdapter<String> adapter_menu, adapter_units;
    private String[] menu_items = {"Fuel Economy", "Temperature"}; //, "Length", "Mass", "Temperature", "Volume"};
    private String[] fuel_items = {"Km/Liter", "Miles/Gallon"};
    private String[] length_items = {"Kilometer", "Meter", "Centimeter", "Mile", "Yard", "Foot", "Inch"};
    private String[] mass_items = {"Kilogram", "gram", "Pound", "Ounce"};
    private String[] temperature_items = {"Celsius", "Fahrenheit", "Kelvin"};
    private String[] volume_items = {"Liter", "Cubic Centimeter (Milliliter)", "Gallon", "Quart", "Pint", "Ounce"};
    private String unit_before, unit_after;
    private EditText et_before;
    private TextView tv_after;
    private FloatingActionButton btn_convert;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setMenu();  // menu spinner
        setUnit();  // unit spinner

        // when before_ and after_ spinner is selected, bring the right calculation
        et_before = findViewById(R.id.et_before);
        tv_after = findViewById(R.id.tv_after);

        // when the convert button is clicked,
        FloatingActionButton btn_convert = findViewById(R.id.btn_convert);
        btn_convert.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                try {
                    String inputText = et_before.getText().toString();

                    // if input number is not null,
                    if (!inputText.isEmpty()){
                        double num1 = Double.parseDouble(et_before.getText().toString());

                        // if both spinner units have been selected,
                        if (!unit_before.isEmpty() && !unit_after.isEmpty()){

                            // calculate and show the result
                            double num2 = UnitConverter(unit_before, unit_after, num1);
                            tv_after.setText(String.format("%.4f", num2));
                            Toast.makeText(MainActivity.this, "Calculated", LENGTH_SHORT).show();

                        } else { // if any spinner units have not been selected,
                            Toast.makeText(MainActivity.this, "Please choose units", LENGTH_SHORT).show();
                        }

                    } else { // if input number is null
                        Toast.makeText(MainActivity.this, "Input a number", LENGTH_SHORT).show();
                    }

                } catch (Exception e){
                    Log.e("OnClick", "Error converting input: " + e.getMessage());
                }

            }
        });

    }

    private void setMenu() {

        spinner_menu = findViewById(R.id.spinner_menu);
        spinner_before = findViewById(R.id.spinner_before);
        spinner_after = findViewById(R.id.spinner_after);

        adapter_menu = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, menu_items);
        adapter_menu.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner_menu.setAdapter(adapter_menu);

        // when the unit category is selected, change the before_ and after_ spinner
        spinner_menu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

                String selectedItem = (String) parent.getItemAtPosition(position);
                if (selectedItem.equals("Fuel Economy")) {
                    adapter_units = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, fuel_items);

                } else if (selectedItem.equals("Length")) {
                    adapter_units = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, length_items);

                } else if (selectedItem.equals("Mass")) {
                    adapter_units = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, mass_items);

                } else if (selectedItem.equals("Temperature")) {
                    adapter_units = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, temperature_items);

                } else {
                    adapter_units = new ArrayAdapter<String>(MainActivity.this,
                            android.R.layout.simple_spinner_item, volume_items);
                }

                adapter_units.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                spinner_before.setAdapter(adapter_units);
                spinner_after.setAdapter(adapter_units);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }

    private void setUnit() {

        // get the input unit from the first spinner
        spinner_before.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.unit_before = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        // get the output unit from the second spinner
        spinner_after.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                MainActivity.this.unit_after = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

    }


    public Double UnitConverter(String unit_before, String unit_after, double num){

        double result = num;

        switch (unit_before) {
            // for Fuel Economy
            case "Km/Liter":
                if (unit_after.equals("Miles/Gallon")){
                    result = num * 2.35215;
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                }
                break;
            case "Miles/Gallon":
                if (unit_after.equals("Km/Liter")){
                    result = num/2.35215;
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                }
                break;

            // for Temperature
            case "Celsius":
                if (unit_after.equals("Fahrenheit")){
                    result = (num * 9/5) + 32;
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                } else if (unit_after.equals("Kelvin")){
                    result = num + 273.15;
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                } break;
            case "Fahrenheit":
                if (unit_after.equals("Celsius")){
                    result = (num-32) * 5/9;
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                } else if (unit_after.equals("Kelvin")){
                    result = (num-32) * 5/9 + 273.15;
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                } break;
            case "Kelvin":
                if (unit_after.equals("Celsius")){
                    result = (num-273.15);
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                } else if (unit_after.equals("Fahrenheit")){
                    result = (num-273.15) * 9/5 + 32;
                    Toast.makeText(this, "Converted", LENGTH_SHORT).show();
                } break;

            default:
                Toast.makeText(this, "Change Unit Option", LENGTH_SHORT).show();
        }

        return result;
    }

}