package com.rpl.sicfo.ui.coustumview

import android.content.Context
import android.graphics.Canvas
import android.graphics.drawable.Drawable
import android.text.Editable
import android.text.TextWatcher
import android.util.AttributeSet
import android.view.View
import androidx.appcompat.widget.AppCompatEditText
import androidx.core.content.ContextCompat
import com.rpl.sicfo.R

class EditTextPassword : AppCompatEditText {

    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        init()
    }

    private var editTextBackground: Drawable? = null
    private var editTextErrorBackground: Drawable? = null

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        hint = context.getString(R.string.password)
        textAlignment = View.TEXT_ALIGNMENT_VIEW_START
        background = editTextBackground ?: ContextCompat.getDrawable(context, R.drawable.bg_edit_text)

        if (error != null && editTextErrorBackground != null) {
            background = editTextErrorBackground
        }
    }

    private fun init() {

        editTextBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text)
        editTextErrorBackground = ContextCompat.getDrawable(context, R.drawable.bg_edit_text_error)


        addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun afterTextChanged(p0: Editable?) {
                if (p0?.length ?: 0 <= 5) {
                    setError(context.getString(R.string.password_minimum_character), null)
                } else {
                    error = null
                }
            }
        })
    }
}