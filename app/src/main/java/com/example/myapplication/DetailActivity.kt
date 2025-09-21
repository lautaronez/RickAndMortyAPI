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

// Activity to display detailed information about a selected character
class DetailActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)


        // Adjust padding for system bars (status bar / navigation bar)
        val rootView = findViewById<View>(R.id.rootLayout) // ID Root Layout from activity_detail
        ViewCompat.setOnApplyWindowInsetsListener(rootView) { view, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            view.updatePadding(top = systemBars.top, bottom = systemBars.bottom)
            insets
        }

        // Get character ID passed from MainActivity
        val characterId = intent.getIntExtra("characterId", 0)

        // Set up "Back" button to close this activity and return to MainActivity
        val btnVolver = findViewById<Button>(R.id.btnVolver)
        btnVolver.setOnClickListener {
            finish() // Cierra DetailActivity y vuelve a MainActivity
        }

        // Make API request to get character details by ID

        RetrofitClient.apiService.getCharacterById(characterId)
            .enqueue(object: Callback<Personaje> {
                override fun onResponse(call: Call<Personaje>, response: Response<Personaje>) {
                    if(response.isSuccessful) {
                        val c = response.body()!! // Make API request to get character details by ID

                        // Update UI with character details
                        findViewById<TextView>(R.id.tvNameDetail).text = c.name
                        findViewById<TextView>(R.id.tvStatus).text = "Status: ${c.status}"
                        findViewById<TextView>(R.id.tvSpecies).text = "Species: ${c.species}"
                        findViewById<TextView>(R.id.tvGender).text = "Gender: ${c.gender}"
                        findViewById<TextView>(R.id.tvOrigin).text = "Origin: ${c.origin.name}"
                        findViewById<TextView>(R.id.tvLocation).text = "Location: ${c.location.name}"

                        // Load character image
                        Glide.with(this@DetailActivity).load(c.image)
                            .into(findViewById(R.id.ivCharacterDetail))
                    }
                }
                // Fail api request
                override fun onFailure(call: Call<Personaje>, t: Throwable) {}
            })
    }
}
