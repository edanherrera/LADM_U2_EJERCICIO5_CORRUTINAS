package mx.tecnm.tepic.ladm_u2_ejercicio5_corrutinas

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.coroutines.*
import mx.tecnm.tepic.ladm_u2_ejercicio5_corrutinas.databinding.ActivityMainBinding
import kotlin.coroutines.EmptyCoroutineContext

class MainActivity : AppCompatActivity() {
    lateinit var binding : ActivityMainBinding
    var mensajes = arrayOf("Pienso luego existo",
        "Ya no pensamos","Gracias a dios es viernes","El fin justifica los medios","#noMeRepruebesBenigno")
    var cadena =""
    var contador = 0
    var scoope = CoroutineScope(Job()+Dispatchers.Main)
    var objetoCoroutineControlada = scoope.launch(EmptyCoroutineContext,CoroutineStart.LAZY){
        while(true){
            binding.etJob.text = mensajes[contador++]
            if(contador == mensajes.size) contador = 0
        }
        delay(500L)
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnRunblocking.setOnClickListener{
            rutina1erPlanoAsincrono()
            binding.etRunblocking.text= cadena
        }
        binding.btnGlobalscope.setOnClickListener{
            rutina2d0PlanoAsincrona()
        }

        binding.btnJob.setOnClickListener{
            /*if(objetoCoroutineControlada.isActive){
                objetoCoroutineControlada.cancel()
            }else{
                objetoCoroutineControlada.start()
            }*/
            if (objetoCoroutineControlada.isActive){
             objetoCoroutineControlada.cancel()
             return@setOnClickListener
            }
            if (objetoCoroutineControlada.isCancelled){
                objetoCoroutineControlada = scoope.launch(EmptyCoroutineContext,CoroutineStart.LAZY){
                    while(true){
                        binding.etJob.text = mensajes[contador++]
                        if(contador == mensajes.size) contador = 0
                    }
                    delay(500L)
                }
            }
            objetoCoroutineControlada.start()
        }

    }

    fun rutina1erPlanoAsincrono() = runBlocking {
        launch {
            for(texto in mensajes){
                cadena+="\n ${texto}"
                delay(500L)
            }
            cadena = "MENSAJES CONCATENADOS:"

        }
    }

    fun rutina2d0PlanoAsincrona() = GlobalScope.launch{
        for(texto in mensajes){
            runOnUiThread{
            binding.etGlobalscope.text = texto}
            delay(3000L)
        }
    }
}

