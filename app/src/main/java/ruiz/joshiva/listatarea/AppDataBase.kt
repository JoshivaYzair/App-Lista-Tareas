package ruiz.joshiva.listatarea

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Tarea::class], version = 3)
abstract class AppDataBase : RoomDatabase() {
    abstract fun tareaDAO(): tareaDAO
}