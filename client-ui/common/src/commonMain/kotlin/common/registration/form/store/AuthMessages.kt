package registration.form.store


const val CYRILLIC_IN_LOGIN = "Login can't contain cyrillic letters"
const val CYRILLIC_IN_PASS = "Password can't contain cyrillic letters"
fun textToShort(text: String, bound: Int) = "$text should be longer than $bound"