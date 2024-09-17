package com.example.lnupvle

import android.app.DownloadManager
import android.content.Context
import android.net.Uri
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.storage.FirebaseStorage
import java.io.File

class LectureAdapter (
    private val lecturesList: List<Lecture>,
    private val context: Context
)
    : RecyclerView.Adapter<LectureAdapter.LectureViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LectureViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.lecture_item, parent, false)

        return LectureViewHolder(view)
    }

    override fun getItemCount(): Int {
        return lecturesList.size
    }

    override fun onBindViewHolder(holder: LectureViewHolder, position: Int) {
        val lecture = lecturesList[position]
        val localFile = File.createTempFile("Download", "mp3")

        holder.lectureNameText.text = "Назва лекції: ${lecture.lectureName}"
        holder.lectureIdText.text = "Ідентифікатор лекції: ${lecture.lectureId}"
        holder.lectureCardLayout.setOnClickListener() {
            val storageRef = FirebaseStorage.getInstance().getReference("app")
        }
    }

    private fun startDownload(url: String) {
        val request = DownloadManager.Request(Uri.parse(url))
            .setTitle("Завантаження лекції")
            .setDescription("Завантажується файл...")
            .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
            .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "lecture.mp3") // Замініть на бажаний шлях

        val downloadManager = context.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
        downloadManager.enqueue(request)
    }

    class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val lectureNameText: TextView = itemView.findViewById(R.id.lecture_name_text)
        val lectureIdText: TextView = itemView.findViewById(R.id.lecture_id_text)
        val lectureCardLayout: LinearLayout = itemView.findViewById(R.id.lecture_card_layout)
    }



    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}