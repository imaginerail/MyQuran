package com.aneeq.myquran.fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.util.Linkify
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.aneeq.myquran.R

class AboutFragment : Fragment() {
    lateinit var txtwhatisQuran: TextView
    lateinit var txtQuranIntro: TextView
    lateinit var txtpp: TextView
    lateinit var txtdd: TextView
    private lateinit var btnabtquran: Button
    lateinit var btntnc: Button
    lateinit var btndev: Button
    lateinit var llpp: LinearLayout
    lateinit var lldd: LinearLayout
    lateinit var txtemail: TextView
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragment_about, container, false)
        txtQuranIntro = view.findViewById(R.id.txtQuranIntro)
        btndev = view.findViewById(R.id.btndev)
        llpp = view.findViewById(R.id.llpp)
        lldd = view.findViewById(R.id.lldd)
        btnabtquran = view.findViewById(R.id.btnabtquran)
        btntnc = view.findViewById(R.id.btntnc)
        txtwhatisQuran = view.findViewById(R.id.txtwhatisQuran)
        txtpp = view.findViewById(R.id.txtpp)
        txtdd = view.findViewById(R.id.txtdd)
        txtemail = view.findViewById(R.id.txtemail)


        llpp.visibility = View.GONE
        lldd.visibility = View.GONE


        txtpp.text = privacyPolicy()
        txtdd.text = developerContact()
        Linkify.addLinks(txtdd, Linkify.ALL)
        txtemail.setOnClickListener {
            sendEmail()
        }

        txtwhatisQuran.text = "What is Qur'an all about?"
        txtQuranIntro.text = quranIntro()

        btntnc.setOnClickListener {
            if (llpp.visibility == View.GONE) {
                llpp.visibility = View.VISIBLE
                btntnc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_godo, 0)
            } else {
                llpp.visibility = View.GONE
                btntnc.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_goto, 0)
            }
        }
        btndev.setOnClickListener {
            if (lldd.visibility == View.GONE) {
                lldd.visibility = View.VISIBLE
                btndev.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_godo, 0)
            } else {
                lldd.visibility = View.GONE
                btndev.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.ic_goto, 0)
            }
        }




        return view

    }

    private fun quranIntro(): String {
        return "The word Qur’an literally means 'the reading' or 'the recitation'," +
                " and refers to the divinely revealed scripture given to Prophet Muhammad (Peace Be Upon him)." +
                " The Qur’an is believed to be the final revelation from God to humanity.\n\n" +
                "The chapters and verses of the Qur’an were revealed throughout Prophet Muhammad’s mission," +
                " over a span of close to twenty-three years, from 610-632 C.E. Contrary to common misconception," +
                " Prophet Muhammad (PBUH) is not the author of the Qur’an. Rather, he is viewed as" +
                " the chosen recipient and the ideal implementor of commandments contained therein." +
                " He arranged the 114 chapters into the sequence we find in the Qur’an today." +
                " Scholars, both Muslim and non-Muslim, agree that the Qur’an has remained unchanged to the present.\n\n" +
                "Translations of the Qur’an exist in many languages throughout the world but" +
                " while they are useful as renderings or explanations of the Qur’an," +
                " only the original Arabic text is considered to be the Qur’an itself."
    }

    private fun privacyPolicy(): String {

        return "Your privacy is important to us." +
                " It is AlQuran's policy to respect your privacy regarding" +
                " any information we may collect from you across our website," +
                " AlQuran, and other sites we own and operate.\n\n" +
                "We only ask for personal information when we truly need it" +
                " to provide a service to you. We collect it by fair and lawful means," +
                " with your knowledge and consent. We also let you know why we’re collecting" +
                " it and how it will be used.\n\n" +
                "We only retain collected information for as long" +
                " as necessary to provide you with your requested service. What data we store, we’ll" +
                " protect within commercially acceptable means to prevent loss and theft, as well as" +
                " unauthorised access, disclosure, copying, use or modification.\n\n" +
                "We don’t share any personally" +
                " identifying information publicly or with third-parties, except when required to by law.\n\n" +
                "Our website may link to external sites that are not operated by us." +
                " Please be aware that we have no control over the content and practices of these sites," +
                " and cannot accept responsibility or liability for their respective privacy policies.\n\n" +
                "You are free to refuse our request for your personal information," +
                " with the understanding that we may be unable to provide you with some of your desired services.\n\n" +
                "Your continued use of our website will be regarded as acceptance" +
                " of our practices around privacy and personal information." +
                " If you have any questions about how we handle user data and" +
                " personal information, feel free to contact us.\n\n" +
                "Hopefully that has clarified things for you and as was previously" +
                " mentioned if there is something that you aren't sure whether you" +
                " need or not it's usually safer to leave cookies enabled in case" +
                " it does interact with one of the features you use on our site.\n\n" +
                "This policy is effective as of March 2022."
    }

    private fun developerContact(): String {
        return "Assalamualaikum, I am an independent Developer from India.\n\n" +
                "This App was created using" +
                " the Al-Quran Cloud Api from https://alquran.cloud/api.\n\n" +
                "The authors and their translations are purely from the Api itself and I bear no responsibility for " +
                "any mistranslations.\n\n" +
                "If you would like to help me with your valuable feedback/suggestions or have any requests for me," +
                " Please contact me using the below mail.\n\n" +
                "Jazakallah Khair for using this App, please rate it on the Play Store.\n\n"
    }

    private fun sendEmail() {
        val send = Intent(Intent.ACTION_SENDTO)
        val uriText = "mailto:" + Uri.encode("www.belligerent@gmail.com") +
                "?subject=" + Uri.encode("Message from")
        val uri = Uri.parse(uriText)

        send.data = uri

        try {
            startActivity(Intent.createChooser(send, "Send mail..."))
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}