
import com.fasterxml.jackson.annotation.JsonProperty

import org.hibernate.validator.constraints.Length

class Saying {
    @get:JsonProperty
    private var id: Long? = null

    @Length(max = 3)
    @get:JsonProperty
    private var content: String? = null

    constructor() {
        // Jackson deserialization
    }

    constructor(id: Long, content: String) {
        this.id = id
        this.content = content
    }
}