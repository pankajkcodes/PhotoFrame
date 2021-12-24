package com.pankajkcodes.photoframe

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class MainActivity : AppCompatActivity() {


    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE,
        Manifest.permission.READ_EXTERNAL_STORAGE
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        checkPermission()


        val getContent =
            registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->


                val intent = Intent(this, FrameEditor::class.java)
                intent.putExtra("path", uri.toString())
                startActivity(intent)
            }

        findViewById<Button>(R.id.selectImg).setOnClickListener {
            getContent.launch("image/*")

        }
    }

    private fun checkPermission(): Boolean {
        var result: Int
        val permissionNeeded: MutableList<String> = ArrayList()

        for (p in permissions) {
            result = ContextCompat.checkSelfPermission(this, p)

            if (result == PackageManager.PERMISSION_GRANTED) {
                permissionNeeded.add(p)
            }

        }

        if (permissionNeeded.isNotEmpty()) {

            ActivityCompat.requestPermissions(this, permissionNeeded.toTypedArray(), 1)

            return false
        }

        return true
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == 1) {
            if (grantResults[0] == PackageManager.PERMISSION_DENIED) {
                Toast.makeText(this, "Please Allow Permission", Toast.LENGTH_LONG).show()

            }
            checkPermission()
        }
    }
}