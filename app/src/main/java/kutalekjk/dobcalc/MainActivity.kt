package kutalekjk.dobcalc

import android.app.DatePickerDialog
import android.icu.text.SimpleDateFormat
import android.icu.util.Calendar
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.RequiresApi
import java.util.*

class MainActivity : AppCompatActivity() {
    private var tvSelectedDate : TextView? = null
    private var tvAgeInMinutes : TextView? = null

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnDatePicker: Button = findViewById(R.id.btnDatePicker)

        tvSelectedDate = findViewById(R.id.tvSelectedDate)
        tvAgeInMinutes = findViewById(R.id.tvAgeInMinutes)

        btnDatePicker.setOnClickListener { view ->

            clickDatePicker()
        }
    }

    @RequiresApi(Build.VERSION_CODES.N)
    private fun clickDatePicker() {
        val myCalendar = Calendar.getInstance()
        val year = myCalendar.get(Calendar.YEAR)
        val month = myCalendar.get(Calendar.MONTH)
        val day = myCalendar.get(Calendar.DAY_OF_MONTH)

        // Create date dialog picker to be able to pick a date from a calendar widget
        val dpd = DatePickerDialog(
            this,
            DatePickerDialog.OnDateSetListener { view, selectedYear, selectedMonth, selectedDayOfMonth ->
                Toast.makeText(
                    this,
                    "Year was $selectedYear, month was ${selectedMonth + 1}, day of month was $selectedDayOfMonth",
                    Toast.LENGTH_LONG
                ).show()

                // Set the selected date text
                val selectedDate = "$selectedDayOfMonth/${selectedMonth + 1}/$selectedYear"
                tvSelectedDate?.text = selectedDate

                // Create instance of class for formatting and parsing dates
                val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.ENGLISH)

                // Parse the date from string
                val theDate = sdf.parse(selectedDate)
                theDate?.let {
                    // Convert the selected date from milliseconds to minutes
                    val selectedDateInMinutes = theDate.time / 60_000

                    // Get the current date
                    val currentDate = sdf.parse(sdf.format(System.currentTimeMillis()))
                    currentDate?.let { val currentDateInMinutes = currentDate.time / 60_000

                        // Calculate the difference
                        val differenceInMinutes = currentDateInMinutes - selectedDateInMinutes
                        tvAgeInMinutes?.text = differenceInMinutes.toString()  }
                    }
            }, year, month, day
        )

        // Assert for only picking dates from the past, not from the future (max val = yesterday)
        dpd.datePicker.maxDate = System.currentTimeMillis() - 86_400_000
        dpd.show()
    }
}