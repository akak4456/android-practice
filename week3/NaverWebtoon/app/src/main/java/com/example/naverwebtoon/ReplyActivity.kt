package com.example.naverwebtoon

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.naverwebtoon.databinding.ActivityReplyBinding
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class ReplyActivity : AppCompatActivity() {
    private lateinit var binding:ActivityReplyBinding

    private var reply:ArrayList<ReplyInfo> = ArrayList()

    private lateinit var accessId:String

    private lateinit var adapter:ReplyAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReplyBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val spId: SharedPreferences = getSharedPreferences("sharedId", Context.MODE_PRIVATE)
        if(spId.contains("alreadyId")){
            accessId = spId.getString("alreadyId","none").toString()
        }else{
            accessId = "none"
        }

        reply.add(ReplyInfo("전현택","2021-04-03 23:13","중간에 \"반 티값은 니가 잘 관리했어야지,,\" 이거 어케 알았냐? 잃어버렸다 했지 뭘 잃어버렸다는지 얘기안했는데???\n",9826,112,false,false))
        reply.add(ReplyInfo("신경훈","2021-04-03 23:13","근데 오디오헤드 관심 1도 없다는걸 알게되면 되게 서운해할듯...\n",9826,112,false,false))
        reply.add(ReplyInfo("전현택","2021-04-03 23:13","중간에 \"반 티값은 니가 잘 관리했어야지,,\" 이거 어케 알았냐? 잃어버렸다 했지 뭘 잃어버렸다는지 얘기안했는데???\n",9826,112,false,false))
        reply.add(ReplyInfo("정쥰","2021-04-03 23:13","이지아가 훔친 반티값으로 타겟팅한거 같음 그래서 가방이나 사물함같은데선 나올리가 없지..\n",9826,112,false,false))
        reply.add(ReplyInfo("전현택","2021-04-03 23:13","중간에 \"반 티값은 니가 잘 관리했어야지,,\" 이거 어케 알았냐? 잃어버렸다 했지 뭘 잃어버렸다는지 얘기안했는데???\n",9826,112,false,false))
        reply.add(ReplyInfo("정쥰","2021-04-03 23:13","이지아가 훔친 반티값으로 타겟팅한거 같음 그래서 가방이나 사물함같은데선 나올리가 없지..\n",9826,112,false,false))
        reply.add(ReplyInfo("신경훈","2021-04-03 23:13","근데 오디오헤드 관심 1도 없다는걸 알게되면 되게 서운해할듯...\n",9826,112,false,false))
        reply.add(ReplyInfo("정쥰","2021-04-03 23:13","이지아가 훔친 반티값으로 타겟팅한거 같음 그래서 가방이나 사물함같은데선 나올리가 없지..\n",9826,112,false,false))
        reply.add(ReplyInfo("신경훈","2021-04-03 23:13","근데 오디오헤드 관심 1도 없다는걸 알게되면 되게 서운해할듯...\n",9826,112,false,false))
        reply.add(ReplyInfo("전현택","2021-04-03 23:13","중간에 \"반 티값은 니가 잘 관리했어야지,,\" 이거 어케 알았냐? 잃어버렸다 했지 뭘 잃어버렸다는지 얘기안했는데???\n",9826,112,false,false))
        reply.add(ReplyInfo("신경훈","2021-04-03 23:13","근데 오디오헤드 관심 1도 없다는걸 알게되면 되게 서운해할듯...\n",9826,112,false,false))
        reply.add(ReplyInfo("전현택","2021-04-03 23:13","중간에 \"반 티값은 니가 잘 관리했어야지,,\" 이거 어케 알았냐? 잃어버렸다 했지 뭘 잃어버렸다는지 얘기안했는데???\n",9826,112,false,false))
        reply.add(ReplyInfo("정쥰","2021-04-03 23:13","이지아가 훔친 반티값으로 타겟팅한거 같음 그래서 가방이나 사물함같은데선 나올리가 없지..\n",9826,112,false,false))
        reply.add(ReplyInfo("전현택","2021-04-03 23:13","중간에 \"반 티값은 니가 잘 관리했어야지,,\" 이거 어케 알았냐? 잃어버렸다 했지 뭘 잃어버렸다는지 얘기안했는데???\n",9826,112,false,false))
        reply.add(ReplyInfo("정쥰","2021-04-03 23:13","이지아가 훔친 반티값으로 타겟팅한거 같음 그래서 가방이나 사물함같은데선 나올리가 없지..\n",9826,112,false,false))
        reply.add(ReplyInfo("신경훈","2021-04-03 23:13","근데 오디오헤드 관심 1도 없다는걸 알게되면 되게 서운해할듯...\n",9826,112,false,false))
        reply.add(ReplyInfo("정쥰","2021-04-03 23:13","이지아가 훔친 반티값으로 타겟팅한거 같음 그래서 가방이나 사물함같은데선 나올리가 없지..\n",9826,112,false,false))
        reply.add(ReplyInfo("신경훈","2021-04-03 23:13","근데 오디오헤드 관심 1도 없다는걸 알게되면 되게 서운해할듯...\n",9826,112,false,false))

        adapter = ReplyAdapter(this,reply)

        binding.replyRv.adapter = adapter

        val helper = ItemTouchHelper(ItemTouchHelperCallback(adapter))
        helper.attachToRecyclerView(binding.replyRv)

        binding.replyRv.layoutManager = LinearLayoutManager(this)

        binding.addReplyCnt.text = "0 / 500"
        binding.addReplyMsg.addTextChangedListener(object:TextWatcher{
            override fun afterTextChanged(s: Editable?) {
                runOnUiThread{
                    binding.addReplyCnt.text = String.format("%d / 500",binding.addReplyMsg.length())
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }
        })
        val imm: InputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        binding.addReplyHide.setOnClickListener{
            if(accessId == "none"){
                val dlg: AlertDialog.Builder = AlertDialog.Builder(this)
                dlg.setTitle("댓글 추가")
                dlg.setMessage("로그인을 먼저 해주세요!")
                dlg.setPositiveButton("확인") { dialog, which ->
                    val intent = Intent(this, LoginList::class.java)
                    startActivity(intent)
                }
                dlg.show()
            }else{
                binding.addReplyAuthor.text = accessId
                binding.addReplyMsg.requestFocus()

                imm.showSoftInput(binding.addReplyMsg, InputMethodManager.SHOW_IMPLICIT)
                binding.addReplyHide.visibility = View.INVISIBLE
                binding.addReplyShow.visibility = View.VISIBLE
            }
        }

        binding.addReplyBtn.setOnClickListener{
            val sdfNow = SimpleDateFormat("yyyy-MM-dd HH:mm");
            val time = sdfNow.format(Date(System.currentTimeMillis()));
            adapter.addData(ReplyInfo(accessId,time.toString(),binding.addReplyMsg.text.toString()+"\n\n",0,0,false,false))
            Toast.makeText(this,"댓글을 달았습니다",Toast.LENGTH_SHORT).show()
            binding.addReplyHide.visibility = View.VISIBLE
            binding.addReplyShow.visibility = View.INVISIBLE
            binding.addReplyMsg.setText("")
            binding.replyRv.scrollToPosition(adapter.size()-1)
            imm.hideSoftInputFromWindow(binding.addReplyMsg.windowToken,0)
        }
    }

    override fun onBackPressed() {
        if(binding.addReplyShow.visibility == View.VISIBLE){
            binding.addReplyHide.visibility = View.VISIBLE
            binding.addReplyShow.visibility = View.INVISIBLE
        }else{
            super.onBackPressed()
        }
    }
}