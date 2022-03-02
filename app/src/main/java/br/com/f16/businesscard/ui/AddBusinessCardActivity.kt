package br.com.f16.businesscard.ui

import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import br.com.f16.businesscard.App
import br.com.f16.businesscard.R
import br.com.f16.businesscard.data.BusinessCard
import br.com.f16.businesscard.databinding.ActivityAddBusinessCardBinding
import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog
import com.github.dhaval2404.colorpicker.model.ColorShape
import com.github.dhaval2404.colorpicker.model.ColorSwatch


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

        var cardColor = "#ff33b5e5"
        binding.btColor.setOnClickListener {
            MaterialColorPickerDialog
                .Builder(this)
                .setTitle(R.string.hint_cor)
                .setColorShape(ColorShape.SQAURE)
                .setColorSwatch(ColorSwatch._400)
                .setDefaultColor(cardColor)
                .setColorListener { color, colorHex ->
                    binding.ivColor.setBackgroundColor(Color.parseColor(colorHex))
                    cardColor = colorHex
                }
                .show()
        }

        binding.btConfirmar.setOnClickListener {
            val businessCard = BusinessCard(
                nome = binding.tilName.editText?.text.toString(),
                empresa = binding.tilEmpresa.editText?.text.toString(),
                email = binding.tilEmail.editText?.text.toString(),
                telefone = binding.tilFone.editText?.text.toString(),
                fundo = cardColor
            )

            mainViewModel.insert(businessCard)

            Toast.makeText(this, R.string.txt_sucess,Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}