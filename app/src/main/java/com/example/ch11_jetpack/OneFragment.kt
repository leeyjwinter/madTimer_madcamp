package com.example.ch11_jetpack

import android.content.Context
import android.content.res.AssetManager
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Rect
import android.graphics.drawable.Drawable
import android.icu.lang.UCharacter.GraphemeClusterBreak.L
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.Toast
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.ViewCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.ch11_jetpack.databinding.FragmentOneBinding
import com.example.ch11_jetpack.databinding.ItemRecyclerviewBinding
import org.json.JSONArray
import org.json.JSONObject
import org.json.JSONTokener


// 항목 뷰를 가지는 역할
class MyViewHolder(val binding: ItemRecyclerviewBinding) :
    RecyclerView.ViewHolder(binding.root)
// 항목 구성자. 어댑터
class MyAdapter(val datas: MutableList<String>) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    // 항목 개수를 판단하기 위해 자동 호출
    override fun getItemCount(): Int {
        return datas.size
    }
    // 항목 뷰를 가지는 뷰 홀더를 준비하기 위해 자동 호출
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):
            RecyclerView.ViewHolder =
        MyViewHolder(ItemRecyclerviewBinding.inflate(LayoutInflater.from(
            parent.context), parent, false))


    // 각 항목을 구성하기 위해 호출
    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val binding = (holder as MyViewHolder).binding
        // 뷰에 데이터 출력
        binding.itemData.text = datas[position]

        holder.itemView.setOnClickListener {
            Toast.makeText(holder.itemView.context," ${datas[position]}",Toast.LENGTH_SHORT).show()
        }
//        holder.itemView.setOnClickListener {
//            itemClickListener.onClick(it,position) }
//    }
//
//    interface onItemClickListener : AdapterView.OnItemClickListener {
//        fun onClick(v:View,position: Int)
//    }
//
//    fun setItemClickListener(onItemClickListener: onItemClickListener){
//        this.itemClickListener = onItemClickListener
//    }
//
//    private lateinit var itemClickListener : AdapterView.OnItemClickListener
    }


}
// 리사이클러 뷰 꾸미기
class MyDecoration(val context: Context): RecyclerView.ItemDecoration() {
    // 모든 항목이 출력된 후 호출
    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State)
    {
        super.onDrawOver(c, parent, state)
        // 뷰 크기 계산
        val width = parent.width
        val height = parent.height
        // 이미지 크기 계산
        val dr: Drawable? = ResourcesCompat.getDrawable(context.getResources(),
            R.drawable.call, null)
        val drWidth = dr?.intrinsicWidth
        val drHeight = dr?.intrinsicHeight

        // 이미지를 출력할 위치 계산
        val left = width / 2 - drWidth?.div(2) as Int
        val top = height / 2 - drHeight?.div(2) as Int
        // 이미지 출력
        c.drawBitmap(
            BitmapFactory.decodeResource(context.getResources(), R.drawable.call),
            left.toFloat(),
            top.toFloat(),
            null
        )
    }
    // 각 항목을 꾸미기 위해 호출
    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        super.getItemOffsets(outRect, view, parent, state)
        val index = parent.getChildAdapterPosition(view) + 1
//        if (index % 3 == 0) // left, top, right, bottom
//            outRect.set(10, 10, 10, 60)
//        else
        outRect.set(10, 10, 10, 0)
//        view.setBackgroundColor(Color.parseColor("#B3F6F6F6"))
        ViewCompat.setElevation(view, 20.0f)
    }
}
class OneFragment : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentOneBinding.inflate(inflater, container, false)
        // 리사이클러 뷰를 위한 가상 데이터 준비
        val datas = mutableListOf<String>()
//        for(i in 1..9){
//            datas.add("Item $i")
//
//        }
        val assetManager:AssetManager = requireContext().resources.assets
        val inputStream = assetManager.open("phonebook.json")
        val jsonString = inputStream.bufferedReader().use{it.readText()}
        val jObject = JSONObject(jsonString)
        val jArray = jObject.getJSONArray("phonebook")

        for(i in 0 until jArray.length()){
            val obj = jArray.getJSONObject(i)
            val name = obj.getString("name")
            val number = obj.getString("number")
            datas.add(" $name : $number")
        }

        // 리사이클러 뷰에 LayoutManager, Adapter, ItemDecoration 적용
        val layoutManager = LinearLayoutManager(activity)
        binding.recyclerView.layoutManager=layoutManager
        val adapter= MyAdapter(datas)
        binding.recyclerView.adapter=adapter
        binding.recyclerView.addItemDecoration(MyDecoration(activity as Context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

    }
}