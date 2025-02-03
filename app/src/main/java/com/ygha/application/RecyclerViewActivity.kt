package com.ygha.application

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class RecyclerViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recycler_view)

        val carList = mutableListOf<Car>()

        for(i in 1 .. 100){
            carList.add(Car(""+i+"번째 자동차", ""+i+"번째 엔진"))
        }

        val recyclerView = findViewById<RecyclerView>(R.id.recyclerview)
        recyclerView.adapter = RecyclerViewAdapter(carList, LayoutInflater.from(this))
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)

        // recyclerView에는 setOnItemClickListener가 없다.
        // ViewHolder class 에서 각 item에 대해 setOnClickListener 를 사용한다.






    }
}

class RecyclerViewAdapter(
    //outer class
    var carList : MutableList<Car>,
    var inflater: LayoutInflater
):RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>(){

    //inner class
    inner class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){
        //itemView의 상세 뷰 컴포넌트를 리턴한다.
        var carImage:ImageView
        var nthCar:TextView
        var nthEngine:TextView
        init {
            carImage = itemView.findViewById(R.id.carImage)
            nthCar = itemView.findViewById(R.id.nthCar)
            nthEngine = itemView.findViewById(R.id.nthEngine)

            itemView.setOnClickListener {
                val position:Int = adapterPosition
                val car = carList.get(position) // ViewHolder가 inner 이기 때문에 carList를 사용 가능
                Log.d("testt", car.nthCar)
            }
        }
    }



    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        //itemView를 return 한다.
        val view = inflater.inflate(R.layout.car_item, parent, false)
        return ViewHolder(view)

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        //데이터를 뷰의 item에 연결 시킨다.
        holder.nthCar.text = carList.get(position).nthCar
        holder.nthEngine.text = carList.get(position).nthEngine
    }

    override fun getItemCount(): Int {
        return carList.size
    }




}