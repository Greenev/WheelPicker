package com.github.vikramezhil.wheelpicker.adapter

import android.content.Context
import android.graphics.Typeface
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.LinearLayout.VERTICAL
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.vikramezhil.wheelpicker.R
import com.github.vikramezhil.wheelpicker.databinding.PickerWheelHorizontalItemBinding
import com.github.vikramezhil.wheelpicker.databinding.PickerWheelVerticalItemBinding
import com.github.vikramezhil.wheelpicker.props.OnWheelPickerListener
import com.github.vikramezhil.wheelpicker.props.WheelPickerProperties

/**
 * Wheel Picker Adapter
 * @author vikram.ezhil
 */

class WPAdapter(private var context: Context, private var props: WheelPickerProperties, private val listener: OnWheelPickerListener): RecyclerView.Adapter<WPAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return if (props.orientation == VERTICAL) {
            val binding = PickerWheelVerticalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.itemTextSize =
                parent.context.resources.getDimension(
                    if (props.isWide) R.dimen.wp_wide_txt_size
                    else R.dimen.wp_txt_size
                )
            ViewHolder(binding.root)
        } else {
            val binding = PickerWheelHorizontalItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            binding.itemTextSize =
                parent.context.resources.getDimension(
                    if (props.isWide) R.dimen.wp_wide_txt_size
                    else R.dimen.wp_txt_size
                )
            binding.itemTextSize = parent.context.resources.getDimension(R.dimen.wp_txt_size)
            ViewHolder(binding.root)
        }
    }

    override fun getItemCount(): Int {
        return props.itemsList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val itemSelected = (position == props.selectedItemPos && props.enableItemHighlight && !props.scrolling)

        holder.llPicker.tag = position
        holder.llPicker.setBackgroundColor(if (itemSelected) props.selectedItemBgColor else props.unselectedItemBgColor)
        holder.llPicker.setOnClickListener {
            listener.onItemSelected(it.tag as Int, props.itemsList[position])
        }

        holder.topBorder.visibility = if (itemSelected && props.showDivider) View.VISIBLE else View.INVISIBLE
        holder.topBorder.setBackgroundColor(props.dividerColor)
        if (props.orientation == VERTICAL) {
            holder.topBorder.layoutParams.width = props.dividerWidth
            holder.bottomBorder.layoutParams.width = props.dividerWidth
        } else {
            holder.topBorder.layoutParams.height = props.dividerWidth
            holder.bottomBorder.layoutParams.height = props.dividerWidth
        }

        holder.bottomBorder.visibility = if (itemSelected && props.showDivider) View.VISIBLE else View.INVISIBLE
        holder.bottomBorder.setBackgroundColor(props.dividerColor)

        val params = holder.itemVal.layoutParams
        if (props.orientation == VERTICAL) { params.width = props.height } else { params.height = props.height }
        holder.itemVal.layoutParams = params
        holder.itemVal.text = props.itemsList[position]

        if (props.itemsTextStyle != 0) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                holder.itemVal.setTextAppearance(props.itemsTextStyle)
            } else {
                @Suppress("DEPRECATION")
                holder.itemVal.setTextAppearance(context, props.itemsTextStyle)
            }
        }

        holder.itemVal.setTextColor(if (itemSelected) props.selectedItemTxtColor else props.unselectedItemTxtColor)
        holder.itemVal.alpha = if (itemSelected) props.selectedItemTxtAlpha else props.unselectedItemsTxtAlpha

        if (props.itemsTxtBold && !props.itemsTxtItalic) holder.itemVal.setTypeface(holder.itemVal.typeface, Typeface.BOLD)

        if (props.itemsTxtItalic && !props.itemsTxtBold) holder.itemVal.setTypeface(holder.itemVal.typeface, Typeface.ITALIC)

        if (props.itemsTxtBold && props.itemsTxtItalic) holder.itemVal.setTypeface(holder.itemVal.typeface, Typeface.BOLD_ITALIC)
    }

    /**
     * Updates the wheel picker adapter
     * @param props WheelPickerProperties The wheel picker properties
     */
    fun update(props: WheelPickerProperties) {
        this.props = props
        notifyDataSetChanged()
    }

    /**
     * View holder class
     */
    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        var llPicker: LinearLayout = itemView.findViewById(R.id.ll_wp)
        val topBorder: View = itemView.findViewById(R.id.wp_top_border)
        val bottomBorder: View = itemView.findViewById(R.id.wp_bottom_border)
        val itemVal: TextView = itemView.findViewById(R.id.wp_item)
    }
}