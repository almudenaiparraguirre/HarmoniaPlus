import com.google.common.truth.Truth.assertThat
import com.mariana.harmonia.MainActivity
import com.mariana.harmonia.MainActivity.Registro.validarContraseña
import org.junit.Test

class RegistroActivityTest {

    @Test
    fun addition_isCorrect() {
        val result = validarContraseña("Harmonia34")
        assertThat(result).isTrue()
    }

    @Test
    fun validarRegistro(){
        val result = MainActivity.Registro.validarInputRegistro(
            "Harmonia",
            "123",
            "123"
        )
        assertThat(result).isTrue()
    }
}