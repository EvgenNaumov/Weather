package view

import android.R.attr.dial
import android.app.AlertDialog
import android.content.ContentResolver
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.provider.ContactsContract
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.appweather.R
import com.example.appweather.databinding.FragmentWorkWithContentProviderBinding


class ContentProviderFragment : Fragment() {
    private var _binding: FragmentWorkWithContentProviderBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentWorkWithContentProviderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        checkPermission()
    }

    val REQUEST_CODE_CONTACTS = 999
    val REQUEST_CODE_PHONE_NUMBER = 888
    val REQUEST_CODE_CALL_PHONE = 1
    private fun checkPermission() {

        var isGrantedREadContact = false
        var isGrantedReadPhone  = false

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isGrantedREadContact = true
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_CONTACTS)){
            explain(getString(R.string.title_request_permissions_contacts), getString(R.string.message_permissions_contacts), REQUEST_CODE_CONTACTS)
        } else{
            mRequestPermission(android.Manifest.permission.READ_CONTACTS, REQUEST_CODE_CONTACTS)
        }

        if (ContextCompat.checkSelfPermission(
                requireContext(),
                android.Manifest.permission.READ_PHONE_NUMBERS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            isGrantedReadPhone = true
        } else if (shouldShowRequestPermissionRationale(android.Manifest.permission.READ_PHONE_NUMBERS)){
            explain(getString(R.string.title_request_permissions_phone), getString(R.string.message_permissions_phone),REQUEST_CODE_PHONE_NUMBER)
        } else{
            mRequestPermission(android.Manifest.permission.READ_PHONE_NUMBERS, REQUEST_CODE_PHONE_NUMBER)
        }

       if (isGrantedREadContact && isGrantedReadPhone) {
           getContacts()
       }

    }

    private fun mRequestPermission(permissionsString:String, intRequest:Int) {
        requestPermissions(
            arrayOf(permissionsString),
            intRequest
        )
    }

    private fun explain(title:String,  messageText: String, requestCode:Int) {
        AlertDialog.Builder(requireContext()).apply {
            setTitle(title)
            setMessage(messageText)

            when(requestCode){
                REQUEST_CODE_CONTACTS->setPositiveButton("Предоставить") { _, _ -> mRequestPermission(android.Manifest.permission.READ_CONTACTS, REQUEST_CODE_CONTACTS) }
                REQUEST_CODE_PHONE_NUMBER->setPositiveButton("Предоставить") { _, _ -> mRequestPermission(android.Manifest.permission.READ_PHONE_NUMBERS, REQUEST_CODE_PHONE_NUMBER) }
            }

            setNegativeButton("Отказать") { dialog, _ -> dialog.dismiss() }
            show()
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {

        var getPermissionsREAD_CONTACTS = false
        var getPermissionsREAD_PHONE_NUMBERS = false
        var getPermissionsREAD_CALL_PHONE = false

        if (requestCode == REQUEST_CODE_CONTACTS) {
            for (i in permissions.indices) {
                if (permissions[i] == android.Manifest.permission.READ_CONTACTS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getPermissionsREAD_CONTACTS = true
                } else {
                    explain(getString(R.string.title_request_permissions_contacts), getString(R.string.message_permissions_contacts), REQUEST_CODE_CONTACTS)
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        if (requestCode == REQUEST_CODE_PHONE_NUMBER) {
            for (i in permissions.indices) {
                if (permissions[i] == android.Manifest.permission.READ_PHONE_NUMBERS && grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    getPermissionsREAD_PHONE_NUMBERS = true
//                    getContacts()
                } else {
                    explain(getString(R.string.title_request_permissions_phone), getString(R.string.message_permissions_phone),REQUEST_CODE_PHONE_NUMBER)
                }
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        }

        if (getPermissionsREAD_CONTACTS && getPermissionsREAD_PHONE_NUMBERS) {
            getContacts()
        }
    }


    private fun getContacts() {
        val contentResolver: ContentResolver = requireContext().contentResolver

        val cursor = contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME + " ASC"
        ) // или DESC

        cursor?.let {
            for (i in 0 until it.count) {
                if (cursor.moveToPosition(i)) {
                    val columnNameIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME)
                    val name: String = cursor.getString(columnNameIndex)
                    val columnPhoneIndex =
                        cursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    val numberPhone:String = cursor.getString(columnPhoneIndex)
                    binding.containerForContacts.addView(TextView(requireContext()).apply {
                        textSize = 30f
                        text = name.plus( " phone ").plus(numberPhone)
                        setOnClickListener{
                            makeCallPhone(numberPhone)
                        }
                    })
                }
            }
        }
    }

    private fun makeCallPhone(number:String) {
        Toast.makeText(requireContext(),number,Toast.LENGTH_SHORT).show()
        if (number!=null) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    android.Manifest.permission.CALL_PHONE
                ) == PackageManager.PERMISSION_GRANTED){
                startActivity(Intent(Intent.ACTION_CALL, Uri.parse("tel:$number")))
            } else {
                requestPermissions(arrayOf(android.Manifest.permission.CALL_PHONE), 1)
            }


        }
    }

    companion object {
        @JvmStatic
        fun newInstance() = ContentProviderFragment()
    }
}
