package com.example.lnupvle.ui.fragments.lesson

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import com.example.lnupvle.R
import com.example.lnupvle.dataClass.Lecture
import com.example.lnupvle.utilits.navigate
import com.example.lnupvle.utilits.showToast
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage

class FragmentLectureEdit : Fragment() {
    private lateinit var createNameField: EditText
    private lateinit var createIdField: EditText
    private lateinit var deleteNameField: EditText

    private lateinit var nameLecture: String
    private lateinit var idLecture: String
    private lateinit var deletename: String
    private lateinit var fileFormat: String

    private lateinit var setDOCXButton: Button
    private lateinit var setXLSXButton: Button
    private lateinit var setPPTXButton: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_lecture_edit, container, false)

        createNameField = view.findViewById(R.id.lecture_create_name)
        createIdField = view.findViewById(R.id.lecture_create_id)
        deleteNameField = view.findViewById(R.id.lecture_delete_name)

        val createButton = view.findViewById<Button>(R.id.button_create_lecture)
        val deleteButton = view.findViewById<Button>(R.id.button_delete_lecture)
        val toBackButton = view.findViewById<Button>(R.id.to_back_button)

        fileFormat = "docx"

        setDOCXButton = view.findViewById(R.id.button_set_docx)
        setXLSXButton = view.findViewById(R.id.button_set_xlsx)
        setPPTXButton = view.findViewById(R.id.button_set_pptx)

        setDOCXButton.setOnClickListener() {
            setFormatFile(setDOCXButton, "docx")
        }

        setXLSXButton.setOnClickListener() {
            setFormatFile(setXLSXButton, "xlsx")
        }

        setPPTXButton.setOnClickListener() {
            setFormatFile(setPPTXButton, "pptx")
        }

        val pickFile = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()){
            if (it.resultCode == RESULT_OK && it.data != null){
                val uri = it.data!!.data
                uploadFile(uri!!)
            }
        }

        createButton.setOnClickListener() {
            nameLecture = createNameField.text.toString()
            idLecture = createIdField.text.toString()

            if (nameLecture.isEmpty() || idLecture.isEmpty()) {
                showToast("Заповніть поля для створення")
            } else {
                val intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "*/*"
                pickFile.launch(intent)
            }
        }

        deleteButton.setOnClickListener() {
            deletename = deleteNameField.text.toString()
            deleteFile()
        }

        toBackButton.setOnClickListener() {
            navigate(R.id.action_LectureEdit_to_Lesson)
        }

        return view
    }

    private fun setFormatFile(button: Button, file: String) {
        setDOCXButton.setBackgroundResource(R.drawable.round_button)
        setXLSXButton.setBackgroundResource(R.drawable.round_button)
        setPPTXButton.setBackgroundResource(R.drawable.round_button)

        fileFormat = file
        button.setBackgroundResource(R.drawable.shaded_button)
    }

    private fun uploadFile (uri: Uri) {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val lessonId = userPref.getString("LID", "").toString()

        FirebaseStorage.getInstance().getReference("app")
            .child("Lessons").child(lessonId).child("$idLecture")
            .putFile(uri)
            .addOnSuccessListener {
                val databaseRef = FirebaseDatabase.getInstance().getReference("app")
                val lectureRef = databaseRef.child("lectures")
                    .child(lessonId).child(idLecture)
                val lectureData = Lecture(nameLecture, idLecture, fileFormat)
                lectureRef.setValue(lectureData)
                showToast("Лекцію завантажено успішно")
                navigate(R.id.action_LectureEdit_to_Lesson)
            }
            .addOnFailureListener {
                showToast("Не вдалося завантажити лекцію")
            }
    }

    private fun deleteFile () {
        val userPref = requireActivity().getSharedPreferences("UserPref", android.content.Context.MODE_PRIVATE)
        val lessonId = userPref.getString("LID", "").toString()

        FirebaseStorage.getInstance().getReference("app")
            .child("Lessons").child(lessonId).child("$deletename")
            .delete()
            .addOnSuccessListener {
                val databaseRef = FirebaseDatabase.getInstance().getReference("app")
                val lectureRef = databaseRef.child("lectures")
                    .child(lessonId).child(deletename)
                lectureRef.removeValue()
                showToast("Файл видалено")
                navigate(R.id.action_LectureEdit_to_Lesson)
            }
            .addOnFailureListener {
                showToast("Не вдалося видалити файл")
            }
    }
}