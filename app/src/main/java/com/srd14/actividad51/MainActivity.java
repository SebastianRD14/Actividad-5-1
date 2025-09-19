package com.srd14.actividad51;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    private Spinner spinnerCategory;
    private TextView tvDate, tvTime;
    private EditText etDescription;
    private ImageButton ibPickDate, ibPickTime, ibAdd, ibClear;
    private ListView lvItems;

    private final ArrayList<String> items = new ArrayList<>();
    private ArrayAdapter<String> listAdapter;
    private Calendar selected; // fecha/hora seleccionadas


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        // Vincular con ID
        spinnerCategory = findViewById(R.id.spinnerCategory);
        tvDate = findViewById(R.id.tvDate);
        tvTime = findViewById(R.id.tvTime);
        etDescription = findViewById(R.id.etDescription);
        ibPickDate = findViewById(R.id.ibPickDate);
        ibPickTime = findViewById(R.id.ibPickTime);
        ibAdd = findViewById(R.id.ibAdd);
        ibClear = findViewById(R.id.ibClear);
        lvItems = findViewById(R.id.lvItems);

        //Spinner
        ArrayAdapter<CharSequence> spinnerAdapter = ArrayAdapter.createFromResource(
                this, R.array.categorias, android.R.layout.simple_spinner_item);
        spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCategory.setAdapter(spinnerAdapter);

        // Image button para elegir la fecha
        ibPickDate.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            DatePickerDialog datePicker = new DatePickerDialog(
                    MainActivity.this,
                    (view, year, month, dayOfMonth) -> {
                        if (selected == null) selected = Calendar.getInstance();
                        selected.set(Calendar.YEAR, year);
                        selected.set(Calendar.MONTH, month);
                        selected.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                        updateDateTimeLabels(); // Actualizar las etiquetas de fecha
                    },
                    now.get(Calendar.YEAR),
                    now.get(Calendar.MONTH),
                    now.get(Calendar.DAY_OF_MONTH)
            );
            datePicker.show();
        });

        // Botón para elegir la hora
        ibPickTime.setOnClickListener(v -> {
            Calendar now = Calendar.getInstance();
            TimePickerDialog timePicker = new TimePickerDialog(
                    MainActivity.this,
                    (view, hourOfDay, minute) -> {
                        if (selected == null) selected = Calendar.getInstance();
                        selected.set(Calendar.HOUR_OF_DAY, hourOfDay);
                        selected.set(Calendar.MINUTE, minute);
                        updateDateTimeLabels(); // Actualizar las etiquetas de hora
                    },
                    now.get(Calendar.HOUR_OF_DAY),
                    now.get(Calendar.MINUTE),
                    true
            );
            timePicker.show();
        });

        // Botón para agregar un elemento a la lista
        ibAdd.setOnClickListener(v -> {
            String category = spinnerCategory.getSelectedItem().toString();
            String date = tvDate.getText().toString();
            String time = tvTime.getText().toString();
            String desc = etDescription.getText().toString();

            // Construir el texto
            String newItem = category + " - " + date + " " + time + "\n" + desc;
            items.add(newItem);

            listAdapter.notifyDataSetChanged(); // Actualiza la lista
            etDescription.setText("");
        });

        // Botón para limpiar la lista
        ibClear.setOnClickListener(v -> {
            items.clear();
            listAdapter.notifyDataSetChanged();
        });

        // Configurar la lista
        listAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_2, android.R.id.text1, items);
        lvItems.setAdapter(listAdapter);

        // Evento long press para eliminar un elemento
        lvItems.setOnItemLongClickListener((parent, view, position, id) -> {
            items.remove(position);
            listAdapter.notifyDataSetChanged();
            return true;
        });

    }

    private void updateDateTimeLabels() {
        tvDate.setText(new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(selected.getTime()));
        tvTime.setText(new SimpleDateFormat("HH:mm", Locale.getDefault()).format(selected.getTime()));
    }
}
