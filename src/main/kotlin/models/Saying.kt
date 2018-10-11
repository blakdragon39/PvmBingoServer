package models

import org.hibernate.validator.constraints.Length

class Saying(val id: Long,
             @Length(max=3) val content: String)