package com.aneeq.myquran.activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.aneeq.myquran.R
import com.aneeq.myquran.util.ConnectionManager

val TAG = "stb"

class SrchByTxtActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_srch_by_txt)

       DisplayDetails()
        val map = mapOf(
            Pair(2, 2),
            Pair(1, 1),
            Pair(4, 77),
            Pair(3, 50),
            Pair(5, 106),
            Pair(6, 128),
            Pair(7, 151),
            Pair(10, 208),
            Pair(9, 187),
            Pair(11, 221),
            Pair(16, 267),
            Pair(14, 255),
            Pair(15, 262),
            Pair(8, 177),
            Pair(18, 293),
            Pair(13, 249),
            Pair(20, 312),
            Pair(12, 235),
            Pair(19, 305),
            Pair(21, 322),
            Pair(23, 342),
            Pair(17, 282),
            Pair(22, 332),
            Pair(25, 359),
            Pair(27, 377),
            Pair(28, 385),
            Pair(30, 404),
            Pair(32, 415),
            Pair(24, 350),
            Pair(26, 367),
            Pair(29, 396),
            Pair(34, 428),
            Pair(33, 418),
            Pair(31, 411),
            Pair(38, 453),
            Pair(36, 440),
            Pair(43, 489),
            Pair(42, 483),
            Pair(40, 467),
            Pair(41, 477),
            Pair(37, 446),
            Pair(45, 499),
            Pair(39, 458),
            Pair(35, 434),
            Pair(46, 502),
            Pair(44, 496),
            Pair(48, 511),
            Pair(49, 515),
            Pair(56, 534),
            Pair(47, 507),
            Pair(58, 542),
            Pair(62, 553),
            Pair(50, 518),
            Pair(60, 549),
            Pair(61, 551),
            Pair(63, 554),
            Pair(54, 528),
            Pair(51, 520),
            Pair(53, 526),
            Pair(64, 556),
            Pair(52, 523),
            Pair(55, 531),
            Pair(59, 545),
            Pair(57, 537),
            Pair(66, 560),
            Pair(65, 558),
            Pair(68, 564),
            Pair(67, 562),
            Pair(69, 566),
            Pair(70, 568),
            Pair(71, 570),
            Pair(73, 574),
            Pair(74, 575),
            Pair(72, 572),
            Pair(76, 578),
            Pair(75, 577),
            Pair(77, 580),
            Pair(78, 582),
            Pair(79, 583),
            Pair(80, 585),
            Pair(81, 586),
            Pair(83, 587),
            Pair(82, 587),
            Pair(85, 590),
            Pair(84, 589),
            Pair(87, 591),
            Pair(86, 591),
            Pair(88, 592),
            Pair(91, 595),
            Pair(95, 597),
            Pair(94, 596),
            Pair(90, 594),
            Pair(98, 598),
            Pair(101, 600),
            Pair(100, 599),
            Pair(93, 596),
            Pair(96, 597),
            Pair(105, 601),
            Pair(106, 602),
            Pair(113, 604),
            Pair(99, 599),
            Pair(107, 602),
            Pair(102, 600),
            Pair(110, 603),
            Pair(114, 604),
            Pair(104, 601),
            Pair(97, 598),
            Pair(109, 603),
            Pair(103, 601),
            Pair(108, 602),
            Pair(92, 595),
            Pair(89, 593),
            Pair(111, 603),
            Pair(112,604)
        ).toSortedMap()
//        Log.d(TAG, map.toString())
        val surpg=map.values
//        Log.d(TAG, surpg.toString())
    }

    private fun DisplayDetails() {
        val queue = Volley.newRequestQueue(this)
        val url = "http://api.alquran.cloud/v1/ayah/1:1"

        if (ConnectionManager().checkConnection(this)) {

//creating a json object
            val jsonObjectRequest = object : JsonObjectRequest(
                Method.GET, url, null, Response.Listener {
                    val data = it.getJSONObject("data")

                    Log.d(TAG, data.getString("text"))


                },
                Response.ErrorListener {
                    it.printStackTrace()

                }) {

            }
            queue.add(jsonObjectRequest)
        }
    }


}
//{1=1, 2=2, 3=50, 4=77, 5=106, 6=128, 7=151, 8=177, 9=187, 10=208, 11=221, 12=235,
// 13=249, 14=255, 15=262, 16=267, 17=282, 18=293, 19=305, 20=312, 21=322, 22=332,
// 23=342, 24=350, 25=359, 26=367, 27=377, 28=385, 29=396, 30=404, 31=411, 32=415,
// 33=418, 34=428, 35=434, 36=440, 37=446, 38=453, 39=458, 40=467, 41=477, 42=483,
// 43=489, 44=496, 45=499, 46=502, 47=507, 48=511, 49=515, 50=518, 51=520, 52=523,
// 53=526, 54=528, 55=531, 56=534, 57=537, 58=542, 59=545, 60=549, 61=551, 62=553,
//
// 63=554, 64=556, 65=558, 66=560, 67=562, 68=564, 69=566, 70=568, 71=570, 72=572,
// 73=574, 74=575, 75=577, 76=578, 77=580, 78=582, 79=583, 80=585, 81=586, 82=587,
//
// 83=587, 84=589, 85=590, 86=591, 87=591, 88=592, 89=593, 90=594, 91=595, 92=595,
// 93=596, 94=596, 95=597, 96=597, 97=598, 98=598, 99=599, 100=599, 101=600, 102=600,
// 103=601, 104=601, 105=601, 106=602, 107=602, 108=602, 109=603, 110=603, 111=603, 112=604, 113=604, 114=604}


