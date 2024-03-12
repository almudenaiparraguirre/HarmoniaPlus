import android.os.Looper
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.mariana.harmonia.activitys.InicioSesion
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

class InicioSesionTest {

    @Mock
    private lateinit var firebaseAuth: FirebaseAuth

    private lateinit var inicioSesion: InicioSesion

    /* @Before
    fun setUp() {
         MockitoAnnotations.openMocks(this)
         inicioSesion = InicioSesion()
         inicioSesion.firebaseAuth = firebaseAuth

         // Configuración de Looper.myLooper() para evitar errores
         Looper.prepare()
     }*/

    /*@Test
    fun signIn_Successful() {
        // Simular el éxito de la autenticación
        val task: Task<AuthResult> = mock(Task::class.java) as Task<AuthResult>
        `when`(firebaseAuth.signInWithEmailAndPassword(anyString(), anyString())).thenReturn(task)
        `when`(task.isSuccessful).thenReturn(true)

        // Llamar al método de inicio de sesión
        inicioSesion.signIn("test@example.com", "password")

        // Verificar que se haya iniciado sesión correctamente
        // Aquí puedes agregar más aserciones según sea necesario
        Assert.assertTrue(true)
    }*/
}
