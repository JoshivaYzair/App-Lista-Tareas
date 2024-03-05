package ruiz.joshiva.listatarea

import android.content.DialogInterface
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.room.Room


class MainActivity : AppCompatActivity() {
    lateinit var et_tarea: EditText
    lateinit var btn_agregar: Button
    lateinit var listView_tarea: ListView

    lateinit var lista:ArrayList<String>
    lateinit var adapter: ArrayAdapter<String>

    lateinit var db: AppDataBase


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        et_tarea = findViewById(R.id.ed_tarea)
        btn_agregar = findViewById(R.id.btn_agregar)
        listView_tarea = findViewById(R.id.listview_tareas)

        lista = ArrayList()

        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, lista)
        listView_tarea.adapter = adapter

        db = Room.databaseBuilder(
            applicationContext,
            AppDataBase::class.java, "tareas-db"
        ).allowMainThreadQueries().build()

        cargar()

        btn_agregar.setOnClickListener {
            var tarea_str = et_tarea.text.toString()


            if (tarea_str.isNotEmpty()){
                var tarea = Tarea(desc = tarea_str)
                db.tareaDAO().agregarTarea(tarea)
                lista.add(tarea_str)
                adapter.notifyDataSetChanged()
                et_tarea.setText("")
            }else{
                Toast.makeText(this, "Llenar campo", Toast.LENGTH_SHORT).show()
            }

        }

        listView_tarea.onItemClickListener = AdapterView.OnItemClickListener { parent, view, position, id ->
            mostrarDialogoEditar(position)
        }
        listView_tarea.onItemLongClickListener = AdapterView.OnItemLongClickListener { parent, view, position, id ->
            mostrarDialogoEliminar(position)
            true
        }
    }

    private fun mostrarDialogoEditar(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Editar tarea")

        val input = EditText(this)
        input.setText(lista[position])
        builder.setView(input)

        builder.setPositiveButton("Guardar") { dialog, id ->
            val nuevaTarea = input.text.toString()
            if (nuevaTarea.isNotEmpty()){
                var tarea_desc = lista[position]
                var tarea = db.tareaDAO().getTarea(tarea_desc)
                tarea.desc = nuevaTarea
                db.tareaDAO().actualizarTarea(tarea)
                lista[position] = nuevaTarea
                adapter.notifyDataSetChanged()
                dialog.dismiss()
            }else{
                Toast.makeText(this, "Llenar campo", Toast.LENGTH_SHORT).show()
            }

        }

        builder.setNegativeButton("Cancelar") { dialog, id ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun mostrarDialogoEliminar(position: Int) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Eliminar tarea")
        builder.setMessage("¿Estás seguro de que deseas eliminar esta tarea?")

        builder.setPositiveButton("Sí") { dialog, id ->
            var tarea_desc = lista[position]
            var tarea = db.tareaDAO().getTarea(tarea_desc)
            db.tareaDAO().eliminarTarea(tarea)
            lista.removeAt(position)
            adapter.notifyDataSetChanged()
            dialog.dismiss()
        }

        builder.setNegativeButton("Cancelar") { dialog, id ->
            dialog.cancel()
        }

        builder.show()
    }

    private fun cargar(){
        var listadb = db.tareaDAO().obtenerTareas()
        for (tarea in listadb){
            lista.add(tarea.desc)
        }


    }
}