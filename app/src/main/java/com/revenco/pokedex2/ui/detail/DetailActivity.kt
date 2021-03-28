package com.revenco.pokedex2.ui.detail

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
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
                .listener(glidePalette(it))
                .into(detailHeadPokdexImage)
            detailMiddleNameText.text = it.name
            detailViewModel.fetchPokemonInfo(it.name)
        }
        detailViewModel.resultLiveData.whatIfNotNull {
            it.observe(this,
                Observer<PukdexResult<PokemonInfo>?> { result ->
                    isShowLoading(result)
                    when (result) {
                        is PukdexResult.Success -> {
                            loadDataToLayout(result)
                        }
                        is PukdexResult.Error -> {
                        }
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

    private fun loadDataToLayout(result: PukdexResult.Success<PokemonInfo>) {
        result.successResult.whatIfNotNull {
            detailMiddleWeightText.text = it.getWeightString()
            detailMiddleHeightText.text = it.getHeightString()
            detailMiddleRecycler.adapter =
                DetailMiddleRecyclerAdapter(it.types)
            detailBottomHPPV.apply {
                max = maxHp.toFloat()
                progress = it.hp.toFloat()
                labelText = it.getHpString()
            }
            detailBottomATKPV.apply {
                max = maxAttack.toFloat()
                progress = it.attack.toFloat()
                labelText = it.getAttackString()
            }
            detailBottomDEFPV.apply {
                max = maxDefense.toFloat()
                progress = it.defense.toFloat()
                labelText = it.getDefenseString()
            }

            detailBottomSPDPV.apply {
                max = maxSpeed.toFloat()
                progress = it.speed.toFloat()
                labelText = it.getSpeedString()
            }
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

    private fun glidePalette(it: Pokemon) =
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