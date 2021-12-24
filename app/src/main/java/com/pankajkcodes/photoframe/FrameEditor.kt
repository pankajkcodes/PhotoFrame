package com.pankajkcodes.photoframe

import android.content.ContentResolver
import android.content.ContentValues
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Build.VERSION.SDK_INT
import android.os.Bundle
import android.provider.MediaStore
import android.provider.SyncStateContract.Helpers.insert
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.impulsive.zoomimageview.ZoomImageView

class FrameEditor : AppCompatActivity(), OnFrameClick {

    private lateinit var userImg: ZoomImageView
    private lateinit var saveBtn: Button
    private lateinit var frameContainer: ImageView
    private lateinit var screenShot: RelativeLayout
    private lateinit var recyclerView: RecyclerView
    private lateinit var list: MutableList<FrameList>

    private val frameLayouts = arrayOf(
        R.drawable.frame_0,
        R.drawable.frame_2,
        R.drawable.frame_3,
        R.drawable.frame_4,
        R.drawable.frame_5,
        R.drawable.frame_6,
        R.drawable.frame_7,
        R.drawable.frame_8,
        R.drawable.frame_9,
        R.drawable.frame_10,
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_frame_editor)


        val path = intent.getStringExtra("path")

        userImg = findViewById(R.id.userImg)
        saveBtn = findViewById(R.id.saveBtn)
        frameContainer = findViewById(R.id.frameContainer)
        recyclerView = findViewById(R.id.frameRecyclerView)
        screenShot = findViewById(R.id.screenshot)

        Glide.with(this).load(path).into(userImg)

        list = ArrayList()

        initFrameList()

        recyclerView.adapter = FrameAdapter(this, list, this)

        saveBtn.setOnClickListener {

            storeImg(getScreenShot(screenShot))
        }

    }

    private fun initFrameList() {
        for (j in frameLayouts) {
            list.add(FrameList(j))
        }
    }


    override fun frameClick(position: Int) {
        Glide.with(this).load(list[position].bgFrame).into(frameContainer)
    }


    private fun getScreenShot(view: View): Bitmap {

        view.isDrawingCacheEnabled = true
        return Bitmap.createBitmap(view.getDrawingCache())

        view.isDrawingCacheEnabled = false
    }

    private fun storeImg(bitmap: Bitmap){
        var uri: Uri? = null
        uri = if (SDK_INT >= Build.VERSION_CODES.R) {
            MediaStore.Images.Media.getContentUri(MediaStore.VOLUME_EXTERNAL_PRIMARY)
        }else{
            MediaStore.Images.Media.EXTERNAL_CONTENT_URI

        }

        val contentValues = ContentValues().apply {
            put(MediaStore.Images.Media.DISPLAY_NAME,"display_name.jpg")
            put(MediaStore.Images.Media.MIME_TYPE,"images/*")
            put(MediaStore.Images.Media.WIDTH,bitmap.width)
            put(MediaStore.Images.Media.HEIGHT,bitmap.height)
        }

        try {
            val v= contentResolver.insert(uri,contentValues)
            val outputStream = contentResolver.openOutputStream(v!!)
            bitmap.compress(Bitmap.CompressFormat.JPEG,100,outputStream)
            Toast.makeText(this,"Image Saved",Toast.LENGTH_LONG).show()
        } catch (e: Exception) {
            Toast.makeText(this,"Image Not Saved",Toast.LENGTH_LONG).show()
        }


    }

}