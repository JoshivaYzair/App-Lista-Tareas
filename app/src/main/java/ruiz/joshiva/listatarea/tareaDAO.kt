package ruiz.joshiva.listatarea

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update

@Dao
interface tareaDAO {

    @Query("SELECT * FROM tarea")
    fun obtenerTareas(): List<Tarea>

    @Insert
    fun agregarTarea(tarea: Tarea)

    @Delete
    fun eliminarTarea(tarea: Tarea)

    @Update
    fun actualizarTarea(tarea: Tarea)

    @Query ("SELECT * FROM tarea WHERE `desc` = :description")
    fun getTarea(description: String): Tarea
}