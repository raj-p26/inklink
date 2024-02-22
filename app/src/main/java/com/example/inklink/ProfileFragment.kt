package com.example.inklink

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast

import androidx.fragment.app.Fragment

import com.example.inklink.dbhelpers.UserTableHelper
import com.example.inklink.models.User

class ProfileFragment : Fragment() {
    private lateinit var user: User
    private lateinit var editFirstName: TextView
    private lateinit var editLastName: TextView
    private lateinit var editUsername: TextView
    private lateinit var editEmail: TextView
    private lateinit var editPassword: TextView
    private lateinit var editAbout: TextView
    private lateinit var editButton: Button
    private lateinit var userTableHelper: UserTableHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)
        val sharedPrefs = context?.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        userTableHelper = UserTableHelper(activity!!.applicationContext)
        user = userTableHelper.getUserByEmail(sharedPrefs?.getString("email", null))!!
        editFirstName = view.findViewById(R.id.profile_edit_name)
        editLastName = view.findViewById(R.id.profile_edit_last_name)
        editUsername = view.findViewById(R.id.profile_edit_username)
        editEmail = view.findViewById(R.id.profile_edit_email)
        editPassword = view.findViewById(R.id.profile_edit_password)
        editAbout = view.findViewById(R.id.profile_edit_about)
        editButton = view.findViewById(R.id.profile_edit_button)

        setDetails()

        editButton.setOnClickListener {
            if (isValid()) {
                val user = User(
                    userTableHelper.getUserByEmail(this@ProfileFragment.user.email)!!.id,
                    editFirstName.text.toString(),
                    editLastName.text.toString(),
                    editUsername.text.toString(),
                    editEmail.text.toString(),
                    editPassword.text.toString(),
                    editAbout.text.toString()
                )

                userTableHelper.updateUser(user)
                Toast.makeText(activity, "Profile Updated successfully", Toast.LENGTH_SHORT).show()
                val editor = sharedPrefs?.edit()
                editor?.putString("fname", editUsername.text.toString())
                editor?.putString("email", editEmail.text.toString())
                editor?.apply()

                val intent = Intent(context, MainActivity::class.java)
                activity?.startActivity(intent)
                activity?.finish()
            }
        }

        return view
    }

    /**
     * This method is used to set details to corresponding EditTexts.
     */
    private fun setDetails() {
        editFirstName.text = user.firstName
        editLastName.text = user.lastName
        editUsername.text = user.username
        editEmail.text = user.email
        editAbout.text = user.about
    }

    /**
     * This method is used to check whether the data is valid or not.
     *
     * @return `true` if data is valid otherwise `false` is returned.
     */
    private fun isValid(): Boolean {
        val fname = editFirstName.text.toString()
        val lname = editLastName.text.toString()
        val email = editEmail.text.toString()
        val prefs = activity!!.getSharedPreferences("auth_prefs", Context.MODE_PRIVATE)
        val helper = UserTableHelper(activity!!.applicationContext)

        if (fname.isEmpty()) {
            showError(editFirstName, "First name cannot be blank")
            return false
        }

        if (lname.isEmpty()) {
            showError(editLastName, "Last name cannot be blank")
            return false
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            showError(editEmail, "Invalid email address")
            return false
        }

        if (editEmail.text.toString() != prefs.getString("email", null) && helper.exists(email)) {
            showError(editEmail, "Email is already taken!")
            return false
        }

        return true
    }

    /**
     * This method is shows error in targeted View and sets its focus.
     *
     * @param view targeted view.
     * @param msg actual string message.
     */
    private fun showError(view: TextView, msg: String) {
        view.error = msg
        view.requestFocus()
    }
}
