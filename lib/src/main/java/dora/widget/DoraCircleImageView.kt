package dora.widget

import android.content.Context
import android.util.AttributeSet

open class DoraCircleImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : DoraRoundRectImageView(context, attrs, defStyleAttr) {

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        setCornerRadius((measuredWidth / 2).coerceAtMost(measuredHeight / 2).toFloat())
    }
}