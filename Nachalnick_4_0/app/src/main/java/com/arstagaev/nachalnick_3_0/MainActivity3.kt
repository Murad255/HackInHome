package com.arstagaev.nachalnick_3_0

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.w3c.dom.Text

class MainActivity3 : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main3)

        var st1 = findViewById<TextView>(R.id.st1)
        var st2 = findViewById<TextView>(R.id.st2)
        var st3 = findViewById<TextView>(R.id.st3)
        var st4 = findViewById<TextView>(R.id.st4)

        //var a = (0..100).random()

        st1.setBackgroundColor(Color.GREEN)
        st2.setBackgroundColor(Color.RED)
        st3.setBackgroundColor(Color.GREEN)
        st4.setBackgroundColor(Color.RED)

        st1.setText("ок")
        st2.setText("отстает")
        st3.setText("ок")
        st4.setText("внимание")
    }
}