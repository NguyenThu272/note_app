package nguyenxuanthu.demo.noteapp.activity

import android.graphics.drawable.ColorDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import dagger.hilt.android.AndroidEntryPoint
import nguyenxuanthu.demo.noteapp.R
import nguyenxuanthu.demo.noteapp.entities.Note
import nguyenxuanthu.demo.noteapp.fragment.MainFragment
import nguyenxuanthu.demo.noteapp.listener.MainActivityListener

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),MainActivityListener {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_main)

        supportActionBar!!.setBackgroundDrawable(ColorDrawable(resources.getColor(R.color.grey,theme)))
        supportFragmentManager.commit {
            setReorderingAllowed(true)
            add(R.id.fragment_container_view,MainFragment())
        }
    }

    override fun onOpenFragment(fragment: Fragment,) {
          supportFragmentManager.beginTransaction()
            .replace(R.id.fragment_container_view,fragment)
            .addToBackStack(fragment::class.java.name)
            .commit()

    }


}