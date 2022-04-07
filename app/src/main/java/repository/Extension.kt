package repository

import android.view.View
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.fragment_details.view.*
import view.main.DetailsFragment
//        Snackbar.make(binding.mainView, "Получилось", Snackbar.LENGTH_LONG).show()
//        Toast.makeText(requireContext(),"РАБОТАЕТ",Toast.LENGTH_SHORT).show()

fun String.showSnackbar(binding:View) = Snackbar.make(binding.mainView, this, Snackbar.LENGTH_LONG).show()
fun View.showSnackbar(s:String) = Snackbar.make(this, s, Snackbar.LENGTH_LONG).show()
fun View.createAndShow(text: String, actionText:String, action: (View)->Unit, length:Int = Snackbar.LENGTH_SHORT) = Snackbar.make(this,text,length).setAction(actionText,action).show()