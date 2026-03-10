package dora.widget

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapShader
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Shader
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.util.AttributeSet
import android.util.TypedValue
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide
import dora.widget.avatar.R
import androidx.core.graphics.createBitmap
import androidx.core.content.withStyledAttributes

open class DoraRoundRectImageView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var bitmap: Bitmap? = null
    private var bitmapPaint: Paint? = null
    private var borderPaint: Paint? = null
    private var bitmapShader: BitmapShader? = null
    private var cornerRadius: Float = 0f
    private var drawBorder: Boolean = false
        set(value) {
            field = value
            invalidate()
        }
    private var borderColor: Int = 0
        set(value) {
            field = value
            invalidate()
        }
    private var borderWidth: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private val rect = android.graphics.RectF()

    private fun initPaints() {
        bitmapPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)
        borderPaint!!.style = Paint.Style.STROKE
        borderPaint!!.strokeCap = Paint.Cap.ROUND
        borderPaint!!.color = borderColor
        borderPaint!!.strokeWidth = borderWidth
    }

    private fun initAttrs(context: Context, attrs: AttributeSet?) {
        context.withStyledAttributes(attrs, R.styleable.DoraRoundRectImageView) {
            cornerRadius = getDimension(
                R.styleable.DoraRoundRectImageView_dview_cornerRadius,
                TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5f, resources.displayMetrics)
            )
            drawBorder = getBoolean(R.styleable.DoraRoundRectImageView_dview_drawBorder, drawBorder)
            borderColor =
                getColor(R.styleable.DoraRoundRectImageView_dview_borderColor, borderColor)
            borderWidth =
                getDimension(R.styleable.DoraRoundRectImageView_dview_borderWidth, borderWidth)
        }
    }

    /**
     * 设置圆角大小。
     *
     * @param cornerRadius 0 ~ width / 2, 0 ~ height / 2
     */
    fun setCornerRadius(cornerRadius: Float) {
        this.cornerRadius = cornerRadius
        invalidate()
    }

    private fun refresh() {
        val bmp = bitmap ?: return
        val shader = BitmapShader(bmp, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        val matrix = android.graphics.Matrix()
        val scale = maxOf(
            width.toFloat() / bmp.width,
            height.toFloat() / bmp.height
        )
        matrix.setScale(scale, scale)
        shader.setLocalMatrix(matrix)
        bitmapShader = shader
        bitmapPaint?.shader = bitmapShader
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        refresh()
    }

    override fun onDraw(canvas: Canvas) {
        rect.set(0f, 0f, width.toFloat(), height.toFloat())
        canvas.drawRoundRect(rect, cornerRadius, cornerRadius, bitmapPaint!!)
        if (drawBorder) {
            val half = borderWidth / 2
            rect.set(half, half, width - half, height - half)
            canvas.drawRoundRect(rect, cornerRadius, cornerRadius, borderPaint!!)
        }
    }

    override fun setImageResource(resId: Int) {
        super.setImageResource(resId)
        val drawable = drawable
        if (drawable != null) {
            bitmap = getBitmapFromDrawable(drawable)
            refresh()
        }
    }

    fun loadUrl(url: String) {
        Glide.with(this)
            .load(url)
            .dontTransform()
            .into(this)
    }

    override fun setImageURI(uri: Uri?) {
        super.setImageURI(uri)
        val drawable = drawable
        if (drawable != null) {
            bitmap = if (uri != null) getBitmapFromDrawable(drawable) else null
            refresh()
        }
    }

    override fun setImageDrawable(drawable: Drawable?) {
        super.setImageDrawable(drawable)
        bitmap = getBitmapFromDrawable(drawable)
        refresh()
    }

    private fun getBitmapFromDrawable(drawable: Drawable?): Bitmap? {
        if (drawable == null) {
            return null
        }
        return if (drawable is BitmapDrawable) {
            drawable.bitmap
        } else try {
            val bitmap = if (drawable is ColorDrawable) {
                createBitmap(COLOR_DRAWABLE_DIMENSION, COLOR_DRAWABLE_DIMENSION, BITMAP_CONFIG)
            } else {
                createBitmap(drawable.intrinsicWidth, drawable.intrinsicHeight, BITMAP_CONFIG)
            }
            val canvas = Canvas(bitmap)
            if (drawBorder) {
                drawable.setBounds(
                    borderWidth.toInt() / 2,
                    borderWidth.toInt() / 2,
                    (canvas.width - borderWidth).toInt(),
                    (canvas.height - borderWidth).toInt()
                )
            } else {
                drawable.setBounds(0, 0, canvas.width, canvas.height)
            }
            drawable.draw(canvas)
            bitmap
        } catch (e: Exception) {
            null
        }
    }

    companion object {
        private val BITMAP_CONFIG = Bitmap.Config.ARGB_8888
        private const val COLOR_DRAWABLE_DIMENSION = 1
    }

    init {
        initAttrs(context, attrs)
        initPaints()
    }
}