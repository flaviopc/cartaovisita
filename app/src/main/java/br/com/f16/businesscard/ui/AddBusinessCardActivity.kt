package br.com.f16.businesscard.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import br.com.f16.businesscard.App
import br.com.f16.businesscard.R
import br.com.f16.businesscard.data.BusinessCard
import br.com.f16.businesscard.databinding.ActivityAddBusinessCardBinding


class AddBusinessCardActivity : AppCompatActivity() {
    private val binding by lazy { ActivityAddBusinessCardBinding.inflate(layoutInflater) }

    private val mainViewModel: MainViewModel by viewModels {
        MainViewModelFactory((application as App).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        insertListeners()
    }

    private fun insertListeners() {
        binding.btClose.setOnClickListener {
            finish()
        }

        binding.btConfirmar.setOnClickListener {
            val businessCard = BusinessCard(
                nome = binding.tilName.editText?.text.toString(),
                empresa = binding.tilEmpresa.editText?.text.toString(),
                email = binding.tilEmail.editText?.text.toString(),
                telefone = binding.tilFone.editText?.text.toString(),
                fundo = binding.tilCor.editText?.text.toString(),
            )

            mainViewModel.insert(businessCard)

            Toast.makeText(this, R.string.txt_sucess,Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}