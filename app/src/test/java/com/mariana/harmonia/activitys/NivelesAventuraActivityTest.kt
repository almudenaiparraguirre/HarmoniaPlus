import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mariana.harmonia.activities.NivelesAventuraActivity
import org.junit.Before
import org.junit.runner.RunWith

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
