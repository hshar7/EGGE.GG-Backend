import org.springframework.data.repository.CrudRepository

fun <T, String> CrudRepository<T, String>.findOne(id: String): T? = findById(id).orElse(null)
