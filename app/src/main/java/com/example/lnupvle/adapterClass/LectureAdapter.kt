package com.example.lnupvle.adapterClass

import android.app.DownloadManager
import android.content.Context
import android.os.Environment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.net.toUri
import androidx.recyclerview.widget.RecyclerView
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.Lecture
import com.google.firebase.storage.FirebaseStorage

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

        val filetype = lecture.lectureFormat

        holder.lectureNameText.text = lecture.lectureName
        holder.lectureIdText.text = "Ідентифікатор: ${lecture.lectureId}"

        if (filetype == "docx") {
            holder.fileImageView.setImageResource(R.drawable.docx)
        }
        if (filetype == "xlsx") {
            holder.fileImageView.setImageResource(R.drawable.excel)
        }

        if (filetype == "pptx") {
            holder.fileImageView.setImageResource(R.drawable.present)
        }


        holder.lectureCardLayout.setOnClickListener() {
            val userPref = context.getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
            val lessonId = userPref.getString("LID", "").toString()

            val storageRef = FirebaseStorage.getInstance().getReference("app")
                .child("Lessons/$lessonId/${lecture.lectureId}")
            storageRef.downloadUrl.addOnSuccessListener { uri ->
                val filename = lecture.lectureName
                startDownload(uri.toString(), filename, filetype)
            }.addOnFailureListener { exception ->
                showToast("Не вдалося отримати url: $exception")
            }
        }
    }

    private fun startDownload(url: String, filename: String, filetype: String) {
        try {
            var downloadManager = context.getSystemService(DownloadManager::class.java)
            val request = DownloadManager.Request(url.toUri())
                .setMimeType("*/*")
                .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                .setTitle("$filename")
                .setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, "$filename.$filetype")
            downloadManager.enqueue(request)


            showToast("Завантажено успішно")
        } catch (e: Exception) {
            showToast(e.toString())
        }
    }

    class LectureViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val lectureNameText: TextView = itemView.findViewById(R.id.lecture_name_text)
        val lectureIdText: TextView = itemView.findViewById(R.id.lecture_id_text)
        val lectureCardLayout: LinearLayout = itemView.findViewById(R.id.lecture_card_layout)
        val fileImageView: ImageView = itemView.findViewById(R.id.file_image)
    }

    private fun showToast(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}