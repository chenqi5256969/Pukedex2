package com.revenco.pokedex2.ui.adapter

import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.widget.TextView
import com.chad.library.adapter.base.BaseQuickAdapter
import com.chad.library.adapter.base.viewholder.BaseViewHolder
import com.revenco.pokedex2.R
import com.revenco.pokedex2.model.PokemonInfo
import com.revenco.pokedex2.utils.PokemonTypeUtils


class DetailMiddleRecyclerAdapter constructor(types: MutableList<PokemonInfo.TypeResponse>) :
    BaseQuickAdapter<PokemonInfo.TypeResponse, BaseViewHolder>(
        data = types,
        layoutResId = R.layout.item_detail_recycler
    ) {
    override fun convert(holder: BaseViewHolder, item: PokemonInfo.TypeResponse) {
        val detailItemText = holder.getView<TextView>(R.id.detailItemText)
        val typeColor = PokemonTypeUtils.getTypeColor(item.type.name)
        val gradientDrawable = detailItemText.background as GradientDrawable
        gradientDrawable.setColor(typeColor)
        detailItemText.text = item.type.name
    }
}