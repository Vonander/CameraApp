package com.vonander.japancvcameraapp.presentation.ui.overlay

import android.content.Context
import android.content.res.Configuration
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Size
import android.view.View
import com.google.mlkit.vision.face.Face

class FaceDetectionOverlay @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
): View(context, attrs, defStyleAttr) {

    private var previewHeight: Int = 0
    private var previewWidth: Int = 0
    private var widthScaleFactor = 1.0f
    private var heightScaleFactor = 1.0f
    private var orientation = Configuration.ORIENTATION_PORTRAIT
    private var faces = emptyArray<Face>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.GREEN
        style = Paint.Style.STROKE
        strokeWidth = 5.0f
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        drawOverlay(canvas);
    }

    fun setOrientation(orientation: Int) {
        this.orientation = orientation
    }

    fun setPreviewSize(size: Size) {
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            previewWidth = size.width
            previewHeight = size.height
        } else {
            previewWidth = size.height
            previewHeight = size.width
        }
    }

    fun setFaces(faceList: List<Face>) {
        faces = faceList.toTypedArray()
        postInvalidate()
    }

    private fun drawOverlay(canvas: Canvas) {
        widthScaleFactor = width.toFloat() / previewWidth
        heightScaleFactor = height.toFloat() / previewHeight
        for (face in faces) {
            drawFaceBorder(face, canvas)
        }
    }

    private fun drawFaceBorder(face: Face, canvas: Canvas) {
        val padding = 30
        val bounds = face.boundingBox
        val left = translateX(bounds.left.toFloat()) - padding
        val top = translateY(bounds.top.toFloat()) - padding
        val right = translateX(bounds.right.toFloat()) + padding
        val bottom = translateY(bounds.bottom.toFloat()) + padding
        canvas.drawRect(left, top, right, bottom, paint)
    }

    private fun translateX(x: Float): Float = x * widthScaleFactor
    private fun translateY(y: Float): Float = y * heightScaleFactor
}