package com.revenco.pokedex2.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.revenco.pokedex2.R
import com.revenco.pokedex2.model.Pokemon
import com.revenco.pokedex2.model.PokemonInfo
import com.revenco.pokedex2.model.PokemonInfo.Companion.maxAttack
import com.revenco.pokedex2.model.PokemonInfo.Companion.maxDefense
import com.revenco.pokedex2.model.PokemonInfo.Companion.maxHp
import com.revenco.pokedex2.model.PokemonInfo.Companion.maxSpeed
import com.revenco.pokedex2.model.base.PukdexResult
import com.revenco.pokedex2.ui.adapter.DetailMiddleRecyclerAdapter
import com.skydoves.transformationlayout.TransformationAppCompatActivity
import com.skydoves.transformationlayout.TransformationCompat
import com.skydoves.transformationlayout.TransformationLayout
import com.skydoves.whatif.whatIf
import com.skydoves.whatif.whatIfNotNull
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_detail.*

@AndroidEntryPoint
class DetailActivity : TransformationAppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        val pokemon: Pokemon? = intent.getParcelableExtra(EXTRA_POKEMON)
        val detailViewModel = ViewModelProvider(this)[DetailViewModel::class.java]

        pokemon?.also {
            Glide.with(this).load(it.getImageUrl()).into(detailHeadPokdexImage)
            Glide.with(this).load(it.getImageUrl())
                .listener(glidePaletteListener(it))
                .into(detailHeadPokdexImage)
            detailMiddleNameText.text = it.name
            detailViewModel.fetchPokemonInfo(it.name)
        }
        detailViewModel.resultLiveData.whatIfNotNull {
            it.observe(this,
                Observer<PukdexResult<PokemonInfo>> { result ->
                    isShowLoading(result)
                    result.successResult?.also { pokemonInfo ->
                        loadDataToLayout(pokemonInfo)
                    }
                    result?.ErrorMsg?.also {
                        Log.i("DetailActivity-异常->", it)
                    }
                })
        }
    }

    private fun isShowLoading(result: PukdexResult<PokemonInfo>?) {
        result?.ShowLoading.whatIf(
            whatIf = { detailLoading.visibility = View.VISIBLE },
            whatIfNot = {
                detailLoading.visibility = View.GONE
            })
    }

    private fun loadDataToLayout(result: PokemonInfo) {
        detailMiddleWeightText.text = result.getWeightString()
        detailMiddleHeightText.text = result.getHeightString()
        detailMiddleRecycler.adapter =
            DetailMiddleRecyclerAdapter(result.types)
        detailBottomHPPV.apply {
            max = maxHp.toFloat()
            progress = result.hp.toFloat()
            labelText = result.getHpString()
        }
        detailBottomATKPV.apply {
            max = maxAttack.toFloat()
            progress = result.attack.toFloat()
            labelText = result.getAttackString()
        }
        detailBottomDEFPV.apply {
            max = maxDefense.toFloat()
            progress = result.defense.toFloat()
            labelText = result.getDefenseString()
        }

        detailBottomSPDPV.apply {
            max = maxSpeed.toFloat()
            progress = result.speed.toFloat()
            labelText = result.getSpeedString()
        }
    }

    companion object {
        const val EXTRA_POKEMON = "EXTRA_POKEMON"
        fun toDetailActivity(
            context: Context,
            transformationLayout: TransformationLayout,
            pokemon: Pokemon
        ) {
            val intent = Intent(context, DetailActivity::class.java)
            intent.putExtra(EXTRA_POKEMON, pokemon)
            TransformationCompat.startActivity(transformationLayout, intent)

        }
    }

    private fun glidePaletteListener(it: Pokemon) =
        GlidePalette.with(it.getImageUrl())
            .use(BitmapPalette.Profile.MUTED_LIGHT)
            .intoCallBack { palette ->
                val lightRgb =
                    palette?.dominantSwatch?.rgb ?: Color.parseColor("#FFD600")
                window.apply {
                    statusBarColor = lightRgb
                }
                val darkRgb =
                    palette?.darkMutedSwatch?.rgb ?: Color.parseColor("#D53A47")
                val mixColor = intArrayOf(lightRgb, darkRgb)
                GradientDrawable().apply {
                    colors = mixColor
                    detailHeadFL.background = this
                }
            }.crossfade(true)
}