import io.dropwizard.Configuration
import org.hibernate.validator.constraints.NotEmpty

class HelloWorldConfiguration : Configuration() {
    @NotEmpty lateinit var template: String
    @NotEmpty lateinit var defaultName: String

    var amountToAdd: Int = 0
}