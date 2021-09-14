package com.ayodeleochoa.ayoapps

import android.app.Activity
import android.content.Context
import android.os.AsyncTask
import android.os.Build
import android.os.Bundle
import android.util.TypedValue
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import androidx.room.Room
import com.ayodeleochoa.ayoapps.models.AppDatabase
import com.ayodeleochoa.ayoapps.models.Student


class RoomDatabaseActivity : AppCompatActivity()
{
   // private lateinit var binding: ActivityRoomDatabaseBinding
    lateinit var listView: ListView
    var students: MutableList<Student> = ArrayList()
   // var arrayList: ArrayList<MyData> = ArrayList()
    var adapter: MyAdapter? = null
    val context = this

    var editFirstName: EditText? = null
    var editLastName: EditText? = null
    var editGrade: EditText? = null


    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN_MR1)
    override fun onCreate(savedInstanceState: Bundle?)
    {
        super.onCreate(savedInstanceState)
      //  binding = ActivityRoomDatabaseBinding.inflate(layoutInflater)
      //  binding = DataBindingUtil.setContentView(this, R.layout.activity_room_database)
        setContentView(R.layout.activity_room_database)
      //  setContentView(binding.root)

        var db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
        db =  Room.databaseBuilder(this, AppDatabase::class.java, "StudentDB").allowMainThreadQueries().build()
        val studentDao = db.studentDao()
        students = studentDao.getAll()
        val studentList = students
        println("Total students = ${students.count()}")


        val knowledgeFragment = DatabaseFragment()
        val fragmentManager: FragmentManager = supportFragmentManager
        val fragmentTransaction: FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.knowledge_fragment, knowledgeFragment)
            .setCustomAnimations(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
            .commit()


        title = "STUDENTS"
        listView = findViewById(R.id.databaseListView)
      //  arrayList.add(MyData("Ayodele", "Ochoa", 95))
      //  arrayList.add(MyData("Essence", "Ochoa", 86))
      //  arrayList.add(MyData("Zion", "Ochoa", 65))
        adapter = MyAdapter(this, studentList as java.util.ArrayList<Student>)
        listView.adapter = adapter

        val editFirstName = findViewById<EditText>(R.id.editFirstName)
        val editLastName = findViewById<EditText>(R.id.editLastName)
        val editGrade = findViewById<EditText>(R.id.editGrade)

        // Add button functionality
        val btnAddStudent = findViewById(R.id.btnAddStudent) as Button
        btnAddStudent.setOnClickListener {

            println("AddStudent button clicked.")

            val firstName: String = editFirstName?.text.toString()
            val lastName: String = editLastName?.text.toString()
            val grade: String = editGrade?.text.toString()
            var intGrade = 0
            if (grade.length > 0)
            {
                intGrade = grade.toInt()
            }


            println("firstName = " + firstName)
            if (firstName.length > 0 && lastName.length > 0 && grade != null)
            {
                AddStudent(firstName, lastName, intGrade)
                editFirstName?.clearFocus()
                editFirstName?.text?.clear()
                editLastName?.clearFocus()
                editLastName?.text?.clear()
                editGrade?.clearFocus()
                editGrade?.text?.clear()

                hideSoftKeyboard()

                val toast = Toast.makeText(context, "New student added to database!", Toast.LENGTH_LONG)
                toast.show()
            }
            else
            {
                println("Please input all sections")
                val toast = Toast.makeText(context, "Please input all sections", Toast.LENGTH_LONG)
                toast.show()
            }
        }

        val btnDeleteTable = findViewById(R.id.btnDeleteTable) as Button
        btnDeleteTable.setOnClickListener {
            studentDao.deleteAll();
            students.clear();
            adapter?.notifyDataSetChanged();
            adapter = MyAdapter(this, students as java.util.ArrayList<Student>)
            listView.adapter = adapter

            val toast = Toast.makeText(context, "Database table cleared.", Toast.LENGTH_LONG)
            toast.show()
        }



        // Handle enter/done button being pressed
        editFirstName.setOnKeyListener { v, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    btnAddStudent.performClick()

                    //return true
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
        editLastName.setOnKeyListener { v, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    btnAddStudent.performClick()
                    //return true
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
        editGrade.setOnKeyListener { v, keyCode, event ->
            when {
                //Check if it is the Enter-Key,      Check if the Enter Key was pressed down
                ((keyCode == KeyEvent.KEYCODE_ENTER) && (event.action == KeyEvent.ACTION_DOWN)) -> {

                    btnAddStudent.performClick()
                    //return true
                    return@setOnKeyListener true
                }
                else -> false
            }
        }
    }



    fun Activity.hideSoftKeyboard(){
        (getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager).apply {
            hideSoftInputFromWindow(currentFocus?.windowToken, 0)
        }
    }

    open fun AddStudent(fName: String, lName: String, grade: Int)
    {
        var db = Room.databaseBuilder(
            context,
            AppDatabase::class.java, "database-name"
        ).build()
        db =  Room.databaseBuilder(this, AppDatabase::class.java, "StudentDB").allowMainThreadQueries().build()
        val studentDao = db.studentDao()

        val currentID = (students.count() + 1)
        studentDao.insert(Student(currentID, fName, lName, grade))

        students.clear();
        students = studentDao.getAll()
        adapter?.notifyDataSetChanged();
        adapter = MyAdapter(this, students as java.util.ArrayList<Student>)
        listView.adapter = adapter

    }


}

//Class MyAdapter
class MyAdapter(private val context: Context, private val newArrayList: java.util.ArrayList<Student>) : BaseAdapter()
{
    private lateinit var serialNum: TextView
    private lateinit var name: TextView
    private lateinit var contactNum: TextView
    override fun getCount(): Int {
        return newArrayList.size
    }
    override fun getItem(position: Int): Any {
        return position
    }
    override fun getItemId(position: Int): Long {
        return position.toLong()
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View? {
        var convertView = convertView
        convertView = LayoutInflater.from(context).inflate(R.layout.database_list_item, parent, false)
        serialNum = convertView.findViewById(R.id.txtFirstName)
        name = convertView.findViewById(R.id.txtLastName)
        contactNum = convertView.findViewById(R.id.txtGrade)
        serialNum.text = newArrayList[position].firstName
        name.text = newArrayList[position].lastName
        contactNum.text = newArrayList[position].grade.toString()
        return convertView
    }


    fun DeleteStudent(position: Int)
    {
        println("Deleted position = " + position);
    }
}


class MyData(var fName: String, var lName: String, var grade: Int)




// extension method to convert pixels to dp
fun Int.toDp(context: Context):Int = TypedValue.applyDimension(
    TypedValue.COMPLEX_UNIT_DIP,this.toFloat(),context.resources.displayMetrics
).toInt()
private class StudentAsyncTask(email: String, phone: String, license: String) :
    AsyncTask<Void?, Void?, Int>() {
    //Prevent leak

    private val email: String
    private val phone: String
    private val license: String

    private var mContext: Context? = null

    fun StudentAsyncTask(context: Context?) {
        mContext = context
    }
    override fun doInBackground(vararg p0: Void?): Int?
    {

        /*val db = Room.databaseBuilder(
            mContext!!,
            AppDatabase::class.java, "student-db"
        ).build()*/

      //  val studentDao = db.studentDao()
       // val students: List<Student> = studentDao.getAll()
      //  print("Total students = ${students.count()}")
        return 0
    }

    override fun onPostExecute(agentsCount: Int)
    {

    }

    init {
        this.email = email
        this.phone = phone
        this.license = license
    }
}