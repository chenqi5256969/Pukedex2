package com.revenco.pokedex2.ui.adapter


import android.widget.ImageView
import android.widget.TextView
import androidx.palette.graphics.Palette
import com.bumptech.glide.Glide
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.module.LoadMoreModule
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.github.florent37.glidepalette.BitmapPalette
import com.github.florent37.glidepalette.GlidePalette
import com.google.android.material.card.MaterialCardView
import com.revenco.pokedex2.R
import com.revenco.pokedex2.model.Pokemon


class MainRecyclerAdapter :
    BaseQuickAdapter<Pokemon, BaseViewHolder>(R.layout.item_main_recycler), LoadMoreModule {

    override fun convert(holder: BaseViewHolder, item: Pokemon) {
        val imageView = holder.getView<ImageView>(R.id.mainItemImage)
        val textView = holder.getView<TextView>(R.id.mainItemText)
        val cardView = holder.getView<MaterialCardView>(R.id.mainItemCardView)

        textView.text = item.name

        Glide.with(imageView.context).load(item.getImageUrl())
            .listener(
                GlidePalette.with(item.getImageUrl())
                    .use(BitmapPalette.Profile.MUTED_LIGHT)
                    .intoCallBack { palette ->
                        palette?.dominantSwatch?.rgb?.also {
                            cardView.setCardBackgroundColor(it)
                        }
                    }.crossfade(true)
            )
            .into(imageView)


    }


}