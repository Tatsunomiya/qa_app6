package tatsunomiya.com.qaaapp2

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_question_detail.*


class QuestionDetailActivity: AppCompatActivity() {
    var favoriteSwitch : String = "0"

    private var favoriteGenre: String =  "5"






    private lateinit var mQuestion: Question
    private lateinit var mAdapter: QuestionDetailListAdapter
    private lateinit var mAnswerRef: DatabaseReference




    private val mEventListener = object : ChildEventListener {
        override fun onCancelled(p0: DatabaseError) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildMoved(p0: DataSnapshot, p1: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildChanged(p0: DataSnapshot, p1: String?) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildRemoved(p0: DataSnapshot) {
            TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        }

        override fun onChildAdded(dataSnapshot: DataSnapshot, s: String?) {
            val map = dataSnapshot.value as Map<String, String>
            val answerUid = dataSnapshot.key ?: ""


            for (answer in mQuestion.answers) {
                if (answerUid == answer.answerUid) {
                    return
                }
            }

            val body = map["body"] ?: ""
            val name = map["name"] ?: ""
            val uid = map["uid"] ?: ""

            val answer = Answer(body, name, uid, answerUid)
            mQuestion.answers.add(answer)
            mAdapter.notifyDataSetChanged()


        }

    }


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question_detail)














        val extras = intent.extras
        mQuestion = extras.get("question") as Question




        title = mQuestion.title

        mAdapter = QuestionDetailListAdapter(this, mQuestion)
        listView.adapter = mAdapter
        mAdapter.notifyDataSetChanged()

        fab.setOnClickListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user == null) {
                val intent = Intent(applicationContext, LoginActivity::class.java)
                startActivity(intent)
            } else { val intent = Intent(applicationContext, AnswerSendActivity::class.java)
                intent.putExtra("question", mQuestion)
                startActivity(intent)
            }

        }




        val dataBaseReference = FirebaseDatabase.getInstance().reference
        mAnswerRef = dataBaseReference.child(ContentsPATH).child(mQuestion.genre.toString()).child(AnswersPATH)
        mAnswerRef.addChildEventListener(mEventListener)


        button1.setOnClickListener() {
            if (favoriteSwitch == "0") {
                favoriteSwitch = "1"



                button1.setImageResource(R.drawable.favorite2)
                FirebaseDatabase.getInstance().reference
                val user = FirebaseAuth.getInstance().currentUser

                val dataBaseReference = FirebaseDatabase.getInstance().reference
                val genreRef = dataBaseReference.child(ContentsPATH).child(mQuestion.genre
                    .toString()).child(mQuestion.questionUid)

                val favoriteRef = dataBaseReference.child(ContentsPATH).child(favoriteGenre).child(mQuestion.questionUid)

                if (user == null) {
                    // ログインしていない場合は何もしない

                } else {
                    // 変更した表示名をFirebaseに保存する
//                    val userRef = mDataBaseReference.child(UsersPATH).child(user!!.uid)
                    val data = HashMap<String, Any>()
                    data["favorite"] = favoriteSwitch
                    genreRef.updateChildren(data)
                    favoriteRef.updateChildren(data)

                }

            } else {
                button1.setImageResource(R.drawable.favorite1)
                favoriteSwitch = "0"

                FirebaseDatabase.getInstance().reference

                FirebaseDatabase.getInstance().reference
                val user = FirebaseAuth.getInstance().currentUser

                val dataBaseReference = FirebaseDatabase.getInstance().reference
                val genreRef = dataBaseReference.child(ContentsPATH).child(mQuestion.genre
                    .toString()).child(mQuestion.questionUid)
                val favoriteRef = dataBaseReference.child(ContentsPATH).child(favoriteGenre).child(mQuestion.questionUid)



                if (user == null) {
                    // ログインしていない場合は何もしない
                } else {
                    // 変更した表示名をFirebaseに保存する
//                    val userRef = mDataBaseReference.child(UsersPATH).child(user!!.uid)
                    val data = HashMap<String, Any>()
                    data["favorite"] = favoriteSwitch
                    genreRef.updateChildren(data)
                    favoriteRef.removeValue()


                }

            }

        }




}


    

}






