package com.example.mchs.presentation;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.RetryPolicy;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.mchs.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class AddItemActivity extends AppCompatActivity implements View.OnClickListener {

    private EditText editTextItemName, editTextBrand, editAddress;
    private TextView dateTextView2, dateTextView3, jobTexts;
    private Button buttonAddItem;
    private String selectedCity, selectedCity2;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_item);

        editTextItemName = findViewById(R.id.et_item_name);
        editTextBrand = findViewById(R.id.et_brand);
        editAddress = findViewById(R.id.address);
        dateTextView2 = findViewById(R.id.dateTextView2);
        dateTextView3 = findViewById(R.id.dateTextView3);
        jobTexts = findViewById(R.id.jobTexts);

        buttonAddItem = findViewById(R.id.btn_add_item);
        buttonAddItem.setOnClickListener(this);

        TextView dateTextView = findViewById(R.id.dateTextView);
        String currentDate = getCurrentDate();
        dateTextView.setText(currentDate);
        showAllUsersData();

        Spinner spinnerCities = findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.options, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities.setAdapter(adapter);

        spinnerCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCity = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });

        Spinner spinnerCities2 = findViewById(R.id.spinner2);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this,
                R.array.options2, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCities2.setAdapter(adapter2);

        spinnerCities2.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parentView, View selectedItemView, int position, long id) {
                selectedCity2 = parentView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parentView) {
            }
        });
    }

    private void addItemToSheet() {
        final ProgressDialog loading = ProgressDialog.show(this, "Данные отправляются", "Пожалуйста, подождите..");
        final String name = editTextItemName.getText().toString().trim();
        final String brand = editTextBrand.getText().toString().trim();
        final String address = editAddress.getText().toString().trim();

        final String names = dateTextView2.getText().toString().trim();
        final String region = dateTextView3.getText().toString().trim();
        final String job = jobTexts.getText().toString().trim();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, "https://script.google.com/macros/s/AKfycbyv8wjTBrAwg-itOv5XiC1jS6q0TMKz1AI78-iCeDTXpyb6OxdE8OGxwBx2rTqYWmBh/exec",
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        loading.dismiss();
                        Toast.makeText(AddItemActivity.this, response, Toast.LENGTH_LONG).show();

                        Intent intent = new Intent(AddItemActivity.this, ProfileActivity.class);
                        startActivity(intent);
                        finish();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        loading.dismiss();
                        // Обработка ошибки
                        Toast.makeText(AddItemActivity.this, "Error: " + error.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
        ) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();

                params.put("action", "addItem");
                params.put("itemName", name);
                params.put("brand", brand);
                params.put("address", address);
                params.put("city", selectedCity);
                params.put("theme", selectedCity2);

                params.put("names", names);
                params.put("region", region);
                params.put("job", job);

                return params;
            }
        };

        int socketTimeOut = 50000;

        RetryPolicy retryPolicy = new DefaultRetryPolicy(socketTimeOut, 0, DefaultRetryPolicy.DEFAULT_BACKOFF_MULT);
        stringRequest.setRetryPolicy(retryPolicy);

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(stringRequest);
    }

    @Override
    public void onClick(View v) {
        if (v == buttonAddItem) {
            addItemToSheet();
        }
    }

    public void showAllUsersData() {
        Intent intent = getIntent();
        String nameUser = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");

        String job = intent.getStringExtra("job");
        dateTextView3.setText(email);
        dateTextView2.setText(nameUser);
        jobTexts.setText(job);
    }

    private String getCurrentDate() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm:ss", Locale.getDefault());
        Date currentDate = new Date();
        return dateFormat.format(currentDate);
    }
}