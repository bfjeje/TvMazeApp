package com.ellerbach.tvmazeapp.util

import android.content.Context
import android.util.AttributeSet
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView

class NonScrollExpandableListView : ExpandableListView {

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    )

    override fun setAdapter(adapter: ExpandableListAdapter?) {
        super.setAdapter(adapter)
    }

    override fun setOnChildClickListener(onChildClickListener: OnChildClickListener) {
        super.setOnChildClickListener(onChildClickListener)
    }

    public override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val heightMeasureSpecCustom = MeasureSpec.makeMeasureSpec(
            Integer.MAX_VALUE shr 2, MeasureSpec.AT_MOST
        )
        super.onMeasure(widthMeasureSpec, heightMeasureSpecCustom)
        val params = layoutParams
        params.height = measuredHeight
    }
}