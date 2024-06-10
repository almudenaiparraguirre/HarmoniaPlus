package com.mariana.harmonia.activities.iniciarSesion

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.R
import com.mariana.harmonia.interfaces.PlantillaActivity
import com.mariana.harmonia.utils.Utils

/**
 * ContrasenaRestablecidaCorrectamenteActivity es una Activity que indica al usuario que la contraseña
 * se ha restablecido correctamente. Permite al usuario navegar hacia la pantalla de inicio de sesión
 * o salir de la aplicación.
 */
class ContrasenaRestablecidaCorrectamenteActivity : AppCompatActivity(), PlantillaActivity {

    /**
     * Método llamado cuando la actividad se está creando. Se encarga de inicializar la interfaz
     * de usuario y otros componentes necesarios.
     * @param savedInstanceState Si no es nulo, esta actividad está siendo reconstituida a partir de un estado guardado previamente.
     */
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.contrasena_restablecida_correctamente_activity)
        Utils.degradadoTexto(this, R.id.titleTextView,R.color.rosa,R.color.morado)
    }

    /**
     * Método llamado cuando se hace clic en el botón "Iniciar sesión". Inicia la actividad principal
     * de la aplicación y finaliza la actividad actual.
     * @param view La vista del botón que se ha hecho clic.
     */
    fun irIniciarSesion(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }

    /**
     * Método llamado cuando se hace clic en el botón "Salir". Cierra la aplicación.
     * @param view La vista del botón que se ha hecho clic.
     */
    fun irSalir(view: View) {
        Utils.salirAplicacion(this)
    }
}