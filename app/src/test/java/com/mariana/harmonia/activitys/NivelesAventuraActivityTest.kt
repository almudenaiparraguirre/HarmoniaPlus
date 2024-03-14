import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.mariana.harmonia.activitys.NivelesAventuraActivity
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import com.google.common.truth.Truth.assertThat

@RunWith(AndroidJUnit4::class)
class NivelesAventuraActivityTest {

    private lateinit var activity: NivelesAventuraActivity

    @Before
    fun setup() {
        // Inicializar la actividad bajo prueba
        activity = NivelesAventuraActivity()
        activity.onCreate(null)
    }
}
