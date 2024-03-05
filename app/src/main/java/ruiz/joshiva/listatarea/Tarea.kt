package ruiz.joshiva.listatarea

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity (tableName = "tarea")
data class Tarea (
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    var desc: String
    )