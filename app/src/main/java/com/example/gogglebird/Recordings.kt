package com.example.gogglebird

import android.content.pm.PackageManager
import android.media.MediaRecorder
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Environment
import android.widget.Button
import android.widget.Toast
import androidx.core.app.ActivityCompat
import java.io.IOException
import android.Manifest
import androidx.core.content.ContextCompat

class Recordings : AppCompatActivity() {

    //variables
    private lateinit var mediaRecorder: MediaRecorder
    private var isRecording = false // when the activity starts
    //path the save the file
    private val audioFilePath = "${Environment.getExternalStorageDirectory().absolutePath}/my_rec.3gp"
    //lateinit var btnRec: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recordings)

        //Typecast
        val btnRec = findViewById<Button>(R.id.button)

        //permissions
        if (checkPermissions())
        {
            //Can Change to Firebase
            mediaRecorder = MediaRecorder()
            mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
            mediaRecorder.setOutputFile(audioFilePath)
            mediaRecorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)

            btnRec.setOnClickListener(){
                //same btn --> change the text
                if (isRecording){
                    //method to stop recording
                    stopRecording()
                    btnRec.text = "Record"

                }
                else
                {
                    // method to start recording
                    startRecording()
                    btnRec.text = "Stop Recording"
                }
            }

        } //  end if checkPermissions
        else
        {
            //do something --> maybe a method
            requestPermissions()
        }
    }
    private fun checkPermissions(): Boolean{
        val writeExternalStoragePermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
        val recordAudioPermission = ContextCompat.checkSelfPermission(
            this,
            Manifest.permission.RECORD_AUDIO
        )
        return writeExternalStoragePermission ==
                PackageManager.PERMISSION_GRANTED &&
                recordAudioPermission == PackageManager.PERMISSION_GRANTED

    }
    private fun requestPermissions(){
        ActivityCompat.requestPermissions(
            this,
            arrayOf(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO
            ),
            0
        )
    }

    private fun startRecording(){
        try{
            mediaRecorder.prepare()
            mediaRecorder.start()
            isRecording = true
            Toast.makeText(this, "Recording started", Toast.LENGTH_SHORT).show()
        } catch (e: IOException){
            e.printStackTrace()
        }
    }

    private fun stopRecording(){
        mediaRecorder.stop()
        mediaRecorder.release()
        isRecording = false
        Toast.makeText(this, "Recording stopped", Toast.LENGTH_SHORT).show()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if(requestCode == 0){
            if (grantResults.isNotEmpty() && grantResults[0] ==
                PackageManager.PERMISSION_GRANTED){
                recreate()
            } else {
                Toast.makeText(this, "Permissions denied", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if(isRecording){
            stopRecording()
        }
    }

}