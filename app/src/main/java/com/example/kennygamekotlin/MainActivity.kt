package com.example.kennygamekotlin

import android.content.DialogInterface
import android.os.Bundle
import android.os.CountDownTimer
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.kennygamekotlin.databinding.ActivityMainBinding
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    private lateinit var binding:ActivityMainBinding
    private val handler = Handler(Looper.getMainLooper())
    private var screenWidth=0
    private var screenHeight=0
    private var imageViewWidth=0
    private var imageViewHeight=0
    private var score=0

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityMainBinding.inflate(layoutInflater)
        val view = binding.root
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(view)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        binding.imageView.visibility= View.VISIBLE
        binding.imageView.setImageResource(R.drawable.k4)
        calculateScreenAndViewDimessions()
        startRandomMovement()
        binding.imageView.setOnClickListener {
            score++
            binding.score.text="Score: $score"

            it.animate()
                .scaleX(0.9f)
                .scaleY(0.9f)
                .setDuration(100)
                .withEndAction {
                    it.animate()
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(100)
                    .start()
                }.start()
        }

        object : CountDownTimer(15000,1000){
            override fun onTick(millisUntilFinished: Long) {
                binding.time.text="Time: ${millisUntilFinished / 1000}"
            }

            override fun onFinish() {
                val alert = AlertDialog.Builder(this@MainActivity)
                alert.setTitle("Game Over")
                alert.setMessage("Restart The Game")
                alert.setCancelable(false)
                alert.setPositiveButton("Yes", object : DialogInterface.OnClickListener {
                    override fun onClick(dialog: DialogInterface?, which: Int) {
                        val intent = intent
                        finish()
                        startActivity(intent)
                    }
                })
                alert.setNegativeButton("No"){ p0,p1 ->
                    finishAffinity()
                }
                alert.show()
            }

        }.start()
    }
    private fun calculateScreenAndViewDimessions(){
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        screenWidth = displayMetrics.widthPixels
        screenHeight = displayMetrics.heightPixels

        binding.imageView.post{
            imageViewWidth=binding.imageView.width
            imageViewHeight=binding.imageView.height

            Log.d("Dimensions", "Screen: $screenWidth x $screenHeight")
            Log.d("Dimensions", "ImageView: $imageViewWidth x $imageViewHeight")
        }
    }
    private fun startRandomMovement() {
       if(imageViewWidth ==0 || imageViewHeight==0){
           binding.imageView.post{
               startRandomMovement()
           }
           return
       }
        val maxX=screenWidth-imageViewWidth
        val maxY=screenHeight-imageViewHeight

        val randomX = Random.nextInt(0,maxX).toFloat()
        val randomY = Random.nextInt(0,maxY).toFloat()

        binding.imageView.animate()
            .x(randomX)
            .y(randomY)
            .setDuration(200)
            .withEndAction {
                handler.postDelayed({ startRandomMovement() }, 250)
            }
            .start()
    }
}