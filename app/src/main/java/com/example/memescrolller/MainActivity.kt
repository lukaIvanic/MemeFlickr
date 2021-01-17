package com.example.memescrolller

import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.app.DownloadManager
import android.content.Context
import android.content.Context.DOWNLOAD_SERVICE
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.AsyncTask
import android.os.Bundle
import android.os.Environment
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.PopupMenu
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.getSystemService
import androidx.core.content.FileProvider
import androidx.core.graphics.drawable.toBitmap
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.memescrolller.ui.MemeAdapter
import com.example.memescrolller.ui.MemeViewModel
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main_rec_try.*
import java.io.File
import java.io.FileOutputStream
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*
import java.util.jar.Manifest
import kotlin.math.abs


@AndroidEntryPoint
class MainActivity : AppCompatActivity(), PopupMenu.OnMenuItemClickListener,
    MemeAdapter.MemeOnClickListener {


    private lateinit var viewpager: ViewPager2
    private lateinit var memeAdapter: MemeAdapter

    private lateinit var imageViewForDownlaod: ImageView;

    private val DOWNLOAD_REQUEST_CODE = 1

    private val viewModel by viewModels<MemeViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //TODO: make an activity before the scroller for choosing subreddits

        memeAdapter = MemeAdapter(this, viewModel.repository.memePagingSource.realm)


        viewpager = findViewById(R.id.viewPager)
        viewpager.getChildAt(0).overScrollMode = View.OVER_SCROLL_NEVER
        viewpager.adapter = memeAdapter

        viewpager.offscreenPageLimit = 1


        if (memeAdapter.itemCount == 0) {
            viewModel.meme.observe(this) {
                memeAdapter.submitData(this.lifecycle, it)
            }
        } else {
            Toast.makeText(this, "Gotchya nigga", Toast.LENGTH_LONG).show()
            Log.i("GOTCHYA", "NIGGA")
        }

        val transformer = CompositePageTransformer()

        transformer.apply {
            addTransformer(MarginPageTransformer(8))
            addTransformer(object : ViewPager2.PageTransformer {
                override fun transformPage(page: View, position: Float) {
                    val v: Float = 1 - abs(position)
                    page.scaleY = 0.8f + v * 0.2f
                }
            })
        }
        viewpager.setPageTransformer(transformer)


    }

    fun showPopup(v: View) {
        val popupMenu: PopupMenu = PopupMenu(this, v)
        popupMenu.setOnMenuItemClickListener(this)
        popupMenu.inflate(R.menu.options_menu)
        popupMenu.show()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        if (item == null) return false
        return false
    }

    override fun onShareClick(url: String) {
        Log.i("ON SHARE", "Clicked")
        val intent = Intent().apply {
            action = Intent.ACTION_SEND
            putExtra(Intent.EXTRA_TEXT, url)
            type = "text/plain"
        }
        val shareIntent = Intent.createChooser(intent, null)
        startActivity(shareIntent)
    }


    override fun onDownloadClick(title: String, url: String) {
        try {

            val request: DownloadManager.Request = DownloadManager.Request((Uri.parse(url)))

            request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI)

            request.setTitle("$title.jpg")


            request.allowScanningByMediaScanner()

            request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            request.setDestinationInExternalPublicDir(
                Environment.DIRECTORY_PICTURES,
                "" + "$title.jpg" + System.currentTimeMillis()
            )

            var downloadManager = getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
            downloadManager.enqueue(request)
        }catch (e: Exception){
            e.printStackTrace()
            Toast.makeText(this, "Download failed", Toast.LENGTH_SHORT).show()
        }


    }

}