package com.example.myapplication

import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.widget.Button
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.updatePadding
import android.widget.Toast

class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)

        val rootView = findViewById<View>(R.id.rootLayout)
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top, bottom = systemBars.bottom)
            insets
        }

        val characterId = intent.getIntExtra("characterId", 0)

        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener { finish() }

        RetrofitClient.apiService.getCharacterById(characterId)
            .enqueue(object: Callback<Personaje> {
                override fun onResponse(call: Call<Personaje>, response: Response<Personaje>) {
                    if(response.isSuccessful && response.body() != null) {
                        val c = response.body()!!

                        findViewById<TextView>(R.id.tvNameDetail).text = c.name
                        findViewById<TextView>(R.id.tvStatus).text = "Status: ${c.status}"
                        findViewById<TextView>(R.id.tvSpecies).text = "Species: ${c.species}"
                        findViewById<TextView>(R.id.tvGender).text = "Gender: ${c.gender}"
                        findViewById<TextView>(R.id.tvOrigin).text = "Origin: ${c.origin.name}"
                        findViewById<TextView>(R.id.tvLocation).text = "Location: ${c.location.name}"

                        Glide.with(this@DetailActivity).load(c.image)
                            .into(findViewById(R.id.ivCharacterDetail))
                    } else {
                        Toast.makeText(this@DetailActivity, "No se encontró el personaje", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Personaje>, t: Throwable) {
                    Toast.makeText(this@DetailActivity, "Error de conexión: ${t.localizedMessage}", Toast.LENGTH_SHORT).show()
                }
            })
    }
}
