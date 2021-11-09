package com.example.watch

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import java.util.*
import kotlin.math.min

class CustomViewWatch @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // Ширина часовой стрелки
    private val mHourPointWidth = 10f

    // Ширина минутной стрелки
    private val mMinutePointWidth = 7f

    // Ширина секундной стрелки
    private val mSecondPointWidth = 4f

    // Прямоугольник указателя
    private val mPointRange = 20F

    // Расстояние между шкалой и числом
    private val mNumberSpace = 10f

    // Ширина циферблата
    private val mCircleWidth = 4.0F

    // максимальное значение шкалы
    private val scaleMax = 50

    // минимальное значение шкалы
    private val scaleMin = 25

    // ширина View
    private var mWidth = 0

    // высота View
    private var mHeight = 0

    //  радиус циферблата
    private var radius = 300.0F

    private val mPaint: Paint by lazy {
        Paint()
    }
    private val mRect: Rect by lazy {
        Rect()
    }

    init {
        // устанавливаем размер чисел циферблата
        mPaint.textSize = 35F
        mPaint.typeface = Typeface.DEFAULT_BOLD
        // сглаживаем края
        mPaint.isAntiAlias = true
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        mWidth = onMeasuredSpec(widthMeasureSpec) + (mCircleWidth * 2).toInt()
        mHeight = onMeasuredSpec(heightMeasureSpec) + (mCircleWidth * 3).toInt()

        radius = (mWidth - mCircleWidth * 2) / 2
        setMeasuredDimension(mWidth, mHeight)
    }

    private fun onMeasuredSpec(measureSpec: Int): Int {

        var specViewSize = 0
        val specMode = MeasureSpec.getMode(measureSpec)
        val specSize = MeasureSpec.getSize(measureSpec)

        when (specMode) {
            MeasureSpec.EXACTLY -> {
                specViewSize = specSize
            }
            MeasureSpec.AT_MOST -> {
                specViewSize = min((radius * 2).toInt(), specSize)
            }
        }
        return specViewSize
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX: Float = (mWidth / 2).toFloat()
        val centerY: Float = (mHeight / 2).toFloat()

        canvas.translate(centerX, centerY)

        drawClock(canvas)

        drawClockScale(canvas)

        drawPointer(canvas)

        postInvalidateDelayed(1000)
    }

    // рисуем задний фон и границу часов
    private fun drawClock(canvas: Canvas) {

        // ширина линии
        mPaint.strokeWidth = mCircleWidth
        // цвет линии
        mPaint.color = Color.BLACK
        // указываем, что рисуем линию
        mPaint.style = Paint.Style.STROKE
        // вызываем метод для элементов циферблата
        canvas.drawCircle(0F, 0F, radius, mPaint)
    }

    // рисуем шкалы циферблата
    private fun drawClockScale(canvas: Canvas) {
        for (index in 1..60) {
            //  поворачиваем холст на 6 градусов каждый шаг прорисовки
            canvas.rotate(6F, 0F, 0F)
            // кажое часовое деление рисуем длинной чертой
            if (index % 5 == 0) {
                // ширина черты
                mPaint.strokeWidth = 4.0F
                // рисуем черту
                canvas.drawLine(0F, -radius, 0F, -radius + scaleMax, mPaint)
                // сохраняем холст для дальнейшего переиспользования
                canvas.save()

                mPaint.strokeWidth = 1.0F

                mPaint.style = Paint.Style.FILL
                mPaint.getTextBounds(
                    (index / 5).toString(),
                    0,
                    (index / 5).toString().length,
                    mRect
                )
                canvas.translate(0F, -radius + mNumberSpace + scaleMax + (mRect.height() / 2))
                canvas.rotate((index * -6).toFloat())
                canvas.drawText(
                    (index / 5).toString(), -mRect.width() / 2.toFloat(),
                    mRect.height().toFloat() / 2, mPaint
                )
                canvas.restore()
            }
            // маленькая черта
            else {
                mPaint.strokeWidth = 2.0F
                canvas.drawLine(0F, -radius, 0F, -radius + scaleMin, mPaint)
            }
        }
    }

    private fun drawPointer(canvas: Canvas) {
        // получаем текущее время
        val calendar = Calendar.getInstance()
        val hour = calendar[Calendar.HOUR]
        val minute = calendar[Calendar.MINUTE]
        val second = calendar[Calendar.SECOND]
        // получаем часы и секунды
        val angleHour = (hour + minute.toFloat() / 60) * 360 / 12
        val angleMinute = (minute + second.toFloat() / 60) * 360 / 60
        val angleSecond = second * 360 / 60

        canvas.save()
        canvas.rotate(angleHour, 0F, 0F)

        val rectHour = RectF(
            -mHourPointWidth / 2,
            -radius / 2,
            mHourPointWidth / 2,
            radius / 6
        )

        mPaint.color = Color.BLUE
        mPaint.style = Paint.Style.STROKE
        mPaint.strokeWidth = mHourPointWidth
        canvas.drawRoundRect(rectHour, mPointRange, mPointRange, mPaint)
        canvas.restore()

        canvas.save()

        canvas.rotate(angleMinute, 0F, 0F)
        val rectMinute = RectF(
            -mMinutePointWidth / 2,
            -radius * 3.5f / 5,
            mMinutePointWidth / 2,
            radius / 6
        )

        mPaint.color = Color.BLACK
        mPaint.strokeWidth = mMinutePointWidth
        canvas.drawRoundRect(rectMinute, mPointRange, mPointRange, mPaint)
        canvas.restore()

        canvas.save()

        canvas.rotate(angleSecond.toFloat(), 0F, 0F)
        val rectSecond = RectF(
            -mSecondPointWidth / 2,
            -radius + 10,
            mSecondPointWidth / 2,
            radius / 6
        )

        mPaint.strokeWidth = mSecondPointWidth
        mPaint.color = Color.RED
        canvas.drawRoundRect(rectSecond, mPointRange, mPointRange, mPaint)
        canvas.restore()

        mPaint.style = Paint.Style.FILL
        canvas.drawCircle(
            0F,
            0F, mSecondPointWidth * 4, mPaint
        )
    }
}
