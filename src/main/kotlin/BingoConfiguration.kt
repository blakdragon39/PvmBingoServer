import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty

class BingoConfiguration : Configuration() {
    @NotEmpty lateinit var template: String

}