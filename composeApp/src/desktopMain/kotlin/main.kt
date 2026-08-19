import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.example.ctq_ascenseur.di.initKoin
import com.example.ctq_ascenseur.ui.App

fun main() = application {
    initKoin()
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "CTQ Ascenseur",
    ) {
        App()
    }
}
