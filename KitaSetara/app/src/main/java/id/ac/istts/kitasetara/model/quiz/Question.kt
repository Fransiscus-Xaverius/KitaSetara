package id.ac.istts.kitasetara.model.quiz

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

//this will be stored in room db
@Entity(tableName = "questions")
data class Question(
    @PrimaryKey(autoGenerate = false)
    @ColumnInfo(name = "id") var id : String,
    @ColumnInfo(name = "question") val question : String,
    @ColumnInfo(name = "answer") val answer : String,
    @ColumnInfo(name = "ans1") val option1 : String?,
    @ColumnInfo(name = "ans2") val option2 : String?,
    @ColumnInfo(name = "ans3") val option3 : String?,
    @ColumnInfo(name = "ans4") val option4 : String?
)
