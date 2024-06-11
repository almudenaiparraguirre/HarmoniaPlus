import android.os.Bundle
import androidx.fragment.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.view.WindowManager
import android.graphics.drawable.ColorDrawable
import android.graphics.Color
import com.mariana.harmonia.R

class CustomDialogFragment : DialogFragment() {

    private var message: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setStyle(STYLE_NO_TITLE, R.style.RoundedDialog)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val view = inflater.inflate(R.layout.fragmento_verificar, container, false)

        val imageView: ImageView = view.findViewById(R.id.dialog_image)
        val acceptButton: Button = view.findViewById(R.id.dialog_accept_button)

        // Set the message

        acceptButton.setOnClickListener {
            dismiss()
        }

        return view
    }

    override fun onStart() {
        super.onStart()
        dialog?.window?.setLayout(
            (resources.displayMetrics.widthPixels * 0.85).toInt(),  // 85% del ancho de la pantalla
            WindowManager.LayoutParams.WRAP_CONTENT
        )
        dialog?.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
    }


    fun setMessage(message: String) {
        this.message = message
    }

    companion object {
        @JvmStatic
        fun newInstance(message: String) =
            CustomDialogFragment().apply {
                arguments = Bundle().apply {
                    putString("message", message)
                }
            }
    }
}
