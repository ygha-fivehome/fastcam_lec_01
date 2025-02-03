package com.ygha.application

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.ListView
import android.widget.TextView
import android.widget.Toast
import org.w3c.dom.Text

class ListViewActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list_view)

        var carList = mutableListOf<Car>()
        for(i in 1..100){
            carList.add(Car("" + i + "번째 자동차", ""+i + "번째 엔진"))
        }

        val adapter = ListViewAdapter(
            carList,
            LayoutInflater.from(this),
            this
        )

        val listView = findViewById<ListView>(R.id.listview)
        listView.adapter = adapter

        //아이템 선택
        listView.setOnItemClickListener { parent, view, position, id ->
            val car:Car = adapter.carList.get(position)
            val nthCar = car.nthCar
            val nthEngine = car.nthEngine

            Toast.makeText(
                this,
                nthCar+ "" + nthEngine,
                Toast.LENGTH_LONG
            ).show()
        }

        //데이터 갱신 방법
        findViewById<TextView>(R.id.addCar).setOnClickListener {
            adapter.carList.add(
                Car("안녕 나는 차" , "안녕 나는 엔진")
            )
            adapter.notifyDataSetChanged()
        }

    }
}

class ListViewAdapter(
    var carList: MutableList<Car>,
    val layoutInflater: LayoutInflater,
    val context: Context
):BaseAdapter(){
    override fun getCount(): Int {
        // 전체 데이터 갯수
        return carList.size

    }

    override fun getItem(position: Int): Any {
        //전체 데이터 중에서 선택 (position) 의 데이터를 리턴
        return carList.get(position)
    }

    override fun getItemId(position: Int): Long {
        //잘 사용 안함.
        return position.toLong()

    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
       // view를 inflate 시켜서 최종 view 전달

        val view:View
        val holder:ViewHolder

        if(convertView == null){
            view = layoutInflater.inflate(R.layout.car_item, null)
            holder = ViewHolder()
            holder.carImage = view.findViewById(R.id.carImage)
            holder.nthCar = view.findViewById(R.id.nthCar)
            holder.nthEngine = view.findViewById(R.id.nthEngine)

            view.tag = holder
        }else{
            holder = convertView.tag as ViewHolder
            view = convertView
        }

        val car = carList.get(position)


        //view.tag = holder 해줬기 때문에 아래와 같이 holder.nthCar 이런 식으로 사용 가능하다.
        holder.carImage?.setImageDrawable(
            context.resources.getDrawable(R.drawable.ic_launcher_background))
        holder.nthCar?.text = car.nthCar
        holder.nthEngine?.text = car.nthEngine

        return view


    }
}


//car_item.xml 에 있는 항목대로 ViewHolder를 만든다.
class ViewHolder{
    var carImage:ImageView? = null
    var nthCar:TextView?=null
    var nthEngine:TextView?=null
}