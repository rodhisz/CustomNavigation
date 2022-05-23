package com.rsz.customnavigation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import com.rsz.customnavigation.api.ApiConfig
import com.rsz.customnavigation.model.ResponseUser
import kotlinx.android.synthetic.main.activity_login.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class LoginActivity : AppCompatActivity() {

    lateinit var sph : SharedPreference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sph = SharedPreference(this)

        btn_login.setOnClickListener {
            login()
        }
    }

    private fun login() {
        val email = edt_login_email.text.toString()
        val password = edt_login_pass.text.toString()

        if(email.isEmpty()){
            edt_login_email.error = "Email harus diisi"
            return
        }

        if(password.isEmpty()){
            edt_login_pass.error = "Password harus diisi"
            return
        }

        ApiConfig.instanceRetrofit.login(email,password)
            .enqueue(object : Callback<ResponseUser> {
                override fun onResponse(call: Call<ResponseUser>,response: Response<ResponseUser>) {
                    val response = response.body()

                    if (response != null){
                        if (response.status == 0){
                            Toast.makeText(this@LoginActivity, response.message, Toast.LENGTH_SHORT).show()
                        }else{
                            sph.setStatusLogin(true)

                            startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                            finish()
                        }
                    }
                }

                override fun onFailure(call: Call<ResponseUser>, t: Throwable) {
                    Toast.makeText(this@LoginActivity, t.localizedMessage, Toast.LENGTH_SHORT)
                        .show()
                }
            })
    }
}